package Controller;

import View.Handlers.MainWindowEventHandler;

import java.awt.event.ActionEvent;

public class MainWindowController implements MainWindowEventHandler {
    NoteBankController controller;

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
    public void createQuiz(ActionEvent e) {

    }

    @Override
    public void aboutWindow(ActionEvent e) {

    }

    public MainWindowController(NoteBankController controller){
        this.controller = controller;
    }
}
