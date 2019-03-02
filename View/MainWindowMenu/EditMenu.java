package View.MainWindowMenu;
import View.Handlers.MainWindowEventHandler;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class EditMenu extends JMenu {

    public EditMenu(MainWindowEventHandler eventHandler){
        super("Edit");
        setMnemonic(KeyEvent.VK_E);

        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.setMnemonic(KeyEvent.VK_U);
        undoItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        add(undoItem);
        undoItem.addActionListener(eventHandler::performUndo);


        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.setMnemonic(KeyEvent.VK_R);
        redoItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Y, InputEvent.CTRL_MASK));
        add(redoItem);
        redoItem.addActionListener(eventHandler::performRedo);
    }
}
