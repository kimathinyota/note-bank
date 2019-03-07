package View.RightClickMenus;

import View.Handlers.RightClickEventHandler;

import javax.swing.*;

public class NoteRightClick extends JPopupMenu {

    JMenuItem viewNotes, removeNote, editNotes, createIdea;

    public NoteRightClick(RightClickEventHandler handler) {
        viewNotes = new JMenuItem("View Notes");
        viewNotes.addActionListener(handler::viewNotes);
        removeNote = new JMenuItem("Remove notes");
        removeNote.addActionListener(handler::removeNotes);
        editNotes = new JMenuItem("Edit note");
        editNotes.addActionListener(handler::editNote);
        createIdea = new JMenuItem("Create Idea");
        createIdea.addActionListener(handler::createIdea);

        add(viewNotes);
        add(removeNote);
        add(editNotes);
        add(createIdea);
    }


}
