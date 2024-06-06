import java.awt.Color;

public class User extends Player
{
	private static final String PLAY_MENU_ID = "playScreen";
	private static final String[] ACTION_BUTTON_IDS = {
			"checkButton",
			"foldButton",
			"raiseButton",
			"betButton",
			"callButton"
	};
	
	private static final Color BUTTON_COLOR = new Color(0, 100, 230);
	private static final Color BUTTON_DEACTIVATED_COLOR = new Color(15, 50, 170);
	
	private boolean watingForInput;
	private boolean hasInput;
	private Decision decision;
	
    public User(String name)
    {
        super(name);
        
        watingForInput = false;
        hasInput = false;
        decision = null;
        
        setButtons(false);
    }

    @Override
    public synchronized Decision makeDecision()
    {
    	setButtons(true);
    	watingForInput = true;
    	
    	while (!hasInput || decision == null)
    	{
    		try
    		{
				wait();
			} catch (InterruptedException e) {e.printStackTrace();}
    	}
    	
    	hasInput = false;
    	
    	return decision;
    }
    
    public synchronized void giveInput(Decision decision)
    {
    	if (!watingForInput)
    		return;
    	
    	System.out.println(decision);
    	
    	setButtons(false);
    	
    	this.decision = decision;
    	
    	watingForInput = false;
    	hasInput = true;
    	
    	notify();
    }
    
    public void setButtons(boolean enabled)
    {
    	for (String btn : ACTION_BUTTON_IDS)
    	{
    		Button current = (Button) Main.getMenuItem(PLAY_MENU_ID, btn);
    		current.setActive(enabled);
    		current.setFillColor(enabled ? BUTTON_COLOR : BUTTON_DEACTIVATED_COLOR);
    	}
    }
}
