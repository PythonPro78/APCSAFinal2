import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Main
{
    /*
     * 0: Highest Card
     * 1: Pair
     * 2: Two Pair
     * 3: Three of a Kind
     * 4: Straight
     * 5: Flush
     * 6: Full House
     * 7: Four of a Kind
     * 8: Straight Flush
     * 9: Royal Flush
     */

    public static final Frame frame = new Frame();

    private static Map<String, Menu> menus = new HashMap<String, Menu>();
    private static Menu activatedMenu;
    
    public static User user;
    public static AIPlayer ai1;
    public static AIPlayer ai2;
    public static AIPlayer ai3;
    public static AIPlayer ai4;
    
    public static ArrayList<String> riouxDialogue;
    
    public static SpriteSheet cardSheet;
    public static Game game;

    public static void main(String[] args)
    {
    	BufferedImage cardSheetImg = null;
    	
    	// PUT STRING INTO FINAL VAR (later)
    	try
    	{
    		cardSheetImg = ImageIO.read(new File("src/CardsSpriteSheet.png"));
    	} catch (IOException e) {System.out.println("You dun goofed the path name");}
    	
    	cardSheet = new SpriteSheet(cardSheetImg, 113, 124);
    	cardSheet.setBorders(1, 1);
    	
        try
        {
            createMenus();
        } catch (Exception e) {System.out.println("Ya dun goofed up:\n" + e);}
        
        try
        {
        	loadTeacherDialogue();
        } catch (Exception e) {System.out.println("Bad stuff happened:\n" + e);}
        
        // BUTTON ACTIONS SETUP
        setButtonClickAction("titleScreen", "startButton", new ClickAction() {
            @Override
            public void action() {
            	setMenu("startScreen");
            }
        });

        setButtonClickAction("startScreen", "playButton", new ClickAction() {
            @Override
            public void action() {
                Main.startGame();
            }
        });

        setButtonClickAction("startScreen", "quitButton", new ClickAction() {
            @Override
            public void action() {
                frame.FRAME.dispatchEvent(new WindowEvent(frame.FRAME, WindowEvent.WINDOW_CLOSING));
            }
        });

        setButtonClickAction("playScreen", "checkButton", new ClickAction() {
            @Override
            public void action() {
            	user.giveInput(new Decision(Move.CHECK, 0));
            }
        });
        
        setButtonClickAction("playScreen", "foldButton", new ClickAction() {
            @Override
            public void action() {
            	user.giveInput(new Decision(Move.FOLD, 0));
            }
        });
        
        setButtonClickAction("playScreen", "raiseButton", new ClickAction() {
            @Override
            public void action() {
            	user.setButtons(false);
            	
            	new Prompt("What do you want the new bet to be?", new PromptEndAction() {
                	@Override
                    public void action(String input) {
                    	try
                    	{
                    		user.giveInput( new Decision(Move.RAISE, Integer.parseInt(input)) );
                    	}
                    	catch (Exception e) {
                    		user.setButtons(true);
                    	}
                    }
                });
            }
        });// These curly braces give me anxiety
        
        setButtonClickAction("playScreen", "betButton", new ClickAction() {
            @Override
            public void action() {
            	user.setButtons(false);
            	
            	new Prompt("How much do you want to bet?", new PromptEndAction() {
                	@Override
                    public void action(String input) {
                    	try
                    	{
                    		user.giveInput( new Decision(Move.BET, Integer.parseInt(input)) );
                    	}
                    	catch (Exception e) {
                    		user.setButtons(true);
                    	}
                    }
                });
            }
        });
        
        setButtonClickAction("playScreen", "callButton", new ClickAction() {
            @Override
            public void action() {
            	user.giveInput(new Decision(Move.CALL, 0));
            }
        });
        
        setButtonClickAction("playScreen", "leaveButton", new ClickAction() {
            @Override
            public void action() {
            	setMenu("startScreen");
            	Main.game.endGame();
            	Main.game = null;
            }
        });
        
        getMenuItem("startScreen", "customizeButton").visible = false;
        getMenuItem("startScreen", "settingsButton").visible = false;
        getMenuItem("startScreen", "shopButton").visible = false;
        
        setMenu("titleScreen");
        
        while(true)
        {
            frame.repaint();
        }
    }
    
    public static void loadTeacherDialogue() throws IOException
    {
    	riouxDialogue = new ArrayList<String>();
    	
    	BufferedReader br = new BufferedReader(new FileReader("src/RiouxDialogue.txt"));
    	
    	int numLines = Integer.parseInt(br.readLine());
    	
    	for (int i = 0; i < numLines; i ++)
    	{
    		riouxDialogue.add(br.readLine());
    	}
    	
    	br.close();
    }

    private static void setButtonClickAction(String menuId, String buttonId, ClickAction action)
    {
        ((Button) menus.get(menuId).getItem(buttonId)).setOnClick(action);
    }

    private static void createMenus() throws NumberFormatException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader("src/Menus.txt"));

        int numMenus = Integer.parseInt(br.readLine());

        for (int i = 0; i < numMenus; i ++)
        {
            Menu currentMenu = new Menu();
            menus.put(br.readLine(), currentMenu);

            int numItems = Integer.parseInt(br.readLine());

            for (int j = 0; j < numItems; j ++)
            {
                String currentItem = br.readLine();

                if (currentItem.substring(0, 3).equals("btn"))
                {
                    currentMenu.addItem(makeButton(currentItem));
                }
                else if (currentItem.substring(0, 3).equals("lbl"))
                {
                    currentMenu.addItem(makeLabel(currentItem));
                }
                else if (currentItem.substring(0, 3).equals("img"))
                {
                    currentMenu.addItem(makeImage(currentItem));
                }

            }//END OF ITEMS LOOPS
        }//END OF MENUS LOOP

        br.close();
    }

    private static Button makeButton(String item)
    {
        int i = 0;
        int index = 5;
        String[] stats = new String[10];

        //btn: id, text, x, y, width, height, font, size, color, highlight
        while (index < item.length())
        {
            stats[i] = item.substring(index, item.indexOf(',', index));

            index = item.indexOf(',', index) + 2;
            i ++;
        }

        Font font = new Font(stats[6], 1, Integer.parseInt(stats[7]));
        return new Button(stats[0], stats[1], Integer.parseInt(stats[2]), Integer.parseInt(stats[3]),
                Integer.parseInt(stats[4]), Integer.parseInt(stats[5]), font,
                getRGB(stats[8]), getRGB(stats[9]), frame.buttonManager);
    }

    private static Label makeLabel(String item)
    {
        int i = 0;
        int index = 5;
        String[] stats = new String[9];

        //btn: id, text, x, y, width, height, font, size, color
        while (index < item.length())
        {
            stats[i] = item.substring(index, item.indexOf(',', index));

            index = item.indexOf(',', index) + 2;
            i ++;
        }

        Font font = new Font(stats[6], 1, Integer.parseInt(stats[7]));
        return new Label(stats[0], stats[1], Integer.parseInt(stats[2]), Integer.parseInt(stats[3]),
                Integer.parseInt(stats[4]), Integer.parseInt(stats[5]), font,
                getRGB(stats[8]));
    }

    private static Image makeImage(String item) throws IOException
    {
        int i = 0;
        int index = 5;
        String[] stats = new String[6];

        //btn: id, imageUrl, x, y, width, height
        while (index < item.length())
        {
            stats[i] = item.substring(index, item.indexOf(',', index));

            index = item.indexOf(',', index) + 2;
            i ++;
        }
        
        BufferedImage img;
        
        if (stats[1].equals("null"))
        	img = null;
        else
        	img = ImageIO.read(new File(stats[1]));
        
        return new Image(stats[0], img, Integer.parseInt(stats[2]), Integer.parseInt(stats[3]),
                Integer.parseInt(stats[4]), Integer.parseInt(stats[5]));
    }

    public static Color getRGB(String str)
    {
    	if (str.equals("transparent"))
    		return new Color(0, 0, 0, 0);
    	
        // Each number is separated by a space
        int r = Integer.parseInt(str.substring(0, 3)); // First 3 chars
        int g = Integer.parseInt(str.substring(4, 7)); // Second 3 chars
        int b = Integer.parseInt(str.substring(8, 11)); // Third 3 chars

        return new Color(r, g, b);
    }
    
    private static void startGame()
    {
    	// Make sure the buttons are deactivated before we do our stuff
    	frame.repaint();
    	
    	ArrayList<Player> players = new ArrayList<Player>();
    	game = new Game(players);
    	
    	user = new User("You");
        players.add(user);
        
        // ----------------DELETE ME----------------
        ai1 = new AIPlayer(game, 60, 50, 50, 30, 0.2, 0, "Ahmad");
        players.add(ai1);
        ai2 = new AIPlayer(game, 65, 50, 60, 30, 0.18, 0, "Tyler");
        players.add(ai2);
        ai3 = new AIPlayer(game, 30, 80, 30, 50, 0.14, 0, "Joseph");
        players.add(ai3);
        ai4 = new AIPlayer(game, 40, 100, 45, 40, 0.15, 0, "Josh");
        players.add(ai4);
        ai4 = new AIPlayer(game, 60, 98, 40, 40, 0.125, 0, "Zigaozi");
        players.add(ai4);
        // ----------------DELETE ME----------------
    	
        Thread thread = new Thread(game);
        thread.start();
    }

    public static void setMenu(String id)
    {
        activatedMenu = menus.get(id);
    }
    
    public static Menu getMenu()
    {
        return activatedMenu;
    }
    
    public static MenuItem getMenuItem(String menuId, String itemID)
    {
        return menus.get(menuId).getItem(itemID);
    }
}