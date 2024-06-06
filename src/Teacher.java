import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Teacher implements Runnable
{
	private int textOffsetX, textOffsetY;
	
	private Image img;
	private Label dialogueUI;
	private Rectangle bounds;
	private ArrayList<String> lines;
	private double velocity;
	
	private boolean canInterrupt;
	private double x, y;
	private double desiredX, desiredY;
	private double textX, textY;
	
	private int lineTimer;
	
	private boolean running =  true;
	
	// CONSTRUCTORS
	public Teacher(Image img, Label dialogueUI, Rectangle bounds, ArrayList<String> lines, double velocity)
	{
		this.img = img;
		this.dialogueUI = dialogueUI;
		this.bounds = bounds;
		this.lines = lines;
		this.velocity = velocity;
		
		canInterrupt = true;
		
		textOffsetX = dialogueUI.getWidth();
		textOffsetY = -dialogueUI.getHeight();
		
		Point randomPoint1 = getRandomPoint();
		Point randomPoint2 = getRandomPoint();
		
		x = randomPoint1.x;
		y = randomPoint1.y;
		textX = randomPoint1.x + textOffsetX;
		textY = randomPoint1.y + textOffsetY;
		desiredX = randomPoint2.x;
		desiredY = randomPoint2.y;
		
		moveToDesired();
		
		lineTimer = 0;
		
		running = true;
	}
	
	// SETTERS
	public void allowInterrupt(boolean allow)
	{
		canInterrupt = allow;
	}
	
	// BRAIN METHODS
	private Point getRandomPoint()
	{
		int x = (int) (Math.random()*bounds.width + bounds.x);
		int y = (int) (Math.random()*bounds.height + bounds.y);
		
		return new Point(x, y);
	}
	
	private void moveToDesired()
	{
		double deltaX = desiredX - x;
		double deltaY = desiredY - y;
		
		double magnitude = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
		
		double xVelocity = (double) deltaX/magnitude;
		double yVelocity = (double) deltaY/magnitude;
		
		xVelocity *= velocity;
		yVelocity *= velocity;
		
		x += xVelocity;
		y += yVelocity;
		
		textX = x + textOffsetX;
		textY = y + textOffsetY;
		
		img.setPosition((int) x, (int) y);
		dialogueUI.setPosition((int) textX, (int) textY);
	}
	
	private boolean checkComplete()
	{
		boolean complete = true;
		
		if (Math.abs(x - desiredX) > velocity)
			complete = false;
		if (Math.abs(y - desiredY) > velocity)
			complete = false;
		
		return complete;
	}
	
	public String getRandomDialogue()
	{
		int index = (int) (Math.random() * lines.size());
		
		return lines.get(index);
	}
	
	public void end()
	{
		running = false;
	}

	@Override
	public void run()
	{
		while (running)
		{
			moveToDesired();
			
			if (checkComplete())
			{
				Point randomPoint = getRandomPoint();
				
				desiredX = randomPoint.x;
				desiredY = randomPoint.y;
			}
			
			lineTimer --;
			
			if (lineTimer <= 0)
			{
				dialogueUI.setText(getRandomDialogue());
				lineTimer = (int) (Math.random() * 8) + 47;
			}
			
			try {
				Thread.sleep(33);
			} catch (Exception e) {}
		}
		
	}
}
