package View.MainWindowMenu;

import View.Handlers.MainWindowEventHandler;

import java.util.List;
import javax.swing.*;

/**
 * The menubar for the main window.
 */
public class MainMenuBar extends JMenuBar {

    private FileMenu fileMenu;

    private EditMenu editMenu;

    private IdeaMenu ideaMenu;

    private QuizMenu quizMenu;

    private ViewMenu viewMenu;

    private HelpMenu helpMenu;

    public FileMenu getFileMenu() {
        return fileMenu;
    }

    public EditMenu getEditMenu() {
        return editMenu;
    }

    public IdeaMenu getIdeaMenu() {
        return ideaMenu;
    }

    public QuizMenu getQuizMenu() {
        return quizMenu;
    }

    public ViewMenu getViewMenu() {
        return viewMenu;
    }

    @Override
    public HelpMenu getHelpMenu() {
        return helpMenu;
    }

    public MainMenuBar(MainWindowEventHandler eventHandler, List<String>subjects){

        fileMenu = new FileMenu(eventHandler);
        add(fileMenu);
        editMenu = new EditMenu(eventHandler);
        add(editMenu);
        ideaMenu = new IdeaMenu(eventHandler);
        add(ideaMenu);
        quizMenu = new QuizMenu(eventHandler);
        add(quizMenu);
        viewMenu = new ViewMenu(eventHandler,subjects);
        add(viewMenu);
        helpMenu = new HelpMenu(eventHandler);
        add(helpMenu);

    }

}
