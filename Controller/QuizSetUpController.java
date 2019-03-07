package Controller;

import Model.Idea;
import Model.Topic;
import View.Handlers.QuizSetUpEventHandler;
import View.NoteBankView;
import View.QuizSetUpPage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class QuizSetUpController implements QuizSetUpEventHandler {
    private  NoteBankController controller;

    public QuizSetUpController(NoteBankController controller){
        this.controller = controller;
    }

    @Override
    public void createQuiz(ActionEvent e) {
        /**
         * private Map<Idea,Integer> simulatedIdeas;
         * private List<Idea> currentQuiz;
         */

        QuizSetUpPage quizSetUpPage = controller.getView().quizSetUpPage;
        NoteBankView view = controller.getView();

        int questionType = quizSetUpPage.getQuestionType();
        int noQuestions = quizSetUpPage.getNoQuestions();

        Topic topic = quizSetUpPage.getTopicChosen();
        List<Idea> currentQuiz = new ArrayList<Idea>();
        /*
         * Untested ideas
         * Only key words
         * Only questions
         * All
         */
        for(Idea a: topic.getAllIdeas()) {
            switch(questionType) {
                case 0: if(controller.getQuizReader().getNumberOfTimes(a, new Date(System.currentTimeMillis() - controller.day) )>0)
                            currentQuiz.add(a);
                    break;
                case 1: if(a.getPromptType())
                            currentQuiz.add(a);
                    break;
                case 2: if(!a.getPromptType())
                            currentQuiz.add(a);
                    break;
                default: currentQuiz.add(a);
                    break;
            }
        }

        Collections.shuffle(currentQuiz);

        for(int i=0; i<(currentQuiz.size() - noQuestions); i++){
            currentQuiz.remove(i);
            i-=1;
        }

        controller.resetQuiz(currentQuiz);
        controller.setQuizCreatedTime(new Date(System.currentTimeMillis()));

        if(currentQuiz.size()>0) {
            QuizResponseController quizResponseController = new QuizResponseController(controller);
            view.replace(quizResponseController.getQuizResponse(), "Revision Quiz");
        }else {
            JOptionPane.showMessageDialog(quizSetUpPage, "Can't create empty Quiz. ");
        }
    }

    @Override
    public void chooseTopic(ActionEvent e) {
        NoteBankView view = controller.getView();
        QuizSetUpPage quizSetUpPage = controller.getView().quizSetUpPage;
        Topic selectedItem = quizSetUpPage.getTopicChosen();
        if(selectedItem!=null) {
            view.quizSetUpPage.setSliderMinMax(1, selectedItem.getAllIdeas().size());
        }
    }
}

