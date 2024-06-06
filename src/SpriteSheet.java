import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class SpriteSheet
{
	private Dimension tilePixels;
	private Dimension sheetSize;
	
	private BufferedImage sheet;
	
	// Left, right, top, and bottom
	private int rightBorder, bottomBorder;
	private int xGap, yGap;
	
	public SpriteSheet(BufferedImage sheet, int tileWidth, int tileHeight)
	{
		this.sheet = sheet;
		
		tilePixels = new Dimension(tileWidth, tileHeight);
		sheetSize = new Dimension(sheet.getWidth()/tileWidth, sheet.getHeight()/tileHeight);
		
		rightBorder = 0;
		bottomBorder = 0;
		
		xGap = 0;
		yGap = 0;
	}
	
	public SpriteSheet(BufferedImage sheet, Dimension tilePixels)
	{
		this.sheet = sheet;
		
		this.tilePixels = tilePixels;
		sheetSize = new Dimension(sheet.getWidth()/tilePixels.width,
								  sheet.getHeight()/tilePixels.height);
		
		rightBorder = 0;
		bottomBorder = 0;
		
		xGap = 0;
		yGap = 0;
	}
	
	public void setBorders(int right, int bottom)
	{
		rightBorder = right;
		bottomBorder = bottom;
	}
	
	public void setGap(int x, int y)
	{
		xGap = x;
		yGap = y;
	}
	
	public Dimension getSheetSize()
	{
		return sheetSize;
	}
	
	public BufferedImage getTile(int row, int col)
	{
		return sheet.getSubimage(col * (tilePixels.width+xGap), row * (tilePixels.height+yGap),
								 tilePixels.width + rightBorder, tilePixels.height + bottomBorder);
	}
	
	public BufferedImage getTile(int val)
	{
		return getTile(val/sheetSize.width, val % sheetSize.width);
	}
}
