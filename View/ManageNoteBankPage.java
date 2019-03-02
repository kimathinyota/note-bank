package View;

import Model.Note;
import View.Handlers.ManageNoteBankPageEventHandler;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 
 * View.ManageNoteBankPage:
 * - Upload note button
 * - Remove note button
 * - Create Model.Idea button
 * - View notes button
 * - View notes button
 * - Text
 * - JList of notes
 */
public class ManageNoteBankPage extends JPanel {

	private JButton uploadNote;
	private JButton removeNotes;
	private JButton createIdea;
	private JButton viewNotes;
	private JTextField noteText;
	private JButton deleteCurrentSubject;
	private JButton saveCurrentSubject;
	private JComboBox<String> subjectList;
	private JList<Note> listOfNotes;
	private HashMap<Note, List<String> > allNotes;
	private DefaultListModel<Note> noteModel;
	private JTextField subjectName;
	private DefaultComboBoxModel<String> subjectModel;
	private HashSet<String> subjects;
	private List<Note> plusBufferNotes;
	private JRadioButton moveSelectedNotesToSubject;
	private String lastSelectedSubject;
	
	public void setCurrentSubject(String subject) {
		this.lastSelectedSubject = subject;
	}
	

	
	public String getCurrentSubject() {
		return this.lastSelectedSubject;
	}
	
	/**
	 * Gets all notes mapped to the input subject
	 * @param subject
	 * @return
	 */
	public List<Note> getNotesOfSubject(String subject){
		if(subject==null) {
			return null;
		}
		List<Note> notes = new ArrayList<Note>();
		if(subject.equals("All")) {
			notes.addAll(allNotes.keySet());
			return notes;
		}
		
		for(Note n: this.allNotes.keySet()) {
			if( allNotes.get(n).contains(subject) )
				notes.add(n);
		}
		return notes;
	} 
	
	/**
	 * Checks if subject name is valid by checking:
	 *  -> Sugject name doesn't already exist
	 *  -> Subject name is not empty
	 *  -> Subject name doesn't equal (or contain) any key words or tokens: (All, +, ,)
	 * @param name
	 * @return
	 */
	public boolean validSubjectName(String name) {
		return (!this.subjects.contains(name) && name!=null && name.length()>0 && !name.equals("All") && !name.equals("+") && !name.contains(","));
	}
	
	/**
	 * This will refresh the notes list
	 */
	public void refreshNotesListView() {
		/*
		 * Yh below code is pretty sloppy:
		 *  -> It is supposed to trigger a document listener for note text that will be used to call the 
		 * displayNotesContainingWords(String word) method to update the list of Notes
		 *  -> It works but it is shockingly highly coupled code that is in urgent need of a rewrite
		 */
		this.noteText.setText("Yooo");
		this.noteText.setText("");
	}
	
	/**
	 *  When user switches to + option in the checkbox, this method needs to be called
	 *  All of the uploaded notes will also be sent to this buffer
	 *  If a user decides to create a new subject, all of the notes within this buffer
	 *  will be assigned to that subject
	 */
	public void startPlusBuffer() {
		this.plusBufferNotes = new ArrayList<Note>();
	}
	
	/**
	 * Will get all of the notes iwthin the buffer as a List
	 * @return all notes in plusBuffer
	 */
	public List<Note> getNotesInBuffer(){
		return this.plusBufferNotes;
	}
	
	/**
	 * Used to clear all of the notes from the current list
	 */
	public void clearNotes() {
		this.noteModel.removeAllElements();
	}
	/**
	 * Will manage updating all of the necessary objects when a new subject is added
	 * @param subject
	 */
	public void saveSubject(String subject) {
		this.subjects.add(subject);
		this.subjectModel.addElement(subject);
		this.subjectModel.setSelectedItem(subject);
	}
	
	/**
	 * Deletes a subject by removing the subject from each Model.Note that maps to it.
	 * You can't delete the + Subject and the All subject
	 * @param subject
	 */
	public void deleteSubject(String subject) {
		if(!subject.equals("All") && !subject.equals("+")) {
			for(List<String> s: this.allNotes.values()) {
				s.remove(subject);
			}
			this.subjectModel.removeElement(subject);
			this.subjectList.setSelectedItem("All");
		}else {
			JOptionPane.showMessageDialog(this, "You can't delete this subject");
		}
	}
	
	/**
	 * Constructor for View.ManageNoteBankPage
	 * @param noteSubjectMap: stores all mappings from Model.Note to list of subject
	 */
	public ManageNoteBankPage(ManageNoteBankPageEventHandler handler, HashMap<Note, List<String>> noteSubjectMap ) {
		
		/*
		 * UPLOAD NOTE
		 * REMOVE NOTE
		 * CREATE IDEA
		 * VIEW NOTES
		 * Buttons
		 */
		this.setLayout( new GridBagLayout() );
		uploadNote = new JButton("UPLOAD NOTE");
		removeNotes = new JButton("REMOVE NOTES");
		createIdea = new JButton("CREATE IDEA");
		viewNotes = new JButton("VIEW NOTES");
		
		//Model for managing subject combobox
		subjectModel = new DefaultComboBoxModel<String>();
		subjectModel.addElement("All");
		subjectModel.addElement("+");
		
		subjects = new HashSet<String>();
		/*
		 * Get all subjects and add to subjects HashSet
		 */
		for(List<String> subjectList: noteSubjectMap.values()) {
			for(String subject: subjectList ) {
				this.subjects.add(subject);
			}
		}
		/*
		 * Add all subjects to the subject combobox
		 */
		for(String subject: this.subjects) {
			this.subjectModel.addElement(subject);
		}
		subjectList = new JComboBox<String>(subjectModel);
		subjectList.setSelectedItem("All");
		this.lastSelectedSubject = "All";
		
		/*
		 * Add all notes to the Model.Note list
		 */
		this.noteModel = new DefaultListModel<Note>();
		for(Note m: noteSubjectMap.keySet()) {
			noteModel.addElement(m);
		}
		
		//Notes List
		listOfNotes = new JList<Note>(noteModel) ;
		listOfNotes.setBorder(BorderFactory.createTitledBorder("Notes"));
		
		JScrollPane notes = new JScrollPane(listOfNotes);
		notes.setBorder(BorderFactory.createEmptyBorder());
		
		// Move selected notes to subject
		moveSelectedNotesToSubject = new JRadioButton( "<html><p align=\"center\">"+"MOVE" + "<br>" + "SELECTED" + "<br>" + "TO NEXT"+"<br>" + "SUBJECT" + "</p</html>" );
		moveSelectedNotesToSubject.setHorizontalAlignment(JLabel.CENTER);
		
		JPanel buttonPanel = new JPanel(new GridLayout(5,1));
		buttonPanel.add(uploadNote);
		buttonPanel.add(removeNotes);
		buttonPanel.add(createIdea);
		buttonPanel.add(viewNotes);
		buttonPanel.add(moveSelectedNotesToSubject);
		
		//Search TextField
		this.noteText = new JTextField();
		
		JPanel centerPanel = new JPanel( new BorderLayout() );
		centerPanel.add(noteText,BorderLayout.NORTH);
		centerPanel.add(notes, BorderLayout.CENTER);
		
		allNotes = noteSubjectMap;
		
;		//Save and Delete buttons:
		this.saveCurrentSubject = new JButton("CREATE SUBJECT");
		this.deleteCurrentSubject = new JButton("DELETE SUBJECT");
		
		//Name of subject (will hold the name of the subject created)
		this.subjectName = new JTextField();

		
		JPanel subjectCreationPanel = new JPanel(new BorderLayout());
		JPanel subjectCreateDeleteButtonPanel = new JPanel( new GridLayout(1,2));
		subjectCreateDeleteButtonPanel.add(this.saveCurrentSubject);
		subjectCreateDeleteButtonPanel.add(this.deleteCurrentSubject);
		
		subjectCreationPanel.add(this.subjectName, BorderLayout.CENTER);
		subjectCreationPanel.add(subjectCreateDeleteButtonPanel,BorderLayout.EAST);
		
		
		//Layout
		/*  [       Y - Pick Subject         ]  
		 *  [                ] (TextField)
		 *   ----------------
		 *  |                | [ Upload note ]       
		 *  |                | [ Remove note ]          
		 *  |                | [ Create Model.Idea ]
		 *  |                |  [View notes  ]          
		 *   ----------------
		 *  [           ] [CREATE..] [DELETE.]
		 */
		
		JPanel manageNotePanel = new JPanel(new BorderLayout());
		manageNotePanel.add(subjectList, BorderLayout.NORTH);
		manageNotePanel.add(centerPanel, BorderLayout.CENTER);
		manageNotePanel.add(buttonPanel, BorderLayout.EAST);
		manageNotePanel.add(subjectCreationPanel, BorderLayout.SOUTH);

		uploadNote.addActionListener(handler::uploadNote);
		removeNotes.addActionListener(handler::removeNote);
		createIdea.addActionListener(handler::createIdea);
		viewNotes.addActionListener(handler::viewNotes);


		noteText.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						displayNotesContainingWords(getTextNote());
					}
					@Override
					public void removeUpdate(DocumentEvent e) {
						displayNotesContainingWords(getTextNote());
					}
					@Override
					public void changedUpdate(DocumentEvent e) {
						displayNotesContainingWords(getTextNote());
					}
				}
		);

		deleteCurrentSubject.addActionListener(handler::deleteSubject);
		saveCurrentSubject.addActionListener(handler::saveSubject);
		subjectList.addActionListener(handler::subjectChange);
		
		manageNotePanel.setPreferredSize(new Dimension(500,500));
		this.add(manageNotePanel);
		
		
	}
	

	/**
	 * This will display all of the notes to the JList that: 
	 *  1) contain input string, words
	 *  2) Are a part of the currently selected subject
	 * @param words
	 */
	public void displayNotesContainingWords(String words){
		if(!this.getSelectedSubject().equals("+")) {
			this.noteModel.removeAllElements();
			for(Note n: this.getNotesOfSubject( this.getSelectedSubject())) {
				if(n.getName().replaceAll("-"," ").toLowerCase().contains(words.toLowerCase()) ) {
					this.noteModel.addElement(n);
				}
			}
		}
	}
	
	/**
	 * Gets the search name inputted for the notes list
	 * @return
	 */
	public String getTextNote() {
		return this.noteText.getText();
	}
	
	/**
	 * Will validate input subject name and will create
	 * (or not if not valid) the new subject
	 */
	public void createNewSubject() {
		String priorSubject = this.getSelectedSubject();
		//subject selected before new subject is created
		String subjectName = this.getInputSaveName();

		if(this.validSubjectName(subjectName)) {
			this.saveSubject(subjectName);
			if(priorSubject.equals("+")) {
				// add all notes from Buffer to the newly created subject 
				List<Note> ns = new ArrayList<Note>(this.getNotesInBuffer());
			
				if(ns!=null) {
					for(Note n: ns) {
						this.addNote(n,subjectName);
					}
				}
			}
		}else {
			JOptionPane.showMessageDialog(this, "You haven't entered a valid subject name");
		}
	}
	
	/**
	 * Will add the input note to the selected subject
	 * @param note
	 */
	public void addNote(Note note, String subject) {
		List<String> selectedSubject = this.allNotes.get(note);
		
		if(selectedSubject==null)
			selectedSubject = new ArrayList<String>();
		
		if(subject.equals("+")) {
			this.plusBufferNotes.add(note);
		}else if(!subject.equals("All")) {
			if(!selectedSubject.contains(subject))
				selectedSubject.add(getSelectedSubject());
		}
		allNotes.put(note, selectedSubject);
		this.noteModel.addElement(note);
		this.refreshNotesListView();
	}     
	
	/**
	 * Removes note from the selected subject:
	 * 	if selected subject is All, it will delete the note
	 * @param note
	 */
	public void removeNote(Note note) {
		if(this.getSelectedSubject().equals("All")) {
			allNotes.remove(note);
			this.noteModel.removeElement(note); 
			note.delete();
		
			
		}else {
			List<String> subjectList = allNotes.get(note);
			subjectList.remove(this.getSelectedSubject());
			this.refreshNotesListView();
			this.noteModel.removeElement(note);
		}
	}
	
	public List<String> getAllSubjects(List<Note>notes){
		HashSet<String> subjects = new HashSet<String>();
		for(Note n: notes) {
			if(allNotes.get(n)!=null)
				subjects.addAll(allNotes.get(n));
		}
		ArrayList<String> subs = new ArrayList<String>();
		subs.addAll(subjects);
		return subs;
	}
	
	public void addNotes(Note note, List<String>subjects) {
		List<String> selectedSubject = this.allNotes.get(note);
		if(selectedSubject==null) {
			selectedSubject = new ArrayList<String>();
		}
		selectedSubject.addAll(subjects);
		allNotes.put(note, selectedSubject);
	}
	
	/**
	 * 
	 * @return subject name
	 */
	public String getInputSaveName() {
		return this.subjectName.getText();
	}
	
	/**
	 * 
	 * @return selected subject
	 */
	public String getSelectedSubject() {
		return (String) this.subjectList.getSelectedItem();
	}
	
	/**
	 * 
	 * @return selected note
	 */
	public List<Note> getSelectedNotes() {
		return this.listOfNotes.getSelectedValuesList();

	}
	
	public int getSizeOfNotes() {
		return this.listOfNotes.getSelectedIndex();
		//return this.noteModel.size();
	}

	/**
	 * Gets all notes from notes JList as a list
	 * @return gets notes as list 
	 */
	public List<Note> getNotes(){
		List<Note> notes = new ArrayList<Note>();
		for(int i=0; i<this.noteModel.getSize(); i++) {
			notes.add(this.noteModel.get(i));
		}
		return notes;
		
	}
	
	public boolean shouldMoveSelectedToNextSubject() {
		return this.moveSelectedNotesToSubject.isSelected();
	}
	
	public void resetMoveSelectedToNextSubject() {
		this.moveSelectedNotesToSubject.setSelected(false);
	}
	
	/*
	 *  Methods to allow controller to add required action listeners for each button:
	 */
	public void addUploadActionListener(ActionListener uploadActionListener) {
		this.uploadNote.addActionListener(uploadActionListener);
	}
	
	public void addRemoveNotesActionListener(ActionListener removeNotesActionListener) {
		this.removeNotes.addActionListener(removeNotesActionListener);
	}
	
	public void addCreateIdeaActionListener(ActionListener createIdeaActionListener ) {
		this.createIdea.addActionListener(createIdeaActionListener);
	}
	
	public void addViewNotesActionListener(ActionListener viewNotesActionListener) {
		this.viewNotes.addActionListener(viewNotesActionListener);
	}
	
	public void addSubjectChangeActionListener(ActionListener subjectChangeActionListener) {
		this.subjectList.addActionListener(subjectChangeActionListener);
	}
	
	public void addDeleteSubjectActionListener(ActionListener deleteSubjectActionListener) {
		this.deleteCurrentSubject.addActionListener(deleteSubjectActionListener);
	}
	
	public void addSaveSubjectActionListener(ActionListener saveSubjectActionListener) {
		this.saveCurrentSubject.addActionListener(saveSubjectActionListener);
	}
	
	public void addNoteTextDocumentListener(DocumentListener textDocumentListener) {
		this.noteText.getDocument().addDocumentListener(textDocumentListener);
	}
	

}
