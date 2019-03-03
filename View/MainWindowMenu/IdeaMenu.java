package View.MainWindowMenu;

import View.Handlers.MainWindowEventHandler;
import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class IdeaMenu extends JMenu {
    JMenuItem stopFilteringItem;
    JMenuItem combineItem;
    JMenuItem disbandItem;
    JMenuItem moveItem;
    JMenuItem refreshItem;

    public IdeaMenu(MainWindowEventHandler eventHandler) {
        super("Idea");
        //setMnemonic(KeyEvent.VK_H);

        combineItem = new JMenuItem("Combine");
        //combineItem.setMnemonic(KeyEvent.VK_H);
        combineItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_I, InputEvent.CTRL_MASK));
        add(combineItem);

        disbandItem = new JMenuItem("Disband");
        disbandItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_D, InputEvent.CTRL_MASK));
        //disbandItem.setMnemonic(KeyEvent.VK_H);
        add(disbandItem);

        moveItem = new JMenuItem("Move");
        moveItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_M, InputEvent.CTRL_MASK));
        add(moveItem);

        refreshItem = new JMenuItem("Refresh");
        //refreshItem.setMnemonic(KeyEvent.VK_H);
        add(refreshItem);


        moveItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_R, InputEvent.CTRL_MASK));


        stopFilteringItem = new JMenuItem("No Filtering");
        //stopFilteringItem.setMnemonic(KeyEvent.VK_H);
        add(stopFilteringItem);

    }

    public void setStopFilteringEnabled(boolean enabled){
        this.stopFilteringItem.setEnabled(enabled);
    }

    public void setMoveEnabled(boolean enabled){
        this.moveItem.setEnabled(enabled);
    }

    public void setCombineEnabled(boolean enabled){
        this.combineItem.setEnabled(enabled);
    }

    public void setDisbandEnabled(boolean enabled){
        this.disbandItem.setEnabled(enabled);
    }

    public void setRefreshEnabled(boolean enabled){
        this.refreshItem.setEnabled(enabled);
    }



}
