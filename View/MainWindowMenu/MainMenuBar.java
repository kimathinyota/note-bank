package View.MainWindowMenu;

import View.Handlers.MainWindowEventHandler;

import javax.swing.*;

/**
 * The menubar for the main window.
 */
public class MainMenuBar extends JMenuBar {


    public MainMenuBar(MainWindowEventHandler eventHandler){
        JMenu fileMenu = new FileMenu(eventHandler);
        add(fileMenu);
        JMenu editMenu = new EditMenu(eventHandler);
        add(editMenu);
        JMenu viewMenu = new ViewMenu(eventHandler);
        add(viewMenu);
        JMenu helpMenu = new HelpMenu(eventHandler);
        add(helpMenu);
    }

}
