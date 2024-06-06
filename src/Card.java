import java.awt.*;
import java.awt.image.BufferedImage;

public class Card
{
    private static final Dimension CARD_RATIO = new Dimension(7, 9);
    
    public static final String[] SUITS = {"Clubs", "Diamonds", "Hearts", "Spades"};
    public static final char[] VALUES = {'A', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'J', 'Q', 'K'};
    // Useless Suit Number
    // 00      01   0110
    // 00 = clubs | 01 = diamonds | 10 = hearts | 11 = spades
    // 13 = King | 12 = Queen | 11 = Jack | 1 = Ace

    //BRAIN METHODS
    public static byte makeCard(String suit, char value)
    {
        return makeCard(stringToSuit(suit), charToVal(value));
    }
    public static byte makeCard(int suit, char value)
    {
        return makeCard(suit, charToVal(value));
    }
    public static byte makeCard(String suit, int value)
    {
        return makeCard(stringToSuit(suit), value);
    }
    public static byte makeCard(int suit, int value)
    {
        return (byte) ((suit << 4) | value);
    }



    public static byte getSuit(byte card)
    {
        return (byte) ((card>>4) & 3);
    }

    public static byte getValue(byte card, boolean aceBest)
    {
        // If the ace is best, the ace is 12 and everything else moves down one
        if (aceBest)
        {
            if ((card & 15) == 0) return 12;

            return (byte) ((card & 15) - 1);
        }
        // Otherwise, keep everything the same
        else
        {
            return getValue(card);
        }
    }
    public static byte getValue(byte card)
    {
        return (byte) (card & 15);
    }

    public static String cardToString(byte card)
    {
        return SUITS[getSuit(card)] + " " + VALUES[getValue(card)];
    }

    public static byte[] sortCards(byte[] cards, boolean aceBest)
    {
        byte[] sortedArr = new byte[cards.length];

        // Insertion sort
        for (int i = 0; i < cards.length; i ++)
        {
            sortedArr[i] = cards[i];

            for (int j = i; j >= 0; j --)
            {
                if (j == 0 || getValue(sortedArr[j], aceBest) >= getValue(sortedArr[j - 1], aceBest))
                {
                    break;
                }

                byte temp = sortedArr[j];
                sortedArr[j] = sortedArr[j - 1];
                sortedArr[j - 1] = temp;
            }
        }

        return sortedArr;
    }

    public static byte[] sortCards(byte[] cards)
    {
        return sortCards(cards, false);
    }

    public static void drawCard(BufferedImage card, int x, int y, double size, Graphics2D g2D)
    {
    	g2D.drawImage(card,
    				  (int) (x * MenuItem.scale() + MenuItem.screenX()),
    				  (int) (y * MenuItem.scale() + MenuItem.screenY()), 
    				  (int) (card.getWidth() * size * MenuItem.scale()),
    				  (int) (card.getHeight() * size * MenuItem.scale()),
    				  Main.frame);
    }
    
    public static int cardToTile(byte card, SpriteSheet sheet)
    {
    	return getValue(card)%sheet.getSheetSize().width +
    		   getSuit(card)*sheet.getSheetSize().width;
    }

    //PRIVATE BRAIN METHODS
    // Converts a string to an integer representing it's suit
    // Clubs = 0   Diamonds = 1   Hearts = 2   Spades = 3
    private static int stringToSuit(String str)
    {
        for (int i = 0; i < SUITS.length; i ++)
        {
            if (str.equals(SUITS[i])) return i;
        }

        return -1;
    }

    // Converts a char to an integer representing it's value
    // A=0   2=1   3=2   8=7   0=9   J=10   Q=11   K=12
    private static int charToVal(char ch)
    {
        for (int i = 0; i < VALUES.length; i ++)
        {
            if (ch == VALUES[i]) return i;
        }

        return -1;
    }
}