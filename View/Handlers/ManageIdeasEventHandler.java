package View.Handlers;

import javax.swing.event.TreeSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public interface ManageIdeasEventHandler {

    void combine(ActionEvent e);
    void disband(ActionEvent e);
    void edit(ActionEvent e);
    void delete(ActionEvent e);
    void move(ActionEvent e);
    void viewAllIdeas(ActionEvent e);

}
