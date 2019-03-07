package View.MainWindowMenu;

import View.Handlers.MainWindowEventHandler;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class ViewMenu extends JMenu {


    public SubjectMenu getSubjectMenu() {
        return subjectMenu;
    }

    SubjectMenu subjectMenu;
    JMenuItem ideaMenu;
    JMenuItem noteMenu;

    public JMenuItem getIdeaMenu() {
        return ideaMenu;
    }

    public JMenuItem getNoteMenu() {
        return noteMenu;
    }

    ViewMenu(MainWindowEventHandler handler, List<String> subjects) {
        super("View");
        setMnemonic(KeyEvent.VK_V);
        subjectMenu = new SubjectMenu(handler,subjects);
        add(subjectMenu);
        ideaMenu = new JMenuItem("Topics/Ideas");
        ideaMenu.addActionListener(handler::viewIdeasTopics);
        add(ideaMenu);

        noteMenu = new JMenuItem("Note");
        noteMenu.addActionListener(handler::viewNotes);
        add(noteMenu);




    }
}
