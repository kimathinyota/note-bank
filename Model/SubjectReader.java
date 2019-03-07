package Model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

public class SubjectReader {
	private File csvFile;
	
	public String delete(Note note) {
		if(note==null)
			return null;
		String replaceCSV = "";
		Scanner csvReader;
		try {
			csvReader = new Scanner(csvFile);
			String word,path;
			String delete = null;
			while(csvReader.hasNextLine()) {
				word = csvReader.nextLine();
				if(!StringUtils.isAllBlank(word) && !StringUtils.isAllEmpty(word) ) {
					String[] csvWord = word.split(",");
					path = csvWord[0];
					if(!note.getFile().getPath().equals(path) ) {
						replaceCSV += word + System.lineSeparator();
					}else {
						delete = word;
						break;
					}
				}
			}
			csvReader.close();
			FileWriter writeFile = new FileWriter(csvFile);
			writeFile.write(replaceCSV);
			writeFile.close();
			return delete;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public void save(Map<Note, List<String>> notes) {
		for(Note a: notes.keySet()) {
			this.save(a, notes.get(a));
		}
	}
	
	private String toCSV(Collection<String>subjects) {
		String s = "";
		if(subjects.isEmpty())
			return s;
		for(String a: subjects) {
			s+=a+",";
		}
		return s.substring(0, s.length()-1);
	}
	
	public Note getEquivalentNote(List<Note>notes, String path) {
		for(Note n: notes) {
			if(n.getFile().getPath().equals(path)) {
				return n;
			}
		}
		return null;
	}
	
	public HashMap<Note,List<String>> getAllNotes(List<Note>existingNotes) throws FileNotFoundException{
		HashMap<Note,List<String>> notes = new HashMap<Note,List<String>>();
		Scanner csvReader = new Scanner(csvFile);
		String word,path;
		for(Note n: existingNotes) {
			notes.put(n, new ArrayList<String>());
		}
		while(csvReader.hasNextLine()) {
			word = csvReader.nextLine().trim();
			if(!StringUtils.isAllBlank(word) && !StringUtils.isAllEmpty(word) ) {
				String[] csvWord = word.split(",");
				path = csvWord[0];
				Note n = this.getEquivalentNote(existingNotes, path);
				if(n!=null) {
					ArrayList<String> subjects = new ArrayList<String>();
					for(int i=1; i<csvWord.length;i++) {
						subjects.add(csvWord[i]);
					}
					notes.put(n, subjects);
				}
			}	
		}
		this.save(notes);
		csvReader.close();
		return notes;
	}
	
	public void save(Note a, Collection<String>subjects){
		if(a==null)
			return;
		try {
			String record = this.delete(a);
			if(record==null) {
				record = a.getFile().getPath() +  "," + toCSV(subjects) + System.lineSeparator();
			}else {
				record = record.split(",")[0] + "," + toCSV(subjects) + System.lineSeparator();
			}
			BufferedWriter out = new BufferedWriter( new FileWriter(csvFile,true));
			out.write(record);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public SubjectReader(String loc) throws IOException {
		File fileReader = new File(loc + "subject.csv");
		this.csvFile = fileReader;
		if(!fileReader.exists()) {
			FileWriter writeFile = new FileWriter(csvFile);
			writeFile.write("");
			writeFile.close();
		}
	}
}
