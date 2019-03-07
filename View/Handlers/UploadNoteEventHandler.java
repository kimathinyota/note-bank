package View.Handlers;

import java.awt.event.ActionEvent;

public interface UploadNoteEventHandler {

    void choosePDF(ActionEvent e);
    void chooseImage(ActionEvent e);
    void uploadImage(ActionEvent e);
    void uploadText(ActionEvent e);
    void uploadPDF(ActionEvent e);
    void back(ActionEvent e);

}
