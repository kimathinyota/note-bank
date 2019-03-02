package View;

import Model.Idea;
import View.Handlers.QuizResultsEventHandler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

public class QuizResultsPage extends JPanel {
	
	private JLabel quizResultsTitle;
	private JComboBox<Idea> pickIdea;
	private JLabel completionTime;
	private JComboBox<String> ideaFrequency;
	private JLabel userSatisfaction;
	private JProgressBar readinessLevel;
	private JButton seeIdea;
	private JLabel finalReadiness;
	private JButton finishQuiz;
	
	private void setReadinessLevel(int level) {
		this.readinessLevel.setValue(level);
	}
	
	public void addSeeIdeaActionListener(ActionListener seeIdeaActionListener) {
		this.seeIdea.addActionListener(seeIdeaActionListener);
	}
	
	private void setFinalReadiness() {
		String value = "";
		if(readinessLevel.getValue()>89) {
			value = "ESPECIALLY";
		}else if(readinessLevel.getValue()>79) {
			value = "";
		}else if(readinessLevel.getValue()>69) {
			value = "NEARLY";
		}else if(readinessLevel.getValue()>49) {
			value  = "NOT";
		}else  {
			value = "NOWHERE NEAR";
		}
		this.finalReadiness.setText(  "YOU ARE " + value + " READY FOR " + pickIdea.getSelectedItem().toString().toUpperCase() );
	}

	private void setCompletionTime(int completionTimeInSeconds) {
		int hours = Math.floorDiv(completionTimeInSeconds, 60*60);
		int hoursMod = Math.floorMod(completionTimeInSeconds, 60*60);
		int mins = Math.floorDiv(hoursMod, 60);
		int secs = Math.floorMod(hoursMod, 60);
	
		String hour = (hours>9 ? ""+hours : "0"+hours);
		String min = (mins>9 ? ""+mins : "0"+mins);
		String sec = (secs>9 ? ""+secs : "0"+secs);
		
		String compTime = "COMPLETION TIME: " +  hour + ":" +min + ":" + sec;
		this.completionTime.setText(compTime);
	}
	
	private void setUserSatisfaction(String satisfaction) {
		String satisf = "USER SATISFACTION: " + satisfaction;
		this.userSatisfaction.setText(satisf);
	}
	
	
	
	public void setAnalysis(int completionTimeInSeconds, Integer[]ideaSimulationTimes, double userSatisfactionLastHour, int readinessLevel  ) {
		this.setCompletionTime(completionTimeInSeconds);
		DefaultComboBoxModel<String> ideasSeenModel = new DefaultComboBoxModel<String>();
		String[] lastX = {"HOUR", "DAY", "WEEK", "FORTNIGHT","MONTH"};
		for(int i=0; i<5; i++) {
			ideasSeenModel.addElement(   "LAST " + lastX[i] + ": " + (ideaSimulationTimes[i]!=null ? ideaSimulationTimes[i]: 0) + " times "   );
		}
		this.ideaFrequency.setModel(ideasSeenModel);
		this.setUserSatisfaction(userSatisfactionLastHour+"/"+5);
		this.setReadinessLevel(readinessLevel);
		this.setFinalReadiness();
	}

	
	public void addPickIdeaActionListeners(ActionListener pickIdeaActionListener) {
		this.pickIdea.addActionListener(pickIdeaActionListener);
	}
	
	public void addFinishQuizActionListeners(ActionListener finishQuizActionListener) {
		this.finishQuiz.addActionListener(finishQuizActionListener);
	}

	public Idea getSelectedIdea() {
		return (Idea) this.pickIdea.getSelectedItem();
	}
	
	public void refreshIdeasComboBox() {
		this.pickIdea.setSelectedIndex(0);
	}
	
	//public void add
	
	
	
	public QuizResultsPage(QuizResultsEventHandler handler, List<Idea>ideas) {
		//Results Title -> Label
		this.quizResultsTitle = new JLabel("QUIZ RESULTS");
		quizResultsTitle.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		quizResultsTitle.setHorizontalAlignment(JLabel.CENTER);
		
		Idea[] ideasArray = ideas.toArray(new Idea[ideas.size()]);
		//Pick Model.Idea -> ComboBox
		
		
		this.pickIdea = new JComboBox<Idea>(ideasArray);
		//pickIdea.setBorder(BorderFactory.createTitledBorder("PICK IDEA"));
		JScrollPane pickId = new JScrollPane(pickIdea);
		pickId.setBorder(BorderFactory.createTitledBorder("PICK IDEA"));
		JPanel resultsPickIdea = new JPanel(new GridLayout(2,1));
		resultsPickIdea.add(quizResultsTitle);
		resultsPickIdea.add(pickId);
		
		
		//resultsPickIdea.add(p);
		
		//Analysis Section -> Panel
		JPanel analysis = new JPanel(new GridLayout(4,1));
		
			//Completion Time -> Label
		this.completionTime = new JLabel("COMPLETION TIME: ");
			//Model.Idea Frequency -> ComboBox
		this.ideaFrequency = new JComboBox<String>();
		this.ideaFrequency.setBorder(BorderFactory.createTitledBorder("IDEA FREQUENCY"));
			//User Satisfaction -> Label
		this.userSatisfaction = new JLabel("USER SATISFACTION: YEET");
			//Readiness Level -> Progress Bar
		this.readinessLevel = new JProgressBar();
		readinessLevel.setBorder(BorderFactory.createTitledBorder("READINESS LEVEL"));
		
		analysis.setBorder(BorderFactory.createTitledBorder("ANALYSIS"));	
		analysis.add(completionTime);
		analysis.add(ideaFrequency);
		analysis.add(userSatisfaction);
		analysis.add(readinessLevel);
		
		//See Model.Idea -> Button
		JPanel modifyFinalReadinessFinish = new JPanel(new GridLayout(3,1));
		this.seeIdea = new JButton("SEE IDEA");
		
		
		//Final Readiness Level -> Label
		this.finalReadiness = new JLabel("YOU ARE NOT READY FOR FUNCTIONS");
		
		//Finish Quiz -> Label
		this.finishQuiz = new JButton("FINISH QUIZ");
		modifyFinalReadinessFinish.add(seeIdea);
		modifyFinalReadinessFinish.add(finalReadiness);
		modifyFinalReadinessFinish.add(finishQuiz);
		
		//Layering Quiz Results Page
		JPanel quizResultsPanel = new JPanel(new BorderLayout());
		quizResultsPanel.add(resultsPickIdea, BorderLayout.NORTH);
		quizResultsPanel.add(analysis, BorderLayout.CENTER);
		quizResultsPanel.add(modifyFinalReadinessFinish, BorderLayout.SOUTH);
		
		this.setLayout(new GridBagLayout());
		this.add(quizResultsPanel);

		this.finishQuiz.addActionListener(handler::finishQuiz);
		this.seeIdea.addActionListener(handler::seeIdea);
		this.pickIdea.addActionListener(handler::pickIdea);
		
		quizResultsPanel.setPreferredSize(new Dimension(350,540));
		

	}
	
}
