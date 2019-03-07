package View.MainWindowMenu;

import View.Handlers.MainWindowEventHandler;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Class for FileMenu
 */
class FileMenu extends JMenu {

    JMenuItem importItem;
    JMenuItem exportItem;
    JMenuItem quitItem;
    NewMenu newMenu;



    public JMenu getNewMenu() {
        return newMenu;
    }

    public FileMenu(MainWindowEventHandler eventHandler){
        super("File");
        setMnemonic(KeyEvent.VK_F);

        newMenu = new NewMenu(eventHandler);
        add(newMenu);

        importItem = new JMenuItem("Import");
        importItem.setMnemonic(KeyEvent.VK_I);
        importItem.setEnabled(false);
        add(importItem);
        importItem.addActionListener(eventHandler::performImport);

        exportItem = new JMenuItem("Export");
        exportItem.setMnemonic(KeyEvent.VK_E);
        exportItem.setEnabled(false);
        add(exportItem);
        exportItem.addActionListener(eventHandler::performImport);

        quitItem = new JMenuItem("Quit");
        quitItem.setMnemonic(KeyEvent.VK_Q);
        quitItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_ESCAPE, InputEvent.CTRL_MASK));
        add(quitItem);
        quitItem.addActionListener(eventHandler::performQuit);



    }



}
