package View.Handlers;

import javax.swing.event.DocumentEvent;
import java.awt.event.ActionEvent;


public interface ManageNoteBankPageEventHandler {

    void uploadNote(ActionEvent e);
    void removeNote(ActionEvent e);
    void createIdea(ActionEvent e);
    void viewNotes(ActionEvent e);
    void subjectChange(ActionEvent e);
    void deleteSubject(ActionEvent e);
    void saveSubject(ActionEvent e);

}
