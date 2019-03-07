package View.MainWindowMenu;

import View.Handlers.MainWindowEventHandler;

import javax.swing.*;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;



public class NewMenu extends JMenu {

    private JMenuItem note;
    private JMenuItem idea;
    private JMenuItem subject;

    public JMenuItem getNote() {
        return note;
    }

    public JMenuItem getIdea() {
        return idea;
    }

    public JMenuItem getSubject() {
        return subject;
    }

    public NewMenu(MainWindowEventHandler handler){

        this.setText("New");
        this.setMnemonic(KeyEvent.VK_N);
        //this.setAccelerator(KeyStroke.getKeyStroke(
               // KeyEvent.VK_N, InputEvent.CTRL_MASK));


        note = new JMenuItem("Note");
        note.setMnemonic(KeyEvent.VK_U);
        note.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, InputEvent.CTRL_MASK));
        add(note);
        note.addActionListener(handler::uploadNote);

        idea = new JMenuItem("Idea");
        idea.setMnemonic(KeyEvent.VK_I);
        idea.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_I, InputEvent.CTRL_MASK));
        add(idea);
        idea.addActionListener(handler::createIdea);

        subject = new JMenuItem("Subject");
        subject.addActionListener(handler::createSubject);
        //subject.setMnemonic(KeyEvent.VK_Q);
        //subject.setAccelerator(KeyStroke.getKeyStroke(
        //        KeyEvent.VK_ESCAPE, InputEvent.CTRL_MASK));
        add(subject);
        //subject.addActionListener(eventHandler::performQuit);
    }
}
