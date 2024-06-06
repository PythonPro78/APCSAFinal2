import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

class MenuItem
{
	public final static int MAX_WIDTH = 800;
	public final static int MAX_HEIGHT = 450;
	
	private static ArrayList<String> allIds = new ArrayList<String>();
	private static double scale = 1;
	
	private static int screenX = 0;
	private static int screenY = 0;
	
    protected int x, y;
    protected int width, height;

	private String id;
	
	private boolean useScale;
	protected boolean visible;
    
    // CONSTRUCTORS
    public MenuItem(String id, int x, int y, int width, int height)
    {
		if (duplicateId(id))
		{
			throw new IllegalArgumentException();
		}

    	this.id = id;
    	this.x = x;
    	this.y = y;
    	this.width = width;
    	this.height = height;
    	
    	useScale = true;
    	visible = true;
    }
    
    // GETTERS
    public String getId()
    {
    	return id;
    }
    
    public int getX()
    {
    	return useScale ? (int) (x*scale + screenX) : x;
    }
    
    public int getY()
    {
    	return useScale ? (int) (y*scale + screenY) : y;
    }
    
    public int getWidth()
    {
    	return useScale ? (int) (width * scale) : width;
    }
    
    public int getHeight()
    {
    	return useScale ? (int) (height * scale) : height;
    }
    
    public boolean usingScale()
    {
    	return useScale;
    }
    
    // SETTERS
    public void setPosition(int x, int y)
    {
    	this.x = x;
    	this.y = y;
    }
    
    public void setPosition(Point point)
    {
    	x = point.x;
    	y = point.y;
    }
    
    public void setSize(int width, int height)
    {
    	this.width = width;
    	this.height = height;
    }
    
    public void setUseScale(boolean useScale)
    {
    	this.useScale = useScale;
    }
    
    // BRAIN METHODS
    public void draw(Graphics2D g2D)
    {
    	System.out.println(scale);
    	
    	g2D.setColor(Color.BLUE);
    	g2D.fillRect(getX(), getY(), getWidth(), getHeight());
    }
    
    protected static boolean duplicateId(String id)
    {
    	for (int i = 0; i < allIds.size(); i ++)
    	{
    		if (id == allIds.get(i))
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public static Rectangle updateScale(int width, int height)
    {
    	double xScale = (double) width / MAX_WIDTH;
    	double yScale = (double) height / MAX_HEIGHT;
    	
    	if (xScale < yScale)
    	{
    		scale = xScale;
    		
    		screenX = 0;
    		screenY = (int) (height/2 - MAX_HEIGHT*scale/2);
    	}
    	else
    	{
    		scale = yScale;
    		
    		screenY = 0;
    		screenX = (int) (width/2 - MAX_WIDTH*scale/2);
    	}
    	
    	return new Rectangle(screenX, screenY, (int) (MAX_WIDTH*scale), (int) (MAX_HEIGHT*scale));
    }
    
    public static double scale()
    {
    	return scale;
    }
    
    public static int screenX()
    {
    	return screenX;
    }
    
    public static int screenY()
    {
    	return screenY;
    }
}