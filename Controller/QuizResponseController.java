package Controller;

import View.Handlers.QuizResponseEventHandler;
import View.NoteBankView;
import View.QuizResponsePage;
import View.ViewNotesPage;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class QuizResponseController implements QuizResponseEventHandler {
    NoteBankController controller;
    private QuizResponsePage quizResponse;

    public QuizResponsePage getQuizResponse(){
        return quizResponse;
    }

    @Override
    public void submit(ActionEvent e) {
        String evaluatedAnswer = quizResponse.getResponse();
        QuizAnswerEvaluationController quizAnswerEvaluationController = new QuizAnswerEvaluationController(controller,evaluatedAnswer);
        NoteBankView view = controller.getView();
        view.replace(quizAnswerEvaluationController.getQuizAnswerEvaluationPage(), "Revision Quiz" );

    }

    @Override
    public void quit(ActionEvent e) {
        NoteBankView view = controller.getView();
        view.setFixedPage(view.manageNoteBankPage,"Manage Note Bank Page");

    }

    @Override
    public void viewPromptNote(ActionEvent e) {
        NoteBankView view = controller.getView();
        if(quizResponse.getPromptNotes().isEmpty()) {
            JOptionPane.showMessageDialog(quizResponse, "No prompt notes");
            return;
        }

        ViewNotesController viewNotesController = new ViewNotesController(controller,quizResponse.getPromptNotes(),false);

        view.addFixedPage(viewNotesController.getViewNotes(), "View Prompt Notes");
    }

    public QuizResponseController(NoteBankController controller){
        this.controller = controller;
        quizResponse = new QuizResponsePage(this,controller.getCurrentQuizIdea());

    }
}
