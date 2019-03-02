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

    public CreateAnIdeaController(NoteBankController controller, List<Note>notes, List<String> keyWords, String promptInp, Boolean isPromptKeyWord, Note finalNote) {
        createAnIdeaPage = new CreateAnIdeaPage(this,notes,keyWords, promptInp, isPromptKeyWord, finalNote);
        this.controller = controller;
    }

    public CreateAnIdeaController(NoteBankController controller, Idea idea) {
        createAnIdeaPage = new CreateAnIdeaPage(this,idea);
        this.controller = controller;
    }

    public CreateAnIdeaController(NoteBankController controller, HashMap<Note,Integer> notes, List<String> keyWords, String promptInp, Boolean isPromptKeyWord, Note finalNote) {
        createAnIdeaPage = new CreateAnIdeaPage(this,notes,keyWords, promptInp, isPromptKeyWord, finalNote);
        this.controller = controller;
    }

    public CreateAnIdeaController(NoteBankController controller) {
        createAnIdeaPage = new CreateAnIdeaPage(this);
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
    public void addNote(ActionEvent e) {
        NoteBankView view = controller.getView();
        SelectNotePageController selectNotePageController = new SelectNotePageController(controller,view.manageNoteBankPage.getNotes());
        SelectNotesPage selectNotesPage = selectNotePageController.getSelectNotePage();
        selectNotesPage.setPreviousPage(createAnIdeaPage);
        view.addFixedPage(selectNotesPage, "Select notes to add to Idea");
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

            if(createAnIdeaPage.getPreviousPage() instanceof ManageNoteBankPage){
                view.manageIdeasPage.addToRoot(createdIdea);
            }

            if(createAnIdeaPage.getPreviousPage() instanceof ViewNotesPage) {
                view.manageIdeasPage.addToRoot(createdIdea);
                view.deletePage(createAnIdeaPage);
                view.restorePages();
                view.deletePage(createAnIdeaPage.getPreviousPage());
                view.restorePages();
                view.switchPage( ((ViewNotesPage) createAnIdeaPage.getPreviousPage() ).getPreviousPage() );

            }else {
                //Assumes it is being edited
                view.deletePage(createAnIdeaPage);
                view.restorePages();
                view.switchPage(createAnIdeaPage.getPreviousPage());
            }
            view.manageIdeasPage.refresh();
            controller.save();

        }else {
            JOptionPane.showMessageDialog(createAnIdeaPage, "Invalid prompt entered");
        }
    }

    @Override
    public void viewAllNotes(ActionEvent e) {
        NoteBankView view = controller.getView();
        ViewNotesController viewNotesController = new ViewNotesController(controller,createAnIdeaPage.getNotes(),false);
        ViewNotesPage viewNotesPage = viewNotesController.getViewNotes();
        viewNotesPage.setPreviousPage(createAnIdeaPage);
        view.addFixedPage(viewNotesPage, "Select Notes to Add to Idea");

    }
}
