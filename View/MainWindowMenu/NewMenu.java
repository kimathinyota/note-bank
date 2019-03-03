package View.MainWindowMenu;

import View.Handlers.MainWindowEventHandler;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;



public class NewMenu extends JMenu {

    JMenuItem note;
    JMenuItem idea;
    JMenuItem subject;

    public NewMenu(MainWindowEventHandler handler){

        this.setText("New");
        note = new JMenuItem("Note");
        note.setMnemonic(KeyEvent.VK_I);
        note.setEnabled(false);
        add(note);
        //note.addActionListener(eventHandler::performImport);

        idea = new JMenuItem("Idea");
        idea.setMnemonic(KeyEvent.VK_E);
        idea.setEnabled(false);
        add(idea);
        //idea.addActionListener(eventHandler::performImport);

        subject = new JMenuItem("Subject");
        //subject.setMnemonic(KeyEvent.VK_Q);
        //subject.setAccelerator(KeyStroke.getKeyStroke(
        //        KeyEvent.VK_ESCAPE, InputEvent.CTRL_MASK));
        add(subject);
        //subject.addActionListener(eventHandler::performQuit);
    }
}
