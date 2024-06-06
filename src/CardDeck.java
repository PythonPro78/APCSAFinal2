public class CardDeck
{
    // CONSTANTS
    public static final int DECK_SIZE = 52;

    // Useless Suit Number
    // 00      01   0110
    // 11 = clubs | 10 = diamonds | 01 = hearts | 00 = spades
    // 13 = King | 12 = Queen | 11 = Jack | 1 = Ace
    private byte[] cards;
    private int cardIndex;

    //CONSTRUCTOR
    CardDeck(int numberOfDecks)
    {
        cards = new byte[numberOfDecks * DECK_SIZE];

        setCards();

        cardIndex = 0;
    }

    CardDeck()
    {
        cards = new byte[DECK_SIZE];

        setCards();

        cardIndex = 0;
    }

    //SETTERS
    public void shuffle()
    {
        int randomIndex;

        // Fisher–Yates shuffle Algorithm
        for (int i = cards.length - 1; i > 0; i --)
        {
            randomIndex = (int) (Math.random()*i);

            byte temp = cards[i];
            cards[i] = cards[randomIndex];
            cards[randomIndex] = temp;
        }

        cardIndex = 0;
    }

    //GETTERS
    // Gets the current card and then moves to the next one
    public byte takeCard(boolean shuffleEnd)
    {
        cardIndex ++;

        if (cardIndex > cards.length)
        {
        	// Shuffle the deck when we reach the end
            if (shuffleEnd) shuffle();

            cardIndex = 1;
        }

        return cards[cardIndex - 1];
    }

    public byte takeCard()
    {
        return takeCard(false);
    }

    public int getCardsLeft()
    {
        return cards.length - cardIndex;
    }

    public int getNumberOfCards()
    {
        return cards.length;
    }

    //BRAIN METHODS
    // Populates the cards array
    private void setCards()
    {
        for (int currentDeck = 0; currentDeck < cards.length; currentDeck += DECK_SIZE)
        {
            for (int suit = 0; suit < 4; suit++)
            {
                for (int value = 0; value < 13; value++)
                {
                    cards[currentDeck + suit*13 + value] = Card.makeCard(suit, value);
                }
            }
        }

    }//END OF SET CARDS

    //TO STRING
    public String toString()
    {
        String output = cards.length + ":";

        for (byte card : cards)
        {
            output += "\n" + Card.cardToString(card);
        }

        return output;
    }

//    public void sort()
//    {
//        cards = Card.sortCards(cards, true);
//    }
}
