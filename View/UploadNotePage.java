package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Model.Book;
import Model.Image;
import Model.TextExcerpt;
import View.Handlers.UploadNoteEventHandler;
import org.apache.commons.lang3.*;


public class UploadNotePage extends JPanel {
	
	private JButton backUpload;
	private JTabbedPane noteTypePane;
	private JTextArea enterTextArea;
	private JTextField textName;
	private JButton textUploadToBank;


	private JPanel previousPage;
	private JLabel uploadImageLabel;
	private JButton chooseImage;
	private JLabel chosenImageLabel;
	private JLabel uploadPDFLabel;
	private JButton choosePDF;
	private JLabel chosenPDFLabel;
	private JLabel imageLabel;
	private JLabel pdfLabel;
	private JButton uploadPDFToBank;
	private JButton uploadImageToBank;
	private JTextField pdfName ;
	private JTextField imageName ;
	private String pathToDirectory;

	private File chosenImage;
	private File chosenPDF;
	
	public void setChosenImage(File chosenImage) {
		this.chosenImage = chosenImage;
	}
	
	public void setChosenPDF(File chosenPDF) {
		this.chosenPDF = chosenPDF;
	}
	
	public File getChosenImage() {
		File file = this.chosenImage;
		this.chooseImage = null;
		return file;
	}
	
	public void setPDFName(String name) {
		this.chosenPDFLabel.setText(name);
	}
	
	public void setImageName(String name) {
		this.chosenImageLabel.setText(name);
	}
	
	public File getChosenPDF() {
		File file = this.chosenPDF;
		this.choosePDF = null;
		return file;
	}
	
	public JPanel getPreviousPage() {
		return this.previousPage;
	}

	public void setPreviousPage(JPanel previousPage) {
		this.previousPage = previousPage;
	}

	public String getTextName() {
		return this.textName.getText();
	}
	
	public String getTextContent() {
		return this.enterTextArea.getText();
	}
	
	public String getImageName() {
		return this.imageName.getText();
	}
	
	public String getPDFName() {
		return this.pdfName.getText();
	}

	public boolean isNameValid(String name) {
		return StringUtils.isAlphanumericSpace(name) && name.length()>1;

	}
	
	public TextExcerpt getTextExcerpt() throws IOException {
		if(this.isNameValid( this.getTextName()  ))
			return (new TextExcerpt(this.textName.getText().replaceAll("\\s","-") , this.getTextContent(), pathToDirectory)  );
		return null;
	}
	
	public Image getImage() throws IOException {
		if(this.chooseImage==null || !isNameValid(this.imageName.getText()))
			return null;
		
		return new Image(this.imageName.getText().replaceAll("\\s","-"), this.chosenImage.getPath(),pathToDirectory);
	}
	
	public Book getPDF() throws IOException {
		if(this.choosePDF==null || !isNameValid(this.pdfName.getText()))
			return null;
		
		return new Book(this.pdfName.getText().replaceAll("\\s","-"), this.chosenPDF.getPath(),pathToDirectory);
	}
	
	public void resetTextNote() {
		this.textName.setText("");
		this.enterTextArea.setText("");
	}
	
	public void resetImageNote() {
		this.imageName.setText("");
		this.chosenImageLabel.setText("No Model.Image Chosen");
	}
	
	public void resetPDFNote() {
		this.pdfName.setText("");
		this.chosenPDFLabel.setText("No PDF Chosen");
	}
	
	
	public void nextPanel() {
		this.noteTypePane.setSelectedIndex(  (this.noteTypePane.getSelectedIndex() + 1) % this.noteTypePane.getTabCount()  );
	}
	
	public UploadNotePage(UploadNoteEventHandler handler, String pathToDirectory) {
		this.chooseImage = null;
		this.chosenPDF = null;
		this.pathToDirectory = pathToDirectory;
		JPanel uploadNotePanel = new JPanel(new BorderLayout());
		
		//BACK BUTTON
		backUpload = new JButton("BACK");
		JPanel backUploadPanel = new JPanel(new GridLayout(1,4));
		backUploadPanel.add(backUpload);
		backUploadPanel.add(Box.createHorizontalGlue());
		backUploadPanel.add(Box.createHorizontalGlue());
		backUploadPanel.add(Box.createHorizontalGlue());
		
		
		//TABBED PANE: TEXT & PDF/IMAGE
		
		noteTypePane = new JTabbedPane();
		uploadNotePanel.add(backUploadPanel, BorderLayout.NORTH);
		uploadNotePanel.add(noteTypePane, BorderLayout.CENTER);
		
		
		//TEXT TAB:
		
		JPanel uploadTextTabPanel = new JPanel(new BorderLayout());
		
			//ENTER TEXT -> Text Area
		enterTextArea = new JTextArea();
		enterTextArea.setLineWrap(true);
		enterTextArea.setWrapStyleWord(true);
		enterTextArea.setFont(  new Font("Arier",Font.PLAIN,14)  );
		enterTextArea.setBorder(BorderFactory.createTitledBorder("ENTER TEXT"));
		JScrollPane enterText = new JScrollPane(enterTextArea);
		enterText.setBorder(  BorderFactory.createEmptyBorder()  );
		
			//NAME OF NOTE -> Text Field
		JPanel nameUploadPanel = new JPanel(new BorderLayout());
		textName = new JTextField();
		textName.setBorder(BorderFactory.createTitledBorder("NAME TEXT NOTE"));
		nameUploadPanel.add(textName, BorderLayout.CENTER);
		
			//UPLOAD TO BANK -> Button
		textUploadToBank = new JButton("UPLOAD TO BANK");
		nameUploadPanel.add(textUploadToBank, BorderLayout.SOUTH);
		
		uploadTextTabPanel.add(enterText, BorderLayout.CENTER);
		uploadTextTabPanel.add(nameUploadPanel, BorderLayout.SOUTH);
		
		
		//PDF TAB:
		JPanel uploadPDFPanel = new JPanel(new GridLayout(3,1));
		
			//UPLOAD A FILE LABEL
		JPanel uploadPDFLabelPanel = new JPanel(new GridLayout(3,1));
		uploadPDFLabel = new JLabel("Upload an PDF file");
		
			// PDF: [Choose PDF] No PDF chosen
		pdfLabel = new JLabel("PDF:");
		pdfLabel.setHorizontalAlignment(JLabel.CENTER);
		
		JPanel choosePDFPanel = new JPanel(new GridLayout(1,1));
		choosePDF = new JButton("Choose PDF");
		choosePDFPanel.add(choosePDF);
		
		chosenPDFLabel = new JLabel("No PDF chosen");
		chosenPDFLabel.setHorizontalAlignment(JLabel.CENTER);
		
		
		
		JPanel pdfChosenPanel = new JPanel(new GridLayout(1,4));
		pdfChosenPanel.add(pdfLabel);
		pdfChosenPanel.add(choosePDFPanel);
		pdfChosenPanel.add(chosenPDFLabel);
		pdfChosenPanel.add(Box.createHorizontalGlue());
		
		uploadPDFLabelPanel.add(uploadPDFLabel);
		uploadPDFLabelPanel.add(pdfChosenPanel);
		uploadPDFLabelPanel.add(Box.createVerticalGlue());
		
		
			//NAME OF NOTE LABEL
		pdfName = new JTextField();
		pdfName.setBorder(BorderFactory.createTitledBorder("NAME OF NOTE"));
		
			//UPLOAD TO BANK
		uploadPDFToBank = new JButton("UPLOAD TO BANK");
		
		
		
		uploadPDFPanel.add(uploadPDFLabelPanel);
		
		JPanel namePDFUpload = new JPanel(new GridLayout(2,1));
		namePDFUpload.add(pdfName);
		namePDFUpload.add(uploadPDFToBank);
		
		uploadPDFPanel.add(namePDFUpload);
		uploadPDFPanel.add(Box.createVerticalGlue());
		
		
		
		//IMAGE TAB:
		JPanel uploadImagePanel = new JPanel(new GridLayout(3,1));
		
			//UPLOAD A FILE LABEL
		JPanel uploadImageLabelPanel = new JPanel(new GridLayout(3,1));
		uploadImageLabel = new JLabel("Upload an Model.Image file");
		
			// Model.Image: [Choose Model.Image] No Model.Image chosen
		imageLabel = new JLabel("Model.Image:");
		imageLabel.setHorizontalAlignment(JLabel.CENTER);
		
		JPanel chooseImagePanel = new JPanel(new GridLayout(1,1));
		chooseImage = new JButton("Choose Model.Image");
		chooseImagePanel.add(chooseImage);
		
		chosenImageLabel = new JLabel("No Model.Image chosen");
		chosenImageLabel.setHorizontalAlignment(JLabel.CENTER);
		
		
		
		JPanel imageChosenPanel = new JPanel(new GridLayout(1,4));
		imageChosenPanel.add(imageLabel);
		imageChosenPanel.add(chooseImagePanel);
		imageChosenPanel.add(chosenImageLabel);
		imageChosenPanel.add(Box.createHorizontalGlue());
		
		uploadImageLabelPanel.add(uploadImageLabel);
		uploadImageLabelPanel.add(imageChosenPanel);
		uploadImageLabelPanel.add(Box.createVerticalGlue());
		
		
			//NAME OF NOTE LABEL
		imageName = new JTextField();
		imageName.setBorder(BorderFactory.createTitledBorder("NAME OF NOTE"));
		
			//UPLOAD TO BANK
		uploadImageToBank = new JButton("UPLOAD TO BANK");
		
		
		
		uploadImagePanel.add(uploadImageLabelPanel);
		
		JPanel nameImageUpload = new JPanel(new GridLayout(2,1));
		nameImageUpload.add(imageName);
		nameImageUpload.add(uploadImageToBank);
		
		uploadImagePanel.add(nameImageUpload);
		uploadImagePanel.add(Box.createVerticalGlue());
		
		//ADD TEXT & PDF/IMAGE PANELS TO TABBED PANE
		noteTypePane.add("TEXT", uploadTextTabPanel);
		noteTypePane.add("IMAGE", uploadImagePanel);
		noteTypePane.add("PDF", uploadPDFPanel);
		
		//Finish structuring UPLOAD NOTE tab
		this.setLayout( new GridBagLayout());
		uploadNotePanel.setPreferredSize(new Dimension(500,500));
		uploadNotePanel.add(backUploadPanel, BorderLayout.NORTH);
		uploadNotePanel.add(noteTypePane, BorderLayout.CENTER);
		this.add(uploadNotePanel);


		//Add listeners:
		this.backUpload.addActionListener(handler::back);
		this.chooseImage.addActionListener(handler::chooseImage);
		this.choosePDF.addActionListener(handler::choosePDF);
		this.uploadImageToBank.addActionListener(handler::uploadImage);
		this.uploadPDFToBank.addActionListener(handler::uploadPDF);
		this.textUploadToBank.addActionListener(handler::uploadText);

	}
}
