import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class QuizResponsePage extends JPanel {
	
	
	private JButton quitQuiz;	
	private JLabel quizTitle;
	private JTextArea quizPromptResponse;	
	private JTextArea quizAnswerResponse;	
	private JButton submitAnswer;
	private JButton viewPromptNotes;
	private List<Note> promptNotes;
	
	public String getResponse() {
		return this.quizAnswerResponse.getText();
	}
	
	public void setPrompt(String prompt) {
		this.quizPromptResponse.setText(prompt);
	}
	
	public List<Note> getPromptNotes(){
		return this.promptNotes;
	}
	
	public QuizResponsePage(Idea idea) {
		this.promptNotes = idea.getNotes(Idea.PROMPT_NOTE);
		
		JPanel quizResponsePanel = new JPanel(new BorderLayout());
		JPanel promptAnswerPanel = new JPanel( new GridLayout(2,1) );
		this.viewPromptNotes = new JButton("VIEW PROMPT NOTES");
		this.quitQuiz = new JButton("QUIT");
		JPanel quitTitlePanel = new JPanel(new GridLayout(2,1));
		JPanel quitPanel = new JPanel(new GridLayout(1,3));
			
		quitPanel.add(quitQuiz);
		quitPanel.add(Box.createHorizontalGlue());
		quitPanel.add(this.viewPromptNotes);
		quitTitlePanel.add(quitPanel);
		
		this.quizTitle = new JLabel("QUIZ");
		
		quizTitle.setBorder( BorderFactory.createLineBorder(Color.BLACK));
		quizTitle.setHorizontalAlignment(JLabel.CENTER);
		
		
				
		quitTitlePanel.add(quizTitle);
		

		this.quizPromptResponse = new JTextArea(idea.getPrompt());
		this.quizPromptResponse.setLineWrap(true);
		this.quizPromptResponse.setWrapStyleWord(true);
		this.quizPromptResponse.setEnabled(false);
		this.quizPromptResponse.setFont(  new Font("Arier",Font.BOLD,20)  );
		quizPromptResponse.setBorder(BorderFactory.createTitledBorder("PROMPT"));
		
		
		
		JScrollPane prompt = new JScrollPane(this.quizPromptResponse);
		prompt.setBorder(  BorderFactory.createEmptyBorder()  );
		this.quizAnswerResponse = new JTextArea();
		this.quizAnswerResponse.setLineWrap(true);
		this.quizAnswerResponse.setFont(  new Font("Arier",Font.PLAIN,14)  );
		this.quizAnswerResponse.setWrapStyleWord(true);
		quizAnswerResponse.setBorder(BorderFactory.createTitledBorder("ANSWER"));
		JScrollPane answer = new JScrollPane(this.quizAnswerResponse);
		answer.setBorder(  BorderFactory.createEmptyBorder()  );
		promptAnswerPanel.add(prompt);
		promptAnswerPanel.add(answer);
		
		this.submitAnswer = new JButton("SUBMIT ANSWER");
		
		quizResponsePanel.add(promptAnswerPanel, BorderLayout.CENTER);
		quizResponsePanel.add(quitTitlePanel, BorderLayout.NORTH);
		quizResponsePanel.add(submitAnswer, BorderLayout.SOUTH);
		
		this.setLayout(new GridBagLayout());
		this.add(quizResponsePanel);
		quizResponsePanel.setPreferredSize(new Dimension(450,600));

	}
	
	public void addSubmitAnswerActionListener(ActionListener submitAnswerActionListener) {
		this.submitAnswer.addActionListener(submitAnswerActionListener);
	}
	
	public void addQuitQuizActionListener(ActionListener quitQuizActionListener) {
		this.quitQuiz.addActionListener(quitQuizActionListener);
	}
	
	public void addViewPromptNotesActionListener(ActionListener viewPromptNoteActionListener) {
		this.viewPromptNotes.addActionListener(viewPromptNoteActionListener);
	}
	
	
	
}
