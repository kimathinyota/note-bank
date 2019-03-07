package Controller;

import Model.Note;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.List;

import View.CreateAnIdeaPage;
import View.Handlers.ViewNotesEventHandler;
import View.NoteBankView;
import View.ViewNotesPage;

import javax.swing.*;

public class ViewNotesController implements ViewNotesEventHandler {
    private ViewNotesPage viewNotesPage;
    private NoteBankController controller;

    public ViewNotesPage getViewNotes(){
        return this.viewNotesPage;
    }

    public ViewNotesController(NoteBankController controller, List<Note>selectedNotes, boolean enableCreateIdeaFunctionality){
        this.controller = controller;
        if(selectedNotes.size()>0)
            viewNotesPage = new ViewNotesPage(this,enableCreateIdeaFunctionality,selectedNotes,controller.getPathToDirectory());
    }

    @Override
    public void back(ActionEvent e) {
        NoteBankView view = controller.getView();
        JPanel prevPage = viewNotesPage.getPreviousPage();
        view.deletePage(viewNotesPage);
        view.restorePages();
        view.switchPage(prevPage);

    }

    @Override
    public void createIdea(ActionEvent e) {
        NoteBankView view = controller.getView();
        CreateAnIdeaController createAnIdeaController = new CreateAnIdeaController(controller,viewNotesPage.getSelectedNotes(),null,null,null, null,view.manageNoteBankPage.getNotes());
        CreateAnIdeaPage createAnIdea = createAnIdeaController.getCreateAnIdeaPage();
        createAnIdea.setPreviousPage(viewNotesPage);
        view.addFixedPage(createAnIdea, "Create Idea");
    }

    @Override
    public void clearSelectToggle(ActionEvent e) {
        viewNotesPage.manageToggle();
    }

    @Override
    public void addToIdea(ItemEvent e) {
        if(viewNotesPage.isAddToIdeaChecked()) {
            viewNotesPage.selectCurrentImage();
        }else {
            viewNotesPage.deselectCurrentImage();
        }

    }

    @Override
    public void nextPage(ActionEvent e) {
        viewNotesPage.nextImage();
    }

    @Override
    public void previousPage(ActionEvent e) {
        viewNotesPage.prevImage();
    }
}
