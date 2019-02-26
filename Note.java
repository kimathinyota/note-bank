import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

public abstract class Note {
	protected String name;
	protected File note;
	protected String pathToDirec;
	protected String type;
	
	/**
	 * Getter for name
	 * @return 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Setter for name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**I
	 * Getter for type
	 * @return
	 */
	public String getType() {
		return type;
	}
	
	
	/**
	 * Constructor for Note
	 * @param name - name of the note
	 * @param path - path of note file that will be created
	 * @param type - type (Book, Image, or TextExcerpt)
	 * @param pathToSaveDirectory - location to save file at 
	 * @throws IOException
	 */
	public Note(String name, String path, String type, String pathToSaveDirectory) throws IOException {
		this.name = name;
		this.note = (new File(path));
		this.pathToDirec = pathToSaveDirectory;
		this.type = type;
		
		String fileExtension = note.getName().substring(note.getName().indexOf("."), note.getName().length());

		File directory = new File(pathToDirec);
		if(!directory.exists())
			directory.mkdir();
		
		File dest = new File(pathToDirec + name + fileExtension );
		
	    Files.copy(note.toPath(),dest.toPath() , StandardCopyOption.REPLACE_EXISTING);
	    
	    this.note = dest;
	}
	
	/**
	 * Deletes note: Existing file will be removed
	 */
	public void delete() {
		note.delete();
	}
	
	/**
	 * Gets index of input image. 
	 * Every note can be converted to an image 
	 * @param image
	 * @return
	 */
	public abstract int indexOfImage(BufferedImage image);
	
	/**
	 * Getter for the file
	 * @return
	 */
	public File getFile() {
		return this.note;
	}	

	/**
	 * Returns Note as an image
	 * @return list of images
	 */
	public abstract List<BufferedImage> toImages();
	
	/**
	 * Converts note to XML
	 * @return 
	 */
	public abstract String toXML();
	
	/**
	 * Constructor for
	 * @param noteName 
	 * @param type
	 */
	public Note(String noteName, String type) {
		this.name = noteName;
		this.pathToDirec = new File("").getAbsolutePath() + File.separator + "notes" + File.separator;
		note = null;
		this.type = type;
	}
	
	/**
	 * Object is equal if they share the same file path
	 */
	@Override
	public boolean equals(Object n) {
		if(n instanceof Note)
			return this.getFile().equals(((Note) n).getFile());
		return false;
	}
	/**
	 * Override hash code to match equals
	 */
	@Override
	public int hashCode() {
		return this.getFile().getPath().hashCode();
	}
	
	
	public String toString() {
		return type + ": " + name.replaceAll("-"," ");
	}
	
}
