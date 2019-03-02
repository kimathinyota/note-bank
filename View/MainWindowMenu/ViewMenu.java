package View.MainWindowMenu;

import View.Handlers.MainWindowEventHandler;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class ViewMenu extends JMenu {

    ViewMenu(MainWindowEventHandler eventHandler) {
        super("View");
        setMnemonic(KeyEvent.VK_V);

    }
}
