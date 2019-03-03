package View.MainWindowMenu;

import View.Handlers.MainWindowEventHandler;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class QuizMenu extends JMenu {

    public QuizMenu(MainWindowEventHandler eventHandler) {
        super("Quiz");
        setMnemonic(KeyEvent.VK_H);

        JMenuItem createQuizItem = new JMenuItem("Create Quiz");
        createQuizItem.setMnemonic(KeyEvent.VK_H);
        add(createQuizItem);
        createQuizItem.addActionListener(eventHandler::helpWindow);
    }

}
