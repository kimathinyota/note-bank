package Controller;

import Model.Idea;
import Model.QuizReader;
import View.CreateAnIdeaPage;
import View.Handlers.QuizResultsEventHandler;
import View.NoteBankView;
import View.QuizResultsPage;

import java.awt.event.ActionEvent;
import java.util.Date;

public class QuizResultsController implements QuizResultsEventHandler {

    private QuizResultsPage resultsPage;
    private NoteBankController controller;

    @Override
    public void pickIdea(ActionEvent e) {
        Idea idea = resultsPage.getSelectedIdea();
        QuizReader quizReader = controller.getQuizReader();

        long currentTime = System.currentTimeMillis();
        double averageCompletionTime = quizReader.getAverageCompletionTime(idea, new Date(currentTime-controller.day) );
        Integer[] ideaSimulationTimes = { quizReader.getNumberOfTimes(idea, new Date(currentTime - controller.hour) ),
                quizReader.getNumberOfTimes(idea, new Date(currentTime - controller.day) ),
                quizReader.getNumberOfTimes(idea, new Date(currentTime - controller.week) ),
                quizReader.getNumberOfTimes(idea, new Date(currentTime - controller.week) ),
                quizReader.getNumberOfTimes(idea, new Date(currentTime - controller.month) ) };

        double weightedIdeasTimes = 0;
        for(int i=0; i<ideaSimulationTimes.length; i++) {
            weightedIdeasTimes += (100*Math.pow(2, (0-i))*ideaSimulationTimes[i]);
        }

        weightedIdeasTimes/=controller.getMaxWeightedAverageSimulationPerTime();

        if(weightedIdeasTimes>100)
            weightedIdeasTimes = 100;


        double userSatisfactionLastDay = quizReader.getAverageConfidenceLevel(idea, new Date(currentTime-controller.day));

        double readinessLevel =  (((100*userSatisfactionLastDay)/4) + weightedIdeasTimes)/2;

        resultsPage.setAnalysis((int) averageCompletionTime, ideaSimulationTimes, userSatisfactionLastDay, (int) readinessLevel);

    }

    @Override
    public void finishQuiz(ActionEvent e) {
        NoteBankView view = controller.getView();
        view.replace(view.quizSetUpPage, "Revision Quiz" );
    }

    @Override
    public void seeIdea(ActionEvent e) {
        NoteBankView view = controller.getView();
        Idea idea = resultsPage.getSelectedIdea();
        CreateAnIdeaController createAnIdeaController = new CreateAnIdeaController(controller,idea);
        CreateAnIdeaPage createAnIdeaPage = createAnIdeaController.getCreateAnIdeaPage();
        createAnIdeaPage.changeTitle("Edit Idea");
        createAnIdeaPage.setPreviousPage(resultsPage);
        view.addFixedPage(createAnIdeaPage, "Edit Idea");
        controller.save();
    }

    public QuizResultsPage getQuizResultsPage(){
        return this.resultsPage;
    }

    public QuizResultsController(NoteBankController controller){
        this.controller = controller;
        resultsPage = new QuizResultsPage(this,controller.getQuiz());

    }

}
