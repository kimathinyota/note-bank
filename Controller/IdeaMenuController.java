package Controller;

import Model.Idea;
import View.ManageIdeasPage;
import View.NoteBankView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

public class IdeaMenuController {

    NoteBankController controller;

    public void combine() {
        ManageIdeasPage manageIdeasPage = controller.getView().manageIdeasPage;
        manageIdeasPage.combineNodes(manageIdeasPage.getSelected());
        controller.save();
    }

    public void disband() {
        ManageIdeasPage manageIdeasPage = controller.getView().manageIdeasPage;
        manageIdeasPage.disbandNodes(manageIdeasPage.getSelected());
        controller.save();
    }

    public void move() {
        ManageIdeasPage manageIdeasPage = controller.getView().manageIdeasPage;
        manageIdeasPage.moveNode(manageIdeasPage.getSelected());
        controller.save();


    }

    public void viewAllIdeas() {
        ManageIdeasPage manageIdeasPage = controller.getView().manageIdeasPage;
        NoteBankView view = controller.getView();
        manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(view.manageNoteBankPage.getNotes(), manageIdeasPage.getSelectedTopics());

    }

    public void showAllIdeasAndTopicsForSelected(){
        NoteBankView view = controller.getView();
        ManageIdeasPage manageIdeasPage = view.manageIdeasPage;
        manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(view.manageNoteBankPage.getNotes(), manageIdeasPage.getSelectedTopics());
    }

    public void refreshIdeasTree(){
        NoteBankView view = controller.getView();
        ManageIdeasPage manageIdeasPage = view.manageIdeasPage;
        manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(view.manageNoteBankPage.getNotes(), null);
    }

    public void stopSubjectFiltering(ItemEvent e) {
        if(e.getStateChange()==ItemEvent.SELECTED){
            this.showAllIdeasAndTopicsForSelected();
        }else if(e.getStateChange()==ItemEvent.DESELECTED){
            this.refreshIdeasTree();
        }
    }

    public IdeaMenuController(NoteBankController controller){
        this.controller = controller;
    }
}
