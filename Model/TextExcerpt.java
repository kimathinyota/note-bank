package Model;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.w3c.dom.Element;

public class TextExcerpt extends Note {
	private String textContent;
	final PDFont HELTIVICA_BOLD = PDType1Font.HELVETICA_BOLD;
	
	private void writeToFile(String textContent) throws IOException {
		FileWriter storeFile = new FileWriter(this.note);
		storeFile.write(textContent);
		storeFile.close();
	}
	
	public TextExcerpt(String name, String textContent, String pathToSaveDirectory) throws IOException {
		super(name,"Text");
		this.textContent = textContent;
		this.pathToDirec = pathToSaveDirectory;
		this.note =  new File(this.pathToDirec +   name + ".txt");
		this.writeToFile(textContent);
	}
	
	public TextExcerpt(File file, String name) throws IOException {
		super(name,"Text");
		this.note = file;
		String textContent = "";
		Scanner fileScanner = new Scanner(file);
		
		while(fileScanner.hasNextLine())
			textContent += fileScanner.nextLine() + "\n";
		
		this.textContent = textContent;
		fileScanner.close();
	}
	
	public void setTextContent(String newContent) throws IOException {
		this.textContent = newContent;
		this.writeToFile(newContent);
	}
	
	public String getTextContent() {
		return this.textContent;
	}
	
	

	public static TextExcerpt fromXML(Element text, Collection<Note>allExistingNotes, String pathToSaveDirectory) throws IOException {
		String name = ((Element) text.getElementsByTagName("Name").item(0)).getTextContent() ;
		
		if(allExistingNotes==null) {
			return null;
		}
		
		for(Note n: allExistingNotes) {
			if(n.getName().equals(name) && n.getType().equals("Text")) {
				return (TextExcerpt) n;
			}
		}

		return null;
	}
	
	private List<BufferedImage> recentlyConvertedImages;
	
	public List<BufferedImage> toImages() {

		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2D =img.createGraphics();
		Font font = new Font("Arial", Font.BOLD, 17);
        g2D.setFont(font);
        FontMetrics fm = g2D.getFontMetrics();
        int width = fm.stringWidth(this.textContent);
        int panelWidth = 420;
        int widthFactor = ((width*1000)/panelWidth);       
        int numWords = 1000 * this.textContent.length() / widthFactor;

        ArrayList<String> words = new ArrayList<String>();
 
        for(int i=0; i<numWords; i++) {
        		if(this.textContent.length() - i*numWords < numWords) {
        			words.add(this.textContent.substring(i*numWords, i*numWords + this.textContent.length() - i*numWords) );
        			break;
        		}
        		words.add(this.textContent.substring(i*numWords, i*numWords+numWords)+System.lineSeparator());
        		
        }
        
        int height = fm.getAscent();
        g2D.dispose();

        img = new BufferedImage((int) (panelWidth*1.2), height*words.size(), BufferedImage.TYPE_INT_ARGB);
        g2D = img.createGraphics();
        g2D.setFont(font);
        fm = g2D.getFontMetrics();
        g2D.setColor(Color.BLACK);

        int ascent = 0;
        for(String w: words) {
        		ascent += fm.getAscent();
        		g2D.drawString(w, 0, ascent);

        }
        
        g2D.dispose();
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>(); 
        images.add(img);
        
        recentlyConvertedImages = images;
        
        return images;
        
	}
	
	public String toXML() {
		String xml = "<Note>" + System.lineSeparator()
						+ "<Text>" + System.lineSeparator()
							+ "<Name>" + this.name + "</Name>" + System.lineSeparator()
							+ "<Path>" + this.note.getPath() + "</Path>" + System.lineSeparator()
						+ "</Text>" + System.lineSeparator() +
			        "</Note>";
		
		return xml;
	}

	@Override
	public int indexOfImage(BufferedImage image) {
		if(recentlyConvertedImages!=null)
			return this.recentlyConvertedImages.indexOf(image);
		return -1;
	}

	
	
}
