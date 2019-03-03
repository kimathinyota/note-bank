package View;

import Controller.ManageIdeasController;
import Controller.ManageNoteBankController;
import Controller.QuizSetUpController;
import Model.Note;
import Model.Topic;
import View.Handlers.MainWindowEventHandler;
import View.Handlers.ManageIdeasEventHandler;
import View.Handlers.ManageNoteBankPageEventHandler;
import View.Handlers.QuizSetUpEventHandler;
import View.MainWindowMenu.MainMenuBar;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;

public class NoteBankView extends JFrame{

	public ManageIdeasPage manageIdeasPage;
	public ManageNoteBankPage manageNoteBankPage;
	public QuizSetUpPage quizSetUpPage;
	public JTabbedPane navigationPane;
	private MainMenuBar menuBar;

	public MainMenuBar getMenus(){
		return this.menuBar;

	}

	private Stack<Map<JPanel, String>> deletedPagesStack;
	
	public void deleteAllPages() {
		Map<JPanel, String> deletedPages = new HashMap<JPanel,String>();
		for(int i=0; i<navigationPane.getTabCount(); i++) {
			deletedPages.put((JPanel) navigationPane.getComponentAt(i), navigationPane.getTitleAt(i));
		}
		deletedPagesStack.push(deletedPages);
		navigationPane.removeAll();
	}
	
	public void addFixedPage(Component page, String title) {
		this.deleteAllPages();
		this.navigationPane.add(title, page);
	}
	
	public void switchPage(Component page) {
		this.navigationPane.setSelectedComponent(page);
	}
	
	
	
	

	public void restorePages() {
		Map<JPanel, String> deletedPages = this.deletedPagesStack.pop();
		for(JPanel panel: deletedPages.keySet()) {
			navigationPane.addTab( deletedPages.get(panel), panel);
		}
	}
	
	public void deletePage(JPanel page) {
		navigationPane.remove(page);
	}
	
	public void restoreAndInsert(JPanel page, String title, int index ) {
		this.restorePages();
		this.navigationPane.insertTab(title, null, page, null, index);
	}
	
	public void replace(JPanel page, String title) {
		for(int i=0; i<this.navigationPane.getTabCount(); i++) {
			if(navigationPane.getTitleAt(i).equals(title)) {
				this.navigationPane.setComponentAt(i, page);
				this.navigationPane.setSelectedIndex(0);
				this.navigationPane.setSelectedIndex(i);
				break;
			}
		}
	}
	
	public void add(JPanel page, String title) {
		this.navigationPane.add(title, page);
	}
	
	public void addTabbedPaneChangeListener(ChangeListener changeListener) {
		this.navigationPane.addChangeListener(changeListener);
	}
		
	public void removeFixed(JPanel page, JPanel switchPage) {
		navigationPane.remove(page);
		for(int i=0; i<navigationPane.getTabCount(); i++) {
			navigationPane.getComponentAt(i).setEnabled(true);
		}
		
		if(switchPage!=null)
			navigationPane.setSelectedComponent(switchPage);	
	}
	

	

		
	public NoteBankView(Topic allTopics, HashMap<Note,List<String>>allNotes, ManageNoteBankPageEventHandler manageNoteBankController,
						ManageIdeasEventHandler manageIdeasController, QuizSetUpEventHandler quizSetUpController, MainWindowEventHandler mainHandler) {


		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e){
			System.out.println("No Default UI");
		}

		this.setTitle("Note Bank");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		navigationPane = new JTabbedPane(JTabbedPane.BOTTOM);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		navigationPane.setPreferredSize(	new Dimension((int) (screenSize.getWidth()*0.5), (int) (0.85*screenSize.getHeight())) );
		
		deletedPagesStack = new Stack<Map<JPanel, String>>();
		
		manageNoteBankPage = new ManageNoteBankPage(manageNoteBankController,allNotes);
		manageIdeasPage = new ManageIdeasPage(manageIdeasController,allTopics);
		quizSetUpPage = new QuizSetUpPage(quizSetUpController);
		
		
		navigationPane.addTab("Manage Note Bank", manageNoteBankPage);
		navigationPane.addTab("Manage Ideas", manageIdeasPage);
		navigationPane.addTab("Revision Quiz", quizSetUpPage);

		navigationPane.setSelectedIndex(0);



		menuBar = new MainMenuBar(mainHandler,ManageNoteBankPage.getAllSubjects(allNotes));

		this.setJMenuBar(menuBar);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout( new GridLayout(1,1) );
		mainPanel.add(navigationPane);
		this.setContentPane(mainPanel);
		this.pack();
		this.setVisible(true);
	}
	
}
