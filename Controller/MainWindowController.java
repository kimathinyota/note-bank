package Controller;

import View.*;
import View.Handlers.MainWindowEventHandler;
import View.Handlers.QuizSetUpEventHandler;

import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

public class MainWindowController implements MainWindowEventHandler {

    NoteBankController controller;
    ViewMenuController viewMenuController;
    IdeaMenuController ideaMenuController;
    FileMenuController fileMenuController;
    EditMenuController editMenuController;

    @Override
    public void viewNotes(ActionEvent e) {
        viewMenuController.viewNotes();
    }

    @Override
    public void uploadNote(ActionEvent e) {
        fileMenuController.uploadNotes();
    }

    @Override
    public void performImport(ActionEvent e) {

    }

    @Override
    public void performExport(ActionEvent e) {

    }

    @Override
    public void performQuit(ActionEvent e) {

    }

    @Override
    public void performUndo(ActionEvent e) {

    }

    @Override
    public void performRedo(ActionEvent e) {

    }

    @Override
    public void helpWindow(ActionEvent e) {

    }

    @Override
    public void switchToNotesBank(ActionEvent e) {
        NoteBankView view = controller.getView();
        view.setFixedPage(view.manageNoteBankPage,"Manage Note Bank");

    }

    @Override
    public void pageChanged(ChangeEvent e) {
        NoteBankView view = controller.getView();
        Component page = view.getPage();

        if(! (page instanceof ManageNoteBankPage) ){
            view.getMenus().getIdeaMenu().setEnabled(false);
        }

        boolean isQuizPage = (page instanceof QuizSetUpPage) || (page instanceof QuizResultsPage)
                        ||  (page instanceof QuizResponsePage) || (page instanceof QuizAnswerEvaluationPage);



        boolean shouldDisplayNote = !isQuizPage;

        System.out.println(shouldDisplayNote);

        view.getMenus().getViewMenu().getNoteMenu().setEnabled(shouldDisplayNote);
        view.getMenus().getFileMenu().getNewMenu().setEnabled(shouldDisplayNote);
        view.getMenus().getEditMenu().getRemoveNotes().setEnabled(shouldDisplayNote);



        boolean canModifyIdeas = page instanceof ManageIdeasPage;
        view.getMenus().getIdeaMenu().setEnabled(canModifyIdeas);
        view.getMenus().getEditMenu().getEditIdea().setEnabled(canModifyIdeas);
        view.getMenus().getEditMenu().getRemoveTopicOrIdea().setEnabled(canModifyIdeas);
    }

    @Override
    public void createQuiz(ActionEvent e) {
        NoteBankView view = controller.getView();
        view.setFixedPage(view.quizSetUpPage,"Revision Quiz");

    }

    @Override
    public void subjectRemove(ActionEvent e) {
        editMenuController.deleteSubject();
    }

    @Override
    public void viewIdeasTopics(ActionEvent e) {
        NoteBankView view = controller.getView();
        view.setFixedPage(view.manageIdeasPage,"Manage Ideas");
        view.getMenus().getIdeaMenu().setEnabled(true);
    }

    @Override
    public void createIdea(ActionEvent e) {
        this.fileMenuController.createIdea();
    }

    @Override
    public void removeNote(ActionEvent e) {
        editMenuController.removeNote();
    }

    @Override
    public void aboutWindow(ActionEvent e) {

    }

    public void subjectSelected(ItemEvent e) {
        viewMenuController.subjectSelected(e);
    }

    public void createSubject(ActionEvent e){
        fileMenuController.createSubject(e);
    }

    @Override
    public void combineNotes(ActionEvent e) {
        ideaMenuController.combine();
    }

    @Override
    public void disbandNotes(ActionEvent e) {
        ideaMenuController.disband();
    }

    @Override
    public void move(ActionEvent e) {
        ideaMenuController.move();
    }

    @Override
    public void editIdea(ActionEvent e) {
        editMenuController.editIdea();
    }

    @Override
    public void disableSubjectFiltering(ItemEvent e) {
        //manageIdeasController
       ideaMenuController.stopSubjectFiltering(e);
    }

    @Override
    public void deleteTopicIdea(ActionEvent e) {
        editMenuController.removeIdea();
    }

    public MainWindowController(NoteBankController controller){
        this.controller = controller;
        viewMenuController = new ViewMenuController(this.controller);
        fileMenuController = new FileMenuController(this.controller);
        editMenuController = new EditMenuController(this.controller);
        ideaMenuController = new IdeaMenuController(this.controller);
    }
}
