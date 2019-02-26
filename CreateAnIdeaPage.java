import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 
 * CreateAnIdeaPage:
 * Page for creating an idea
 *  - Back button
 *  - Enter prompt
 *  - Specify if prompt is key word or question
 *  - Let user view, add & remove notes 
 *  - Let user select final note
 *  - Let user view, add and remove key-words
 * @author kimathi nyota
 *
 */
public class CreateAnIdeaPage extends JPanel {
	
	private JButton back;
	private JLabel createIdeaTitle;
	private JTextArea prompt;
	private JRadioButton keyWord;
	private JRadioButton question;
	private ButtonGroup keyWordQuestionButtonGroup;
	private JButton addNote;
	private JButton removeNotes;
	private JButton selectFinalNote;
	private NotesPanel notesPanel;
	private DefaultListModel<Note> finalNoteModel;
	private JList<String> listOfKeyWords;
	private DefaultListModel<String> keyWordModel;
	private JList<Note> finalNote;
	private JButton addKeyWord;
	private JButton removeKeyWord;
	private JButton saveIdea;
	private JPanel previousPage;
	private Idea editIdea;
	private JButton viewAllNotes;
	
	/**
	 * Getter for previous page
	 * @return previous page
	 */
	public JPanel getPreviousPage() {
		return this.previousPage;
	}
	
	public List<Note> getNotes(){
		return this.notesPanel.getAllNotes();
	}
	
	/**
	 * Setter for previous page
	 * @param previousPage
	 */
	public void setPreviousPage(JPanel previousPage) {
		this.previousPage = previousPage;
	}
	
	/**
	 * Getter for prompt
	 * @return prrompt
	 */
	public String getPromptText() {
		return this.prompt.getText();
	}
	
	/**
	 * Set the border text to input title
	 * @param title
	 */
	public void changeTitle(String title) {
		this.createIdeaTitle.setText(title.toUpperCase());
	}
	
	/**
	 * Page to set up and instantiate all necessary objects for CreateIdeaPage
	 * @param notes - set of notes used to initialise member JList: listOfNotes
	 * @param keyWords - set of key words used to initialise member JList: listOfKey
	 * @param promptInp - input prompt to initialise member prompt 
	 * @param isPromptKeyWord - used to set selected Key word or Question checkboxe
	 * @param finalNote - used to initialise member JList: finalNote
	 */
	private void setUpPage(HashMap<Note,Integer>notes, List<String>keyWords, String promptInp,Boolean isPromptKeyWord, Note finalNote) {
		this.setListOfNotes(notes);
		this.setListOfKeyWords(keyWords);
		
		/*
		 * Back button panel
		 * [Back]        
		 */
		JPanel backPanel = new JPanel(new GridLayout(1,4));
		back = new JButton("BACK");
		backPanel.add(back);
		backPanel.add(Box.createHorizontalGlue());
		backPanel.add(Box.createHorizontalGlue());
		backPanel.add(Box.createHorizontalGlue());
		
		/*
		 * [CREATE AN IDEA]
		 * label
		 */
		createIdeaTitle = new JLabel("CREATE AN IDEA");
		createIdeaTitle.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		createIdeaTitle.setHorizontalAlignment(JLabel.CENTER);
		
		/*
		 * Prompt: [                 ]
		 * text area
		 */
		prompt = new JTextArea(  (promptInp==null ? "" : promptInp )  );
		prompt.setLineWrap(true);
		prompt.setBorder( BorderFactory.createTitledBorder("Prompt"));
		JScrollPane promptPane = new JScrollPane(prompt);
		promptPane.setBorder(  BorderFactory.createEmptyBorder()  );
		
		/*
		 * Key word []   Question []
		 * check boxes
		 */
		keyWord = new JRadioButton("KEY WORD");
		question = new JRadioButton("QUESTION");
		keyWordQuestionButtonGroup = new ButtonGroup();
		keyWordQuestionButtonGroup.add(keyWord);
		keyWordQuestionButtonGroup.add(question);
		
		if(isPromptKeyWord==null)
			isPromptKeyWord = true;
		keyWordQuestionButtonGroup.setSelected(keyWord.getModel(), isPromptKeyWord);
		keyWordQuestionButtonGroup.setSelected(question.getModel(), !isPromptKeyWord);
		
		
		JPanel keyWordQuestionPanel = new JPanel(new GridLayout(1,2));
		keyWordQuestionPanel.add(keyWord);
		keyWordQuestionPanel.add(question);

		JPanel createIdeaNorthSection = new JPanel(new GridLayout(4,1));
		createIdeaNorthSection.add(backPanel);
		createIdeaNorthSection.add(createIdeaTitle);
		createIdeaNorthSection.add(promptPane);
		createIdeaNorthSection.add(keyWordQuestionPanel);

		/*
		 * ADD and REMOVE Note Buttons
		 *   [ ADD NOTE ]
		 *  [ REMOVE NOTE ]
		 */
		addNote = new JButton("ADD NOTE");
		removeNotes = new JButton("REMOVE NOTES");
		
		this.finalNoteModel = new DefaultListModel<Note>();
		if(finalNote!=null)
			finalNoteModel.addElement(finalNote);
		
		/*
		 *  [SELECT FINAL NOTE]  - Button
		 *  [                 ]  - Text Area
		 */
		
		
		this.finalNote = new JList<Note>(finalNoteModel);
		JScrollPane finalNotePane = new JScrollPane(this.finalNote);
		finalNotePane.setPreferredSize( new Dimension(30,50) );
		
		selectFinalNote = new JButton("SELECT FINAL NOTE");

		JPanel finalNotePanel = new JPanel( new GridLayout(2,1) );
		finalNotePanel.add(selectFinalNote);
		finalNotePanel.add(finalNotePane);
		
		JPanel buttonPanel = new JPanel(new GridLayout(3,1));
		buttonPanel.add(addNote);
		buttonPanel.add(removeNotes);
		
		
		buttonPanel.add(finalNotePanel);
		
		
		/*
		 * View all notes
		 */
		viewAllNotes = new JButton("View all notes");
		
		
		JPanel createIdeaNotePanel = new JPanel(new BorderLayout());
		createIdeaNotePanel.add(createIdeaNorthSection, BorderLayout.NORTH);
		createIdeaNotePanel.add(this.notesPanel, BorderLayout.CENTER);
		createIdeaNotePanel.add(buttonPanel, BorderLayout.EAST);
		
		
		JPanel createIdeaPanel = new JPanel(new GridLayout(3,1));
		createIdeaPanel.add(createIdeaNorthSection);
		createIdeaPanel.add(createIdeaNotePanel);
				
		keyWordModel = new DefaultListModel<String>();
		if(keyWords!=null) {
			for(String m: keyWords) {
				keyWordModel.addElement(m);
			}
		}
		
		/*
		 *  Key words text area
		 */
		listOfKeyWords = new JList<String>(keyWordModel) ;
		listOfKeyWords.setBorder( BorderFactory.createTitledBorder("KEY WORDS NEEDED"));
		JScrollPane keyWordsList = new JScrollPane(listOfKeyWords);
		/*
		 *  ADD and REMOVE key-words buttons
		 */
		JPanel keyWordButtonPanel = new JPanel(new GridLayout(2,1));
		addKeyWord = new JButton("ADD");
		removeKeyWord = new JButton("REMOVE");

		keyWordButtonPanel.add(addKeyWord);
		keyWordButtonPanel.add(removeKeyWord);
		
		JPanel createIdeaSouthSection = new JPanel(new BorderLayout());
		
		createIdeaSouthSection.add(viewAllNotes, BorderLayout.NORTH);
		createIdeaSouthSection.add(keyWordsList,BorderLayout.CENTER);
		createIdeaSouthSection.add(keyWordButtonPanel,BorderLayout.EAST);
		
		/*
		 *  SAVE buttons
		 */
		saveIdea = new JButton("SAVE IDEA");
		createIdeaSouthSection.add(saveIdea,BorderLayout.SOUTH);
		createIdeaPanel.add(createIdeaSouthSection);
		
		/*
		 * Page Display:
		 * ----------------------------------------
		 *              CREATE AN IDEA
		 * ----------------------------------------
		 * 	---- Prompt ---------------------------
		 * |_______________________________________|
		 * 			Key word []   Question []
		 *   _____ Note _____
		 *  |                | [      ADD NOTE     ] - Button             
		 *  |                | [    REMOVE NOTE    ] - Button
		 *  |                | [ SELECT FINAL NOTE ] - Button
		 *  |                |  ____ FINAL NOTE ___
		 *  |                | |                   | - Text
		 *  |________________| |___________________|
		 *   ___Key words ___
		 *  |                | [       ADD         ]
		 *  |                | [      REMOVE       ]
		 *  |________________|
		 *   ---------------------------------------
		 *  |             SAVE IDEA                 |
		 *  |_______________________________________|
		 */

		this.setLayout(new GridBagLayout());
		this.add(createIdeaPanel);
		createIdeaPanel.setPreferredSize(new Dimension(420,600));
		
		
	}
	
	/**
	 * Constructor for CreateAnIdeaPage built from below inputs
	 * (not built from an Idea object). Used in program to create
	 * a new Idea entirely.
	 * @param notes
	 * @param keyWords
	 * @param promptInp
	 * @param isPromptKeyWord
	 * @param finalNote
	 */
	public CreateAnIdeaPage(HashMap<Note,Integer>notes, List<String>keyWords, String promptInp,Boolean isPromptKeyWord, Note finalNote) {
		this.setUpPage(notes, keyWords, promptInp, isPromptKeyWord, finalNote);
		editIdea = null;

	}
	
	public CreateAnIdeaPage(List<Note>notes, List<String>keyWords, String promptInp,Boolean isPromptKeyWord, Note finalNote) {
		HashMap<Note, Integer> notesMap = new HashMap<Note, Integer>();
		for(Note n: notes) {
			notesMap.put(n, Idea.NON_PROMPT_NOTE);
		}
		this.setUpPage(notesMap, keyWords, promptInp, isPromptKeyWord, finalNote);
		
		editIdea = null;

	}
	
	public CreateAnIdeaPage() {

		this.setUpPage(null, null, null, null, null);
		
		editIdea = null;

	}
	
	/**
	 * Constructor for CreateAnIdeaPage built from an Idea object
	 * Used in program to edit an existing Idea.
	 * @param idea 
	 */
	public CreateAnIdeaPage(Idea idea) {
		this.setUpPage(idea.getNotesMap(), idea.getKeyWords(), idea.getPrompt(), idea.getPromptType(), idea.getFinalNote());
		editIdea = idea;
	}

	/**
	 * Resets current list of notes in the notes JList to input notesList
	 * @param notesList - input list of notes 
	 */
	private void setListOfNotes(Map<Note,Integer> notesList) {
		this.notesPanel = new NotesPanel(notesList);
	}
	
	/**
	 * @return all notes currently selected
	 */
	public List<Note> getSelectedNotes() {
		return this.notesPanel.getSelectedNotes();

	}
	
	/**
	 * Allows for input note to be displayed on FinalNote JList
	 * @param finalNote - note to be displayed as final note
	 */
	public void setFinalNote(Note finalNote) {
		this.finalNoteModel.clear();
		this.finalNoteModel.addElement(finalNote);
	}
	
	/**
	 * 
	 * @param note
	 */
	public void addNote(Note note) {
		this.notesPanel.addNote(note);
	}
	
	/**
	 * Remove note from the list of notes
	 * @param note
	 */
	public void removeNote(Note note) {
		this.notesPanel.removeNote(note);
		this.finalNoteModel.removeElement(note);
	}
	
	/**
	 * Allows user to enter a key word, and adds it to the 
	 */
	public void addKeyWord() {
		String keyWord = JOptionPane.showInputDialog("Enter Key Word: ");
		this.keyWordModel.addElement(keyWord);
	}
	
	/**
	 * Removes input keyWord from listOfKeyWords
	 * @param keyWord
	 */
	public void removeKeyWord(String keyWord) {
		this.keyWordModel.removeElement(keyWord);
	}
	
	/**
	 * Resets current list of keywords in the keyWords JList to input keyWordList
	 * @param keyWordList
	 */
	public void setListOfKeyWords(List<String> keyWordList) {
		keyWordModel = new DefaultListModel<String>();
		if(keyWordList!=null) {
			for(String m: keyWordList) 
				keyWordModel.addElement(m);
			listOfKeyWords = new JList<String>(keyWordModel) ;
		}
	}
	
	/**
	 * 
	 * @return selected keywords as list
	 */
	public List<String> getSelectedKeyWords() {
		return this.listOfKeyWords.getSelectedValuesList();
	}

	/**
	 * Creates idea, or modifies the idea to be edited
	 * using currently supplied data to the page
	 * @return Idea
	 */
	public Idea getIdea() {
		String prompt = this.prompt.getText();
		List<String>listKeyWords = new ArrayList<String>();
		for(Object keyWords: this.keyWordModel.toArray() ) {
			if(keyWords!=null && !( (String) keyWords).equals("null")) {
				listKeyWords.add((String) keyWords);
			}
			
		}
		
		Note finalNote;
		if(this.finalNote.getModel().getSize()>0)
			finalNote = this.finalNote.getModel().getElementAt(0);
		else
			finalNote = null;
		boolean isPromptKeyWord = this.keyWord.isSelected();
		
		if(this.editIdea!=null) {
			// implies this page has been built from an Idea
			this.editIdea.resetIdea(this.notesPanel.getNotes(), listKeyWords, prompt, isPromptKeyWord, finalNote);
			return this.editIdea;
		}
		
		Idea createdIdea = new Idea(prompt, isPromptKeyWord,null);
		createdIdea.addKeyWord(listKeyWords);
		
		HashMap<Note,Integer> notes = this.notesPanel.getNotes();
	
		createdIdea.addNotes(notes);
		createdIdea.setFinalNote(finalNote);
		
		return createdIdea;
	}
	
	/*
	 * Lets users provide isteners for all the buttons
	 */
	public void addBackActionListener(ActionListener backActionListener) {
		this.back.addActionListener(backActionListener);
	}

	public void addAddNoteActionListener(ActionListener addNoteActionListener) {
		this.addNote.addActionListener(addNoteActionListener);
	}
	
	public void addRemoveNoteActionListener(ActionListener removeNoteActionListener) {
		this.removeNotes.addActionListener(removeNoteActionListener);
	}
	
	public void addSelectFinalNoteActionListener(ActionListener selectFinalNoteActionListener) {
		this.selectFinalNote.addActionListener(selectFinalNoteActionListener);
	}
	
	public void addAddKeyWordActionListener(ActionListener addKeyWordActionListener) {
		this.addKeyWord.addActionListener(addKeyWordActionListener);
	}
	
	public void addRemoveKeyWordsActionListener(ActionListener removeKeyWordsActionListener) {
		this.removeKeyWord.addActionListener(removeKeyWordsActionListener);
	}
	
	public void addSaveIdeaActionListener(ActionListener saveIdeaActionListener) {
		this.saveIdea.addActionListener(saveIdeaActionListener);
	}
	
	public void addViewAllNotes(ActionListener viewAllNotesActionListener) {
		this.viewAllNotes.addActionListener(viewAllNotesActionListener);
	}
}