package Controller;

import Model.*;
import View.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


/**
 * TODO: Fix xml escape character bug [needs patch] (https://www.advancedinstaller.com/user-guide/xml-escaped-chars.html)
 * 
 */
public class NoteBankController {
	NoteBankView noteBankView;
	private Topic allTopics;
	private HashMap<Note,List<String>> allNotes;
	private String pathToDirectory;
	private String xmlFileName;
	private List<Idea> currentQuiz;
	private int currentIdeaPointer;

	public List<Idea> getQuiz(){
		return currentQuiz;
	}

	public void nextIdea(){
		currentIdeaPointer+=1;
	}

	public void resetQuiz(List<Idea>quiz){
		currentQuiz = quiz;
		currentIdeaPointer = 0;
	}

	public void resetQuiz(){
		currentIdeaPointer = 0;
	}

	public QuizReader getQuizReader() {
		return quizReader;
	}

	public SubjectReader getSubjectReader() {
		return subjectReader;
	}

	public void setQuizCreatedTime(Date quizCreatedTime){
		this.quizCreatedTime = quizCreatedTime;
	}

	public int getCurrentIdeaIndex(){
		return currentIdeaPointer;
	}

	public int getSizeOfCurrentQuiz(){
		return currentQuiz.size();
	}

	public Idea getCurrentQuizIdea(){
		return currentQuiz.get(currentIdeaPointer);
	}

	private QuizReader quizReader;
	private SubjectReader subjectReader;

	public Date getQuizCreatedTime() {
		return quizCreatedTime;
	}

	private Date quizCreatedTime;

	public TextExcerpt getEvaluatedAnswer() {
		return evaluatedAnswer;
	}

	public void setEvaluatedAnswer(TextExcerpt evaluatedAnswer) {
		this.evaluatedAnswer = evaluatedAnswer;
	}

	private TextExcerpt evaluatedAnswer;

	public double getMaxWeightedAverageSimulationPerTime() {
		return maxWeightedAverageSimulationPerTime;
	}

	private double maxWeightedAverageSimulationPerTime;

	long hour = (60*60*1000);
	long day = (hour*24);
	long week = (day*7);
	long fortnight = (week*2);
	long month = (fortnight*2);

	public NoteBankView getView(){
		return this.noteBankView;
	}

	public String getPathToDirectory(){
		return  pathToDirectory;
	}

	public static void saveXML(String xml, String loc, String name) throws IOException {
		String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator();
		xmlContent += xml;
		FileWriter xmlFile = new FileWriter(new File(loc+File.separator+name+".xml"));
		
		xmlFile.write(xmlContent);
		xmlFile.close();	
	}
	
	public void updateSubjectFile() {
		this.subjectReader.save(allNotes);	
	}
	
	public List<Note> setUpNoteBank() throws IOException {
		List<Note> notes = new ArrayList<Note>();
		File directory = new File(pathToDirectory);
		String name, path ;
		for(File file: directory.listFiles()) {
			name = file.getName().split("\\.")[0];
			path = file.getPath();
			if(ImageIO.read(file)!=null) {
				notes.add( new Image(name,path,pathToDirectory) );
			}else if(file.getPath().contains(".pdf")) {
				notes.add( new Book(name,path,pathToDirectory) );
			}else if(file.getPath().contains(".txt")) {
				notes.add( new TextExcerpt(file,name) );
			}
		}
		return notes;
	}
	
	public void removeNote(Note note) {
		this.noteBankView.manageNoteBankPage.removeNote(note);
		if(noteBankView.manageNoteBankPage.getSelectedSubject().equals("All")) {
			allTopics.removeNote(note);	
		}
	}
	
	public void addNote(Note note, String subject) {
		this.noteBankView.manageNoteBankPage.addNote(note,subject);
	}	
	
	public void addNote(Note note) {		
		this.addNote(note, noteBankView.manageNoteBankPage.getCurrentSubject());
	}
	
	public void addNote(Note note, List<String>subjects) {
		this.noteBankView.manageNoteBankPage.addNotes(note, subjects);

	}
	
	public void save() {
		try {
			NoteBankController.saveXML(allTopics.toXML(), pathToDirectory, xmlFileName);
			if(noteBankView!=null) {
				noteBankView.manageNoteBankPage.refreshNotesListView();
				noteBankView.quizSetUpPage.clearTopics();
				for(Topic topic: allTopics.getTopics()) {
					noteBankView.quizSetUpPage.addTopic(topic);
				}
				updateQuizSetUp();
			}
		}catch(IOException e) {
			
		}
	}
	
	private void updateQuizSetUp() {
		int numOfIdeas = allTopics.getAllIdeas().size();
		int minNumOfIdeas = 1;
		if(numOfIdeas<1) {
			minNumOfIdeas = 0;
			numOfIdeas = 0;
		}
		noteBankView.quizSetUpPage.setSliderMinMax(minNumOfIdeas, numOfIdeas );
	}

	public NoteBankController() {
		pathToDirectory = new File("").getAbsolutePath() + File.separator + "notes"+ File.separator;
		this.allNotes = new HashMap<Note,List<String> >();
		this.xmlFileName = "save";
		String xmlFileLocation = pathToDirectory + this.xmlFileName + ".xml";



		this.maxWeightedAverageSimulationPerTime = ( 100*1 + 50*3 + 25*10 )/100; 
		// corresponds to studying: once in last hour, 3 times in last day and 10 times in last week

		try {
			subjectReader = new SubjectReader(pathToDirectory);
			this.allNotes = subjectReader.getAllNotes(this.setUpNoteBank());

			File xmlFile = new File(xmlFileLocation);
			this.allTopics = new Topic("All Topics");

			if(!xmlFile.exists()) {
				this.save();
			}else{
				this.allTopics = Topic.fromXML(xmlFileLocation, allNotes.keySet(), pathToDirectory);
			}
			this.quizReader = new QuizReader(pathToDirectory);

		}catch(Exception e) {
			JOptionPane.showMessageDialog(noteBankView, "An error occurred. Model.Note bank can't be loaded");
			e.printStackTrace(); 
		}

		ManageNoteBankController manageNoteBankController = new ManageNoteBankController(this);
		ManageIdeasController manageIdeasController = new ManageIdeasController(this);
		QuizSetUpController quizSetUpController = new QuizSetUpController(this);
		MainWindowController mainWindowController = new MainWindowController(this);
		
		noteBankView = new NoteBankView(allTopics, allNotes, manageNoteBankController, manageIdeasController, quizSetUpController,mainWindowController);
		int numOfIdeas = allTopics.getAllIdeas().size();
		numOfIdeas = (numOfIdeas<1 ? 0 : numOfIdeas);
		int minNumOfIdeas = (numOfIdeas<1 ? 0: 1);

		noteBankView.quizSetUpPage.setSliderMinMax(minNumOfIdeas, numOfIdeas );
		for(Topic topic: allTopics.getTopics()) {
			noteBankView.quizSetUpPage.addTopic(topic);
		}

	}

}
