public class Player
{
    public static final int HAND_SIZE = 5;
    public static final int MAX_CARDS = 2;

    public String name;
//    private Image avatar;
    protected byte[] cards;
    protected int money;
    protected int currentBet;

    private int index;
    
    public boolean didFold;

    //CONSTRUCTORS
    public Player(String name)
    {
        this.name = name;

        cards = new byte[MAX_CARDS];
        index = 0;
        
        money = Game.BUY_IN;
        currentBet = 0;
        didFold = false;
    }

    public Player()
    {
        name = "Unnamed";

        cards = new byte[MAX_CARDS];
        index = 0;
        
        money = Game.BUY_IN;
        currentBet = 0;
        didFold = false;
    }

    //SETTERS
    public void giveCard(byte card)
    {
        if (index >= MAX_CARDS)
        {
            System.out.println("Hand is full");
            return;
        }

        cards[index] = card;

        index ++;
    }
    
    public void resetCards()
    {
    	cards = new byte[MAX_CARDS];
    	index = 0;
    }
    
    public void resetBet()
    {
    	currentBet = 0;
    }
    
    public int takeMoney(int amount)
    {
    	money -= amount;
    	currentBet += amount;
    	
    	return amount;
    }
    
    public int giveMoney(int amount)
    {
    	money += amount;
    	
    	return amount;
    }

    //GETTERS
    public byte getCard(int cardIndex)
    {
        return cards[cardIndex];
    }
    
    public byte[] getCards()
    {
        return cards.clone();
    }
    
    public int getNumCards()
    {
        return index;
    }
    
    public int getMoney()
    {
    	return money;
    }
    
    public int getBet()
    {
    	return currentBet;
    }

    // IDK what this is, there is a better version in the Poker class
    //
//    public byte[] getBestHand(byte[] community)
//    {
//        // Twenty is the maximum possible number of combinations if there are 5 community cards
//        byte[][] possibleHands = new byte[10][HAND_SIZE];
//        int handIndex = 0;
//
//        // For loop mountain
//        for (int firstCard = 0; firstCard < community.length - 2; firstCard ++)
//        {
//            for (int secondCard = firstCard + 1; secondCard < community.length - 1; secondCard ++)
//            {
//                for (int thirdCard = secondCard + 1; thirdCard < community.length; thirdCard ++)
//                {
//                	// Populate the possible hands array with all of the possible hand (should iterate ten times)
//                    possibleHands[handIndex][0] = cards[0];
//                    possibleHands[handIndex][1] = cards[1];
//
//                    possibleHands[handIndex][2] = community[firstCard];
//                    possibleHands[handIndex][3] = community[secondCard];
//                    possibleHands[handIndex][4] = community[thirdCard];
//
//                    handIndex ++;
//                }
//            }
//        }
//
//        return possibleHands[Poker.getBestHand(possibleHands)[0]];
//    }
    
    //BRAIN METHODS
    public Decision makeDecision()
    {
    	int random = (int) (Math.random() * 5);
    	Move move;
    	
    	switch (random)
    	{
    		case 0 -> move = Move.FOLD;
    		case 1 -> move = Move.CHECK;
    		case 2 -> move = Move.BET;
    		case 3 -> move = Move.CALL;
    		default -> move = Move.RAISE;
    	}
    	
    	return new Decision(move);
    }
    
    //TO STRING
    public String toString()
    {
    	return name + ":\n" +
    		   Card.cardToString(cards[0]) + "\n" +
    		   Card.cardToString(cards[1]) + "\n" +
    		   "Bet: " + currentBet + "\n" +
    		   "Cash money: " + money + "\n" +
    		   "Did fold: " + didFold;
    }
}
