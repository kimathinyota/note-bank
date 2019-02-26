import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 
 * DisplayPanel:
 * Used to display a note [ PDF, image or text ]
 */
public class DisplayPanel extends JPanel {
	private ImagePanel imagePanel;
	private JScrollPane imagePane;
	private JPanel textPanel;
	private JScrollPane textPane;
	private JTextArea textContent;
	private JButton zoomIn;
	private JButton zoomOut;
	private JPanel zoomInOut;
	private int currentFontSize;
	private double factor;
	private int currentPane;

	public void addListeners() {
		zoomIn.addActionListener( 
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						zoomIn();
					
					}
				}
				);
		zoomOut.addActionListener( 
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						zoomOut();
					}
				}
				);
	}
	
	/**
	 *  This sets up the Text Panel by setting the layout, instantiating
	 *  the panel, text area and scroll pane.
	 *  	
	 */
	private void setUpTextPanel() {
		this.textPanel = new JPanel(new GridLayout(1,1));
		this.textContent = new JTextArea();
		this.textContent.setBorder(BorderFactory.createEmptyBorder());
		this.textContent.setEnabled(false);
		this.textContent.setLineWrap(true);
		this.textContent.setWrapStyleWord(true);
		this.textContent.setFont(  new Font("Arier",Font.PLAIN,26)  );
		JScrollPane text = new JScrollPane(textContent);
		this.textPanel.add(text);
		currentFontSize = 26;
		
	}
	
	/**
	 * Used to display text
	 * @param text - this string will be displayed 
	 */
	public void display(String text) {
		this.textContent.setEnabled(true);
		this.textContent.setText(text);
		this.textContent.setEnabled(false);
		this.removeAll();
		this.add(textPanel, BorderLayout.CENTER);
		this.add(zoomInOut, BorderLayout.SOUTH);
		currentPane = 0;
	}
	
	/**
	 * Used to display a buffered image
	 * @param image - this image will be displayed
	 */
	public void display(BufferedImage image) {
		imagePanel.drawImage(image,this.factor);
		this.removeAll();
		this.add(imagePanel, BorderLayout.CENTER);
		this.add(zoomInOut, BorderLayout.SOUTH);
		currentPane = 1;
	}
	
	public void zoomIn() {
		if(currentPane==1) {
			this.factor += 0.1;
			imagePanel.refresh(factor);
			return;
		}
		this.textContent.setFont(  new Font("Arier",Font.PLAIN,(int) (currentFontSize+2))  );
		currentFontSize+=2;
	}
	
	public void zoomOut() {
		if(currentPane==1) {
			this.factor -= 0.1;
			imagePanel.refresh(factor);
			return;
		}
		this.textContent.setFont(  new Font("Arier",Font.PLAIN,(int) (currentFontSize-2))  );
		currentFontSize-=2;
	}
	
	/**
	 * Constructor for the disply panel:
	 * 	It instantiates the image panel
	 *  It sets the layout
	 */
	public DisplayPanel() {
		imagePanel = new ImagePanel();
		imagePane = new JScrollPane(imagePanel);
		textPane = new JScrollPane(textPanel);
		zoomIn = new JButton("Zoom In");
		zoomOut = new JButton("Zoom Out");
		this.addListeners();
		
		zoomInOut = new JPanel(new GridLayout(1,2));
		zoomInOut.add(zoomIn);
		zoomInOut.add(zoomOut);
		
		this.setUpTextPanel();
		this.setLayout(new BorderLayout());
		this.factor = 1;
		
	}

}
