import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

interface PromptEndAction
{
	public void action(String input);
}


public class Prompt extends JPanel implements KeyListener, MouseListener, Runnable
{
	private static final long serialVersionUID = -6228282266500953977L;
	private static final Font FONT = new Font("Arial", 0, 20);
	
	public final JFrame FRAME;
	private Button submitButton;
	private ButtonManager buttonManager;
	
	private String prompt;
	private Thread thread;
	
	private String input;

	public Prompt(String prompt, PromptEndAction onClick)
	{
		this.prompt = prompt;
		input = "";
		
		buttonManager = new ButtonManager();
		submitButton = new Button("submitBtn", "Done", 340, 165, 140, 40, FONT, Color.WHITE, Color.LIGHT_GRAY, buttonManager);
		submitButton.setUseScale(false);
		
		submitButton.setActive(true);
		
		submitButton.setOnClick(new ClickAction() {
			@Override
			public void action() {
				onClick.action(input);
				
				FRAME.dispose();
			}
		});
		
		FRAME = new JFrame();

		FRAME.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		FRAME.setVisible(true);
		FRAME.setResizable(false);
		FRAME.setSize(500, 250);
		FRAME.setName("Prompt Window");

		FRAME.add(this);

		FRAME.addKeyListener(this);
		FRAME.addMouseListener(this);
		
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		
		buttonManager.mouseOver(getMousePos().x, getMousePos().y);
		
		g2D.setFont(FONT);
		
		g2D.drawString(prompt, 20, 35);
		
		g2D.drawRect(20, 120, 460, 30);
		g2D.drawString(input, 23, 143);
		
		submitButton.draw(g2D);
	}
	
	public Point getMousePos()
	{
		Point mouse = MouseInfo.getPointerInfo().getLocation();

		mouse.x -= getLocationOnScreen().x;
		mouse.y -= getLocationOnScreen().y;

		return mouse;
	}
	
	public String getInput()
	{
		return input;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if ((int) e.getKeyChar() < 255 && (int) e.getKeyChar() != 8 && (int) e.getKeyChar() != 10)
			input += e.getKeyChar();
		
		// Backspace
		if (e.getKeyCode() == 8 && input.length() > 0)
		{
			input = input.substring(0, input.length() - 1);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		buttonManager.click(getMousePos().x, getMousePos().y);
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}


	
	@Override
	public void run()
	{
		while (FRAME.isVisible())
		{
			repaint();
		}
	}

}
