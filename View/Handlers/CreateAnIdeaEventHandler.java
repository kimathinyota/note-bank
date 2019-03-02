package View.Handlers;

import java.awt.event.ActionEvent;


public interface CreateAnIdeaEventHandler {

    void back(ActionEvent e);
    void addNote(ActionEvent e);
    void removeNote(ActionEvent e);
    void selectFinalNote(ActionEvent e);
    void addKeyWord(ActionEvent e);
    void removeKeyWord(ActionEvent e);
    void saveIdea(ActionEvent e);
    void viewAllNotes(ActionEvent e);


}
