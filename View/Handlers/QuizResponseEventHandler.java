package View.Handlers;

import java.awt.event.ActionEvent;

public interface QuizResponseEventHandler {
    void submit(ActionEvent e);
    void quit(ActionEvent e);
    void viewPromptNote(ActionEvent e);
}
