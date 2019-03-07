package Controller;

import View.Handlers.RightClickEventHandler;
import java.awt.event.ActionEvent;


public class RightClickController implements RightClickEventHandler {

    private ViewMenuController viewMenuController;
    private EditMenuController editMenuController;
    private IdeaMenuController ideaMenuController;
    private FileMenuController fileMenuController;

    @Override
    public void viewNotes(ActionEvent e) {
        viewMenuController.viewNotes();
    }

    @Override
    public void removeNotes(ActionEvent e) {
        editMenuController.removeNote();
    }

    @Override
    public void editIdea(ActionEvent e) {
        editMenuController.editIdea();
    }

    @Override
    public void createIdea(ActionEvent e) {
        fileMenuController.createIdea();
    }

    @Override
    public void combine(ActionEvent e) {
        ideaMenuController.combine();
    }

    @Override
    public void disband(ActionEvent e) {
        ideaMenuController.disband();
    }

    @Override
    public void move(ActionEvent e) {
        ideaMenuController.move();
    }

    @Override
    public void refreshIdeaTree(ActionEvent e) {
        ideaMenuController.refreshIdeasTree();
    }

    @Override
    public void stopFiltering(ActionEvent e) {
        ideaMenuController.showAllIdeasAndTopicsForSelected();
    }

    @Override
    public void removeIdeaTopic(ActionEvent e) {
        editMenuController.removeIdea();
    }

    @Override
    public void editNote(ActionEvent e) {

    }

    public RightClickController(NoteBankController controller){
        viewMenuController = new ViewMenuController(controller);
        editMenuController = new EditMenuController(controller);
        ideaMenuController = new IdeaMenuController(controller);
        fileMenuController = new FileMenuController(controller);
    }
}
