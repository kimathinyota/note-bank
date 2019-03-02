package View.MainWindowMenu;

import View.Handlers.MainWindowEventHandler;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class HelpMenu extends JMenu {
    public HelpMenu(MainWindowEventHandler eventHandler) {
        super("Help");
        setMnemonic(KeyEvent.VK_H);

        JMenuItem helpItem = new JMenuItem("Help");
        helpItem.setMnemonic(KeyEvent.VK_H);
        add(helpItem);
        helpItem.addActionListener(eventHandler::helpWindow);

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setMnemonic(KeyEvent.VK_A);
        add(aboutItem);
        aboutItem.addActionListener(eventHandler::aboutWindow);
}
}
