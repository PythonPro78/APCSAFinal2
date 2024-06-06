import java.util.ArrayList;

public class Poker
{
	public static final int HAND_SIZE = 5;
	public static final int COMMUNITY_SIZE = 5;
	public static final int PLAYER_HAND_SIZE = 2;
	
	public static final int[][] ALL_COMMUNITY_COMBOS = {
			{0, 1, 2},
			{0, 1, 3},
			{0, 1, 4},
			{0, 2, 3},
			{0, 2, 4},
			{0, 3, 4},
			
			{1, 2, 3},
			{1, 2, 4},
			{1, 3, 4},
			
			{2, 3, 4}
	};
	
	public static final String[] RANKS = {
			"Highest Card",
		    "Pair",
		    "Two Pair",
		    "Three of a Kind",
		    "Straight",
		    "Flush",
		    "Full House",
		    "Four of a Kind",
		    "Straight Flush",
		    "Royal Flush"
	};
	
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
	
	public static byte[] getBestPossibleHand(byte[] community, byte[] playerHand)
	{
		// I don't want to make a separate final for every number
		byte[][] allPossibleHands = new byte[ALL_COMMUNITY_COMBOS.length][];
		
		for (int i = 0; i < ALL_COMMUNITY_COMBOS.length; i ++)
		{
			byte[] currentHand = {
						community[ ALL_COMMUNITY_COMBOS[i][0] ],
						community[ ALL_COMMUNITY_COMBOS[i][1] ],
						community[ ALL_COMMUNITY_COMBOS[i][2] ],
						playerHand[0],
						playerHand[1]
				};
			
			allPossibleHands[i] = currentHand;
		}
		
		return getBestHand(allPossibleHands);
	}
	
	public static byte[] getBestHand(byte[][] hands)
	{
		return hands[getBestHandIndex(hands)];
	}
	
	public static int getBestHandIndex(byte[][] hands)
	{
		byte[] bestHand = hands[0];
		int bestHandIndex = 0;

		int highestRank = 0;
		if (hands[0] != null)
			highestRank = getRank(hands[0]);
		
		for (int i = 1; i < hands.length; i ++) 
		{
			if (hands[i] == null)
				continue;
			
			int currentRank = getRank(hands[i]);
			
			if (currentRank > highestRank)
			{
				bestHand = hands[i];
				bestHandIndex = i;
				
				highestRank = currentRank;
			}
			else if (highestRank == currentRank)
			{
				if (bestHand == null)
				{
					bestHand = hands[i];
					bestHandIndex = i;
					
					continue;
				}
				
				int tie =  tieBreaker(bestHand, hands[i]);
				
				// If the challenger won
				if (tie == 1)
				{
					bestHand = hands[i];
					bestHandIndex = i;
				}
			}// END OF ELSE IF
		}// END OF FOR LOOP

		return bestHandIndex;
	}
	
	// 0 represents hand1, 1 represents hand2
	private static int tieBreaker(byte[] hand1, byte[] hand2)
	{
		// I love naming variables
		byte[] hand1Two = Card.sortCards(hand1, true);
		byte[] hand2Two = Card.sortCards(hand2, true);
		
		for (int i = 0; i < hand1Two.length; i ++)
		{
			byte value1 = Card.getValue(hand1Two[0], true);
			byte value2 = Card.getValue(hand2Two[0], true);
			
			if (value1 > value2)
				return 0;
			else if (value2 > value1)
				return 1;
		}
		
		// Each hand tied perfectly 💀
		return 0;
	}
	
	// Returns the value of a hand (Highest card always returns 0)
	public static int getRank(byte[] cards)
	{		
		if (royalFlush(cards)) return 9;
		else if (straightFlush(cards)) return 8;
		else if (fourOfAKind(cards)) return 7;
		else if (fullHouse(cards)) return 6;
		else if (flush(cards)) return 5;
		else if (straight(cards)) return 4;
		else if (threeOfAKind(cards)) return 3;
		else if (twoPair(cards)) return 2;
		else if (pair(cards)) return 1;
		else return 0;
	}

    public static boolean royalFlush(byte[] cards)
    {
        cards = Card.sortCards(cards, true);

        // If it is flush, the first card is a ten, and the last card is an ace
        return flush(cards) && Card.getValue(cards[0]) == 9 && Card.getValue(cards[4]) == 0;
    }

    public static boolean straightFlush(byte[] cards)
    {
        return flush(cards) && straight(cards);
    }
    
    public static boolean fourOfAKind(byte[] cards)
    {
    	return ofAKind(cards)[0] >= 4;
    }
    
    public static boolean fullHouse(byte[] cards)
    {
    	// Easiest way to check if it's [2, 3] or [3, 2]
    	return ofAKind(cards)[0] + ofAKind(cards)[1] == 5;
    }

    public static boolean flush(byte[] cards)
    {
        byte suit = Card.getSuit(cards[0]);

        for (byte card : cards)
        {
            if (Card.getSuit(card) != suit) return false;
        }

        return true;
    }

    public static boolean straight(byte[] cards)
    {
        boolean aceLeastCheck = true;
        boolean aceMostCheck = true;

        cards = Card.sortCards(cards, false);

        // Checks if the difference between the current card and the previous card is NOT one
        for (int i = 1; i < cards.length; i ++)
        {
            if (Card.getValue(cards[i], false) - Card.getValue(cards[i-1], false) != 1)
            {
                aceLeastCheck = false;
                break;
            }
        }

        // Same thing but checks with the ace being the highest
        cards = Card.sortCards(cards, true);
        for (int i = 1; i < cards.length; i ++)
        {
            if (Card.getValue(cards[i], true) - Card.getValue(cards[i-1], true) != 1)
            {
                aceMostCheck = false;
                break;
            }
        }

        // Returns if the cards are in order (the ace can be either first or last)
        return aceMostCheck || aceLeastCheck;
    }
    
    public static boolean threeOfAKind(byte[] cards)
    {
    		return ofAKind(cards)[0] == 3;
    }
    
    public static boolean twoPair(byte[] cards)
    {
    		return ofAKind(cards)[0] == 2 && ofAKind(cards)[1] == 2;
    }
    
    public static boolean pair(byte[] cards)
    {
    		return ofAKind(cards)[0] == 2;
    }
    
    public static int highCard(byte[] cards)
    {
	    	int max = 0;
    	
	    	for (byte b : cards)
	    	{
	    		if (Card.getValue(b, true) > max)
	    		{
	    			max = Card.getValue(b, true);
	    		}
	    	}
    	
	    	return max;
    }
    
    // Returns an array containing the length of a pairs/ofAKinds
    private static int[] ofAKind(byte[] cards)
    {
        int[] kinds = new int[2];
        int firstKindValue = -1;

        cards = Card.sortCards(cards);

        for (int i = 1; i < cards.length; i ++)
        {
        		// If this card's value is the same as the last...
            if (Card.getValue(cards[i]) == Card.getValue(cards[i - 1]))
            {
            		// If this is the first match we found, set the first index to 2 (pair = 2)
                if (firstKindValue == -1)
                {
                    firstKindValue = Card.getValue(cards[i]);
                    kinds[0] = 2;
                }
                // If this matches the first set of cards we found, add one to that set
                else if (Card.getValue(cards[i]) == firstKindValue)
                {
                    kinds[0] ++;
                }
                // Otherwise, it must be a different set, so add one to the second index.
                else
                {
                    if (kinds[1] == 0) kinds[1] = 1;
                    kinds[1] ++;
                }
            }

        }//END OF FOR LOOP

        return kinds;
    }
}
