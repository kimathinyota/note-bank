import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
/***
 * REALLY IMPORTANT:
 * DONT FORGET TO CHANGE TO COMPARE TO ID
 */
/**
 * Book
 * Class used to represent notes as PDF files 
 */
public class Book extends Note {
	
	private Book globalBook;

	/**
	 * gives total number of pages in PDF file
	 */
	private int numberOfPages;
	
	/**
	 * Set specific pages of PDF file e.g. pages 1,3,5,7 
	 */
	private String specifyPages;
	
	/**
	 * Global: Represents a single PDF file that contains all the pages.
	 * Non-global books can be created from a global note with specifc page numbers. 
	 */
	private boolean isGlobal;
	
	private List<BufferedImage> images;
	
	/**
	 * getPage: This returns page as rendered image. Non-zero indexed.
	 * Since getImages is a time intensive method, program must only call it once.
	 * @param pageNumber
	 * @return Page as image of type BufferedImage
	 * @throws IOException 
	 */
	
	public BufferedImage getPage(int pageNumber) throws IOException {
		if(images==null) 
			this.getImages();
		pageNumber-=1;
		return this.images.get(pageNumber);

	}
	
	/**
	 * Getter for global status
	 * @return boolean isGlobal
	 */
	public boolean isGlobal() {
		return this.isGlobal;
	}
	
	/**
	 * Loads all images. A time intensive method that uses the PDFBox API
	 * @throws IOException
	 */
	private void getImages() throws IOException {
		if(globalBook!=null) {
			images = this.globalBook.toImages();
		}else {
			PDDocument document = PDDocument.load(this.note);
			images = new ArrayList<BufferedImage>();
			for(int i=0; i<numberOfPages; i++) {
				PDFRenderer pdfRenderer = new PDFRenderer(document);
				BufferedImage bim = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB);
				images.add(bim);
			}
			document.close();
		}
	}
	
	/**
	 * Constructor for a global book
	 * @param name - book will be reffered to as and saved to the program under this name
	 * @param path - the path to the PDF file on computer
	 * @throws IOException
	 */
	public Book(String name, String path, String pathToSaveDirectory) throws IOException{
		super(name, path, "Book", pathToSaveDirectory);
		PDDocument document = PDDocument.load(this.note);
		numberOfPages = document.getNumberOfPages();
		String pages = "";
		for(int i=1; i<numberOfPages+1; i++)
			pages += i + ",";
		pages.substring(0, pages.length()-1);
		
		this.specifyPages = pages;
		document.close();
		this.isGlobal = true;
		this.globalBook = null;

		
	}
	
	/**
	 * Constructor for non-global book
	 * @param globalBook: This will be the
	 * @param selectedPages
	 * @throws IOException
	 */
	public Book(Book globalBook, String selectedPages, String pathToSaveDirectory) throws IOException{
		super(globalBook.getName(), globalBook.getFile().getPath(), "Book",pathToSaveDirectory);
		
		this.isGlobal = false;
		this.globalBook = globalBook;
		this.specifyPages = selectedPages;
		this.numberOfPages = this.specifyPages.length();
		
		
	}
	
	/**
	 * Creates non-global book from xml file
	 * @param book: Found <Book> .. </Book> element
	 * @param allExistingNotes: Current notes stored in the directory
	 * @return Boook (non-global)
	 * @throws IOException
	 */
	public static Book fromXML(Element book, Collection<Note> allExistingNotes, String pathToSaveDirectory) throws IOException {
		//String name = ((Element) book.getElementsByTagName("Name").item(0)).getTextContent() ;
		String path = ((Element) book.getElementsByTagName("Path").item(0)).getTextContent() ;
		String pages = ((Element) book.getElementsByTagName("Pages").item(0)).getTextContent() ;

		if(allExistingNotes==null) {
			return null;
		}
		
		for(Note n: allExistingNotes) {
			//Global book matching this book is found in file so a new non-global book is created from it
			if(n.getFile().getPath().equals(path)) {
				//change to ID for unique identifier
				return new Book( (Book) n, pages, pathToSaveDirectory);
			}
		}
		
		/*
		 * Reach this point means file doesn't exist in directory 
		 * Implying there is a conflict between data stored in XML 
		 * and existing data in directory
		 * Confilict resolution: Ignore file (by returning null)
		 */
		return null;
	
	}
	
	/**
	 * Converts a specifyPages string to a list of Integers
	 * e.g. 1,2,3,4 to a list [1,2,3,4]
	 * @param pages
	 * @return List<Integer> list of integers
	 */
	private List<Integer> getIntegers(String pages){
		List<String> integerList = Arrays.asList(pages.split(","));
		List<Integer> intList = new ArrayList<Integer>();
		for(String a: integerList) {
			intList.add( Integer.valueOf(a) );
		}
		return intList;
		
	}
	
	/**
	 * 
	 * Why not just return images, since PDF files are static?
	 * 		Non-global pages keep reference to the images list stored in the gobal book
	 * 		images contains images of all pages
	 * 		need to return images of pages as specified in specifyPages
	 *  
	 * @return list of images of each page of book
	 */
	public List<BufferedImage> toImages() {
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		
		for(Integer i: this.getIntegers(specifyPages)) {
			try {
				images.add( this.getPage(i) );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return images;
	}
	
	
	/**
	 * 
	 * @return number of pages
	 */
	public int getNumberOfPages() {
		return numberOfPages;
	}

	/**
	 * Convert a book into xml
	 */
	public String toXML() {

		String xml = "<Note>" + System.lineSeparator()
  						+ "<Book>" + System.lineSeparator()
  							+ "<Name>" + this.name + "</Name>" + System.lineSeparator()
  							+ "<Path>" + this.note.getPath() + "</Path>" + System.lineSeparator()
  							+ "<Pages>" + this.specifyPages + "</Pages>" + System.lineSeparator()
  						+ "</Book>" + System.lineSeparator() +
  					"</Note>";
		return xml;
	}

	/**
	 * Finds pageNumber-1 of current image in Book.
	 * @param inputted image
	 * @return -1 if Book doesn't contain image, else returns indexOfImage
	 */
	public int indexOfImage(BufferedImage image) {
		int index = this.toImages().indexOf(image) +1 ;
		if(this.specifyPages.contains( Integer.toString(index)  )) {
			return (index-1);
		}
		return -1;
		
	}


	/**
	 * @return For Global: returns Book: name,
	 * @return For non-Global: returns Book: name (specifyPages)
	 */
	public String toString() {
		String ext = (this.isGlobal ? "" : " ("+this.specifyPages+")");
		return ("Book: " + this.name.replaceAll("-"," ") +  ext);
	}
	
	

}
