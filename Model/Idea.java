package Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * Idea: represents a prompt attached to a set of notes
 * A set of key words is also associated with a given prompt in an idea
 * Key word: A word that must be included when giving an answer to the given prompt
 * @author kimathinyota
 *
 */

public class Idea {
	private HashMap<Note,Integer> notes;
	private Set<String>keyWords;
	private String uniqueID;
	private String prompt;
	private boolean isPromptKeyWord;
	private Note finalNote;
	
	public final static int PROMPT_NOTE = 0;
	public final static int NON_PROMPT_NOTE =1;
	
	/**
	 * Complete reset of Model.Idea without reinstantiating the object
	 * @param notes
	 * @param keyWords
	 * @param prompt
	 * @param isPromptKeyWord
	 * @param finalNote
	 */
	public void resetIdea(HashMap<Note,Integer>notes, List<String>keyWords, String prompt, Boolean isPromptKeyWord, Note finalNote) {
		this.notes.clear();
		this.keyWords.clear();
		this.notes = notes;
		for(String k: keyWords)
			this.keyWords.add(k);
		this.prompt = prompt;
		this.isPromptKeyWord = isPromptKeyWord;
		this.finalNote = finalNote;
	}
	
	/**
	 * 
	 * @return ID for Model.Idea
	 */
	public String getID() {
		return this.uniqueID;
	}
	
	/**
	 * Getter for notes
	 * @return list of Model.Note
	 */
	public List<Note> getNotes(){
		ArrayList<Note> notes = new ArrayList<Note>();
		notes.addAll(this.notes.keySet());
		return notes;
	}
	
	public HashMap<Note,Integer> getNotesMap(){
		return this.notes;
	}
	
	public List<Note> getNotes(int type){
		ArrayList<Note> notes = new ArrayList<Note>();
		for(Note n: this.notes.keySet()) {
			if(this.notes.get(n).intValue()==type)
				notes.add(n);
		}
		return notes;
	}
	
	/**
	 * Getter for prompt
	 * @return prompt for Model.Idea
	 */
	public String getPrompt() {
		return this.prompt;
	}
	
	/**
	 * Getter for finalNote
	 * @return finalNote
	 */
	public Note getFinalNote() {
		return this.finalNote;
	}
	
	/**
	 * 
	 * @return list of key words
	 */
	public List<String> getKeyWords(){
		List<String> keyWords = new ArrayList<String>();
		for(String kW: this.keyWords) {
			keyWords.add(kW);
		}
		return keyWords;
	}
	
	/**
	 * Setter for final note
	 * @param note
	 */
	public void setFinalNote(Note note) {
		if(notes==null)
			return;
		notes.put(note, Idea.NON_PROMPT_NOTE);
		
		this.finalNote = note;
	}
	
	
	/*
	 * Add and remove methods for key word(s) and note(s)
	 */
	
	public void addNote(Note note) {
		this.notes.put(note,Idea.NON_PROMPT_NOTE);
	}
	
	public void addNote(Note note, boolean isPromptNote) {
		int type = Idea.PROMPT_NOTE;
		if(!isPromptNote)
			type = Idea.NON_PROMPT_NOTE;
		this.notes.put(note, type);
	}
	
	
	public void removeNote(Note note) {
		if(this.finalNote!=null && this.finalNote.equals(note))
			this.finalNote = null;
		notes.remove(note);
		
	}
	
	public void addNotes(Collection<Note> notes) {
		for(Note n: notes) {
			this.addNote(n);
		}
	}
	
	public void addNotes(HashMap<Note, Integer> notes) {
		for(Note n: notes.keySet()) {
			this.notes.put(n, notes.get(n));
		}
	}

	public void addKeyWord(String word) {
		this.keyWords.add(word);
	}
	
	public void addKeyWord(Collection<String>words) {
		this.keyWords.addAll(words);
	}
	
	public void removeKeyWord(String word) {
		this.keyWords.remove(word);
	}
	
	public Boolean contains(Note n) {
		return this.getNotes().contains(n);
	}
	
	/**
	 * Checks if any of the notes within the collection are contained in this idea
	 * @param ns - collection of notes
	 * @return 
	 */
	public Boolean containsAny(Collection<Note> ns) {
		for(Note n: ns) {
			if(this.contains(n)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * getPromptType: returns type of Prompt
	 * @return True -> KeyWord, False -> Question
	 */
	public Boolean getPromptType() {
		return this.isPromptKeyWord;
	}
	
	/**
	 * Find and return note with same name and path as input
	 * @param name
	 * @param path
	 * @return Note
	 */
	public Note getNote(String name, String path) {
		for(Note k: this.notes.keySet())
			if(k!=null && (k.getFile()==null ? path==null : k.getFile().getPath().equals(path)) 
					&& k.getName().equals(name) )
				return k;
		return null;
	}

	/**
	 * Checks if this.ID is equal to input ID
	 * @param ID
	 * @return 
	 */
	public boolean equalsID(String ID) {
		return ID.equals(this.uniqueID);
	}
	
	/**
	 * Creates Idea from an XML file
	 * @param idea: Found <Idea> ... </Idea> element
	 * @param allExistingNotes: Current notes stored in the directory
	 * @return Idea
	 * @throws IOException
	 */
	public static Idea fromXML(Element idea, Collection<Note>allExistingNotes, String pathToSaveDirectory) throws IOException {
		String prompt = ((Element) idea.getElementsByTagName("Prompt").item(0)).getTextContent().trim() ;
		String isPromptKeyWord = ((Element) idea.getElementsByTagName("IsPromptKeyWord").item(0)).getTextContent().trim() ;
		String keyWords = ((Element) idea.getElementsByTagName("KeyWords").item(0)).getTextContent().trim() ;
		String id = ((Element) idea.getElementsByTagName("ID").item(0)).getTextContent().trim() ;
		Idea createdIdea = new Idea(prompt, (isPromptKeyWord.contains("true") ? true : false), id );
		createdIdea.addKeyWordsFromCSV(keyWords);
		
		NodeList nodes = idea.getElementsByTagName("Note");
		for(int i=0; i<nodes.getLength(); i++) {
			if(nodes.item(i) instanceof Element &&
					!((Element) nodes.item(i).getParentNode()).getTagName().equals("FinalNote") ) {
					//ignores note under Final Model.Note tag to avoid repeat notes
				boolean type = ((Element) nodes.item(i).getParentNode()).getTagName().equals("PromptNotes") ;
				
				if( ((Element) nodes.item(i)).getElementsByTagName("Text").getLength() > 0) {
					Element elem = (Element) ((Element) nodes.item(i)).getElementsByTagName("Text").item(0);
					createdIdea.addNote(TextExcerpt.fromXML(elem,allExistingNotes,pathToSaveDirectory),type );
				}else if( ((Element) nodes.item(i)).getElementsByTagName("Image").getLength() > 0 ) {
					Element elem = (Element) ((Element) nodes.item(i)).getElementsByTagName("Image").item(0);
					createdIdea.addNote(Image.fromXML(elem,allExistingNotes,pathToSaveDirectory),type );
				}else if( ((Element) nodes.item(i)).getElementsByTagName("Book").getLength() > 0 ) {
					Element elem = (Element) ((Element) nodes.item(i)).getElementsByTagName("Book").item(0);
					createdIdea.addNote(Book.fromXML(elem,allExistingNotes,pathToSaveDirectory),type );
				}
			}
		}
		
		Element finalNote = (Element) idea.getElementsByTagName("FinalNote").item(0);
		String path = (finalNote.getElementsByTagName("Path").getLength()>0 ? finalNote.getElementsByTagName("Path").item(0).getTextContent() : null);
		String name = (finalNote.getElementsByTagName("Name").getLength()>0 ? finalNote.getElementsByTagName("Name").item(0).getTextContent() : null);
		
		Note fN = ( name==null ? null : createdIdea.getNote(name, path) );
		createdIdea.setFinalNote(fN);
		
		return createdIdea;
	}
	
	/**
	 * Adds all key words given input in csv form
	 * @param commaSeperatedKeyWords e.g. Yellow,Round,Tennis
	 */
	private void addKeyWordsFromCSV(String commaSeperatedKeyWords) {
		for(String kW: commaSeperatedKeyWords.trim().split(",")) {
			this.addKeyWord(kW);
		}
	}

	/**
	 * Convert Idea to XML
	 * @return xml content as string
	 */
	public String toXML() {
		String promptNotesXML = "";
		String nonPromptNotesXML = "";
		  
		for(Note n: notes.keySet()) {
			if(n!=null && notes.get(n).intValue()==Idea.PROMPT_NOTE)
				promptNotesXML += n.toXML() + System.lineSeparator();
			else if(n!=null)
				nonPromptNotesXML += n.toXML() + System.lineSeparator();
		}
			
			
		String keyWordsXML = "";
		for(String k: keyWords) {
			if(!k.equals("null"))
				keyWordsXML += k + ",";
		}
		if(keyWords.size()>0)
			keyWordsXML = keyWordsXML.substring(0,keyWordsXML.length()-1);
		
		String finalNoteXML = "" ;
		
		if(this.finalNote!=null)
			finalNoteXML = finalNote.toXML();
		
		
		String xml = "<Idea>" + System.lineSeparator()
						+ "<Prompt>" + this.prompt + "</Prompt>" + System.lineSeparator()
						+ "<ID>"+this.uniqueID+"</ID>"+ System.lineSeparator()
						+ "<IsPromptKeyWord>" + this.isPromptKeyWord + "</IsPromptKeyWord>" + System.lineSeparator()
						+ "<KeyWords>" + keyWordsXML + "</KeyWords>" + System.lineSeparator()
						+ "<PromptNotes>" + promptNotesXML + "</PromptNotes>"
						+ "<NonPromptNotes>" + nonPromptNotesXML + "</NonPromptNotes>"
						+ "<FinalNote>"
						+ finalNoteXML
						+ "</FinalNote>" + System.lineSeparator() +
					"</Idea>";
		return xml;
		 
	}
		
	public String toString() {
		return ("Idea: " + this.prompt);
	}
    
	/**
	 * Constructor for Idea
	 * Key info: Generates a uniqueID given a null input 
	 * @param prompt
	 * @param isPromptKeyWord
	 * @param uniqueID
	 */
	public Idea(String prompt, boolean isPromptKeyWord, String uniqueID) {
		notes = new HashMap<Note,Integer>();
		this.isPromptKeyWord = isPromptKeyWord;
		this.finalNote = null;
		this.keyWords = new HashSet<String>();
		this.prompt = prompt;
		this.uniqueID = uniqueID;
		if(uniqueID==null)
			this.uniqueID = UUID.randomUUID().toString();
		
		
	}
	
	
}
