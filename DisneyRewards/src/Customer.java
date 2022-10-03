//Abe Hamed
//azh2ujl

public class Customer 
{
	private String guestID;
	private String firstName;
	private String lastName;
	private float amountSpent;
	
	//Constructor
	public Customer()
	{
		firstName = "";
		lastName = "";
		guestID = "";
		amountSpent = 0;
	}
	
	//Overloaded Constructor 
	public Customer(String guestIdent, String first, String last, float amount)
	{
		guestID = guestIdent;
		firstName = first;
		lastName = last;
		amountSpent = amount;
	}

	//Accessors*********
	public String getFirstName()

	{
		return firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public String getGuestID()
	{
		return guestID;
	}
	
	public float getAmountSpent()
	{
		return amountSpent;
	}
	
	//Mutators************
	public void setFirstName(String x)
	{
		firstName = x;
	}
	
	public void setLastName(String x)
	{
		lastName = x;
	}
	
	public void setGuestID(String x)
	{
		guestID = x;
	}
	
	public void setAmountSpent(float x)
	{
		amountSpent = x;
	}
	
	public String toString()
	{
		return guestID + " " + firstName + " " + lastName + " " + String.format("%.2f", amountSpent);
	}
	
	public boolean equals(Customer c)
	{
		if (this.guestID == c.guestID)
		{
			return true;
		}
		
		return false;
	}
	
	//interface for mutators/accessors of Gold and Platinum
	
//	public int getDiscountPercentage()
//	{
//		return 0;
//	}
//	public void setDiscountPercentage(int x)
//	{
//	}
//	public int getBonusBucks()
//	{
//		return 0;
//	}
//	public void setBonusBucks(int x)
//	{
//	}
}



















