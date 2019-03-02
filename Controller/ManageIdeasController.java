package Controller;

import Model.Idea;
import View.Handlers.ManageIdeasEventHandler;
import View.ManageIdeasPage;
import View.NoteBankView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ManageIdeasController implements ManageIdeasEventHandler {
    private  NoteBankController controller;

    public ManageIdeasController(NoteBankController controller){
        this.controller = controller;
    }

    @Override
    public void combine(ActionEvent e) {
        ManageIdeasPage manageIdeasPage = controller.getView().manageIdeasPage;
        manageIdeasPage.combineNodes(manageIdeasPage.getSelected());
        controller.save();
    }

    @Override
    public void disband(ActionEvent e) {
        ManageIdeasPage manageIdeasPage = controller.getView().manageIdeasPage;
        manageIdeasPage.disbandNodes(manageIdeasPage.getSelected());
        controller.save();
    }

    @Override
    public void edit(ActionEvent e) {
        ManageIdeasPage manageIdeasPage = controller.getView().manageIdeasPage;
        Idea idea = manageIdeasPage.getSelectedIdea();
        NoteBankView view = controller.getView();
        if(idea!=null) {
            CreateAnIdeaController createAnIdeaController = new CreateAnIdeaController(controller,idea);
            createAnIdeaController.getCreateAnIdeaPage().changeTitle("Edit Idea");
            createAnIdeaController.getCreateAnIdeaPage().setPreviousPage(manageIdeasPage);
            view.addFixedPage(createAnIdeaController.getCreateAnIdeaPage(), "Edit Idea");
            manageIdeasPage.refresh();
            controller.save();
        }else {
            JOptionPane.showMessageDialog(manageIdeasPage, "No Idea selected");
        }
    }

    @Override
    public void delete(ActionEvent e) {
        ManageIdeasPage manageIdeasPage = controller.getView().manageIdeasPage;
        if(manageIdeasPage.getSelected().size()>0) {
            Object deletedIdea = manageIdeasPage.deleteNode(manageIdeasPage.getSelected().get(0));
            if(deletedIdea instanceof Idea)
                controller.getQuizReader().delete((Idea) deletedIdea);
            controller.save();
        }
    }

    @Override
    public void move(ActionEvent e) {
        ManageIdeasPage manageIdeasPage = controller.getView().manageIdeasPage;
        manageIdeasPage.moveNode(manageIdeasPage.getSelected());
        controller.save();
    }

    @Override
    public void viewAllIdeas(ActionEvent e) {
        ManageIdeasPage manageIdeasPage = controller.getView().manageIdeasPage;
        NoteBankView view = controller.getView();
        manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(view.manageNoteBankPage.getNotes(), manageIdeasPage.getSelectedTopics());

    }
}
