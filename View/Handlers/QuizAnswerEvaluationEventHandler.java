package View.Handlers;

import java.awt.event.ActionEvent;

public interface QuizAnswerEvaluationEventHandler {
    void quit(ActionEvent e);
    void viewNotes(ActionEvent e);
    void setToFinalNote(ActionEvent e);
    void nextMark(ActionEvent e);
    void viewFinalNote(ActionEvent e);

}
