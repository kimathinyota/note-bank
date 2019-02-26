import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class QuizSetUpPage extends JPanel {

	private JLabel quizTitle;
	private JComboBox<Topic> chooseTopic;
	private JSlider noQuestionsSlider;
	private JComboBox<String> questionType;
	private JButton createQuiz;
	private DefaultComboBoxModel<Topic>topicModel;
	
	public void clearTopics() {
		this.topicModel.removeAllElements();
	}
	
	public String getQuizTitle() {
		return quizTitle.getText();
	}
	
	public int getNoQuestions() {
		return this.noQuestionsSlider.getValue();
	}
	
	public int getQuestionType() {
		return this.questionType.getSelectedIndex();
	}
	
	public Topic getTopicChosen() {
		return ((Topic) this.chooseTopic.getSelectedItem() );
	}
	
	public void addTopic(Topic topic) {
		if(topicModel.getIndexOf(topic)<0) 
			topicModel.addElement(topic);
	}
		
	private int highestFactor(int num) {
		if(num==0)
			return 1;
		for(int i=num-1; i>1; i--) {
			if(num%i==0)
				return i;
		}
		return 1;
	}
	
	public void setSliderMinMax(int min, int max ) {
		this.noQuestionsSlider.setMaximum(max);
		this.noQuestionsSlider.setMinimum(min);
		this.noQuestionsSlider.setValue(max);
		
		int factor = Math.round((max-min)/4) ;
		if(highestFactor(factor)==1) {
			factor+=1;
		}
		if( factor==0) {
			factor = 1;
		}

		noQuestionsSlider.setMajorTickSpacing(  factor  );
		noQuestionsSlider.setMinorTickSpacing(  highestFactor(factor)  );
		
		noQuestionsSlider.repaint();
		noQuestionsSlider.revalidate();
	}
	
	public QuizSetUpPage() {
		quizTitle = new JLabel("QUIZ");
		quizTitle.setBorder( BorderFactory.createLineBorder(Color.BLACK));
		quizTitle.setHorizontalAlignment(JLabel.CENTER);
		
		topicModel = new DefaultComboBoxModel<Topic>();
		chooseTopic = new JComboBox<Topic>(topicModel);
		chooseTopic.setBorder( BorderFactory.createTitledBorder("CHOOSE TOPIC"));
		
		noQuestionsSlider = new JSlider(JSlider.HORIZONTAL, 1, 1, 1);
		noQuestionsSlider.setPaintTicks(true);
		noQuestionsSlider.setPaintLabels(true);
		noQuestionsSlider.setBorder( BorderFactory.createTitledBorder("NUMBER OF QUESTIONS"));
		
		String[] typesQuestions = {"Untested Ideas", "Only key words", "Only questions", "All"};
		
		questionType = new JComboBox<String>(typesQuestions);
		questionType.setSelectedItem("All");
		questionType.setBorder( BorderFactory.createTitledBorder("QUESTION TYPE"));
		
		
		//Types of questions:
		/*
		 * Untested ideas
		 * Only key words
		 * Only questions
		 * All
		 */
		
		createQuiz = new JButton("CREATE QUIZ");
		
		JPanel revisionQuizPanelStart = new JPanel(new GridLayout(5,1));
		revisionQuizPanelStart.add(quizTitle);
		revisionQuizPanelStart.add(chooseTopic);
		revisionQuizPanelStart.add(noQuestionsSlider);
		revisionQuizPanelStart.add(questionType);
		revisionQuizPanelStart.add(createQuiz);
		
		this.setLayout( new GridBagLayout() );
		this.add(revisionQuizPanelStart);
		revisionQuizPanelStart.setPreferredSize(new Dimension(300,400));
	}
	
	public void addCreateQuizActionListener(ActionListener createQuizActionListener) {
		this.createQuiz.addActionListener(createQuizActionListener);
	}
	
	public void addChooseTopicActionListener(ActionListener chooseTopicActionListener) {
		this.chooseTopic.addActionListener(chooseTopicActionListener);
	}

	public void removeTopic(String topic) {
		this.chooseTopic.removeItem(topic);
	}

	public void addQuestionType(String qType) {
		this.questionType.addItem(qType);
	}
	
	public void removeQuestionType(String qType) {
		this.questionType.removeItem(qType);
	}

}
