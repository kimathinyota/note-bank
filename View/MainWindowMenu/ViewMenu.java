package View.MainWindowMenu;

import View.Handlers.MainWindowEventHandler;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class ViewMenu extends JMenu {

    JMenu subjectMenu;

    ViewMenu(MainWindowEventHandler handler,List<String> subjects) {
        super("View");
        setMnemonic(KeyEvent.VK_V);
        subjectMenu = new SubjectMenu(handler,subjects);
        add(subjectMenu);

    }
}
