package View.Handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

public interface RightClickEventHandler {

    void viewNotes(ActionEvent e);
    void removeNotes(ActionEvent e);
    void editNote(ActionEvent e);
    void editIdea(ActionEvent e);
    void combine(ActionEvent e);
    void disband(ActionEvent e);
    void move(ActionEvent e);
    void stopFiltering(ActionEvent e);
    void removeIdeaTopic(ActionEvent e);
    void createIdea(ActionEvent e);
    void refreshIdeaTree(ActionEvent e);

}
