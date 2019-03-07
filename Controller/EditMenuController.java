package Controller;

import Model.Idea;
import Model.Note;
import View.ManageIdeasPage;
import View.NoteBankView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class EditMenuController {
    NoteBankController controller;
    public void removeNote() {
        NoteBankView view = controller.getView();

        if(view.manageNoteBankPage.getCurrentSubject().equals("All") &&
                JOptionPane.showConfirmDialog(view,
                        "Are you sure you want to permenantly delete the selected note(s) ?") == JOptionPane.NO_OPTION) {
            return;
        }
        for(Note n: view.manageNoteBankPage.getSelectedNotes()) {
            controller.removeNote(n);
        }
        controller.save();
        controller.updateSubjectFile();
        view.manageNoteBankPage.refreshNotesListView();
        view.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(view.manageNoteBankPage.getNotes(), null );
    }

    public void editIdea() {
        ManageIdeasPage manageIdeasPage = controller.getView().manageIdeasPage;
        Idea idea = manageIdeasPage.getSelectedIdea();
        NoteBankView view = controller.getView();
        if(idea!=null) {
            CreateAnIdeaController createAnIdeaController = new CreateAnIdeaController(controller,idea, view.manageNoteBankPage.getNotes());
            createAnIdeaController.getCreateAnIdeaPage().changeTitle("Edit Idea");
            createAnIdeaController.getCreateAnIdeaPage().setPreviousPage(manageIdeasPage);
            view.addFixedPage(createAnIdeaController.getCreateAnIdeaPage(), "Edit Idea");
            manageIdeasPage.refresh();
            controller.save();
        }else {
            JOptionPane.showMessageDialog(manageIdeasPage, "No Idea selected");
        }
    }

    public void removeIdea( ) {
        ManageIdeasPage manageIdeasPage = controller.getView().manageIdeasPage;
        if(manageIdeasPage.getSelected().size()>0) {
            Object deletedIdea = manageIdeasPage.deleteNode(manageIdeasPage.getSelected().get(0));
            if(deletedIdea instanceof Idea)
                controller.getQuizReader().delete((Idea) deletedIdea);
            controller.save();
        }
    }

    public void deleteSubject(){
        NoteBankView view = controller.getView();
        if(view.manageNoteBankPage.getCurrentSubject().equals("All")){
            JOptionPane.showMessageDialog(view,"Can't delete this subject");
            return;
        }
        boolean shouldDelete = JOptionPane.showConfirmDialog(view,
                "Are you sure you want to delete subject " + view.manageNoteBankPage.getCurrentSubject() + " ?") == JOptionPane.YES_OPTION;
        if(shouldDelete) {
            String delete = view.manageNoteBankPage.getCurrentSubject();
            view.manageNoteBankPage.deleteSubject(delete);
            view.getMenus().getViewMenu().getSubjectMenu().selectSubject("All");
            view.getMenus().getViewMenu().getSubjectMenu().removeSubject(delete);
            view.manageNoteBankPage.setCurrentSubject("All");
            view.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(view.manageNoteBankPage.getNotesOfSubject("All"), null);
            view.manageNoteBankPage.refreshNotesListView();
            controller.updateSubjectFile();

        }
    }

    public EditMenuController(NoteBankController controller){
        this.controller = controller;
    }
}
