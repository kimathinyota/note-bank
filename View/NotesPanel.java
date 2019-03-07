package View;

import Model.Idea;
import Model.Note;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class NotesPanel extends JPanel {
	private JList<Note> listOfPromptNotes;
	private DefaultListModel<Note> promptNoteModel;
	private JList<Note> listOfNonPromptNotes;
	private DefaultListModel<Note> nonPromptNoteModel;
	private JTabbedPane notesPane;
	private JButton remove;
	
	public void addNote(Note n, boolean isPrompt) {
		if(isPrompt) {
			if(!this.promptNoteModel.contains(n)) {
				this.promptNoteModel.addElement(n);
				this.notesPane.setSelectedIndex(0);
			}
			return;
		}
		this.addNote(n);
	}
	public void addNote(Note n) {
		if(!this.nonPromptNoteModel.contains(n))
			this.nonPromptNoteModel.addElement(n);
		this.notesPane.setSelectedIndex(1);
	}
	
	public void addNote(Collection<Note>notes) {
		for(Note n: notes) {
			this.addNote(n);
		}
		this.notesPane.setSelectedIndex(1);
	}
	
	public void clear() {
		this.promptNoteModel.clear();
		this.nonPromptNoteModel.clear();
	}
	
	public void removeNote(Note n) {
		this.promptNoteModel.removeElement(n);
		this.nonPromptNoteModel.removeElement(n);
	}
	
	
	public List<Note> getPromptNotes(){
		ArrayList<Note> notes = new ArrayList<Note>();
		for(int i=0; i<this.promptNoteModel.getSize(); i++) {
			notes.add(this.promptNoteModel.get(i));
		}
		return notes;
	}
	
	public List<Note> getNonPromptNotes(){
		ArrayList<Note> notes = new ArrayList<Note>();
		for(int i=0; i<this.nonPromptNoteModel.getSize(); i++) {
			notes.add(this.nonPromptNoteModel.get(i));
		}
		return notes;
	}
	
	public List<Note> getAllNotes(){
		ArrayList<Note> notes = new ArrayList<Note>();
		notes.addAll(getPromptNotes());
		notes.addAll(getNonPromptNotes());
		return notes;
	}
	
	
	public HashMap<Note,Integer>getNotes(){
		HashMap<Note,Integer> notes = new HashMap<Note,Integer>();
		for(Note n: this.getPromptNotes()) {
			notes.put(n, Idea.PROMPT_NOTE);
		}
		for(Note n: this.getNonPromptNotes()) {
			notes.put(n, Idea.NON_PROMPT_NOTE);
		}
		return notes;
		
	}

	
	public List<Note> getSelectedNotes(){
		JScrollPane fromPane = (JScrollPane) notesPane.getSelectedComponent();
		JList<Note> fromList = (JList<Note>) fromPane.getViewport().getView();
		return fromList.getSelectedValuesList();
	}

	
	private void setUpPage() {
		this.promptNoteModel = new DefaultListModel<Note>();
		this.nonPromptNoteModel = new DefaultListModel<Note>();
		this.listOfPromptNotes = new JList<Note>(promptNoteModel);
		this.listOfNonPromptNotes = new JList<Note>(nonPromptNoteModel);
		this.notesPane = new JTabbedPane();
		this.notesPane.addTab("Prompt Notes", new JScrollPane(this.listOfPromptNotes));
		this.notesPane.addTab("Non Prompt Notes", new JScrollPane(this.listOfNonPromptNotes));
		this.remove = new JButton("REMOVE");
		this.remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(Note n: getSelectedNotes()){
					removeNote(n);
				}
			}
		});

		this.listOfNonPromptNotes.addMouseListener(
				new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount()==2){
							for (Note n: listOfNonPromptNotes.getSelectedValuesList()){
								promptNoteModel.addElement(n);
								nonPromptNoteModel.removeElement(n);
								notesPane.setSelectedIndex(0);
							}
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

		this.listOfPromptNotes.addMouseListener(
				new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount()==2){
							for (Note n: listOfPromptNotes.getSelectedValuesList()){
								promptNoteModel.removeElement(n);
								nonPromptNoteModel.addElement(n);
								notesPane.setSelectedIndex(1);
							}
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


		this.setLayout(new BorderLayout());
		this.add(this.notesPane, BorderLayout.CENTER);
		this.add(remove, BorderLayout.SOUTH);
	}
	
	public NotesPanel(Map<Note,Integer> notes) {
		this.setUpPage();
		this.notesPane.setSelectedIndex(0);
		if(notes!=null) {
			for(Note n: notes.keySet()) {
			this.addNote(n, notes.get(n).intValue()==Idea.PROMPT_NOTE);
		}
		}
		
	}
	
	public NotesPanel(List<Note> notes) {
		this.setUpPage();
		if(notes!=null)
			this.addNote(notes);
	}
	
	
}
