package Controller;

import Model.Idea;
import Model.Note;
import Model.TextExcerpt;
import View.*;
import View.Handlers.QuizAnswerEvaluationEventHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class QuizAnswerEvaluationController implements QuizAnswerEvaluationEventHandler {

    private QuizAnswerEvaluationPage quizAnswerEvaluationPage;
    private NoteBankController controller;

    public QuizAnswerEvaluationPage getQuizAnswerEvaluationPage() {
        return quizAnswerEvaluationPage;
    }

    public QuizAnswerEvaluationController(NoteBankController controller, String evaluatedAnswer) {
        this.controller = controller;
        quizAnswerEvaluationPage = new QuizAnswerEvaluationPage(this,controller.getCurrentQuizIdea(),evaluatedAnswer);
    }

    @Override
    public void quit(ActionEvent e) {
        NoteBankView view = controller.getView();
        view.replace(view.manageNoteBankPage, "Revision Quiz" );
    }

    @Override
    public void viewNotes(ActionEvent e) {
        NoteBankView view = controller.getView();
        ViewNotesController viewNotesController = new ViewNotesController(controller,controller.getCurrentQuizIdea().getNotes(),false);
        viewNotesController.getViewNotes().setPreviousPage(quizAnswerEvaluationPage);
        view.addFixedPage(viewNotesController.getViewNotes(), "View Ideas Notes");
    }

    @Override
    public void setToFinalNote(ActionEvent e) {
        try {
            NoteBankView view = controller.getView();
            if(controller.getEvaluatedAnswer()!=null) {
                controller.removeNote(controller.getEvaluatedAnswer());
            }
            Idea currentIdea = controller.getCurrentQuizIdea();

            controller.setEvaluatedAnswer(new TextExcerpt( ("EvaluatedAnswer ("+currentIdea.getPrompt()+")").replaceAll("\\s","-"),
                    quizAnswerEvaluationPage.getAnswer(), controller.getPathToDirectory()));

            controller.addNote(controller.getEvaluatedAnswer(),view.manageNoteBankPage.getAllSubjects(currentIdea.getNotes()));

            currentIdea.setFinalNote(controller.getEvaluatedAnswer());

            controller.updateSubjectFile();
            controller.save();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void nextMark(ActionEvent e) {
        NoteBankView view = controller.getView();
        Date finishIdeaDate = new Date();

        finishIdeaDate.setTime( System.currentTimeMillis() );
        controller.getQuizReader().save(controller.getCurrentQuizIdea(), (System.currentTimeMillis() - controller.getQuizCreatedTime().getTime())/1000,
                quizAnswerEvaluationPage.getReadinessLevel(), finishIdeaDate);

        controller.nextIdea();

        if(controller.getCurrentIdeaIndex()<controller.getSizeOfCurrentQuiz()) {
            controller.setQuizCreatedTime(new Date(System.currentTimeMillis()));
            QuizResponseController quizResponseController = new QuizResponseController(controller);
            view.replace(quizResponseController.getQuizResponse(), "Revision Quiz");
        }else {

            QuizResultsController quizResultsController = new QuizResultsController(controller);
            quizResultsController.getQuizResultsPage().refreshIdeasComboBox();
            view.replace(quizResultsController.getQuizResultsPage(), "Revision Quiz" );
        }
    }

    @Override
    public void viewFinalNote(ActionEvent e) {
        NoteBankView view = controller.getView();
        Note finalNote = controller.getCurrentQuizIdea().getFinalNote();
        if(finalNote!=null) {
            ArrayList<Note> finalNoteList = new ArrayList<Note>();
            finalNoteList.add(finalNote);
            ViewNotesController viewNotesController = new ViewNotesController(controller,finalNoteList,false);
            ViewNotesPage viewNotesPage = viewNotesController.getViewNotes();
            viewNotesPage.setPreviousPage(quizAnswerEvaluationPage);
            view.addFixedPage(viewNotesPage, "View Final Note");
        }else {
            JOptionPane.showMessageDialog(quizAnswerEvaluationPage, "No final note has been selected");
        }

    }
}
