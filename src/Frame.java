import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JPanel implements KeyListener, MouseListener
{
	private static final long serialVersionUID = 3518972463523316825L;

	public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	public final JFrame FRAME;

	private Dimension size;
	private Rectangle MenuSize;
	
	public ButtonManager buttonManager;
	
	private Menu prevMenu;
	private boolean keys[];

	public Frame()
	{
		FRAME = new JFrame();

		FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FRAME.setVisible(true);
		FRAME.setSize(SCREEN_SIZE.width*3/4, SCREEN_SIZE.height*3/4);

		size = FRAME.getSize();
		MenuSize = MenuItem.updateScale(getWidth(), getHeight());

		FRAME.add(this);

		FRAME.addMouseListener(this);
		FRAME.addKeyListener(this);
		
		buttonManager = new ButtonManager();
		prevMenu = null;
		keys = new boolean[256];
	}

	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;

		g2D.setColor(Color.BLACK);
		g2D.fillRect(0, 0, getWidth(), getHeight());

		updateNewSize();

		g2D.setColor(Color.WHITE);
		g2D.fill(MenuSize);

		if (Main.getMenu() != null)
		{
			if (prevMenu != Main.getMenu())
			{
				if (prevMenu != null)
					prevMenu.deactivate();
				
				prevMenu = Main.getMenu();
				
				Main.getMenu().activate();
			}
			
			buttonManager.mouseOver(getMousePos().x, getMousePos().y);
			Main.getMenu().draw(g2D);
		}
		
		if (keys[92])
		{
			g2D.setColor(Color.BLACK);
			g2D.setFont(new Font("Arial", 1, 20));
			g2D.drawString("This is a bug", 80, 80);
		}
	}

	public Point getMousePos()
	{
		Point mouse = MouseInfo.getPointerInfo().getLocation();

		mouse.x -= getLocationOnScreen().x;
		mouse.y -= getLocationOnScreen().y;

		return mouse;
	}

	public void updateNewSize()
	{
		if (size.getWidth() != getWidth() || size.getHeight() != getHeight())
		{
			size = getSize();
			MenuSize = MenuItem.updateScale(getWidth(), getHeight());
		}
	}

	// KEY LISTENER STUFF
	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() >= 256)
			return;
		
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() >= 256)
			return;
		
		keys[e.getKeyCode()] = false;
	}


	// MOUSE LISTENER STUFF
	@Override
	public void mouseClicked(MouseEvent e)
	{
		buttonManager.click(getMousePos().x, getMousePos().y);
	}

	@Override
	public void mousePressed(MouseEvent e){}

	@Override
	public void mouseReleased(MouseEvent e){}

	@Override
	public void mouseEntered(MouseEvent e){}

	@Override
	public void mouseExited(MouseEvent e){}
}