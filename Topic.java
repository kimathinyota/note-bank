import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
public class Topic {
	private List<Topic> subTopics;
	private List<Idea> ideas;
	private String name;
	
	public String getName() {
		return this.name;
	}
	
	public List<Topic> getSubTopics(){
		return this.subTopics;
	}
	
	public List<Idea> getIdeas(){
		return this.ideas;
	}
	
	public List<Idea> getAllIdeas(){
		List<Idea> allIdeas = new ArrayList<Idea>();
		allIdeas.addAll(this.getIdeas());
		for(Topic a: subTopics) {
			allIdeas.addAll(a.getAllIdeas());
		}
		return allIdeas;
	}
	
	public void add(Topic topic) {
		subTopics.add(topic);
	}
	
	public void add(List<Idea> ideas) {
		this.ideas.addAll(ideas);
	}
	
	public void addAll(List<Topic> topics) {
		this.subTopics.addAll(topics);
	}
	
	public void add(Idea a) {
		this.ideas.add(a);
	}
	
	public void add(Object a) {
		if( a instanceof Idea)
			this.ideas.add( (Idea) a);
		if( a instanceof Topic)
			this.subTopics.add( (Topic) a  );
	}
	
	public Topic(String name) {
		subTopics = new ArrayList<Topic>();
		ideas = new ArrayList<Idea>();
		this.name = name;
	}
	

	private static Topic fromXML(Element topic,Collection<Note>allExistingNotes, String pathToSaveDirectory) throws IOException {
		String name = ((Element) topic.getElementsByTagName("Name").item(0)).getTextContent() ;
		Topic top = new Topic(name);
		NodeList nodes = topic.getElementsByTagName("Idea");
		for(int i=0; i<nodes.getLength(); i++) {
			if(nodes.item(i) instanceof Element) {
				Element elem = (Element) nodes.item(i);
				if(elem.getParentNode().equals(topic)) {
					top.add( Idea.fromXML(elem,allExistingNotes,pathToSaveDirectory) );
				}	
			}	
		}
		nodes = topic.getElementsByTagName("Topic");
		
		for(int i=0; i<nodes.getLength(); i++) {
			if(nodes.item(i) instanceof Element) {
				Element elem = (Element) nodes.item(i);
				if(elem.getParentNode().equals(topic)) {
					top.add( Topic.fromXML(elem,allExistingNotes,pathToSaveDirectory));
				}	
			}	
		}
		return top;
	}
	
	public static Topic fromXML(String loc,Collection<Note>allExistingNotes, String pathToSaveDirectory) throws ParserConfigurationException, FileNotFoundException, SAXException, IOException {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document document = builder.parse( new FileInputStream(loc) );
		
		Element rootElement = document.getDocumentElement();
		
		return Topic.fromXML(rootElement,allExistingNotes, pathToSaveDirectory);
		
	}
		

	public List<Note> getAllNotes( ) {
		List<Note> notes = new ArrayList<Note>();
		for(Idea a: this.ideas ) {
			for(Note n: a.getNotes()) {
				notes.add(n);
			}
		}
		
		for(Topic top: this.subTopics) {
			notes.addAll(  top.getAllNotes() );
		}
		
		return notes;
		
	}
	
	public Boolean contains(Note n) {
		return this.getAllNotes().contains(n);
	}
	
	public Boolean containsAny(Collection<Note> ns) {
		for(Note n: ns) {
			if(this.contains(n)) {
				return true;
			}
		}
		return false;
	}
	
	public List<Topic> getTopics(){
		List<Topic> topics = new ArrayList<Topic>();
		topics.add(this);
		for(Topic t: this.subTopics ) {
			topics.addAll(  t.getTopics()  );
		}
		return topics;
	}
		
	
	
	
	public void delete(Topic a) {
		this.subTopics.remove(a);
	}
	
	public void delete(Idea a) {
		this.ideas.remove(a);
	}
	
	public void delete(Object a) {
		if( a instanceof Idea)
			this.ideas.remove( (Idea) a);
		if( a instanceof Topic)
			this.subTopics.remove( (Topic) a );
	}
	
	public void removeNote(Note note) {
		for(Idea a: this.ideas ) {
			a.removeNote(note);
		}
		
		for(Topic top: this.subTopics) {
			top.removeNote(note);
		}
	}
	
	public String toXML() {
		String xml = "<Topic>" + System.lineSeparator()
					+ "<Name>" + this.name + "</Name>" + System.lineSeparator();
			
			
		for(Idea a: this.ideas) {
			xml += a.toXML() + System.lineSeparator();
		}
				
		for(Topic a: this.subTopics)
			xml+=a.toXML();
		
		xml += System.lineSeparator();
		
		xml += "</Topic>";
		
		return xml;
	}
	
	public String toString() {
		return ("Topic: " + name);
	}
	
	

	
	
	
	
}
