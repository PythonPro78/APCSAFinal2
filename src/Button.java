import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

interface ClickAction
{
	public void action();
}

public class Button extends MenuItem
{
    private String txt;
    private Font font;

    private Color fillColor;
    private Color highlightColor;
    
    private ClickAction actionObj;
    
    private boolean isOver;
    
    private boolean isActive;
    
    // CONSTRUCTORS
    public Button(String id, String txt, int x, int y, int width, int height, Font font, Color fillColor, Color highlightColor, ButtonManager manager)
    {
    	super(id, x, y, width, height);
    	
        this.txt = txt;
        this.font = font;
        this.fillColor = fillColor;
        this.highlightColor = highlightColor;

        isOver = false;
        isActive = false;
        
        manager.addButton(this);
    }
    
    public Button(String id, String txt, int x, int y, int width, int height, ButtonManager manager)
    {
        super(id, x, y, width, height);
    	
        this.txt = txt;
        font = new Font("arial", 1, 15);
        fillColor = Color.BLUE;
        highlightColor = Color.cyan;

        isOver = false;
        isActive = false;
        
        manager.addButton(this);
    }
    
    public Button(String id, int x, int y, int width, int height, ButtonManager manager)
    {
        super(id, x, y, width, height);

        txt = "";
        font = new Font("arial", 1, 15);
        fillColor = Color.BLUE;
        highlightColor = Color.cyan;

        isOver = false;
        isActive = false;
        
        manager.addButton(this);
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
    
    public boolean isActive()
    {
    	return isActive;
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
    
    public void setHighlightColor(Color highlightColor)
    {
    	this.highlightColor = highlightColor;
    }
    
    public void setOnClick(ClickAction obj) 
    {
    	actionObj = obj;
    }
    
    public void setActive(boolean isActive)
    {
    	this.isActive = isActive;
    }
    
    public void setOver(boolean isOver)
    {
    	this.isOver = isOver;
    }
    
    // BRAIN METHODS
    public void onClick()
    {
    	if (actionObj != null)
    	{
    		actionObj.action();
    	}
    }
    
    public void forceClick()
    {
    	onClick();
    }
    
    @Override
    public void draw(Graphics2D g2D)
    {
    	Font scaledFont = font.deriveFont(  (float) (font.getSize() * scale())  );
    	
    	Rectangle2D bounds = scaledFont.getStringBounds(txt, g2D.getFontRenderContext());
    	
    	g2D.setColor(isOver ? highlightColor : fillColor);
    	g2D.fillRect(getX(), getY(), getWidth(), getHeight());
    	
    	g2D.setColor(Color.BLACK);
    	g2D.setFont(scaledFont);
    	g2D.drawString( txt, (int) (getX() + getWidth()/2 - bounds.getCenterX()),
                        (int) (getY() + getHeight()/2 - bounds.getCenterY()) );
    }
    
    // TO STRING
    public String toString()
    {
    	return String.format("x:%d, y:%d, width:%d, height:%d", x, y, width, height);
    }
}