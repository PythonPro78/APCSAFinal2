import java.awt.Graphics2D;
import java.util.ArrayList;

public class Menu
{
	private ArrayList<MenuItem> items;
	private boolean isActivated = false;
	
	// CONSTRUCTORS
	public Menu(ArrayList<MenuItem> items)
    {
		this.items = new ArrayList<MenuItem>(items);
    }
	
    public Menu()
    {
    	items = new ArrayList<MenuItem>();
    }
    
    // GETTERS
    public ArrayList<MenuItem> getItems()
    {
    	return new ArrayList<MenuItem>(items);
    }
    
    public MenuItem getItem(int index)
    {
    	return items.get(index);
    }
    
    public MenuItem getItem(String id)
    {
    	for (MenuItem item : items)
    	{
    		if (item.getId().equals(id))
    		{
    			return item;
    		}
    	}
    	
    	return null;
    }
    
    // SETTERS
    public void addItem(MenuItem newItem)
    {
    	items.add(newItem);
    }
    
    public boolean removeItem(MenuItem item)
    {
    	return items.remove(item);
    }
    
    public MenuItem removeItem(int index)
    {
    	return items.remove(index);
    }
    
    // BRAIN METHODS
    public void draw(Graphics2D g2D)
    {
		if (!isActivated)
			activate();

    	for (MenuItem item : items)
    		if (item.visible)
    			item.draw(g2D);
    }

	public void activate()
	{
		for (MenuItem item : items)
			if (item instanceof Button)
				((Button) item).setActive(true);

		isActivated = true;
	}
    
    public void deactivate()
    {
    	for (MenuItem item : items)
    	{
    		if (item instanceof Button)
    		{
    			((Button) item).setActive(false);
    		}
    	}

		isActivated = false;
    }
    
}