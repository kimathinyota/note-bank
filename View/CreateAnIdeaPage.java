package View;

import Model.Idea;
import Model.Note;
import View.Handlers.CreateAnIdeaEventHandler;
import com.sun.tools.corba.se.idl.constExpr.Not;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

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

	private JButton removeNotes;
	private JButton selectFinalNote;
	private JButton deselectFinalNote;

	private NotesPanel notesPanel;

	private JList<String> listOfKeyWords;
	private SearchList allNotes;
	private DefaultListModel<Note> allNotesList;
	private DefaultListModel<String> keyWordModel;
	private JButton addKeyWord;
	private JButton removeKeyWord;

	private JButton saveIdea;

	private JPanel previousPage;
	private Idea editIdea;
	private JButton viewAllNotes;

	private JLabel chosenSelectedFinalNote;
	private Note finalNote;

	public boolean isEdited(){
		return editIdea!=null;
	}
	
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
	private void setUpPage(CreateAnIdeaEventHandler handler,HashMap<Note,Integer>notes, List<String>keyWords, String promptInp,
						   Boolean isPromptKeyWord, Note finalNote, List<Note>allNotesList) {
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
		//createIdeaNorthSection.add(createIdeaTitle);
		createIdeaNorthSection.add(promptPane);
		createIdeaNorthSection.add(keyWordQuestionPanel);

		/*
		 * ADD and REMOVE Model.Note Buttons
		 *   [ ADD NOTE ]
		 *  [ REMOVE NOTE ]
		 */

		removeNotes = new JButton("REMOVE NOTES");

		
		/*
		 *  [SELECT FINAL NOTE]  - Button
		 *  [                 ]  - Text Area
		 */
		
		

		selectFinalNote = new JButton("SELECT");

		chosenSelectedFinalNote = new JLabel("SELECTED: ");
		chosenSelectedFinalNote.setHorizontalAlignment(SwingConstants.CENTER);
		deselectFinalNote = new JButton("DESELECT");

		this.setFinalNote(finalNote);

		JPanel selectPanel = new JPanel(new BorderLayout());
		JScrollPane pane = new JScrollPane(chosenSelectedFinalNote);
		pane.setBorder( BorderFactory.createEmptyBorder() );
		selectPanel.add(pane,BorderLayout.CENTER);
		Box selectDeselect = Box.createHorizontalBox();
		selectDeselect.add(selectFinalNote);
		selectDeselect.add(deselectFinalNote);
		selectPanel.add(selectDeselect,BorderLayout.EAST);
		createIdeaNorthSection.add(selectPanel);


		/*
		 * View all notes
		 */
		viewAllNotes = new JButton("View all notes");
		
		JPanel createIdeaNotePanel = new JPanel(new BorderLayout());
		createIdeaNotePanel.add(createIdeaNorthSection, BorderLayout.NORTH);

		this.allNotesList = new DefaultListModel<>();
		this.allNotes = new SearchList<Note>(allNotesList);

		JPanel panelNotes = new JPanel(new GridLayout(1,2));
		panelNotes.add(this.notesPanel);
		panelNotes.add(this.allNotes);

		createIdeaNotePanel.add(panelNotes, BorderLayout.CENTER);

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

		allNotes.addMouseListenerForList(
				new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount()==2){
							List<Note> notes = allNotes.getSelectedValuesList();
							notesPanel.addNote(notes);
						}
					}
					@Override
					public void mousePressed(MouseEvent e) {
					}
					@Override
					public void mouseReleased(MouseEvent e) {
					}
					@Override
					public void mouseEntered(MouseEvent e) {
					}
					@Override
					public void mouseExited(MouseEvent e) {
					}
				}
		);


		/*
		 * Page Display:
		 * ----------------------------------------
		 *              CREATE AN IDEA
		 * ----------------------------------------
		 * 	---- Prompt ---------------------------
		 * |_______________________________________|
		 * 			Key word []   Question []
		 *   _____ Model.Note _____
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


		this.removeNotes.addActionListener(handler::removeNote);
		this.addKeyWord.addActionListener(handler::addKeyWord);
		this.removeKeyWord.addActionListener(handler::removeKeyWord);
		this.selectFinalNote.addActionListener(handler::selectFinalNote);
		this.saveIdea.addActionListener(handler::saveIdea);
		this.viewAllNotes.addActionListener(handler::viewAllNotes);
		this.back.addActionListener(handler::back);

		this.setLayout(new GridBagLayout());
		this.add(createIdeaPanel);
		createIdeaPanel.setPreferredSize(new Dimension(420,600));
		
		
	}
	
	/**
	 * Constructor for View.CreateAnIdeaPage built from below inputs
	 * (not built from an Model.Idea object). Used in program to create
	 * a new Model.Idea entirely.
	 * @param notes
	 * @param keyWords
	 * @param promptInp
	 * @param isPromptKeyWord
	 * @param finalNote
	 */
	public CreateAnIdeaPage(CreateAnIdeaEventHandler handler,HashMap<Note,Integer>notes, List<String>keyWords, String promptInp,Boolean isPromptKeyWord, Note finalNote,List<Note>allNotes) {
		this.setUpPage(handler,notes, keyWords, promptInp, isPromptKeyWord, finalNote,allNotes);
		editIdea = null;

	}
	
	public CreateAnIdeaPage(CreateAnIdeaEventHandler handler,List<Note>notes, List<String>keyWords, String promptInp, Boolean isPromptKeyWord, Note finalNote, List<Note>allNotes) {
		HashMap<Note, Integer> notesMap = new HashMap<Note, Integer>();
		for(Note n: notes) {
			notesMap.put(n, Idea.NON_PROMPT_NOTE);
		}
		this.setUpPage(handler,notesMap, keyWords, promptInp, isPromptKeyWord, finalNote,allNotes);
		
		editIdea = null;

	}
	
	public CreateAnIdeaPage(CreateAnIdeaEventHandler handler, List<Note>allNotes) {

		this.setUpPage(handler,null, null, null, null, null,allNotes);
		
		editIdea = null;

	}


	
	/**
	 * Constructor for View.CreateAnIdeaPage built from an Model.Idea object
	 * Used in program to edit an existing Model.Idea.
	 * @param idea 
	 */
	public CreateAnIdeaPage(CreateAnIdeaEventHandler handler,Idea idea, List<Note>allNotes) {
		this.setUpPage(handler,idea.getNotesMap(), idea.getKeyWords(), idea.getPrompt(), idea.getPromptType(), idea.getFinalNote(),allNotes);
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
		this.finalNote = finalNote;
		this.chosenSelectedFinalNote.setText( "Final Note: " + (finalNote==null ? "NONE" : finalNote.toString())  );
	}
	
	/**
	 * 
	 * @param note
	 */
	public void addNote(Note note) {
		System.out.println("Adding: " + note);
		this.notesPanel.addNote(note);
		if(!this.allNotesList.contains(note)){
			this.allNotesList.addElement(note);
		}
		this.allNotes.addElement(note);

	}
	
	/**
	 * Remove note from the list of notes
	 * @param note
	 */
	public void removeNote(Note note) {
		this.notesPanel.removeNote(note);
		if(finalNote==note){
			this.setFinalNote(null);
		}
		this.allNotes.removeElement(note);
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
	 * @return Model.Idea
	 */
	public Idea getIdea() {
		String prompt = this.prompt.getText();
		List<String>listKeyWords = new ArrayList<String>();
		for(Object keyWords: this.keyWordModel.toArray() ) {
			if(keyWords!=null && !( (String) keyWords).equals("null")) {
				listKeyWords.add((String) keyWords);
			}
			
		}
		
		Note finalNote = this.finalNote;

		boolean isPromptKeyWord = this.keyWord.isSelected();
		
		if(this.editIdea!=null) {
			// implies this page has been built from an Model.Idea
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
	

}
