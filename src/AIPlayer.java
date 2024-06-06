public class AIPlayer extends Player
{
	// Gives each rank a weight (0-100) depending on their odds of occurring
	public final static double[] RANK_WEIGHTS =
		{
			99.999846,
			99.99861,
			99.97599,
			99.8559,
			99.8035,
			99.6075,
			97.8872,
			95.2461,
			57.7431,
			49.8823
		};
	
	// All measures are between 0-100
	private int riskTaking;
	private int extremeRiskTaking;
	private int pennyPincher;
	int spendingAmount;
	double betMultiplier;
	int randomness;

	Game game;
	
	public AIPlayer(Game game, int riskTaking, int extremeRiskTaking, int pennyPincher, int spendingAmount, double betMultiplier, int randomness, String name)
    {
		super(name);
		
        this.riskTaking = riskTaking;
        this.extremeRiskTaking = extremeRiskTaking;
        this.pennyPincher = pennyPincher;
        this.spendingAmount = spendingAmount;
        this.betMultiplier = betMultiplier;

        this.randomness = randomness;

        this.game = game;
    }

   public AIPlayer(Game game, int riskTaking, int extremeRiskTaking, int pennyPincher, int spendingAmount, double betMultiplier, int randomness)
   {
       this.riskTaking = riskTaking;
       this.extremeRiskTaking = extremeRiskTaking;
       this.pennyPincher = pennyPincher;
       this.spendingAmount = spendingAmount;
       this.betMultiplier = betMultiplier;

       this.randomness = randomness;

       this.game = game;
   }

   public Decision makeDecision()
   {
       int weGotThat = measureWeGotThat();
       int willingToSpend = (int) ( money * (spendingAmount/100.0) );

       int change = game.getBet() - currentBet;

       if (riskTaking > weGotThat)
       {
           // I chose three because I wanted to, deal with it
           if (change <= 3)
               return new Decision(Move.CHECK, 0);

           return new Decision(Move.FOLD, 0);
       }
       else
       {
           if (willingToSpend < change && extremeRiskTaking < weGotThat)
               return new Decision(Move.FOLD, 0);

           if (pennyPincher < weGotThat)
               return makeBet(willingToSpend, weGotThat);
           else
               return new Decision(Move.CALL, 0);
       }
   }
   
   private Decision makeBet(int willingToSpend, int weGotThat)
   {
	   int bettingAmount = (int) (willingToSpend * betMultiplier * weGotThat/100.0);
	   
	   if (bettingAmount > Main.game.getBet()   &&   bettingAmount + getBet() <= getMoney())
		   return new Decision(Move.BET, bettingAmount);
	   
	   return new Decision(Move.CALL, 0);
   }

   private int measureWeGotThat()
   {
       byte[] everyCard = getEveryCard();
       int[] rankFrequencies = new int[10];

       // The eight wonder of the world
       // The great pyramid of painful debugging
       for (int a = 0; a < everyCard.length; a += 2)//The 2 doubles the speed and halves the pain
       {
           byte[] possibleCombo = new byte[5];

           possibleCombo[0] = everyCard[a];
           if (game.numCommunity() >= 1)
               possibleCombo[0] = game.getCommunity(0);

           for (int b = a + 1; b < everyCard.length; b += 2)//The 2 doubles the speed and halves the pain
           {
               possibleCombo[1] = everyCard[b];
               if (game.numCommunity() >= 2)
                   possibleCombo[1] = game.getCommunity(1);

               for (int c = b + 1; c < everyCard.length; c ++)
               {
                   possibleCombo[2] = everyCard[c];
                   if (game.numCommunity() >= 3)
                       possibleCombo[2] = game.getCommunity(2);

                   for (int d = c + 1; d < everyCard.length; d ++)
                   {
                       possibleCombo[3] = everyCard[d];
                       if (game.numCommunity() >= 4)
                           possibleCombo[3] = game.getCommunity(3);

                       for (int e = d + 1; e < everyCard.length; e ++)
                       {
                           possibleCombo[4] = everyCard[e];
                           if (game.numCommunity() >= 5)
                               possibleCombo[4] = game.getCommunity(4);

                           int possibleRank = Poker.getRank( Poker.getBestPossibleHand(possibleCombo, this.cards) );
                           rankFrequencies[9 - possibleRank] ++; // getRank() returns values between 0-9

                           if (game.numCommunity() >= 5)
                               break;
                       }

                       if (game.numCommunity() >= 4)
                           break;
                   }

                   if (game.numCommunity() >= 3)
                       break;
               }

               if (game.numCommunity() >= 2)
                   break;
           }

           if (game.numCommunity() >= 1)
               break;
       }
       
       double sum = 0;
       int total = 0;
       for (int i = 0; i < rankFrequencies.length; i ++)
       {
    	   sum += rankFrequencies[i] * RANK_WEIGHTS[i];
    	   total += rankFrequencies[i];
       }
       
       return (int) (sum/total);
   }


   private byte[] getEveryCard()
   {
       byte[] allCards = new byte[52];

       for (int suit = 0; suit < 4; suit ++)
           for (int value = 0; value < 13; value ++)
               allCards[suit * 13 + value] = Card.makeCard(suit, value);

       return allCards;
   }
}
