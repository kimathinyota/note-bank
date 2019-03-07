package Model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

public class QuizReader {
	//CSV format: Model.Idea(ID),Completion-Time, Confidence-Level, Last-Seen-Date-Time
	private File csvFile;
	
	public void save(Idea a, long completionTimeSec, int confidenceLevel, Date dateTime  ){
		try {
			String record = a.getID() + "," + completionTimeSec + "," 
							+ confidenceLevel + "," + dateTime.getTime() + System.lineSeparator();
			BufferedWriter out = new BufferedWriter( new FileWriter(csvFile,true));
			out.write(record);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void delete(Idea idea) {
		String replaceCSV = "";
		Scanner csvReader;
		try {
			csvReader = new Scanner(csvFile);
			String word,id;
			while(csvReader.hasNextLine()) {
				word = csvReader.nextLine();
				if(!StringUtils.isAllBlank(word) && !StringUtils.isAllEmpty(word) ) {
					String[] csvWord = word.split(",");
					id = csvWord[0];
					if(!idea.equalsID(id) ) {
						replaceCSV += word + System.lineSeparator();
					}
				}
			}
			csvReader.close();
			FileWriter writeFile = new FileWriter(csvFile);
			writeFile.write(replaceCSV);
			writeFile.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private List<Integer> completionTimeList(Idea idea, Date lastTimeInMins ) throws FileNotFoundException{
		Scanner csvReader = new Scanner(csvFile);
		List<Integer> completionTime = new ArrayList<Integer>();
		String word,id;
		while(csvReader.hasNextLine()) {
			word = csvReader.nextLine();
			if(!StringUtils.isAllBlank(word) && !StringUtils.isAllEmpty(word) ) {
				String[] csvWord = word.split(",");
				id = csvWord[0];
				Date date = new Date();
				
				date.setTime(  Long.valueOf(csvWord[3])  );
				if(idea.equalsID(id) && date.after(lastTimeInMins)) {
					int completionTimeVal = Integer.valueOf(csvWord[1] );
					completionTime.add( completionTimeVal  );
				}
			}	
		}
		csvReader.close();
		return completionTime;
	}
	
	private List<Integer> confidenceLevelList(Idea idea, Date lastTimeInMins ) throws FileNotFoundException{
		Scanner csvReader = new Scanner(csvFile);
		List<Integer> confidenceLevel = new ArrayList<Integer>();
		String word,id;
		while(csvReader.hasNextLine()) {
			word = csvReader.nextLine();
			if(!StringUtils.isAllBlank(word) && !StringUtils.isAllEmpty(word) ) {
				String[] csvWord = word.split(",");
				id = csvWord[0];
				Date date = new Date();
				date.setTime(  Long.valueOf(csvWord[3])  );
				if(idea.equalsID(id) && date.after(lastTimeInMins)) {
					int confidenceLevelVal = Integer.valueOf(csvWord[2] );
					confidenceLevel.add( confidenceLevelVal  );
				}
			}
		}
		csvReader.close();
		return confidenceLevel;
	}
	
	public Integer getNumberOfTimes(Idea idea, Date lastTimeInMins ) {
		Scanner csvReader;
		try {
			csvReader = new Scanner(csvFile);
			int numberOfTimes = 0;
			String word,id;
			while(csvReader.hasNextLine()) {
				word = csvReader.nextLine();
				if(!StringUtils.isAllBlank(word) && !StringUtils.isAllEmpty(word) ) {
					String[] csvWord = word.split(",");
					id = csvWord[0];
					Date date = new Date();
					date.setTime(  Long.valueOf(csvWord[3])  );
					if(idea.equalsID(id) && date.after(lastTimeInMins)) {
						numberOfTimes+=1;
					}
				}
			}
			csvReader.close();
			return numberOfTimes;
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	public Double getAverageCompletionTime(Idea idea, Date lastTimeInMins) {
		try {
			List<Integer> completionTimeList = this.completionTimeList(idea, lastTimeInMins);
			if(completionTimeList.isEmpty())
				return null;
			int sum = 0;
			for(Integer i: completionTimeList)
				sum+=i.intValue();
			double val = sum/completionTimeList.size();
			return val;
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	public Double getAverageConfidenceLevel(Idea idea, Date lastTimeInMins) {
		try {
			List<Integer> confidenceLevelList = this.confidenceLevelList(idea, lastTimeInMins);
			int sum = 0;
			for(Integer i: confidenceLevelList)
				sum+=i.intValue();
			double val = sum/confidenceLevelList.size();
			return val;
		} catch (FileNotFoundException e) {
			return null;
		} 
	}
	
	public QuizReader(String loc) throws IOException {
		File fileReader = new File(loc + "quizAnalytics.csv");
		this.csvFile = fileReader;
		if(!fileReader.exists()) {
			FileWriter writeFile = new FileWriter(csvFile);
			writeFile.write("");
			writeFile.close();
		}
	}
}
