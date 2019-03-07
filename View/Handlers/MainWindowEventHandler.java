package View.Handlers;

import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

/**
 * An interface for a controller to implement that will receive events from the GUI.
 */
public interface MainWindowEventHandler {

    //=====MenuBar======

    /**
     * MenuBar Listener
     * On File->Import button press
     * @param e ActionEvent
     */
    void performImport(ActionEvent e);

    /**
     * MenuBar Listener
     * On File->Export button press
     * @param e ActionEvent
     */
    void performExport(ActionEvent e);

    /**
     * MenuBar Listener
     * On File->Quit button press
     * @param e ActionEvent
     */
    void performQuit(ActionEvent e);

    /**
     * MenuBar Listener
     * On Edit->Undo button press
     * @param e ActionEvent
     */
    void performUndo(ActionEvent e);

    /**
     * MenuBar Listener
     * On Edit->Redo button press
     * @param e ActionEvent
     */
    void performRedo(ActionEvent e);

    /**
     * MenuBar Listener
     * On Help->Help button press
     * @param e ActionEvent
     */
    void helpWindow(ActionEvent e);

    /**
     * MenuBar Listener
     * On Help->About button press
     * @param e ActionEvent
     */
    void aboutWindow(ActionEvent e);


    void createQuiz(ActionEvent e);

    void createSubject(ActionEvent e);

    void subjectSelected(ItemEvent e);

    void switchToNotesBank(ActionEvent e);

    void viewIdeasTopics(ActionEvent e);

    void createIdea(ActionEvent e);

    void removeNote(ActionEvent e);

    void uploadNote(ActionEvent e);

    void viewNotes(ActionEvent e);

    void pageChanged(ChangeEvent e);

    void combineNotes(ActionEvent e);

    void disbandNotes(ActionEvent e);

    void move(ActionEvent e);

    void disableSubjectFiltering(ItemEvent e);

    void deleteTopicIdea(ActionEvent e);

    void subjectRemove(ActionEvent e);

    void editIdea(ActionEvent e);



}



