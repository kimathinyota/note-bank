import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ViewNotesPage extends JPanel {
	
	private JButton back;
	private JButton createIdea;
	private JCheckBox addToIdeaCheckBox;
	private JButton nextPage;
	private JButton prevPage;
	private JLabel pageLocation;
	private DisplayPanel dsplayPanel;
	private List<Integer> selectedPageIndexes;
	private List<BufferedImage> images;
	private List<String> text;
	private List<Note>notes;
	private int currentImage;
	private JPanel previousPage;
	private String pathToDirectory;
	private boolean clearSelectAllToggle;
	private JButton clearSelectToggleButton;
	
	public void addClearSelectButtonActionListener(ActionListener clearSelectActionListener) {
		this.clearSelectToggleButton.addActionListener(clearSelectActionListener);
	}
	
	public void manageToggle() {
		for(int i=0; i<this.images.size()+this.text.size(); i++) {
			if(clearSelectAllToggle && !this.selectedPageIndexes.contains(i) ) {
				this.selectedPageIndexes.add( new Integer(i));
			}else {
				this.selectedPageIndexes.clear();
			}
			
			this.addToIdeaCheckBox.setSelected(clearSelectAllToggle);
			
		}
		this.clearSelectAllToggle = !this.clearSelectAllToggle;
		this.setButtonName();
	}
	
	
	private void setButtonName() {
		String name = "CLEAR ALL";
		if(this.clearSelectAllToggle)
			name = "SELECT ALL";
		this.clearSelectToggleButton.setText(name);
		
	}
	public JPanel getPreviousPage() {
		return this.previousPage;
	}
	
	private boolean isNoteSelected(Note note) {	
		for(Integer index: selectedPageIndexes) {
			if(index.intValue()>=images.size()) { 
				//implies index is within Text list range
				if((note instanceof TextExcerpt))
					if(  text.get(index-images.size()).equals(((TextExcerpt) note).getTextContent())  )
						return true;
			}else if(note.indexOfImage(images.get(index)) != -1)
				return true;
		}
		return false;
	}
	
	public String getCurrentNoteName() {
		String pageLoc = "";

		for(Note n: notes) {
			if( currentImage >=images.size()) {
			//current note is a TextExcerpt
				String content = text.get(currentImage - images.size());
				
				if(n instanceof TextExcerpt && 
						( (TextExcerpt) n).getTextContent().equals(content)  )
					return n.toString();
					
			}else {
				if(n instanceof Book) {
					Book book = (Book) n;
					int index = book.indexOfImage(images.get(currentImage));
					if(index != -1) {
						pageLoc = (index+1) + "/" + book.getNumberOfPages();
						return (book + " (" + pageLoc + ")" );
					}
				}else if(n instanceof Image) {
					return n.toString();
				}
			}
		}

		return null;
	}
	
	private String getBookPageRange(Book n) {
		String pageRange = "";
		for(Integer index: selectedPageIndexes) {
			int foundImageIndex = n.indexOfImage(images.get(index));
			if(foundImageIndex>-1) {
				pageRange += Integer.toString(foundImageIndex+1) + ",";
			}
		}
		System.out.println(pageRange);
		if(pageRange.length()>0)
			return pageRange.substring(0, pageRange.length()-1);
		
		return pageRange;
	}

	
	
	public List<Note> getSelectedNotes(){
		List<Note> selecNotes = new ArrayList<Note>();
		System.out.println(  "Selected Page Indexes: "  );
		for(Integer index: this.selectedPageIndexes) {
			System.out.println(index);
		}
		for(Note n: notes) {
			System.out.println(  "All notes: " + n);
			if(isNoteSelected(n)) {
				System.out.println( "Selected: " +  n  );
				if(n instanceof Book) {
					try {
						Book book = new Book((Book) n,getBookPageRange((Book) n),pathToDirectory);
						selecNotes.add(book);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else {
					selecNotes.add(n);
				}
			}	
		}
		return selecNotes;
	}
	
	public void setPreviousPage(JPanel previousPage) {
		this.previousPage = previousPage;
	}
	
	public void deselectCurrentImage() {
		System.out.println(  "Remove: "  + currentImage );
		this.selectedPageIndexes.remove( new Integer(currentImage) );
	}
	
	public void selectCurrentImage() {
		System.out.println(  "Add: "  + currentImage );
		if(!selectedPageIndexes.contains(new Integer(currentImage))) {
			selectedPageIndexes.add(currentImage);
		}
	}
	
	
	public void nextImage() {
		currentImage = (this.currentImage + 1) % (images.size()+text.size());
		if(currentImage<0) 
			currentImage += (images.size()+text.size());
		this.displayImage();
		this.updateName();
		this.setPageLocationLabel(currentImage+1, images.size()+text.size());
		this.addToIdeaCheckBox.setSelected(this.selectedPageIndexes.contains( new Integer(currentImage)));
	}
	
	public void updateName() {
		this.setName( this.getCurrentNoteName() );
	}
	
	public void prevImage() {
		currentImage = (this.currentImage - 1) % (images.size()+text.size());
		if(currentImage<0) 
			currentImage += (images.size()+text.size());
		this.displayImage();
		this.setPageLocationLabel(currentImage+1, images.size() + text.size());
		this.updateName();
		this.addToIdeaCheckBox.setSelected(this.selectedPageIndexes.contains( new Integer(currentImage)));
	}
	
	public void setName(String name) {
		this.dsplayPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), name));
	}
	
	public void setCreateIdeaButtonName(String name) {
		this.createIdea.setText(name);
	}

	
	public void setCreateIdeaFunctionality(boolean enableCreateIdeaFunctionality) {
		createIdea.setVisible(enableCreateIdeaFunctionality);
		createIdea.setEnabled(enableCreateIdeaFunctionality);
		addToIdeaCheckBox.setVisible(enableCreateIdeaFunctionality);
		addToIdeaCheckBox.setEnabled(enableCreateIdeaFunctionality);
		this.clearSelectToggleButton.setVisible(enableCreateIdeaFunctionality);
		this.clearSelectToggleButton.setEnabled(enableCreateIdeaFunctionality);
	}
	
	
	public ViewNotesPage(boolean enableCreateIdeaFunctionality, List<Note>notes, String pathToDirectory) {
		JPanel backCreateIdeaPanel = new JPanel(new GridLayout(1,4));
		this.pathToDirectory = pathToDirectory;
		back = new JButton("BACK");
		createIdea = new JButton("CREATE IDEA");
		this.selectedPageIndexes = new ArrayList<Integer>();
		
		this.clearSelectToggleButton = new JButton();
		backCreateIdeaPanel.add(back);
		backCreateIdeaPanel.add(Box.createHorizontalGlue());
		backCreateIdeaPanel.add(this.clearSelectToggleButton);
		backCreateIdeaPanel.add(createIdea);

		addToIdeaCheckBox = new JCheckBox("ADD TO IDEA");
		
		
		
		
		nextPage = new JButton("Next Page");
		prevPage = new JButton("Prev Page");
		pageLocation = new JLabel("?/?");
		pageLocation.setHorizontalAlignment(JLabel.CENTER);
		
		
		
		JPanel bottomPanel = new JPanel(new GridLayout(1,4));
		bottomPanel.add(addToIdeaCheckBox);
		bottomPanel.add(prevPage);
		bottomPanel.add(nextPage);
		bottomPanel.add(pageLocation);

		dsplayPanel = new DisplayPanel();
		dsplayPanel.setPreferredSize( new Dimension(605,605));
		this.setCreateIdeaFunctionality(enableCreateIdeaFunctionality);
		
		JPanel viewNotesPanel = new JPanel(new BorderLayout());
		viewNotesPanel.add(backCreateIdeaPanel, BorderLayout.NORTH);
		viewNotesPanel.add(dsplayPanel, BorderLayout.CENTER);
		viewNotesPanel.add(bottomPanel, BorderLayout.SOUTH);
	

		images = new ArrayList<BufferedImage>();
		this.text = new ArrayList<String>();
		this.notes = new ArrayList<Note>();
		for(Note n: notes) {
			if(n!=null)
				this.addImages(n);
		}
		
		this.clearSelectAllToggle = true;
		this.manageToggle();
		
		currentImage = 0;
		
		this.add(viewNotesPanel);
		
		this.updateName();
		this.displayImage();
		this.addToIdeaCheckBox.setSelected(true);
		this.setPageLocationLabel(currentImage+1, images.size()+text.size());
		
		
	}
	
	public void addImages(Note note) {
		if(note instanceof TextExcerpt) {
			this.text.add(((TextExcerpt) note).getTextContent());
		}else {
			images.addAll(note.toImages());
		}
		notes.add(note);
	}
	
	public void displayImage() {
		
		if(currentImage>=images.size()) {
			dsplayPanel.display(text.get(currentImage-images.size()));
		}else {
			dsplayPanel.display(images.get(currentImage));
		}
	}
	
	public boolean isAddToIdeaChecked() {
		return this.addToIdeaCheckBox.isSelected();
	}
	
	
	public List<Integer> getSelectedPageIndexes(){
		return this.selectedPageIndexes;
	}
	
	public void addBackActionListener(ActionListener backActionListener) {
		this.back.addActionListener(backActionListener);
	}
	
	public void addCreateIdeaActionListener(ActionListener createIdeaActionListener) {
		this.createIdea.addActionListener(createIdeaActionListener);
	}
	
	public void addAddToIdeaCheckBoxItemListener(ItemListener addToIdeaCheckBoxItemListener) {
		this.addToIdeaCheckBox.addItemListener(addToIdeaCheckBoxItemListener);
	}
	
	public void addNextPageActionListener(ActionListener nextPageActionListener) {
		this.nextPage.addActionListener(nextPageActionListener);
	}
	
	public void setPageLocationLabel(int currentPageLocation, int numberOfPages) {
		String pageLoc = String.valueOf(currentPageLocation) + " / " + String.valueOf(numberOfPages);
		this.pageLocation.setText(pageLoc);
	}
	
	public void addPreviousPageActionListener(ActionListener previousPageActionListener) {
		this.prevPage.addActionListener(previousPageActionListener);
	}
	
	
	
	
	
	
}
