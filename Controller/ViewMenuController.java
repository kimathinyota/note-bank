package Controller;

import View.ManageNoteBankPage;
import View.NoteBankView;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class ViewMenuController{

    NoteBankController controller;

    public void subjectSelected(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {

            NoteBankView view = controller.getView();
            ManageNoteBankPage manageNoteBankPage = view.manageNoteBankPage;

            JRadioButtonMenuItem item = (JRadioButtonMenuItem) e.getItem();

            boolean isSelected = view.getMenus().getMoveSubject().isSelected();

            manageNoteBankPage.setCurrentSubject(item.getText());

            boolean shouldMoveSelectedNotesToNextSubject =
                    !item.getText().equals("All")
                    &&
                    isSelected
                    && manageNoteBankPage.getSelectedNotes().size() > 0
                    && JOptionPane.showConfirmDialog(manageNoteBankPage,
                    "Are you sure you want move the selected notes to " + item.getText() + " ?") == JOptionPane.YES_OPTION;

            if (shouldMoveSelectedNotesToNextSubject ) {
                manageNoteBankPage.addNotes(item.getText(), manageNoteBankPage.getSelectedNotes());
            }

            view.manageIdeasPage.hideAllTopicsOrIdeasNotAssociatedWithNotes(manageNoteBankPage.getNotesOfSubject(item.getText()), null);
            manageNoteBankPage.refreshNotesListView();
            controller.updateSubjectFile();
        }
    }

    public void viewNotes(){
        ManageNoteBankPage manageNoteBankPage = this.controller.getView().manageNoteBankPage;
        NoteBankView view = controller.getView();
        if(manageNoteBankPage.getSelectedNotes().size()>0) {
            ViewNotesController viewNotesController = new ViewNotesController(this.controller,manageNoteBankPage.getSelectedNotes(),false);
            viewNotesController.getViewNotes().setPreviousPage(manageNoteBankPage);
            view.addFixedPage(viewNotesController.getViewNotes(), "View Notes");
        }else {
            JOptionPane.showMessageDialog(manageNoteBankPage, "You must select notes to view");
        }
    }

    public ViewMenuController(NoteBankController controller){
        this.controller = controller;
    }

}
