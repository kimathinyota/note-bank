package View;

import Model.Note;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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

	private JTextField noteText;
	private JList<Note> listOfNotes;
	private HashMap<Note, List<String> > allNotes;
	private DefaultListModel<Note> noteModel;
	private HashSet<String> subjects;
	private String lastSelectedSubject;
	
	public void setCurrentSubject(String subject) {
		lastSelectedSubject = subject;
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
			if( allNotes.get(n).contains(subject) ) {
				notes.add(n);
			}

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
		this.displayNotesContainingWords(null);
	}


	/**
	 *  When user switches to + option in the checkbox, this method needs to be called
	 *  All of the uploaded notes will also be sent to this buffer
	 *  If a user decides to create a new subject, all of the notes within this buffer
	 *  will be assigned to that subject

	public void startPlusBuffer() {
		this.plusBufferNotes = new ArrayList<Note>();
	}
	*/

	/**
	 * Will get all of the notes iwthin the buffer as a List
	 * @return all notes in plusBuffer

	public List<Note> getNotesInBuffer(){
		return this.plusBufferNotes;
	}*/
	
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
	private void saveSubject(String subject) {
		this.subjects.add(subject);
	}
	
	/**
	 * Deletes a subject by removing the subject from each Model.Note that maps to it.
	 * You can't delete the + Subject and the All subject
	 * @param subject
	 */
	public String deleteSubject(String subject) {
		if(!subject.equals("All") ) {
			for(List<String> s: this.allNotes.values()) {
				s.remove(subject);
			}
			subjects.remove(subject);

			return subject;
		}else {
			JOptionPane.showMessageDialog(this, "You can't delete this subject");
		}
		return null;
	}
	
	/**
	 * Constructor for View.ManageNoteBankPage
	 * @param noteSubjectMap: stores all mappings from Model.Note to list of subject
	 */
	public ManageNoteBankPage(HashMap<Note, List<String>> noteSubjectMap ) {
		
		/*
		 * UPLOAD NOTE
		 * REMOVE NOTE
		 * CREATE IDEA
		 * VIEW NOTES
		 * Buttons
		 */
		this.setLayout( new GridBagLayout() );
		

		
		subjects = new HashSet<String>();
		/*
		 * Get all subjects and add to subjects HashSet
		 */



		this.subjects.addAll(ManageNoteBankPage.getAllSubjects(noteSubjectMap));


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

		
		//Search TextField
		this.noteText = new JTextField();
		
		JPanel centerPanel = new JPanel( new BorderLayout() );
		centerPanel.add(noteText,BorderLayout.NORTH);
		centerPanel.add(notes, BorderLayout.CENTER);
		
		allNotes = noteSubjectMap;

		
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
		//manageNotePanel.add(subjectList, BorderLayout.NORTH);
		manageNotePanel.add(centerPanel, BorderLayout.CENTER);
		//manageNotePanel.add(subjectCreationPanel, BorderLayout.SOUTH);


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

		//subjectList.addActionListener(handler::subjectChange);
		
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
			for(Note n: this.getNotesOfSubject( lastSelectedSubject)) {
				if(words==null || n.getName().replaceAll("-"," ").toLowerCase().contains(words.toLowerCase()) ) {
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
	public String createNewSubject() {
		String priorSubject = this.getSelectedSubject();
		//subject selected before new subject is created

		String subjectName = JOptionPane.showInputDialog("Enter subject name: ");

		if(subjectName==null)
			return null;

		if(this.validSubjectName(subjectName)) {
			this.saveSubject(subjectName);
			return subjectName;
		}else {
			JOptionPane.showMessageDialog(this, "You haven't entered a valid subject name");
		}
		return null;
	}
	
	/**
	 * Will add the input note to the selected subject
	 * @param note
	 */
	public void addNote(Note note, String subject) {
		List<String> selectedSubject = this.allNotes.get(note);
		
		if(selectedSubject==null)
			selectedSubject = new ArrayList<String>();
		

		if(!subject.equals("All")) {
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
			subjectList.remove(lastSelectedSubject);
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

	public static List<String> getAllSubjects(HashMap<Note,List<String>>allNotes){
		HashSet<String> subjects = new HashSet<String>();
		for(List<String>val: allNotes.values())
			subjects.addAll(val);

		List<String> list = new ArrayList<>();
		list.addAll(subjects);
		return list;
	}
	
	public void addNotes(Note note, List<String>subjects) {
		List<String> selectedSubject = this.allNotes.get(note);
		if(selectedSubject==null) {
			selectedSubject = new ArrayList<String>();
		}
		selectedSubject.addAll(subjects);
		allNotes.put(note, selectedSubject);
	}

	public void addNotes(String subject, List<Note>notes){
		for(Note n: notes){
			this.addNote(n,subject);
		}
	}
	
	/**
	 * 
	 * @return subject name

	public String getInputSaveName() {
		return this.subjectName.getText();
	}*/
	
	/**
	 * 
	 * @return selected subject
	 */
	public String getSelectedSubject() {
		return (String) this.lastSelectedSubject;
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

	public void addRightClickListener(MouseListener listener){
		this.listOfNotes.addMouseListener(listener);
	}

}
