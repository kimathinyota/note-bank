package Controller;

import Model.Book;
import Model.Image;
import Model.Note;
import Model.TextExcerpt;
import View.CreateAnIdeaPage;
import View.Handlers.UploadNoteEventHandler;
import View.NoteBankView;
import View.UploadNotePage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class UploadNoteController implements UploadNoteEventHandler {
    private UploadNotePage uploadNotePage;
    private NoteBankController controller;

    public UploadNotePage getUploadNotePage(){
        return this.uploadNotePage;
    }

    public UploadNoteController(NoteBankController controller){
        uploadNotePage = new UploadNotePage(this,controller.getPathToDirectory());
        NoteBankView view = controller.getView();
        this.controller = controller;
    }

    @Override
    public void choosePDF(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new FileNameExtensionFilter("PDF File", "pdf"));
        fc.setAcceptAllFileFilterUsed(false);
        int returnVal = fc.showOpenDialog(uploadNotePage);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            uploadNotePage.setChosenPDF(fc.getSelectedFile());
            uploadNotePage.setPDFName(fc.getSelectedFile().getName());
        }
    }

    @Override
    public void chooseImage(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Model.Image files", ImageIO.getReaderFileSuffixes()));
        fc.setAcceptAllFileFilterUsed(false);
        int returnVal = fc.showOpenDialog(uploadNotePage);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            uploadNotePage.setChosenImage(fc.getSelectedFile());
            uploadNotePage.setImageName(fc.getSelectedFile().getName());
        }
    }

    @Override
    public void uploadImage(ActionEvent e) {
        try {
            NoteBankView view = controller.getView();
            Image imageNote = uploadNotePage.getImage();
            if(imageNote!=null) {
                controller.addNote(imageNote);
                uploadNotePage.resetImageNote();

                uploadNote(imageNote);

            }else {
                JOptionPane.showMessageDialog(uploadNotePage, "Make sure you have chosen a valid note name and have selected a file to add ");
            }
        } catch (IOException e1) {

        }
    }

    private void uploadNote(Note note){
        NoteBankView view = controller.getView();
        JPanel prevPage = uploadNotePage.getPreviousPage();
        if(prevPage instanceof CreateAnIdeaPage) {
            ((CreateAnIdeaPage) prevPage).addNote(note);
        }

        uploadNotePage.nextPanel();
        view.manageNoteBankPage.refreshNotesListView();
        view.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(view.manageNoteBankPage.getNotes(), null );
        controller.save();
        controller.updateSubjectFile();
    }

    @Override
    public void uploadText(ActionEvent e) {
        NoteBankView view = controller.getView();
        try {
            TextExcerpt text = uploadNotePage.getTextExcerpt();
            if(text!=null) {
                controller.addNote(text);
                uploadNotePage.resetTextNote();

                uploadNote(text);

            }else {
                JOptionPane.showMessageDialog(uploadNotePage, "Make sure you have chosen a valid note name");
            }
        }catch(IOException e1) {

        }
    }

    @Override
    public void uploadPDF(ActionEvent e) {
        try {
            NoteBankView view = controller.getView();
            Book pdfNote = uploadNotePage.getPDF();
            if(pdfNote!=null) {
                controller.addNote(pdfNote);

                uploadNotePage.resetPDFNote();
                uploadNote(pdfNote);

            }else {
                JOptionPane.showMessageDialog(uploadNotePage, "Make sure you have chosen a valid note name and have selected a file to add ");
            }
        } catch (IOException e1) {

        }
    }

    @Override
    public void back(ActionEvent e) {
        NoteBankView view = controller.getView();
        JPanel prevPage = uploadNotePage.getPreviousPage();
        view.deletePage(uploadNotePage);
        view.restorePages();
        view.switchPage(prevPage);
    }
}
