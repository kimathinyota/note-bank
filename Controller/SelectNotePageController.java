package Controller;
import View.Handlers.SelectNotePageEventHandler;
import View.NoteBankView;
import View.SelectNotesPage;
import Model.Note;
import View.UploadNotePage;
import View.ViewNotesPage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;

public class SelectNotePageController implements SelectNotePageEventHandler {
    NoteBankController controller;
    SelectNotesPage selectNotesPage;

    public SelectNotesPage getSelectNotePage(){
        return selectNotesPage;
    }

    public  SelectNotePageController(NoteBankController controller, Collection<Note> notes){
        selectNotesPage = new SelectNotesPage(this,notes);

    }

    @Override
    public void restoreList(ActionEvent e) {
        selectNotesPage.restoreList();
    }

    @Override
    public void addNotes(ActionEvent e) {
        if(selectNotesPage.getAllNotes().size()>0) {
            NoteBankView view = controller.getView();
            ViewNotesController viewNotesController = new ViewNotesController(controller,selectNotesPage.getAllNotes(),true);
            ViewNotesPage viewNotesPage = viewNotesController.getViewNotes();
            viewNotesPage.setPreviousPage(selectNotesPage);
            view.addFixedPage(viewNotesPage, "Select notes to add to Model.Idea");
            viewNotesPage.setCreateIdeaButtonName("Add selected notes");
        }
    }

    @Override
    public void removeNotes(ActionEvent e) {
        for(Note n: selectNotesPage.getSelectedNotes()) {
            selectNotesPage.removeNote(n);
        }
    }

    @Override
    public void back(ActionEvent e) {
        NoteBankView view = controller.getView();
        JPanel prevPage = selectNotesPage.getPreviousPage();
        view.deletePage(selectNotesPage);
        view.restorePages();
        view.switchPage(prevPage);
    }

    @Override
    public void uploadNote(ActionEvent e) {
        NoteBankView view = controller.getView();
        UploadNoteController uploadNoteController = new UploadNoteController(controller);
        UploadNotePage uploadNotePage = uploadNoteController.getUploadNotePage();
        uploadNotePage.setPreviousPage(selectNotesPage);
        view.addFixedPage(uploadNotePage, "Upload Note");
    }

    @Override
    public void clear(ActionEvent e) {
        selectNotesPage.clear();
    }
}
