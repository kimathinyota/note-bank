import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;


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
	private QuizReader quizReader;
	private SubjectReader subjectReader;
	private Date quizCreatedTime;
	private TextExcerpt evaluatedAnswer;
	private double maxWeightedAverageSimulationPerTime;

	long hour = (60*60*1000);
	long day = (hour*24);
	long week = (day*7);
	long fortnight = (week*2);
	long month = (fortnight*2);	

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
		this.noteBankView.removeNote(note);
		if(noteBankView.manageNoteBankPage.getSelectedSubject().equals("All")) {
			allTopics.removeNote(note);	
		}
	}
	
	public void addNote(Note note, String subject) {
		this.noteBankView.addNote(note, subject);
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
	
	public void addListeners(SelectNotesPage selectNotesPage) {
		selectNotesPage.addAddNotesActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(selectNotesPage.getAllNotes().size()>0) {
							ViewNotesPage viewNotesPage = new ViewNotesPage(true,selectNotesPage.getAllNotes(),pathToDirectory);
							viewNotesPage.setPreviousPage(selectNotesPage);
							addListeners(viewNotesPage);
							noteBankView.addFixedPage(viewNotesPage, "Select notes to add to Idea");
							viewNotesPage.setCreateIdeaButtonName("Add selected notes");
						}
					}	
				}
				);
		selectNotesPage.addClearActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						selectNotesPage.clear();	
					}
				}
				);
		
		selectNotesPage.addNoteTextDocumentListener(
				new DocumentListener() {
					public void displayNotes() {
						selectNotesPage.displayNotesContainingWords(selectNotesPage.getTextNote());
					}
					@Override
					public void insertUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						this.displayNotes();
						
					}
					@Override
					public void removeUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						this.displayNotes();
						
					}
					@Override
					public void changedUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						this.displayNotes();
					}
				}
				);
		
		selectNotesPage.addRemoveNotesActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						for(Note n: selectNotesPage.getSelectedNotes()) {
							selectNotesPage.removeNote(n);
						}
					}
				}
				);
		
		selectNotesPage.addRestoreListActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						selectNotesPage.restoreList();	
					}
				}
				);
		
		selectNotesPage.addBackActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JPanel prevPage = selectNotesPage.getPreviousPage();
						noteBankView.deletePage(selectNotesPage);
						noteBankView.restorePages();
						noteBankView.switchPage(prevPage);
					}
				}
				);
		
		selectNotesPage.addUploadNoteActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						UploadNotePage uploadNotePage = new UploadNotePage(pathToDirectory);
						uploadNotePage.setPreviousPage(selectNotesPage);
						addListeners(uploadNotePage);
						noteBankView.addFixedPage(uploadNotePage, "Upload Note");
					}
					
				}
				);
	}
	
	public void addListeners(QuizResultsPage resultsPage) {
		resultsPage.addPickIdeaActionListeners(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Idea idea = resultsPage.getSelectedIdea();
						
						long currentTime = System.currentTimeMillis();
						double averageCompletionTime = quizReader.getAverageCompletionTime(idea, new Date(currentTime-day) );
						Integer[] ideaSimulationTimes = { quizReader.getNumberOfTimes(idea, new Date(currentTime - hour) ), 
														 quizReader.getNumberOfTimes(idea, new Date(currentTime - day) ), 
														 quizReader.getNumberOfTimes(idea, new Date(currentTime - week) ), 
														 quizReader.getNumberOfTimes(idea, new Date(currentTime - week) ),
														 quizReader.getNumberOfTimes(idea, new Date(currentTime - month) ) };
						
						double weightedIdeasTimes = 0;
						for(int i=0; i<ideaSimulationTimes.length; i++) {
							weightedIdeasTimes += (100*Math.pow(2, (0-i))*ideaSimulationTimes[i]);
						}
						
						weightedIdeasTimes/=maxWeightedAverageSimulationPerTime;
						
						if(weightedIdeasTimes>100)
							weightedIdeasTimes = 100;
						
						
						double userSatisfactionLastDay = quizReader.getAverageConfidenceLevel(idea, new Date(currentTime-day));
						
						double readinessLevel =  (((100*userSatisfactionLastDay)/4) + weightedIdeasTimes)/2;
						
						resultsPage.setAnalysis((int) averageCompletionTime, ideaSimulationTimes, userSatisfactionLastDay, (int) readinessLevel);
						
					}	
				}
				);
		resultsPage.addFinishQuizActionListeners(
					new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							noteBankView.replace(noteBankView.quizSetUpPage, "Revision Quiz" );
						}
						
					}
				);
		
		resultsPage.addSeeIdeaActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Idea idea = resultsPage.getSelectedIdea();
						CreateAnIdeaPage createAnIdeaPage = new CreateAnIdeaPage(idea);
						addListeners(createAnIdeaPage);
						createAnIdeaPage.changeTitle("Edit Idea");
						createAnIdeaPage.setPreviousPage(resultsPage);
						noteBankView.addFixedPage(createAnIdeaPage, "Edit Idea");
						save();	
					}	
				}
				);
	}
	
	
	public void addListeners(QuizAnswerEvaluationPage quizAnswerEvaluationPage) {
		quizAnswerEvaluationPage.addQuitQuizActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						noteBankView.replace(noteBankView.quizSetUpPage, "Revision Quiz" );
					}
				}
				);
		
		quizAnswerEvaluationPage.addNextMarkActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Date finishIdeaDate = new Date();
						finishIdeaDate.setTime( System.currentTimeMillis() );
						quizReader.save(currentQuiz.get(currentIdeaPointer), (System.currentTimeMillis() - quizCreatedTime.getTime())/1000, 
								quizAnswerEvaluationPage.getReadinessLevel(), finishIdeaDate);
						
						currentIdeaPointer+=1;
						
						if(currentIdeaPointer<currentQuiz.size()) {
							quizCreatedTime = new Date(System.currentTimeMillis());
							QuizResponsePage responsePage = new QuizResponsePage(currentQuiz.get(currentIdeaPointer));
							addListeners(responsePage);
							noteBankView.replace(responsePage, "Revision Quiz");
	
						}else {
							QuizResultsPage resultsPage = new QuizResultsPage(currentQuiz);
							addListeners(resultsPage);
							resultsPage.refreshIdeasComboBox();
							noteBankView.replace(resultsPage, "Revision Quiz" );
						}
					}
				}
				
				);
		quizAnswerEvaluationPage.addSetToFinalNoteActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							if(evaluatedAnswer!=null) {
								removeNote(evaluatedAnswer);
							}
							Idea currentIdea = currentQuiz.get(currentIdeaPointer);
							evaluatedAnswer = new TextExcerpt( ("EvaluatedAnswer ("+currentIdea.getPrompt()+")").replaceAll("\\s","-"),
										quizAnswerEvaluationPage.getAnswer(), pathToDirectory);
							
							addNote(evaluatedAnswer, noteBankView.manageNoteBankPage.getAllSubjects(currentIdea.getNotes()));
							currentIdea.setFinalNote(evaluatedAnswer);
							
							updateSubjectFile();
							save();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					
				}
				);
		
		quizAnswerEvaluationPage.addViewFinalNoteOnlyActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Note finalNote = currentQuiz.get(currentIdeaPointer).getFinalNote();
						if(finalNote!=null) {
							ArrayList<Note> finalNoteList = new ArrayList<Note>();
							finalNoteList.add(finalNote);
							ViewNotesPage viewNotesPage = new ViewNotesPage(false,finalNoteList,pathToDirectory);
							viewNotesPage.setPreviousPage(quizAnswerEvaluationPage);
							addListeners(viewNotesPage);
							noteBankView.addFixedPage(viewNotesPage, "View Final Note");
						}else {
							JOptionPane.showMessageDialog(quizAnswerEvaluationPage, "No final note has been selected");
						}
					}
				}
				);
		
		quizAnswerEvaluationPage.addViewNotesActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ViewNotesPage viewNotesPage = new ViewNotesPage(false,currentQuiz.get(currentIdeaPointer).getNotes(),pathToDirectory);
						viewNotesPage.setPreviousPage(quizAnswerEvaluationPage);
						addListeners(viewNotesPage);
						noteBankView.addFixedPage(viewNotesPage, "View Ideas Notes");	
					}
				}
				);		
		
		quizAnswerEvaluationPage.addEvaluateAnswerDocumentListener(
				new DocumentListener() {
					public void anyChange() {
						List<String> keyWords = currentQuiz.get(currentIdeaPointer).getKeyWords();
						String answer = quizAnswerEvaluationPage.getAnswer();
						quizAnswerEvaluationPage.clearMissingKeyWords();
						for(String keyWord: keyWords) {
							if(!answer.toLowerCase().contains(keyWord.toLowerCase())) {
								quizAnswerEvaluationPage.addMissingKeyWord(keyWord);
							}
						}
					}
					@Override
					public void insertUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						anyChange();
					}
					@Override
					public void removeUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						anyChange();
					}
					@Override
					public void changedUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						anyChange();
					}
				}
				);
		
	}
	
	public void addListeners(QuizResponsePage responsePage) {
		responsePage.addSubmitAnswerActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String evaluatedAnswer = responsePage.getResponse();
						QuizAnswerEvaluationPage quizAnswerEvaluationPage = new QuizAnswerEvaluationPage(currentQuiz.get(currentIdeaPointer),evaluatedAnswer);
						addListeners(quizAnswerEvaluationPage);
						quizAnswerEvaluationPage.refreshEvaluateAnswer();
						noteBankView.replace(quizAnswerEvaluationPage, "Revision Quiz" );
					}
				}
				);
		
		responsePage.addQuitQuizActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						noteBankView.replace(noteBankView.quizSetUpPage, "Revision Quiz" );
					}
				}
				);
		
		responsePage.addViewPromptNotesActionListener( 
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if(responsePage.getPromptNotes().isEmpty()) {
							JOptionPane.showMessageDialog(responsePage, "No prompt notes");
							return;
						}
						ViewNotesPage viewNotesPage = new ViewNotesPage(false,responsePage.getPromptNotes(),pathToDirectory);
						viewNotesPage.setPreviousPage(responsePage);
						addListeners(viewNotesPage);
						noteBankView.addFixedPage(viewNotesPage, "View Prompt Notes");	
						
					}
					
				}
				);
		
	}
	
	public void addListeners(QuizSetUpPage quizSetUpPage) {		
		quizSetUpPage.addChooseTopicActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Topic selectedItem = quizSetUpPage.getTopicChosen();
						if(selectedItem!=null) {
							noteBankView.quizSetUpPage.setSliderMinMax(1, selectedItem.getAllIdeas().size());
						}		
					}
				}
				);
		
		quizSetUpPage.addCreateQuizActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						/**
						 * private Map<Idea,Integer> simulatedIdeas;
						 * private List<Idea> currentQuiz;
						 */
						int questionType = quizSetUpPage.getQuestionType();
						int noQuestions = quizSetUpPage.getNoQuestions();
						
						Topic topic = quizSetUpPage.getTopicChosen();
						currentQuiz = new ArrayList<Idea>();
						/*
						 * Untested ideas
						 * Only key words
						 * Only questions
						 * All
						 */
						for(Idea a: topic.getAllIdeas()) {
							switch(questionType) {
							case 0: if(quizReader.getNumberOfTimes(a, new Date(System.currentTimeMillis() - day) )>0)
										currentQuiz.add(a);
									break;
							case 1: if(a.getPromptType())
										currentQuiz.add(a);
									break;
							case 2: if(!a.getPromptType())
										currentQuiz.add(a);
									break;
							default: currentQuiz.add(a);
									 break;
							}
						}
						Collections.shuffle(currentQuiz);
						
						for(int i=0; i<(currentQuiz.size() - noQuestions); i++) 
							currentQuiz.remove(i);
						
						currentIdeaPointer = 0;
						quizCreatedTime = new Date(System.currentTimeMillis());
						
						if(currentQuiz.size()>0) {
							QuizResponsePage responsePage = new QuizResponsePage(currentQuiz.get(currentIdeaPointer));
							addListeners(responsePage);
							noteBankView.replace(responsePage, "Revision Quiz");
						}else {
							JOptionPane.showMessageDialog(quizSetUpPage, "Can't create empty Quiz. ");
						}
					}					
				}
				);	
	}
	
	public void addListeners(ManageIdeasPage manageIdeasPage) {
		manageIdeasPage.addCombineActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						manageIdeasPage.combineNodes(manageIdeasPage.getSelected());
						save();
						
					}
				}
				);
		
		manageIdeasPage.addDisbandActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						manageIdeasPage.disbandNodes(manageIdeasPage.getSelected());
						
						save();
					}
				}
				);
		
		manageIdeasPage.addDeleteActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(manageIdeasPage.getSelected().size()>0) {
							Object deletedIdea = manageIdeasPage.deleteNode(manageIdeasPage.getSelected().get(0));
							if(deletedIdea instanceof Idea)
								quizReader.delete((Idea) deletedIdea);
							
							save();
							
						}
					}
				}
				);
		
		manageIdeasPage.addEditActionListener( 
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Idea idea = manageIdeasPage.getSelectedIdea();
						if(idea!=null) { 
							CreateAnIdeaPage createAnIdeaPage = new CreateAnIdeaPage(idea);
							addListeners(createAnIdeaPage);
							createAnIdeaPage.changeTitle("Edit Idea");
							createAnIdeaPage.setPreviousPage(manageIdeasPage);
							noteBankView.addFixedPage(createAnIdeaPage, "Edit Idea");
							manageIdeasPage.refresh();
							
							save();
						}else {
							JOptionPane.showMessageDialog(manageIdeasPage, "No Idea selected");
						}
					}
				}
				);
		
		manageIdeasPage.addMoveActionListener( 
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						manageIdeasPage.moveNode(manageIdeasPage.getSelected());
						save();
					}
					
				}
				);
		
		manageIdeasPage.addViewAllIdeasActionListener( 
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(noteBankView.manageNoteBankPage.getNotes(), manageIdeasPage.getSelectedTopics());
					}
					
				}
				);
		
	}
	
	private void addListeners(UploadNotePage uploadNotePage) {
		uploadNotePage.addTextUploadActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							TextExcerpt text = uploadNotePage.getTextExcerpt();
							if(text!=null) {
								addNote(text);
								uploadNotePage.resetTextNote();
								uploadNotePage.nextPanel();
				
								JPanel prevPage = uploadNotePage.getPreviousPage();
								if(prevPage instanceof CreateAnIdeaPage) {
									((CreateAnIdeaPage) prevPage).addNote(text);
								}else if(prevPage instanceof SelectNotesPage) {
									((SelectNotesPage) prevPage).addNote(text);
								}
								noteBankView.manageNoteBankPage.refreshNotesListView();
								noteBankView.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(noteBankView.manageNoteBankPage.getNotes(), null );
								save();
								updateSubjectFile();
								
							}else {
								JOptionPane.showMessageDialog(uploadNotePage, "Make sure you have chosen a valid note name");
							}
						}catch(IOException e1) {
							
						}
					}
				}
				);
		
		uploadNotePage.addChooseImageActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFileChooser fc = new JFileChooser();
						fc.addChoosableFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
						fc.setAcceptAllFileFilterUsed(false);
						int returnVal = fc.showOpenDialog(uploadNotePage);
						if(returnVal == JFileChooser.APPROVE_OPTION) {
							uploadNotePage.setChosenImage(fc.getSelectedFile());
							uploadNotePage.setImageName(fc.getSelectedFile().getName());
						}	
					}
				}
				);
		
		uploadNotePage.addChoosePDFActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFileChooser fc = new JFileChooser();
						fc.addChoosableFileFilter(new FileNameExtensionFilter("PDF File", "pdf"));
						fc.setAcceptAllFileFilterUsed(false);
						int returnVal = fc.showOpenDialog(uploadNotePage);
						if(returnVal == JFileChooser.APPROVE_OPTION) {
							uploadNotePage.setChosenPDF(fc.getSelectedFile());
							uploadNotePage.setPDFName(fc.getSelectedFile().getName());
						}	
					}
				}
				);
		
		uploadNotePage.addUploadImageToBankActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							Image imageNote = uploadNotePage.getImage();
							if(imageNote!=null) {
								addNote(imageNote);
								uploadNotePage.resetImageNote();
								
								JPanel prevPage = uploadNotePage.getPreviousPage();
								if(prevPage instanceof CreateAnIdeaPage) {
									((CreateAnIdeaPage) prevPage).addNote(imageNote);
								}else if(prevPage instanceof SelectNotesPage) {
									((SelectNotesPage) prevPage).addNote(imageNote);
								}
								
								uploadNotePage.nextPanel();
								noteBankView.manageNoteBankPage.refreshNotesListView();
								noteBankView.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(noteBankView.manageNoteBankPage.getNotes(), null );
								save();
								updateSubjectFile();
								
								
							}else {
								JOptionPane.showMessageDialog(uploadNotePage, "Make sure you have chosen a valid note name and have selected a file to add ");
							}
						} catch (IOException e1) {
							
						}
					}
				}
				);
		
		uploadNotePage.addUploadPDFToBankActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							Book pdfNote = uploadNotePage.getPDF();
							if(pdfNote!=null) {
								addNote(pdfNote);
								
								uploadNotePage.resetPDFNote();
								JPanel prevPage = uploadNotePage.getPreviousPage();
								if(prevPage instanceof CreateAnIdeaPage) {
									((CreateAnIdeaPage) prevPage).addNote(pdfNote);
								}else if(prevPage instanceof SelectNotesPage) {
									((SelectNotesPage) prevPage).addNote(pdfNote);
								}
								
								uploadNotePage.nextPanel();
								noteBankView.manageNoteBankPage.refreshNotesListView();
								noteBankView.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(noteBankView.manageNoteBankPage.getNotes(), null );
								save();
								updateSubjectFile();

							}else {
								JOptionPane.showMessageDialog(uploadNotePage, "Make sure you have chosen a valid note name and have selected a file to add ");
							}
						} catch (IOException e1) {
							
						}
					}
				}
				);
		
		uploadNotePage.addBackActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JPanel prevPage = uploadNotePage.getPreviousPage();
						noteBankView.deletePage(uploadNotePage);
						noteBankView.restorePages();
						noteBankView.switchPage(prevPage);
						
					}	
				}
				);
	}
	
	private void addListeners( ManageNoteBankPage manageNoteBankPage ) {
		manageNoteBankPage.addUploadActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						UploadNotePage uploadNotePage = new UploadNotePage(pathToDirectory);
						uploadNotePage.setPreviousPage(manageNoteBankPage);
						addListeners(uploadNotePage);
						noteBankView.addFixedPage(uploadNotePage, "Upload Note");
						manageNoteBankPage.refreshNotesListView();
						noteBankView.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(manageNoteBankPage.getNotes(), null );
						
					}
				}
				);
		manageNoteBankPage.addRemoveNotesActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						for(Note n: manageNoteBankPage.getSelectedNotes()) {
							removeNote(n);
						}
						save();
						updateSubjectFile();
						manageNoteBankPage.refreshNotesListView();
						noteBankView.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(manageNoteBankPage.getNotes(), null );
					}
				}
				);
		manageNoteBankPage.addCreateIdeaActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(manageNoteBankPage.getSelectedNotes().size()>0) {
							ViewNotesPage viewNotesPage = new ViewNotesPage(true,manageNoteBankPage.getSelectedNotes(),pathToDirectory);
							viewNotesPage.setPreviousPage(manageNoteBankPage);
							addListeners(viewNotesPage);
							noteBankView.addFixedPage(viewNotesPage, "Select Notes to Add to Idea");
						}else {
							CreateAnIdeaPage createAnIdeaPage = new CreateAnIdeaPage();
							createAnIdeaPage.setPreviousPage(manageNoteBankPage);
							addListeners(createAnIdeaPage);
							noteBankView.addFixedPage(createAnIdeaPage, "Create an Idea");
							
						}
					
					}
				}
				);
		
		manageNoteBankPage.addNoteTextDocumentListener( 
				new DocumentListener() {
					
					public void displayNotes() {
						manageNoteBankPage.displayNotesContainingWords(manageNoteBankPage.getTextNote());
					}
					@Override
					public void insertUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						this.displayNotes();
						
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						this.displayNotes();
						
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						this.displayNotes();
						
					}
					
				}
				);
		
		manageNoteBankPage.addViewNotesActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(manageNoteBankPage.getSelectedNotes().size()>0) {
							ViewNotesPage viewNotesPage = new ViewNotesPage(false,manageNoteBankPage.getSelectedNotes(),pathToDirectory);
							viewNotesPage.setPreviousPage(manageNoteBankPage);
							addListeners(viewNotesPage);
							
							noteBankView.addFixedPage(viewNotesPage, "View Notes");
						}else {
							JOptionPane.showMessageDialog(manageNoteBankPage, "You must select notes to view");
						}
						
						
					}
				}
				);
		
		manageNoteBankPage.addSaveSubjectActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						manageNoteBankPage.createNewSubject();
						manageNoteBankPage.refreshNotesListView();
						noteBankView.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(manageNoteBankPage.getNotes(), null );
						updateSubjectFile();

					}
					
				}
				);
		
		manageNoteBankPage.addSubjectChangeActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						List<Note> selectedNotes = manageNoteBankPage.getSelectedNotes();
						String selectedSubject = manageNoteBankPage.getSelectedSubject();
		
						boolean shouldMoveSelectedNotesToNextSubject= manageNoteBankPage.shouldMoveSelectedToNextSubject() 
								&& selectedNotes.size()>0 
								&& JOptionPane.showConfirmDialog(manageNoteBankPage, "Are you sure you want move the selected notes to " + selectedSubject+ " ?" )==JOptionPane.YES_OPTION; ;
			
						if(shouldMoveSelectedNotesToNextSubject) {
							for(Note note: selectedNotes) {
								manageNoteBankPage.addNote(note, selectedSubject);		
							}
							manageNoteBankPage.resetMoveSelectedToNextSubject();
	
						}
						if(selectedSubject.contains("+")) {
							manageNoteBankPage.startPlusBuffer();
							manageNoteBankPage.clearNotes();
						}else {
							manageNoteBankPage.refreshNotesListView();
							noteBankView.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(manageNoteBankPage.getNotes(), null );
						}	
						manageNoteBankPage.setCurrentSubject(manageNoteBankPage.getSelectedSubject());
						updateSubjectFile();
					}
				}
				);
		manageNoteBankPage.addDeleteSubjectActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						manageNoteBankPage.deleteSubject(manageNoteBankPage.getSelectedSubject());
						noteBankView.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(manageNoteBankPage.getNotes(), null );
						manageNoteBankPage.refreshNotesListView();
						updateSubjectFile();
					}
					
				}
				);

	}
	
	private void addListeners(ViewNotesPage viewNotesPage) {
		viewNotesPage.addCreateIdeaActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					JPanel prevPage = viewNotesPage.getPreviousPage();
					if( prevPage instanceof SelectNotesPage ) {
						JPanel prevPrevPage = ( (SelectNotesPage) prevPage ).getPreviousPage();
						if(prevPrevPage instanceof CreateAnIdeaPage) {
							CreateAnIdeaPage createIdeaPage = (CreateAnIdeaPage) prevPrevPage;
							for(Note n: viewNotesPage.getSelectedNotes()) {
								createIdeaPage.addNote(n);
							}
						}
						noteBankView.deletePage(viewNotesPage);
						noteBankView.restorePages();
						noteBankView.deletePage(prevPage);
						noteBankView.restorePages();
						noteBankView.switchPage(prevPrevPage);
					}else {
						CreateAnIdeaPage createAnIdea = new CreateAnIdeaPage(viewNotesPage.getSelectedNotes(),												null,null,null, null);
						createAnIdea.setPreviousPage(viewNotesPage);
						addListeners(createAnIdea);
						noteBankView.addFixedPage(createAnIdea, "Create Idea");
					}
				}
			}
				);
		
		viewNotesPage.addNextPageActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						viewNotesPage.nextImage();
					}
				}
					);
		
		viewNotesPage.addPreviousPageActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						viewNotesPage.prevImage();
					}
				}
					);
		
		viewNotesPage.addAddToIdeaCheckBoxItemListener(
				new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if(viewNotesPage.isAddToIdeaChecked()) {
							viewNotesPage.selectCurrentImage();
						}else {
							viewNotesPage.deselectCurrentImage();
						}
					}
				}
				);
		
		viewNotesPage.addBackActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						JPanel prevPage = viewNotesPage.getPreviousPage();
						noteBankView.deletePage(viewNotesPage);
						noteBankView.restorePages();
						noteBankView.switchPage(prevPage);
					}
				}
				);
		
		viewNotesPage.addClearSelectButtonActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						viewNotesPage.manageToggle();
						
					}
					
				}
				
				);

	}
	
	private void addListeners(CreateAnIdeaPage createAnIdeaPage) {
		
		createAnIdeaPage.addAddNoteActionListener(
			new ActionListener(){			
				public void actionPerformed(ActionEvent e) {
					
					SelectNotesPage selectNotesPage = new SelectNotesPage(noteBankView.manageNoteBankPage.getNotes());
					addListeners(selectNotesPage);
					selectNotesPage.setPreviousPage(createAnIdeaPage);
					noteBankView.addFixedPage(selectNotesPage, "Select notes to add to Idea");
					
				}		
			}	
		);
		
		createAnIdeaPage.addAddKeyWordActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						createAnIdeaPage.addKeyWord();
					}	
				}	
			);
		
		createAnIdeaPage.addRemoveKeyWordsActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					for(String keyWord: createAnIdeaPage.getSelectedKeyWords()) {
						createAnIdeaPage.removeKeyWord(keyWord);
					}
				}
			}		
		);
		
		createAnIdeaPage.addRemoveNoteActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					for(Note note: createAnIdeaPage.getSelectedNotes()  ) {
						createAnIdeaPage.removeNote(note);
					}
				}
			}	
		);
		
		createAnIdeaPage.addSelectFinalNoteActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if(createAnIdeaPage.getSelectedNotes().size()>0) {
							createAnIdeaPage.setFinalNote( createAnIdeaPage.getSelectedNotes().get(0)  );
						}
					}
				}	
			);
		
		createAnIdeaPage.addViewAllNotes(
				new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						ViewNotesPage viewNotesPage = new ViewNotesPage(false,createAnIdeaPage.getNotes(),pathToDirectory);
						viewNotesPage.setPreviousPage(createAnIdeaPage);
						addListeners(viewNotesPage);
						noteBankView.addFixedPage(viewNotesPage, "Select Notes to Add to Idea");
					
					}
					
				}
				);
		
		createAnIdeaPage.addSaveIdeaActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if(createAnIdeaPage.getPromptText().length()>3) {
							Idea createdIdea = createAnIdeaPage.getIdea();
							
							if(createAnIdeaPage.getPreviousPage() instanceof ManageNoteBankPage){
								noteBankView.manageIdeasPage.addToRoot(createdIdea);
							}
							
							if(createAnIdeaPage.getPreviousPage() instanceof ViewNotesPage) {
								noteBankView.manageIdeasPage.addToRoot(createdIdea);
								noteBankView.deletePage(createAnIdeaPage);
								noteBankView.restorePages();
								noteBankView.deletePage(createAnIdeaPage.getPreviousPage());
								noteBankView.restorePages();
								noteBankView.switchPage( ((ViewNotesPage) createAnIdeaPage.getPreviousPage() ).getPreviousPage() ); 
								
							}else {
								//Assumes it is being edited
								noteBankView.deletePage(createAnIdeaPage);
								noteBankView.restorePages();
								noteBankView.switchPage(createAnIdeaPage.getPreviousPage());
							}
							noteBankView.manageIdeasPage.refresh();
							save();
							
						}else {
							JOptionPane.showMessageDialog(createAnIdeaPage, "Invalid prompt entered");
						}
					}
				}	
				);
		
		createAnIdeaPage.addBackActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						noteBankView.deletePage(createAnIdeaPage);
						noteBankView.restorePages();
						noteBankView.switchPage(createAnIdeaPage.getPreviousPage());
					}
				}
				);
		
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
			JOptionPane.showMessageDialog(noteBankView, "An error occurred. Note bank can't be loaded");
			e.printStackTrace(); 
		} 
		
		noteBankView = new NoteBankView(allTopics, allNotes);
		int numOfIdeas = allTopics.getAllIdeas().size();
		int minNumOfIdeas = 1;
		if(numOfIdeas<1) {
			minNumOfIdeas = 0;
			numOfIdeas = 0;
		}
		noteBankView.quizSetUpPage.setSliderMinMax(minNumOfIdeas, numOfIdeas );
		for(Topic topic: allTopics.getTopics()) {
			noteBankView.quizSetUpPage.addTopic(topic);
		}
		addListeners(noteBankView.manageNoteBankPage);
		addListeners(noteBankView.manageIdeasPage);
		addListeners(noteBankView.quizSetUpPage);
	}
}
