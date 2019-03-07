package Model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.imageio.ImageIO;
import org.w3c.dom.Element;

/**
 * Image
 * Class used to represent notes as Model.Image files
 */
public class Image extends Note{

	private BufferedImage image;
	
	/**
	 * Constructor for Model.Image note
	 * @param name
	 * @param path
	 * @throws IOException
	 */
	public Image(String name, String path, String pathToSaveDirectory) throws IOException {
		super(name, path, "Image",pathToSaveDirectory);
		image = ImageIO.read(this.note);
	}

	/**
	 * @return images as a list of BufferedImage
	 */
	public List<BufferedImage> toImages() {
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		images.add(image);
		return images;
	}
	
	/**
	 * Creates Image from an XML file
	 * @param image: Found <Image> ... </Image> element
	 * @param allExistingNotes - Current notes stored in the directory
	 * @return Image
	 * @throws IOException
	 */
	public static Image fromXML(Element image, Collection<Note>allExistingNotes, String pathToSaveDirectory) throws IOException {
		String name = ((Element) image.getElementsByTagName("Name").item(0)).getTextContent() ;
		String path = ((Element) image.getElementsByTagName("Path").item(0)).getTextContent() ;
		
		if(allExistingNotes==null) {
			return null;
		}
		
		for(Note n: allExistingNotes) {
			if(n.getFile().getPath().equals(path)) {
				return new Image(name, path,pathToSaveDirectory);
			}
		}
		//Image has been deleted from existing notes (can't be recovered)
		return null;
	}
	
	/**
	 * Getter for image 
	 * @return image
	 */
	public BufferedImage getImage() {
		return this.image;
	}
	/**
	 * Convert Model.Image to XML
	 * @return xml content as string
	 */
	public String toXML() {
		String xml = "<Note>" + System.lineSeparator()
				  		+ "<Image>" + System.lineSeparator()
        						+ "<Name>" + this.name + "</Name>" + System.lineSeparator()
        						+ "<Path>" + this.note.getPath() + "</Path>" + System.lineSeparator()
        					+ "</Image>" + System.lineSeparator() +
        				"</Note>";
		
		return xml;
	}

	/**
	 * Get index of image.
	 * Practically: Returns 0 if this.image equals input image
	 * 				else returns -1
	 * @return index of image
	 */
	public int indexOfImage(BufferedImage image) {
		return (this.image.equals(image) ? 0 : -1);
	}

	

}
