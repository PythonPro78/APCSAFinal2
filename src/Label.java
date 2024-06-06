import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Label extends MenuItem
{
    private String txt;
    private Font font;

    private Color fillColor;

    // CONSTRUCTORS
    public Label(String id, String txt, int x, int y, int width, int height, Font font, Color fillColor)
    {
        super(id, x, y, width, height);

        this.txt = txt;
        this.font = font;
        this.fillColor = fillColor;
    }
    
    public Label(String id, String txt, int x, int y, int width, int height)
    {
        super(id, x, y, width, height);
    	
        this.txt = txt;
        font = new Font("arial", 1, 15);
        fillColor = new Color(0, 0, 0, 0);
    }
    
    public Label(String id, int x, int y, int width, int height)
    {
        super(id, x, y, width, height);
    	
        txt = "";
        font = new Font("arial", 1, 15);
        fillColor = new Color(0, 0, 0, 0);
    }

    // GETTERS
    public String getText()
    {
    	return txt;
    }
    
    public Font getFont()
    {
    	return font;
    }
    
    public Color getFillColor()
    {
    	return fillColor;
    }
    
    // SETTERS
    public void setText(String txt)
    {
    	this.txt = txt;
    }
    
    public void setFont(Font font)
    {
    	this.font = font;
    }
    
    public void setFillColor(Color fillColor)
    {
    	this.fillColor = fillColor;
    }
    
    // BRAIN METHODS
    @Override
    public void draw(Graphics2D g2D)
    {
    	Font scaledFont = font.deriveFont(  (float) (font.getSize() * scale())  );
    	
    	Rectangle2D bounds = scaledFont.getStringBounds(txt, g2D.getFontRenderContext());
    	
    	g2D.setColor(fillColor);
    	g2D.fillRect(getX(), getY(), getWidth(), getHeight());
    	
    	g2D.setColor(Color.BLACK);
    	g2D.setFont(scaledFont);
    	g2D.drawString( txt, (int) (getX() + getWidth()/2 - bounds.getCenterX()),
                        (int) (getY() + getHeight()/2 - bounds.getCenterY()) );
    }
}