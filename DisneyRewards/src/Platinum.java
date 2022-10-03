//Abe Hamed
//azh2ujl

public class Platinum extends Customer
{
	private int bonusBucks;
	
	//Constructor
	public Platinum()
	{
		super();
		bonusBucks = 0;
	}
	
	//Overloaded Constructor
	public Platinum(String guestIdent, String first, String last, float spent, int bucks)
	{
		super(guestIdent, first, last, spent);
		bonusBucks = bucks;
	}
	
	//Accessors*****
	public int getBonusBucks()
	{
		return bonusBucks;
	}
	
	//Mutators*****
	public void setBonusBucks(int x)
	{
		bonusBucks = x;
	}
	
	public String toString()
	{
		return  super.toString() + " " + bonusBucks;
	}

}
