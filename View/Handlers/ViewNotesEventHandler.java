package View.Handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public interface ViewNotesEventHandler {
    void back(ActionEvent e);
    void createIdea(ActionEvent e);
    void addToIdea(ItemEvent e);
    void nextPage(ActionEvent e);
    void previousPage(ActionEvent e);
    void clearSelectToggle(ActionEvent e);

}
