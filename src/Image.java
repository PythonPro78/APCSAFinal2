import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Image extends MenuItem
{
	private static final String voidImageUrl = "src/VoidImage.png";
	
	private BufferedImage img;
	
	// CONSTRUCTORS
	public Image(String id, BufferedImage img, int x, int y, int width, int height)
    {
        super(id, x, y, width, height);
		
        this.img = img;
    }
	
	public Image(String id, int x, int y, int width, int height)
    {
        super(id, x, y, width, height);
		
        img = null;
    }
    
    // GETTERS
    public BufferedImage getImage()
    {
    	return img;
    }
    
    // SETTERS
    public void setImage(BufferedImage img)
    {
    	this.img = img;
    }
    
    // BRAIN METHODS
    @Override
    public void draw(Graphics2D g2D)
    {
    	if (img == null)
    	{
    		BufferedImage voidImg = null;
    		
    		try {
    			voidImg = ImageIO.read(new File(voidImageUrl));
    		} catch (Exception e) {System.out.println("Damn, both images failed to load");}
    		
    		g2D.drawImage(voidImg, getX(), getY(), getWidth(), getHeight(), Main.frame.FRAME);
    	}
    	else
    	{
    		g2D.drawImage(img, getX(), getY(), getWidth(), getHeight(), Main.frame.FRAME);
    	}
    }// END OF DRAW METHOD
}
