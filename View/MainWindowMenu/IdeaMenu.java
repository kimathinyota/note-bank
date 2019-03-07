package View.MainWindowMenu;

import View.Handlers.MainWindowEventHandler;
import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class IdeaMenu extends JMenu {
    JCheckBoxMenuItem stopFilteringItem;
    JMenuItem combineItem;
    JMenuItem disbandItem;
    JMenuItem moveItem;

    public boolean getShouldStopFiltering(){
        return this.stopFilteringItem.isSelected();
    }

    public IdeaMenu(MainWindowEventHandler handler) {
        super("Idea");
        //setMnemonic(KeyEvent.VK_H);

        combineItem = new JMenuItem("Combine");
        combineItem.addActionListener(handler::combineNotes);
        //combineItem.setMnemonic(KeyEvent.VK_H);
        combineItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_I, InputEvent.CTRL_MASK));
        add(combineItem);

        disbandItem = new JMenuItem("Disband");
        disbandItem.addActionListener(handler::disbandNotes);
        disbandItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_D, InputEvent.CTRL_MASK));
        //disbandItem.setMnemonic(KeyEvent.VK_H);
        add(disbandItem);

        moveItem = new JMenuItem("Move");
        moveItem.addActionListener(handler::move);
        moveItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_M, InputEvent.CTRL_MASK));
        add(moveItem);

        moveItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_R, InputEvent.CTRL_MASK));


        stopFilteringItem = new JCheckBoxMenuItem("No Filtering");
        stopFilteringItem.addItemListener(handler::disableSubjectFiltering);
        //stopFilteringItem.setMnemonic(KeyEvent.VK_H);
        add(stopFilteringItem);

        this.setEnabled(false);

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


}
