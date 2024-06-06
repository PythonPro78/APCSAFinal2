enum Move
{
	FOLD,
	CHECK,
	BET,
	CALL,
	RAISE
}

public class Decision
{
	//INSTANCE VARIABLES
	public Move move;
	public int amount;
	
	//CONSTRUCTORS
	public Decision(Move move, int amount)
	{
		this.move = move;
		this.amount = amount;
	}
	
	public Decision(Move move)
	{
		this.move = move;
		amount = 0;
	}
	
	//TO STRING
	public String toString()
	{
		if (amount == 0)
			return move + "";
		else
			return move + " " + amount;
	}
}
