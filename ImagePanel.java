import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Panel used to display an Image
 * @author kimathi nyota
 */
public class ImagePanel extends JPanel{
	
	private BufferedImage image;
	private double factor;

	/*
	public void paintComponent(Graphics g) {
		double factor = ( (image.getHeight()*100)/this.getHeight() > (image.getWidth()*100)/this.getWidth() ? (image.getHeight()*100)/this.getHeight()  
					: (image.getWidth()*100)/this.getWidth() );
		
		
		int height = (int) (this.factor*image.getHeight()*100/(factor)) ;
		int width = (int)  (this.factor*image.getWidth()*100/(factor)) ;
		System.out.println(this.factor);
		System.out.println(this.getHeight() + " - " +  height);
		System.out.println(this.getWidth() + " - " + width);
		
		if(factor==0) {
			height = image.getHeight();
			width = image.getWidth();
		}
		
		
		
		int x = (this.getWidth() - width )/ 2;
		int y = (this.getHeight() - height) / 2;
		

		if(image!=null)
			g.drawImage(image, x, y, width, height, null, this);
		
		//

	}
	*/
	
	
	/**
	 * Used to draw the Image onto this panel as 
	 * large as possible provided it correctly 
	 * matches the given image scale.
	 */
	public static BufferedImage scale(BufferedImage sbi, int dWidth, int dHeight, int x, int y) {
	    BufferedImage dbi = null;
	    if(sbi != null) {
	        dbi = new BufferedImage(dWidth, dHeight, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g = dbi.createGraphics();
	        System.out.println(dWidth + " - " + dHeight);
	        g.drawImage(sbi, 0, 0, dWidth, dHeight, null);

	       
	    }
	    return dbi;
	}
	
	/**
	 * Method to draw input Image onto the panel
	 * @param image - to be drawn onto the panel
	 */
	public void drawImage(BufferedImage image) {
		int provHeight = 700;
		int provWidth = 620;
		double heightFactor = ((100000*provHeight)*this.factor)/image.getHeight();
		double widthFactor = ((100000*provWidth)*this.factor)/image.getWidth();
		double factor = (heightFactor<widthFactor ? heightFactor : widthFactor);
		factor/=100000;

		int height = (int) (image.getHeight()*(factor)) ;
		int width = (int)  (image.getWidth()*(factor)) ;
		int x = (provWidth - width )/ 2;
		int y = (provHeight - height) / 2;
		
		ImageIcon ii = new ImageIcon(scale(image,width,height,x,y));

		JLabel label = new JLabel(ii);
		
		JScrollPane jsp = new JScrollPane(label);
		
		jsp.setBorder(BorderFactory.createEmptyBorder());

		this.removeAll();
		this.setLayout(new GridLayout(1,1));
		this.add(jsp);
		
		this.revalidate();
		this.repaint();
		
		this.image = image;
		
	}
	
	public void drawImage(BufferedImage image, double factor) {
		this.factor = factor;
		this.drawImage(image);
	}
	
	public ImagePanel() {
		this.factor = 1;
		this.setSize(new Dimension(650,750));
		
	}
	
	public void refresh(double factor) {
		this.drawImage(image,factor);
	}
	
}
