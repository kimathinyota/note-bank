package View.MainWindowMenu;
import View.Handlers.MainWindowEventHandler;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class EditMenu extends JMenu {

    JMenuItem notes,undoItem,redoItem,editIdea,removeTopicOrIdea,subjectRemove;

    public JMenuItem getRemoveTopicOrIdea() {
        return removeTopicOrIdea;
    }

    public JMenuItem getRemoveNotes() {
        return notes;
    }

    public JMenuItem getUndoItem() {
        return undoItem;
    }

    public JMenuItem getRedoItem() {
        return redoItem;
    }

    public JMenuItem getEditIdea() {
        return editIdea;
    }



    public EditMenu(MainWindowEventHandler eventHandler){
        super("Edit");
        setMnemonic(KeyEvent.VK_E);

        undoItem = new JMenuItem("Undo");
        undoItem.setMnemonic(KeyEvent.VK_U);
        undoItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        add(undoItem);
        undoItem.addActionListener(eventHandler::performUndo);

        redoItem = new JMenuItem("Redo");
        redoItem.setMnemonic(KeyEvent.VK_R);
        redoItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Y, InputEvent.CTRL_MASK));
        add(redoItem);
        redoItem.addActionListener(eventHandler::performRedo);

        JMenu remove = new JMenu("Remove");
        add(remove);

        subjectRemove = new JMenuItem("Current Subject");
        subjectRemove.addActionListener(eventHandler::subjectRemove);
        remove.add(subjectRemove);


        notes = new JMenuItem("Notes");
        notes.addActionListener(eventHandler::removeNote);
        remove.add(notes);



        removeTopicOrIdea = new JMenuItem("Topic or Idea");
        remove.add(removeTopicOrIdea);
        removeTopicOrIdea.setEnabled(false);
        removeTopicOrIdea.addActionListener(eventHandler::deleteTopicIdea);

        editIdea = new JMenuItem("Idea");
        editIdea.addActionListener(eventHandler::editIdea);
        add(editIdea);
        editIdea.setEnabled(false);
    }
}
