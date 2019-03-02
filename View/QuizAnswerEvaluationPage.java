package View;

import Model.Idea;
import View.Handlers.QuizAnswerEvaluationEventHandler;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

	
public class QuizAnswerEvaluationPage extends JPanel {
	
	private JButton quitQuiz;
	private JButton viewNotes;
	private JTextArea promptMarkQuiz;
	private JTextArea evaluateAnswerQuiz;
	private JButton setToFinalNoteMarkQuiz;
	private JList<String> missingKeyWords;
	private DefaultListModel<String> missingKeyWordsModel;
	private List<String> keyWords;
	
	private JButton viewFinalNote;
	private ButtonGroup confidenceButtonGroup;
	private JRadioButton notReady;
	private JRadioButton almostReady;
	private JRadioButton ready;
	private JRadioButton veryReady;
	private JButton nextMark;
	
	public void refreshEvaluateAnswer() {
		this.evaluateAnswerQuiz.setText( this.evaluateAnswerQuiz.getText());
	}
	
	public void addKeyWord(String keyword) {
		this.missingKeyWordsModel.addElement(keyword);
	}
	
	public void addQuitQuizActionListener(ActionListener quitQuizActionListener) {
		this.quitQuiz.addActionListener(quitQuizActionListener);
	}
	
	public void addViewNotesActionListener(ActionListener viewNotesActionListener) {
		this.viewNotes.addActionListener(viewNotesActionListener);
	}
	
	public void addSetToFinalNoteActionListener(ActionListener setToFinalNoteActionListener) {
		this.setToFinalNoteMarkQuiz.addActionListener(setToFinalNoteActionListener);
	}
	
	public void addNextMarkActionListener(ActionListener nextMarkActionListener) {
		this.nextMark.addActionListener(nextMarkActionListener);
	}

	public void addViewFinalNoteOnlyActionListener(ActionListener viewFinalNoteOnlyActionListener) {
		this.viewFinalNote.addActionListener(viewFinalNoteOnlyActionListener);
	}
	
	public int getReadinessLevel() {
		if(notReady.isSelected())
			return 1;
		if(almostReady.isSelected())
			return 2;
		if(ready.isSelected())
			return 3;
		if(veryReady.isSelected())
			return 4;
		
		return 0;
	}
	
	public void addMissingKeyWord(String keyWord) {
		this.missingKeyWordsModel.addElement(keyWord);
	}
	
	public void clearMissingKeyWords() {
		this.missingKeyWordsModel.removeAllElements();
	}
	
	public String getAnswer() {
		return this.evaluateAnswerQuiz.getText();
	}
	
	public void addEvaluateAnswerDocumentListener(DocumentListener docListener) {
		this.evaluateAnswerQuiz.getDocument().addDocumentListener(docListener);
	}

		
	public QuizAnswerEvaluationPage(QuizAnswerEvaluationEventHandler handler, Idea idea, String evaluatedAnswer) {
		
		JPanel firstSectionMarkPanel = new JPanel(new BorderLayout());
		
		this.viewNotes = new JButton("VIEW ALL NOTES");
		quitQuiz = new JButton("QUIT");
		
		JPanel quitViewNotesPanel = new JPanel(new GridLayout(1,3));
		quitViewNotesPanel.add(quitQuiz);
		quitViewNotesPanel.add(Box.createHorizontalGlue());
		quitViewNotesPanel.add(viewNotes);
		
		firstSectionMarkPanel.add( quitViewNotesPanel, BorderLayout.NORTH);
		JPanel promptEvaluateAnswer = new JPanel(new GridLayout(2,1));
		
		//Prompt Display -> TextArea
		this.promptMarkQuiz = new JTextArea(idea.getPrompt());
		this.promptMarkQuiz.setEnabled(false);
		this.promptMarkQuiz.setLineWrap(true);
		this.promptMarkQuiz.setWrapStyleWord(true);
		this.promptMarkQuiz.setFont(  new Font("Arier",Font.PLAIN,14)  );
		
		
		JScrollPane prompt = new JScrollPane(promptMarkQuiz);
		prompt.setBorder(  BorderFactory.createEmptyBorder()  );
		
		
		promptMarkQuiz.setBorder(BorderFactory.createTitledBorder("PROMPT"));
		
		//Evaluate Answer -> Text Area
		this.evaluateAnswerQuiz = new JTextArea(evaluatedAnswer);
		this.evaluateAnswerQuiz.setLineWrap(true);
		this.evaluateAnswerQuiz.setWrapStyleWord(true);
		this.evaluateAnswerQuiz.setFont(  new Font("Arier",Font.PLAIN,14)  );
		evaluateAnswerQuiz.setBorder(BorderFactory.createTitledBorder("EVALUATED ANSWER"));
		JScrollPane answer = new JScrollPane(evaluateAnswerQuiz);
		answer.setBorder(  BorderFactory.createEmptyBorder()  );
		promptEvaluateAnswer.add(prompt);
		promptEvaluateAnswer.add(answer);
		
		firstSectionMarkPanel.add( promptEvaluateAnswer, BorderLayout.CENTER);
		
		//SET TO FINAL NOTE -> Button
		this.setToFinalNoteMarkQuiz = new JButton("SET TO FINAL NOTE");
		firstSectionMarkPanel.add( setToFinalNoteMarkQuiz, BorderLayout.SOUTH);
		
		//MISSING KEY WORDS -> List
		missingKeyWordsModel = new DefaultListModel<String>();
		
		this.missingKeyWords = new JList<String>(missingKeyWordsModel);
		this.missingKeyWords.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		missingKeyWords.setBorder(BorderFactory.createTitledBorder("MISSING KEY WORDS"));
		
		JScrollPane missingKeyW = new JScrollPane(missingKeyWords);
		
		
		//VIEW FINAL NOTE ONLY -> Button
		this.viewFinalNote = new JButton("VIEW FINAL NOTE ONLY");
		
		JPanel missingKeyWordsViewFinalNotePanel = new JPanel(new BorderLayout());
		missingKeyWordsViewFinalNotePanel.add(missingKeyW, BorderLayout.CENTER);
		missingKeyWordsViewFinalNotePanel.add(viewFinalNote, BorderLayout.SOUTH );
		
		//CONFIDENCE LEVEL -> Panel
		this.confidenceButtonGroup = new ButtonGroup();
		JPanel confidencePanel = new JPanel(new GridLayout(1,4));
			// Not Ready
		notReady = new JRadioButton("Fail");
			// Almost Ready
		almostReady = new JRadioButton("Pass");
			// Ready
		ready = new JRadioButton("Ready");
			// Very Ready
		veryReady = new JRadioButton("Thrive");
		
		confidencePanel.add(notReady);
		confidencePanel.add(almostReady);
		confidencePanel.add(ready);
		confidencePanel.add(veryReady);
		confidenceButtonGroup.add(notReady);
		confidenceButtonGroup.add(almostReady);
		confidenceButtonGroup.add(ready);
		confidenceButtonGroup.add(veryReady);
		
		this.confidenceButtonGroup.setSelected(ready.getModel(), true);
		
		confidencePanel.setBorder(BorderFactory.createTitledBorder("CONFIDENCE LEVEL"));
		
		JPanel confidenceLevelNextPanel = new JPanel(new BorderLayout());
		confidenceLevelNextPanel.add(confidencePanel,BorderLayout.CENTER);
		
		// NEXT -> Button
		this.nextMark = new JButton("NEXT");
		JPanel nextMarkQuizPanel = new JPanel(new GridLayout(1,4));
		nextMarkQuizPanel.add(Box.createHorizontalGlue());
		nextMarkQuizPanel.add(Box.createHorizontalGlue());
		nextMarkQuizPanel.add(Box.createHorizontalGlue());
		nextMarkQuizPanel.add(nextMark);
		confidenceLevelNextPanel.add(nextMarkQuizPanel,BorderLayout.SOUTH);
		
		// Layering Quiz Answer Evaluation Page
		JPanel secondSectionMarkPanel = new JPanel(new GridLayout(2,1));
		secondSectionMarkPanel.add(missingKeyWordsViewFinalNotePanel);
		secondSectionMarkPanel.add(confidenceLevelNextPanel);
		
		JPanel  markQuizResponsePanel = new JPanel(new GridLayout(2,1));
		markQuizResponsePanel.add(firstSectionMarkPanel);
		markQuizResponsePanel.add(secondSectionMarkPanel);

		this.keyWords = idea.getKeyWords();


		this.evaluateAnswerQuiz.getDocument().addDocumentListener(new DocumentListener() {
			public void addMissing(){
				missingKeyWordsModel.removeAllElements();
				for(String keyWord: keyWords){
					if(!keyWords.contains(keyWord)){
						missingKeyWordsModel.addElement(keyWord);
					}
				}
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				addMissing();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				addMissing();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				addMissing();
			}
		});
		this.nextMark.addActionListener(handler::nextMark);
		this.quitQuiz.addActionListener(handler::quit);
		this.setToFinalNoteMarkQuiz.addActionListener(handler::setToFinalNote);
		this.viewFinalNote.addActionListener(handler::viewFinalNote);
		this.viewNotes.addActionListener(handler::viewNotes);

		this.setLayout(new GridBagLayout());
		this.add(markQuizResponsePanel);
		markQuizResponsePanel.setPreferredSize(new Dimension(400,600));

		
	}
	
}
