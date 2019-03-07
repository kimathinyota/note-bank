package Controller;

import Model.Idea;
import Model.Note;
import View.*;
import View.Handlers.CreateAnIdeaEventHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;


public class CreateAnIdeaController implements CreateAnIdeaEventHandler {

    private NoteBankController controller;
    private CreateAnIdeaPage createAnIdeaPage;

    public CreateAnIdeaPage getCreateAnIdeaPage() {
        return createAnIdeaPage;
    }

    public CreateAnIdeaController(NoteBankController controller, List<Note>notes, List<String> keyWords, String promptInp, Boolean isPromptKeyWord, Note finalNote, List<Note>allNotes) {
        createAnIdeaPage = new CreateAnIdeaPage(this,notes,keyWords, promptInp, isPromptKeyWord, finalNote, allNotes);
        this.controller = controller;
    }

    public CreateAnIdeaController(NoteBankController controller, Idea idea, List<Note>allNotes) {
        createAnIdeaPage = new CreateAnIdeaPage(this,idea, allNotes);
        this.controller = controller;
    }

    public CreateAnIdeaController(NoteBankController controller, HashMap<Note,Integer> notes, List<String> keyWords, String promptInp, Boolean isPromptKeyWord, Note finalNote, List<Note>allNotes) {
        createAnIdeaPage = new CreateAnIdeaPage(this,notes,keyWords, promptInp, isPromptKeyWord, finalNote, allNotes);
        this.controller = controller;
    }

    public CreateAnIdeaController(NoteBankController controller,List<Note>allNotes) {
        createAnIdeaPage = new CreateAnIdeaPage(this,allNotes);
        this.controller = controller;
    }

    @Override
    public void back(ActionEvent e) {
        NoteBankView view = controller.getView();
        view.deletePage(createAnIdeaPage);
        view.restorePages();
        view.switchPage(createAnIdeaPage.getPreviousPage());
    }

    @Override
    public void removeNote(ActionEvent e) {
        for(Note note: createAnIdeaPage.getSelectedNotes()  ) {
            createAnIdeaPage.removeNote(note);
        }
    }

    @Override
    public void selectFinalNote(ActionEvent e) {
        if(createAnIdeaPage.getSelectedNotes().size()>0) {
            createAnIdeaPage.setFinalNote( createAnIdeaPage.getSelectedNotes().get(0)  );
        }
    }

    @Override
    public void addKeyWord(ActionEvent e) {
        createAnIdeaPage.addKeyWord();
    }

    @Override
    public void removeKeyWord(ActionEvent e) {
        for(String keyWord: createAnIdeaPage.getSelectedKeyWords()) {
            createAnIdeaPage.removeKeyWord(keyWord);
        }
    }

    @Override
    public void saveIdea(ActionEvent e) {
        NoteBankView view = controller.getView();
        if(createAnIdeaPage.getPromptText().length()>3) {
            Idea createdIdea = createAnIdeaPage.getIdea();
            view.manageNoteBankPage.addNotes(view.manageNoteBankPage.getCurrentSubject(),createdIdea.getNotes());

            if(!createAnIdeaPage.isEdited())
                view.manageIdeasPage.addToRoot(createdIdea);

            if(createAnIdeaPage.getPreviousPage() instanceof ViewNotesPage) {
                view.backToPreviousPage(createAnIdeaPage,createAnIdeaPage.getPreviousPage());
                view.backToPreviousPage(createAnIdeaPage.getPreviousPage(),
                        ((ViewNotesPage) createAnIdeaPage.getPreviousPage()).getPreviousPage());
            }else {
                view.backToPreviousPage(createAnIdeaPage,createAnIdeaPage.getPreviousPage());
            }
            view.manageIdeasPage.refresh();
            controller.save();
            controller.updateSubjectFile();

        }else {
            JOptionPane.showMessageDialog(createAnIdeaPage, "Invalid prompt entered");
        }
    }

    @Override
    public void viewAllNotes(ActionEvent e) {
        NoteBankView view = controller.getView();
        if(createAnIdeaPage.getNotes().size()<1){
            JOptionPane.showMessageDialog(view,"No notes selected");
            return;
        }

        ViewNotesController viewNotesController = new ViewNotesController(controller,createAnIdeaPage.getNotes(),false);
        ViewNotesPage viewNotesPage = viewNotesController.getViewNotes();
        viewNotesPage.setPreviousPage(createAnIdeaPage);
        view.addFixedPage(viewNotesPage, "Select Notes to Add to Idea");

    }
}
