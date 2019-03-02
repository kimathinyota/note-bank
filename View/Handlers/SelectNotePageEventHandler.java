package View.Handlers;
import java.awt.event.ActionEvent;

public interface SelectNotePageEventHandler {
    void restoreList(ActionEvent e);
    void addNotes(ActionEvent e);
    void removeNotes(ActionEvent e);
    void back(ActionEvent e);
    void uploadNote(ActionEvent e);
    void clear(ActionEvent e);
}
