package View.Handlers;

import java.awt.event.ActionEvent;

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



}



