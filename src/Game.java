import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Game implements Runnable
{
	public static final int BUY_IN = 500;
	public static final int BIG_BLIND = BUY_IN/100; // 5
	public static final int SMALL_BLIND = BIG_BLIND/2; // 2
	public static final int MINIMUM_BET = BIG_BLIND; // 5
	public static final int MAX_PLAYERS = 6;
	
	public static final String PLAY_MENU_ID = "playScreen";
	public static final String PLAYER_IMAGE_ID = "player%d";
	public static final String PLAYER_NAME_ID = "player%dName";
	public static final String PLAYER_BET_ID = "player%dBet";
	public static final String USER_CARD1_ID = "userCard1";
	public static final String USER_CARD2_ID = "userCard2";
	public static final String USER_MONEY_ID = "userMoney";
	public static final String USER_BET_ID = "userBet";
	public static final String FLOP_CARD1_ID = "flopCard1";
	public static final String FLOP_CARD2_ID = "flopCard2";
	public static final String FLOP_CARD3_ID = "flopCard3";
	public static final String TURN_CARD_ID = "turnCard";
	public static final String RIVER_CARD_ID = "riverCard";
	public static final String PLAYER_HIGHLIGHT_ID = "playerHighlight";
	public static final String PLAYER_SPEECH_ID = "playerSpeech";
	public static final String END_BACKGROUND_ID = "endBackgroud";
	public static final String WINNING_HAND_ID = "winningHand";
	public static final String WINNING_NAME_ID = "winningName";
	public static final String WINNING_AVATAR_ID = "winningAvatar";
	public static final String END_CARD_ID = "endCard%d";
	public static final String CASH_OUT_LABEL_ID = "cashOut";
	public static final String RIOUX_IMAGE_ID = "riouxImg";
	public static final String RIOUX_DIALOGUE_ID = "riouxDialogue";
	
	private static final int PLAYER_SPEECH_OFFSET_X = -27;
	private static final int PLAYER_SPEECH_OFFSET_Y = -15;
	
	private Teacher rioux;
	private Thread teacherThread;
	
    private byte[] community;
    private CardDeck deck;
    private ArrayList<Player> players;
    private int round;
    private int pot;
    private int dealer;
    private int bet;
    private int numFold;
    
    private int numCommunityCards;

    public Game(ArrayList<Player> players)
    {
        this.players = (ArrayList<Player>) players;

        community = new byte[5];
        round = 0;
        deck = new CardDeck();
        pot = 0;
        dealer = 0;
        bet = 0;
        numFold = 0;
        numCommunityCards = 0;
        
        deck.shuffle();
        
        rioux = new Teacher((Image) Main.getMenuItem(PLAY_MENU_ID, RIOUX_IMAGE_ID),
        					(Label) Main.getMenuItem(PLAY_MENU_ID, RIOUX_DIALOGUE_ID),
        					new Rectangle(50, 50, 550, 100),
        					Main.riouxDialogue,
        					0.4);
        teacherThread = new Thread(rioux);
        teacherThread.start();
    }
    
    public int getBet()
    {
    	return bet;
    }
    
    public byte getCommunity(int index)
    {
    	return community[index];
    }
    
    public int numCommunity()
    {
    	return numCommunityCards;
    }
    
    private int loopPlayers(int index)
    {
    	return index % players.size();
    }
    
    private void playRound() throws InterruptedException
    {
    	Main.setMenu(PLAY_MENU_ID);
    	
    	displayEndUI(false);
    	displayCashOut(false);
    	
    	updateUI();
    	
    	// Set blinds
    	pot += players.get(loopPlayers(dealer + 1)).takeMoney(SMALL_BLIND);
    	pot += players.get(loopPlayers(dealer + 2)).takeMoney(BIG_BLIND);
    	bet = BIG_BLIND;
    	updateUI();
    	
    	// Give each player two cards
    	giveCards();
    	giveCards();
    	updateUI();
    	
    	doBets(loopPlayers(dealer + 3));
    	
    	// Flop
    	community[0] = deck.takeCard(true);
    	community[1] = deck.takeCard(true);
    	community[2] = deck.takeCard(true);
    	numCommunityCards = 3;
    	updateUI();
    	
    	doBets(loopPlayers(dealer + 1));
    	
    	// Second last community card
    	community[3] = deck.takeCard(true);
    	numCommunityCards = 4;
    	updateUI();
    	
    	doBets(loopPlayers(dealer + 1));
    	
    	// Last community card
    	community[4] = deck.takeCard(true);
    	numCommunityCards = 5;
    	updateUI();
    	
    	// Last round of betting
    	doBets(loopPlayers(dealer + 1));
    	
    	// Give money to the winner
    	Player winner = getWinner();
    	
    	winner.giveMoney(pot);
    	
    	displayEndUI(true);
    	updateEndUI(winner);
        
		Thread.sleep(5999);
		
		displayCashOut(true);
		
		Thread.sleep(2500);
    	
    	reset();
    	dealer ++;
    }
    
    private Player getWinner()
    {
    	byte[][] hands = new byte[players.size()][];
    	
    	for (int i = 0; i < players.size(); i ++)
    	{
    		if (players.get(i).didFold)
    			continue;
    			
    		hands[i] = Poker.getBestPossibleHand( community, players.get(i).getCards() );
    	}
    	
    	int bestIndex = Poker.getBestHandIndex(hands);
    	
    	return players.get(bestIndex);
    } 
    
    private void reset()
    {
    	for (Player p : players)
    	{
    		p.didFold = false;
    		p.resetBet();
    		p.resetCards();
    	}
    	
    	community = new byte[5];
    	round = 0;
    	pot = 0;
    	bet = 0;
    	numCommunityCards = 0;
    	numFold = 0;
    	
    	deck.shuffle();
    }
    
    private void giveCards()
    {
    	for (Player p : players)
    	{
    		p.giveCard(deck.takeCard(true));
    	}
    }
    
    private void doBets(int startIndex)
    {
    	int index = startIndex;
    	boolean allDone = false;
    	
    	Label speech = (Label) Main.getMenuItem(PLAY_MENU_ID, PLAYER_SPEECH_ID);
		Label highlight = (Label) Main.getMenuItem(PLAY_MENU_ID, PLAYER_HIGHLIGHT_ID);
    	
    	while (!allDone || players.get(index).getBet() < bet)
    	{
    		if (numFold >= players.size() - 1)
    			break;
    		
    		Player current = players.get(index);
    		Image playerImg = (Image) Main.getMenuItem(PLAY_MENU_ID, String.format(PLAYER_IMAGE_ID, index + 1));
    		
    		speech.visible = false;
    		
    		if (current.didFold)
    		{
    			index = loopPlayers(index + 1);
    			
    			if (index == startIndex) {allDone = true;}
    			
    			continue;
    		}
    		
    		highlight.x = playerImg.x;
    		highlight.y = playerImg.y;
    		
    		Decision decision = null;
    		Boolean isValid = false;
    		
    		while (!isValid)
    		{
    			decision = current.makeDecision();
    			isValid = performMove(current, decision);
    		}
    		
    		speech.setText(decision + "");
    		speech.x = playerImg.x + PLAYER_SPEECH_OFFSET_X;
    		speech.y = playerImg.y + PLAYER_SPEECH_OFFSET_Y;
    		speech.visible = true;
    		
    		index = loopPlayers(index + 1);
    		
    		updateUI();
    		
    		try
    		{
    			Thread.sleep(1000);
    		} catch (Exception e) {System.out.println("lol");}
    		
    		if (index == startIndex) {allDone = true;}
    	}
    }
    
    public boolean performMove(Player current, Decision decision)
    {
    	switch (decision.move)
		{
			case FOLD:
				current.didFold = true;
				current.resetBet();
				numFold ++;
				break;
			case BET:
			case RAISE:
				if (decision.amount <= bet)
					return false;
				else if (decision.amount > current.getMoney() + current.getBet())
					return false;
				
				bet = decision.amount;
			case CALL:
			case CHECK:
				if (bet > current.getBet())
					pot += current.takeMoney(bet - current.getBet());
			
				break;
			default:
				System.out.println("Bro what?");
				return false;
		}
    	
    	return true;
    }
    
    private void updateUI()
    {
    	// Individual players
    	for (int i = 0; i < players.size(); i ++)
    	{
    		Player currentPlayer = players.get(i);
    		
    		if (i >= MAX_PLAYERS)
    		{
    			System.err.println("Too many players! Max = " + MAX_PLAYERS);
    			break;
    		}
    		
    		Label nameUI = (Label) Main.getMenuItem(PLAY_MENU_ID, String.format(PLAYER_NAME_ID, i + 1));
    		Label betUI = (Label) Main.getMenuItem(PLAY_MENU_ID, String.format(PLAYER_BET_ID, i + 1));
    		
    		nameUI.setText(currentPlayer.name);
    		betUI.setText("$" + currentPlayer.getBet());
    		
    		if (currentPlayer instanceof User) 
    			updateUserUI((User) currentPlayer);
    	}
    	
    	// Everything else
    	Image flop1UI = (Image) Main.getMenuItem(PLAY_MENU_ID, FLOP_CARD1_ID);
    	Image flop2UI = (Image) Main.getMenuItem(PLAY_MENU_ID, FLOP_CARD2_ID);
    	Image flop3UI = (Image) Main.getMenuItem(PLAY_MENU_ID, FLOP_CARD3_ID);
    	Image turnUI = (Image) Main.getMenuItem(PLAY_MENU_ID, TURN_CARD_ID);
    	Image riverUI = (Image) Main.getMenuItem(PLAY_MENU_ID, RIVER_CARD_ID);
    	
    	BufferedImage flop1Img = null;
    	BufferedImage flop2Img = null;
    	BufferedImage flop3Img = null;
    	BufferedImage turnImg = null;
    	BufferedImage riverImg = null;
    	
    	if (numCommunityCards >= 3)
    	{
    		flop1Img = Main.cardSheet.getTile(Card.cardToTile(community[0], Main.cardSheet));
    		flop2Img = Main.cardSheet.getTile(Card.cardToTile(community[1], Main.cardSheet));
    		flop3Img = Main.cardSheet.getTile(Card.cardToTile(community[2], Main.cardSheet));
    	}
    	if (numCommunityCards >= 4)
    	{
    		turnImg = Main.cardSheet.getTile(Card.cardToTile(community[3], Main.cardSheet));
    	}
    	if (numCommunityCards >= 5)
    	{
    		riverImg = Main.cardSheet.getTile(Card.cardToTile(community[4], Main.cardSheet));
    	}
    	
    	flop1UI.setImage(flop1Img);
    	flop2UI.setImage(flop2Img);
    	flop3UI.setImage(flop3Img);
    	turnUI.setImage(turnImg);
    	riverUI.setImage(riverImg);
    }
    
    private void updateUserUI(User user)
    {
		BufferedImage card1Img = Main.cardSheet.getTile(Card.cardToTile(user.getCard(0), Main.cardSheet));
		BufferedImage card2Img = Main.cardSheet.getTile(Card.cardToTile(user.getCard(1), Main.cardSheet));
		
		Image card1UI = (Image) Main.getMenuItem(PLAY_MENU_ID, USER_CARD1_ID);
		Image card2UI = (Image) Main.getMenuItem(PLAY_MENU_ID, USER_CARD2_ID);
		
		card1UI.setImage(card1Img);
		card2UI.setImage(card2Img);
		
		
		Label userMoneyUI = (Label) Main.getMenuItem(PLAY_MENU_ID, USER_MONEY_ID);
		Label userBetUI = (Label) Main.getMenuItem(PLAY_MENU_ID, USER_BET_ID);
		
		userMoneyUI.setText("$" + user.getMoney());
		userBetUI.setText("$" + user.getBet());
    }
    
    private void displayEndUI(boolean show)
    {
    	Main.getMenuItem(PLAY_MENU_ID, END_BACKGROUND_ID).visible = show;
    	Main.getMenuItem(PLAY_MENU_ID, WINNING_HAND_ID).visible = show;
    	Main.getMenuItem(PLAY_MENU_ID, WINNING_NAME_ID).visible = show;
    	Main.getMenuItem(PLAY_MENU_ID, WINNING_AVATAR_ID).visible = show;
    	
    	for (int i = 1; i <= Poker.HAND_SIZE; i ++)
    		Main.getMenuItem(PLAY_MENU_ID, String.format(END_CARD_ID, i)).visible = show;
    }
    
    private void displayCashOut(boolean show)
    {
    	Main.getMenuItem(PLAY_MENU_ID, CASH_OUT_LABEL_ID).visible = show;
    }
    
    private void updateEndUI(Player winner)
    {
    	byte[] winningHand = Poker.getBestPossibleHand(community, winner.getCards());
    	
    	Label winningHandUI = (Label) Main.getMenuItem(PLAY_MENU_ID, WINNING_HAND_ID);
    	Label winningNameUI = (Label) Main.getMenuItem(PLAY_MENU_ID, WINNING_NAME_ID);
    	Image winningAvatarUI = (Image) Main.getMenuItem(PLAY_MENU_ID, WINNING_AVATAR_ID);
    	Label cashOutUI = (Label) Main.getMenuItem(PLAY_MENU_ID, CASH_OUT_LABEL_ID);
    	
    	winningHandUI.setText( Poker.RANKS[Poker.getRank(winningHand)] );
    	winningNameUI.setText(winner.name);
    	winningAvatarUI.setImage(null);
    	cashOutUI.setText("$" + pot);
    	
    	for (int i = 1; i <= Poker.HAND_SIZE; i ++)
    	{
    		BufferedImage endCardImg = Main.cardSheet.getTile(Card.cardToTile(winningHand[i-1], Main.cardSheet));
    		Image endCardUI = (Image) Main.getMenuItem(PLAY_MENU_ID, String.format(END_CARD_ID, i));
    		
    		endCardUI.setImage(endCardImg);
    	}
    }
    
    public void endGame()
    {
    	players.clear();
    	rioux.end();
    }

	@Override
	public void run()
	{
		while (players.size() > 0)
		{
			try {
				playRound();
			} catch (Exception e) {System.out.println("Oof");}
		}
	}
}
