package Controller;

import View.CreateAnIdeaPage;
import View.MainWindowMenu.SubjectMenu;
import View.ManageNoteBankPage;
import View.NoteBankView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class FileMenuController {

    NoteBankController controller;
    public void createSubject(ActionEvent e){
        ManageNoteBankPage manageNoteBankPage = this.controller.getView().manageNoteBankPage;
        NoteBankView view = controller.getView();
        String subject = manageNoteBankPage.createNewSubject();

        SubjectMenu subjectMenu = view.getMenus().getViewMenu().getSubjectMenu();
        subjectMenu.addSubject(subject);
        subjectMenu.selectSubject(subject);
        manageNoteBankPage.setCurrentSubject(subject);
        manageNoteBankPage.addNotes(subject,manageNoteBankPage.getSelectedNotes());

        manageNoteBankPage.refreshNotesListView();
        view.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(manageNoteBankPage.getNotes(), null );
        controller.updateSubjectFile();

    }

    public void uploadNotes(){
        NoteBankView view = controller.getView();
        UploadNoteController uploadNoteController = new UploadNoteController(controller);
        uploadNoteController.getUploadNotePage().setPreviousPage((JPanel) controller.getView().getPage());
        view.addFixedPage(uploadNoteController.getUploadNotePage(), "Upload Note");
        view.manageNoteBankPage.refreshNotesListView();
        view.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(view.manageNoteBankPage.getNotes(), null );
    }

    public void createIdea(){
        NoteBankView view = controller.getView();
        if(view.manageNoteBankPage.getSelectedNotes().size()>0) {
            ViewNotesController viewNotesController = new ViewNotesController(controller,view.manageNoteBankPage.getSelectedNotes(),true);
            viewNotesController.getViewNotes().setPreviousPage(view.manageNoteBankPage);
            view.addFixedPage(viewNotesController.getViewNotes(), "Select Notes to Add to Idea");
        }else {
            CreateAnIdeaController createAnIdeaController = new CreateAnIdeaController(controller, view.manageNoteBankPage.getNotesOfSubject("All"));
            CreateAnIdeaPage createAnIdeaPage = createAnIdeaController.getCreateAnIdeaPage();
            createAnIdeaPage.setPreviousPage(view.manageNoteBankPage);
            view.addFixedPage(createAnIdeaPage, "Create an Idea");
        }
    }

    public FileMenuController(NoteBankController controller){
        this.controller = controller;
    }
}
