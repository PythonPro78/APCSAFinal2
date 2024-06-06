import java.util.ArrayList;

public class ButtonManager
{
	private ArrayList<Button> buttons;
	
	public ButtonManager()
	{
		buttons = new ArrayList<Button>();
	}
	
	public void addButton(Button button)
	{
		buttons.add(button);
	}
	
	public void click(int x, int y)
    {
    	Button current;
    	
    	for (int i = 0; i < buttons.size(); i ++)
    	{
    		current = buttons.get(i);
    		
    		if (current.isActive() &&
    			x > current.getX() && x < current.getX() + current.getWidth() &&
    			y > current.getY() && y < current.getY() + current.getHeight()) 
    		{
    			current.onClick();
    		}
    	}
    }
    
    public void mouseOver(int x, int y)
    {
    	Button current;
    	
    	for (int i = 0; i < buttons.size(); i ++)
    	{
    		current = buttons.get(i);

    		if (current.isActive() &&
    			x > current.getX() && x < current.getX() + current.getWidth() &&
    			y > current.getY() && y < current.getY() + current.getHeight())
    		{
    			current.setOver(true);
    		}
    		else
    		{
    			current.setOver(false);
    		}
    	}
    }
}
