package View;

import Model.Note;
import View.Handlers.SelectNotePageEventHandler;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

public class SelectNotesPage extends JPanel {
	
	private List<Note> originalListOfNotes;
	private JList<Note> listOfNotes;
	private DefaultListModel<Note> noteModel;
	
	private JButton back;
	private JButton restoreList;
	private JButton removeNote;
	private JButton addNotes;
	private JPanel previousPage;
	private JButton uploadNote;
	private JButton clear;
	private JTextField noteText;
	
	public void setPreviousPage(JPanel previousPage) {
		this.previousPage = previousPage;
	}
	
	public JPanel getPreviousPage() {
		return this.previousPage;
	}
	
	public void removeNote(Note note) {
		this.noteModel.removeElement(note);
	}
	
	public void restoreList() {
		this.noteModel.clear();
		for(Note n: originalListOfNotes) {
			this.noteModel.addElement(n);
		}
	}
	
	public void addNote(Note note) {
		this.noteModel.addElement(note);
	}
	public List<Note> getSelectedNotes(){
		return this.listOfNotes.getSelectedValuesList();
	}
	
	public List<Note> getAllNotes(){
		List<Note> notes = new ArrayList<Note>();
		for(int i=0; i<this.listOfNotes.getModel().getSize(); i++) {
			notes.add(  this.listOfNotes.getModel().getElementAt(i)  );
		}
		return notes;
	}
	
	public void addRestoreListActionListener(ActionListener restoreListActionListener) {
		this.restoreList.addActionListener(restoreListActionListener);
	}
	
	public void addAddNotesActionListener(ActionListener addNotesActionListener) {
		this.addNotes.addActionListener(addNotesActionListener);
	}
	
	public void addRemoveNotesActionListener(ActionListener removeNotesActionListener) {
		this.removeNote.addActionListener(removeNotesActionListener);
	}
	
	public void addBackActionListener(ActionListener backActionListener) {
		this.back.addActionListener(backActionListener);
	}
	
	public void addUploadNoteActionListener(ActionListener uploadNoteActionListener) {
		this.uploadNote.addActionListener(uploadNoteActionListener);
	}
	
	public void addClearActionListener(ActionListener clearActionListener) {
		this.clear.addActionListener(clearActionListener);
	}
	
	public void clear() {
		this.noteModel.removeAllElements();
	}
	
	public void displayNotesContainingWords(String words){
		this.noteModel.removeAllElements();
		for(Note n: this.originalListOfNotes) {
			if(n.getName().toLowerCase().contains(words.toLowerCase())) {
				this.noteModel.addElement(n);
			}
		}
		
	}
	
	public String getTextNote() {
		return this.noteText.getText();
	}
	
	public void addNoteTextDocumentListener(DocumentListener textDocumentListener) {
		this.noteText.getDocument().addDocumentListener(textDocumentListener);
	}
	
	
	public SelectNotesPage(SelectNotePageEventHandler handler, Collection<Note> notesList) {
		this.setLayout( new GridBagLayout() );
		restoreList = new JButton("RESTORE");
		removeNote = new JButton("REMOVE");
		addNotes = new JButton("ADD NOTES");
		
		JPanel backPanel = new JPanel(new GridLayout(1,4));
		back = new JButton("BACK");
		backPanel.add(back);
		backPanel.add(Box.createHorizontalGlue());
		backPanel.add(Box.createHorizontalGlue());
		backPanel.add(Box.createHorizontalGlue());
		
		noteModel = new DefaultListModel<Note>();
		listOfNotes = new JList<Note>(noteModel) ;
		listOfNotes.setBorder(BorderFactory.createTitledBorder("Notes"));
		
		List<Note> notes = new ArrayList<Note>();
		for(Note n: notesList) {
			notes.add(n);
		}
		this.originalListOfNotes = notes;
		this.restoreList();

		this.back.addActionListener(handler::back);
		this.addNotes.addActionListener(handler::addNotes);
		this.removeNote.addActionListener(handler::removeNotes);
		this.restoreList.addActionListener(handler::restoreList);
		this.clear.addActionListener(handler::clear);
		this.uploadNote.addActionListener(handler::uploadNote);
		
		this.uploadNote = new JButton("Upload Notes");
		this.clear = new JButton("Clear notes");
		
		

		
		JPanel buttonPanel = new JPanel(new GridLayout(4,1));
		buttonPanel.add(restoreList);
		buttonPanel.add(removeNote);
		buttonPanel.add(uploadNote);
		buttonPanel.add(clear);
		buttonPanel.add(addNotes);

		this.noteText = new JTextField();
		
		JScrollPane notesPane = new JScrollPane(listOfNotes);
		
		JPanel centerPanel = new JPanel( new BorderLayout() );
		centerPanel.add(noteText,BorderLayout.NORTH);
		centerPanel.add(notesPane, BorderLayout.CENTER);
		
		
		JPanel selectNotePanel = new JPanel(new BorderLayout());
		selectNotePanel.add(backPanel, BorderLayout.NORTH);
		selectNotePanel.add(centerPanel, BorderLayout.CENTER);
		selectNotePanel.add(buttonPanel, BorderLayout.EAST);
		selectNotePanel.add(addNotes, BorderLayout.SOUTH);
		
		selectNotePanel.setPreferredSize(new Dimension(500,500));
		
		this.add(selectNotePanel);
			
	}
	
	
	
	
	
	
	
}
