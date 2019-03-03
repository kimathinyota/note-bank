package Controller;

import Model.Note;

import View.CreateAnIdeaPage;
import View.Handlers.ManageNoteBankPageEventHandler;
import View.ManageNoteBankPage;
import View.NoteBankView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ManageNoteBankController implements ManageNoteBankPageEventHandler {

    private  NoteBankController controller;

    @Override
    public void uploadNote(ActionEvent e) {
        NoteBankView view = controller.getView();
        UploadNoteController uploadNoteController = new UploadNoteController(controller);
        uploadNoteController.getUploadNotePage().setPreviousPage(view.manageNoteBankPage);
        view.addFixedPage(uploadNoteController.getUploadNotePage(), "Upload Model.Note");
        view.manageNoteBankPage.refreshNotesListView();
        view.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(view.manageNoteBankPage.getNotes(), null );
    }

    @Override
    public void removeNote(ActionEvent e) {
        NoteBankView view = controller.getView();
        for(Note n: view.manageNoteBankPage.getSelectedNotes()) {
            controller.removeNote(n);
        }
        controller.save();
        controller.updateSubjectFile();
        view.manageNoteBankPage.refreshNotesListView();
        view.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(view.manageNoteBankPage.getNotes(), null );
    }

    @Override
    public void createIdea(ActionEvent e) {
        NoteBankView view = controller.getView();
        if(view.manageNoteBankPage.getSelectedNotes().size()>0) {
            ViewNotesController viewNotesController = new ViewNotesController(controller,view.manageNoteBankPage.getSelectedNotes(),true);
            viewNotesController.getViewNotes().setPreviousPage(view.manageNoteBankPage);
            view.addFixedPage(viewNotesController.getViewNotes(), "Select Notes to Add to Idea");
        }else {
            CreateAnIdeaController createAnIdeaController = new CreateAnIdeaController(controller);
            CreateAnIdeaPage createAnIdeaPage = createAnIdeaController.getCreateAnIdeaPage();
            createAnIdeaPage.setPreviousPage(view.manageNoteBankPage);
            view.addFixedPage(createAnIdeaPage, "Create an Idea");
        }
    }

    @Override
    public void viewNotes(ActionEvent e) {
        ManageNoteBankPage manageNoteBankPage = this.controller.getView().manageNoteBankPage;
        NoteBankView view = controller.getView();
        if(manageNoteBankPage.getSelectedNotes().size()>0) {
            ViewNotesController viewNotesController = new ViewNotesController(this.controller,manageNoteBankPage.getSelectedNotes(),false);
            viewNotesController.getViewNotes().setPreviousPage(manageNoteBankPage);
            view.addFixedPage(viewNotesController.getViewNotes(), "View Notes");
        }else {
            JOptionPane.showMessageDialog(manageNoteBankPage, "You must select notes to view");
        }
    }

    @Override
    public void subjectChange(ActionEvent e) {
        ManageNoteBankPage manageNoteBankPage = this.controller.getView().manageNoteBankPage;
        NoteBankView view = controller.getView();
        List<Note> selectedNotes = manageNoteBankPage.getSelectedNotes();
        String selectedSubject = manageNoteBankPage.getSelectedSubject();

        boolean shouldMoveSelectedNotesToNextSubject= manageNoteBankPage.shouldMoveSelectedToNextSubject()
                && selectedNotes.size()>0
                && JOptionPane.showConfirmDialog(manageNoteBankPage,
                "Are you sure you want move the selected notes to " + selectedSubject+ " ?" )==JOptionPane.YES_OPTION; ;

        if(shouldMoveSelectedNotesToNextSubject) {
            for(Note note: selectedNotes) {
                manageNoteBankPage.addNote(note, selectedSubject);
            }
            manageNoteBankPage.resetMoveSelectedToNextSubject();

        }
        if(selectedSubject.contains("+")) {
            manageNoteBankPage.startPlusBuffer();
            manageNoteBankPage.clearNotes();
        }else {
            manageNoteBankPage.refreshNotesListView();
            view.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(manageNoteBankPage.getNotes(), null );
        }
        manageNoteBankPage.setCurrentSubject(manageNoteBankPage.getSelectedSubject());
        controller.updateSubjectFile();
    }

    @Override
    public void deleteSubject(ActionEvent e) {
        ManageNoteBankPage manageNoteBankPage = this.controller.getView().manageNoteBankPage;
        NoteBankView view = controller.getView();
        manageNoteBankPage.deleteSubject(manageNoteBankPage.getSelectedSubject());
        view.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(manageNoteBankPage.getNotes(), null );
        manageNoteBankPage.refreshNotesListView();
        controller.updateSubjectFile();
    }

    @Override
    public void saveSubject(ActionEvent e) {
        ManageNoteBankPage manageNoteBankPage = this.controller.getView().manageNoteBankPage;
        NoteBankView view = controller.getView();
        manageNoteBankPage.createNewSubject();
        manageNoteBankPage.refreshNotesListView();
        view.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(manageNoteBankPage.getNotes(), null );
        controller.updateSubjectFile();
    }


    public ManageNoteBankController(NoteBankController controller){
        this.controller = controller;
    }
}
