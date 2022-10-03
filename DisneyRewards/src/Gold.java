//Abe Hamed
//azh2ujl

public class Gold extends Customer
{
	private int discountPercentage;
	
	//Constructor
	public Gold()
	{
		super();
		discountPercentage = 0;
	}
	
	//Overloaded Constructor
	public Gold(String guestID, String first, String last, float spent, int disPer)
	{
		super(guestID, first, last, spent);
		discountPercentage = disPer;
	}
	
	//Accessors*****
	public int getDiscountPercentage()
	{
		return discountPercentage;
	}
	
	//Mutators*****
	public void setDiscountPercentage(int x)
	{
		discountPercentage = x;
	}
	
	public String toString()
	{
		return  super.toString() + " " + discountPercentage + "%";
	}

}
