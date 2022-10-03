//Abe Hamed
//azh2ujl

import java.util.Scanner;
import java.io.*;

public class Main 
{
	public static void main(String[] args) throws IOException
	{
		Scanner kbd = new Scanner(System.in);
	
		//Creating the regularCustomers array
		Customer[] regularCustomers;
		try
		{
			regularCustomers = customerArrayInitializer(kbd);
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Invalid customer file.");
			return;
		}
		
		//preferredCustomers will ALWAYS be empty until someone is added to it. It will always match the size of the customers inside of it. 
		Customer[] preferredCustomers;
		
		//Creating the preferredCustomers array
		while (true)
		{
			try 
			{
				preferredCustomers = preferredArrayInitializer(kbd);
				break;
			}
			catch (FileNotFoundException e)
			{
				System.out.println("Preferred File Not Found");
				preferredCustomers = new Customer[0];
				break;
			}
		}
		

		//Declaring this here, so that the Scanner does not go back to start every single time the method orderTaker is called. I want it to only read a new line when called. 
		System.out.println("Enter orders file: ");
		File orders; //TODO: MAKE THIS kbd.nextLine() instead
		Scanner orderFile;
		
		try {
			orders = new File(kbd.nextLine());
			orderFile = new Scanner(orders);
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Invalid orders file.");
			return; 
		}
		
		
		while (orderFile.hasNext())
		{
			
			char regOrGoldOrPlat = 'a';
			int indexOfOrderer = 0;
			String[] customerAndRawTotal = orderTaker(orderFile, regularCustomers, preferredCustomers);
			if (customerAndRawTotal == null)
				continue;
			
			//searching for the equivalent customer from the regular array, based on the guestID received from orderFile.
	        for (int i = 0; i < regularCustomers.length; i++)
	        {
	        	if (customerAndRawTotal[0].equals(regularCustomers[i].getGuestID()))
	        	{
	        		//so, the order is created by a regular customer. 
	        		regOrGoldOrPlat = 'r';
	        		indexOfOrderer = i;
	        		break;
	        	}

	        }
	        
	        //searching for the equivalent customer from the preferred Array, based on the guestID received from orderFile.
	        for (int i = 0; i < preferredCustomers.length; i++)
	        {
	        	//checking if the polymorphism is gold or platiunum based on the percent sign. 
	        	if (customerAndRawTotal[0].equals(preferredCustomers[i].getGuestID()) && (((preferredCustomers[i].toString()).charAt((preferredCustomers[i].toString()).length() - 1) == '%')))
	        	{
	        		//so, the order is created by a gold customer. 
	        		regOrGoldOrPlat = 'g';
	        		indexOfOrderer = i;
	        	}
	        	if (customerAndRawTotal[0].equals(preferredCustomers[i].getGuestID()) && (((preferredCustomers[i].toString()).charAt((preferredCustomers[i].toString()).length() - 1)) != '%'))
	        	{
	        		//so, the order is created by a platinum customer. 
	        		regOrGoldOrPlat = 'p';
	        		indexOfOrderer = i;
	        	}
	        	
	        	
	        }
	        //Now the searching is done. We'll move on to discounting the raw totals, and changing customers from reg to plat/gold (if needed).
	        
	        //For all cases that start with a regular customer
	        if (regOrGoldOrPlat == 'r')
	        {
	        	double discPercent = 0;
	        	double rawTotalPlusInitial = regularCustomers[indexOfOrderer].getAmountSpent() + Double.valueOf(customerAndRawTotal[1]);
	        	
	        	//CASE 1: going from < 50 ..to.. >= 50 and < 100 (REGULAR TO GOLD)
	        	if ((regularCustomers[indexOfOrderer].getAmountSpent() < 50) && rawTotalPlusInitial >= 50.0 && rawTotalPlusInitial < 200.0)
	        	{
	        		//System.out.println("CASE 1");
	        		if (rawTotalPlusInitial >= 50.0 && rawTotalPlusInitial < 100.0)
	        		{
	        			discPercent = .05;
	        		}
	        		if (rawTotalPlusInitial >= 100.0 && rawTotalPlusInitial < 150.0)
	        		{
	        			discPercent = .10;
	        		}
	        		if (rawTotalPlusInitial >= 150.0 && rawTotalPlusInitial < 200.0)
	        		{
	        			discPercent = .15;
	        		}
	        		regularCustomers[indexOfOrderer].setAmountSpent(Float.valueOf(customerAndRawTotal[1]) * ((float) (1 - discPercent))  + regularCustomers[indexOfOrderer].getAmountSpent());
	        		
	        		preferredCustomers = addCustomer(preferredCustomers, regularCustomers[indexOfOrderer], 'g'); 
	        		regularCustomers = removeCustomer(regularCustomers, preferredCustomers[preferredCustomers.length - 1]);
	        		((Gold) preferredCustomers[preferredCustomers.length - 1]).setDiscountPercentage((int) (discPercent * 100));
	        	}
	        	
	        	//CASE 2: going from < 50 ..to.. < 50 (REGULAR TO REGULAR)
	        	else if (regularCustomers[indexOfOrderer].getAmountSpent() < 50 && rawTotalPlusInitial < 50)
	        	{
	        		//System.out.println("CASE 2");
	        		regularCustomers[indexOfOrderer].setAmountSpent((float) rawTotalPlusInitial);
	        	}
	        	
	        	//CASE 3: going from < 50 ..to.. >= 200 (REGULAR TO PLATINUM) *with exception
	        	else if (regularCustomers[indexOfOrderer].getAmountSpent() < 50 && rawTotalPlusInitial >= 200)
	        	{
	        		//System.out.println("CASE 3");
	        		//Calculating initial discount
	        		regularCustomers[indexOfOrderer].setAmountSpent(Float.valueOf(customerAndRawTotal[1]) * .85F + regularCustomers[indexOfOrderer].getAmountSpent());
	        		
	        		//*will be put back into Gold with the 15% discount if the discount put them below 200. 
	        		if (regularCustomers[indexOfOrderer].getAmountSpent() >= 200)
	        		{
	        			preferredCustomers = addCustomer(preferredCustomers, regularCustomers[indexOfOrderer], 'p'); 
		        		regularCustomers = removeCustomer(regularCustomers, preferredCustomers[preferredCustomers.length - 1]);
		        		
		        		((Platinum) preferredCustomers[preferredCustomers.length - 1]).setBonusBucks((int) ((preferredCustomers[preferredCustomers.length - 1].getAmountSpent() - 200F) / 5));
	        		}
	        		else
	        		{
	        			preferredCustomers = addCustomer(preferredCustomers, regularCustomers[indexOfOrderer], 'g'); 
		        		regularCustomers = removeCustomer(regularCustomers, preferredCustomers[preferredCustomers.length - 1]);
		        		
		        		((Gold) preferredCustomers[preferredCustomers.length - 1]).setDiscountPercentage(15);
	        		}	
	        		
	        	}
	
	        }
	        //For all cases that start with an existing gold customer.
	        else if (regOrGoldOrPlat == 'g')
	        {
	        	//CASE 4: going from >= 50 and < 200 ..to.. >= 50 and < 200 (GOLD TO GOLD)
	        	
        		float fullTotalWithDiscount = Float.valueOf(customerAndRawTotal[1]) * ((float) (1 - (.01 * ((Gold) preferredCustomers[indexOfOrderer]).getDiscountPercentage()))) + preferredCustomers[indexOfOrderer].getAmountSpent();

        		if (fullTotalWithDiscount <= 100)
        		{
        			//System.out.println("CASE 4");
        			preferredCustomers[indexOfOrderer].setAmountSpent(Float.valueOf(customerAndRawTotal[1]) * ((float) (1 - (.01 * 5))) + preferredCustomers[indexOfOrderer].getAmountSpent());
        		}
        		else if (fullTotalWithDiscount >= 100 && fullTotalWithDiscount < 150)
	        	{
        			//System.out.println("CASE 4");
        			preferredCustomers[indexOfOrderer].setAmountSpent(Float.valueOf(customerAndRawTotal[1]) * ((float) (1 - (.01 * 10))) + preferredCustomers[indexOfOrderer].getAmountSpent());
        			((Gold) preferredCustomers[indexOfOrderer]).setDiscountPercentage(10);
	        	}
        		else if (fullTotalWithDiscount >= 150 && fullTotalWithDiscount < 200)
	        	{
        			//System.out.println("CASE 4");
        			preferredCustomers[indexOfOrderer].setAmountSpent(Float.valueOf(customerAndRawTotal[1]) * ((float) (1 - (.01 * 15))) + preferredCustomers[indexOfOrderer].getAmountSpent());
        			((Gold) preferredCustomers[indexOfOrderer]).setDiscountPercentage(15);
	        	}
	        	
        		//CASE 5: going from >= 50 and < 200 ..to.. > 200 (GOLD TO PLATINUM)
        		if (fullTotalWithDiscount >= 200)
        		{
        			
        			preferredCustomers[indexOfOrderer].setAmountSpent(Float.valueOf(customerAndRawTotal[1]) * ((float) (1 - (.01 * 15))) + preferredCustomers[indexOfOrderer].getAmountSpent());
                    
        			if (preferredCustomers[indexOfOrderer].getAmountSpent() >= 200)
					{
						//TODO: do I gotta keep the customers spot in the array
        				//System.out.println("CASE 5");
        				Customer placeholder = preferredCustomers[indexOfOrderer];
        				preferredCustomers[indexOfOrderer] = new Platinum(placeholder.getGuestID(),placeholder.getFirstName(), placeholder.getLastName(), placeholder.getAmountSpent(), 0);
     
        				((Platinum) preferredCustomers[indexOfOrderer]).setBonusBucks((int) ((preferredCustomers[indexOfOrderer].getAmountSpent() - 200F) / 5));
					}
        			//Back to case 4. If 15% discount applies and it gets them back out of 200+ range, they are not platinum. 
        			else
        			{
        				//System.out.println("CASE 5.4");
            			 ((Gold) preferredCustomers[indexOfOrderer]).setDiscountPercentage(15);
        			}
        		}
	        }
	        
	       //For all cases that begin as Platinum. 
	        else if (regOrGoldOrPlat == 'p')
	        {
	        	 //CASE 6: going from >= 200 ..to.. >= 200 (PLATINUM TO PLATINUM)
	        	//System.out.println("CASE 6");
	        	double valueOfCustomerAndRawTotal1 = Double.valueOf(customerAndRawTotal[1]);
	        	int usableBonusBucks = (int) (valueOfCustomerAndRawTotal1);
	        	
	        	//Even if a customer orders for $2.01, we will still use 3 bonus bucks, not two. This is the nature of the program. 
	        	if ((Float.valueOf(customerAndRawTotal[1])  % 1) != 0)
	        	{
	        		usableBonusBucks++;
	        	}
	            //This is the amount of bonus bucks that WILL be used. It is the smallest of either the amount the customer has, or the possible amount that can be used. 
	        	int realBonusBucks = Math.min(((Platinum) preferredCustomers[indexOfOrderer]).getBonusBucks(), usableBonusBucks);
	        	
	            float thisOrderMinusBonusBucks = Float.valueOf(customerAndRawTotal[1]) - realBonusBucks;
	        	//making sure we don't have a negative order. 
	        	if (thisOrderMinusBonusBucks < 0)
	        	{
	        		thisOrderMinusBonusBucks = 0;
	        	}
	        	preferredCustomers[indexOfOrderer].setAmountSpent(thisOrderMinusBonusBucks + preferredCustomers[indexOfOrderer].getAmountSpent());
	        	((Platinum) preferredCustomers[indexOfOrderer]).setBonusBucks(((Platinum) preferredCustomers[indexOfOrderer]).getBonusBucks() - realBonusBucks);
	        	
	        	//Now, to calculate the earned bonus bucks from this purchase. 
	        	((Platinum) preferredCustomers[indexOfOrderer]).setBonusBucks((int) (thisOrderMinusBonusBucks / 5) + ((Platinum) preferredCustomers[indexOfOrderer]).getBonusBucks());
	        }
	        
	        printResultsFile(regularCustomers, preferredCustomers);
	        
		}
		orderFile.close();
	}
	

	/**
	 * 
	 * @param orderFile - Scanner for the file with orders in it
	 * @return String[] - Array with 2 spots; first index is the guestID of the customer. Second index is the raw amount spent without discounts, on this order. 
	 */
	public static String[] orderTaker(Scanner oFile, Customer[] regularArray, Customer[] preferredArray)
	{
		final float sodaPerOunce = .2F;
		final float teaPerOunce = .12F;
		final float fruitPerOunce = .15F;
		
		final float diameterS = 4.0F; final float diameterM = 4.5F; final float diameterL = 5.5F;
		final float heightS = 4.5F;   final float heightM = 5.75F;  final float heightL = 7.0F;
		final float ouncesS = 12.0F;  final float ouncesM = 20.0F;  final float ouncesL = 32.0F;
		//END CONSTANTS (Users, edit the above when changing your prices or your cup dimensions!)
		
		double surfaceAreaS = diameterS * heightS * Math.PI;
		double surfaceAreaM = diameterM * heightM * Math.PI;
		double surfaceAreaL = diameterL * heightL * Math.PI;
		
		String[] customerAndTotal = new String[2];
		float total = 0F;
		boolean erroneous = false;
		
		String currentLine = oFile.nextLine();
		//INPUT VALIDATION:
		erroneous = orderLineErrorChecker(currentLine, regularArray, preferredArray);
		
		if (erroneous)
			return null;
		
		String[] brokenLine = currentLine.split(" ");
		
		customerAndTotal[0] = brokenLine[0];
		
		//brokenLine[0] is the guest ID. 
		//brokenLine[1] is the size of the drink, either S, M, or L.
		//brokenLine[2] is the type of drink. Soda, tea, or punch.
		//brokenLine[3] is the cost of graphic.
		//brokenLine[4] is the number of drinks. 
		//They are all strings! So they will be converted using Double.valueOf() if needed. 
		
		//The next statement will calculate the total based on which parameters were in the order file. 
		double chosenOunces = 0;
		double chosenSurfaceArea = 0;
		double chosenPerOunce = 0;
		
		//checking if Small, Medium, or Large.
		switch (brokenLine[1].charAt(0)) 
		{	
			case 'S': 
				chosenOunces = ouncesS;
				chosenSurfaceArea = surfaceAreaS;		
				break;
			case 'M':
				chosenOunces = ouncesM;
				chosenSurfaceArea = surfaceAreaM;		
				break;
			case 'L':
				chosenOunces = ouncesL;
				chosenSurfaceArea = surfaceAreaL;		
				break;
		}
		
		//Checking if soda, tea, or punch.
		switch (brokenLine[2])
		{
			case "soda": chosenPerOunce = sodaPerOunce; break;
			case "tea": chosenPerOunce = teaPerOunce; break;
			case "punch": chosenPerOunce = fruitPerOunce; break;
		}
		
		
		total = (float) (Double.valueOf(brokenLine[4]) * ((Double.valueOf(brokenLine[3]) * chosenSurfaceArea) + (chosenPerOunce * chosenOunces)));
	
		
		customerAndTotal[1] = "" + total;
		
		
		return customerAndTotal;
		
		
	}
	
	//CHANGE THIS BEFORE DUE
	public static boolean orderLineErrorChecker(String line, Customer[] regularArray, Customer[] preferredArray)
	{
		String[] brokenLine = line.split(" ");
		boolean matchingID = false;
		boolean correctSize = false;
		boolean correctType = false;
		
		//This part makes sure there are exactly 5 fields
		if (brokenLine.length != 5)
			return true;
		
		//This part checks for garbage characters
		try 
		{
			Integer.valueOf(brokenLine[0]);
			brokenLine[1].charAt(0);
			Double.valueOf(brokenLine[3]);
			Integer.valueOf(brokenLine[4]);
		}
		catch (Exception e)
		{
			return true;
		}
		
		//This section ensures that all orders have known guest ID's 
		for (int i = 0; i < regularArray.length; i++)
		{
			if (brokenLine[0].equals(regularArray[i].getGuestID()))
				matchingID = true;
		}
		for (int i = 0; i < preferredArray.length; i++)
		{
			if (brokenLine[0].equals(preferredArray[i].getGuestID()))
				matchingID = true;
		}
		
		if (!matchingID) 
			return true; 

		//checks if the M or S or L field has only one character.
		if (brokenLine[1].length() != 1)
			return true;
		
		//This part checks if the line has either of the 3 sizes only. 
		if (brokenLine[1].charAt(0) == 'S')
			correctSize = true;
		else if (brokenLine[1].charAt(0) == 'M')
			correctSize = true;
		else if(brokenLine[1].charAt(0) == 'L')
			correctSize = true;
		
		//This part checks that the line has any of the 3 drink types only.
		if(brokenLine[2].equals("soda"))
			correctType = true;
		else if(brokenLine[2].equals("tea"))
			correctType = true;
		else if(brokenLine[2].equals("punch"))
			correctType = true;
		
		if (!correctSize || !correctType)
			return true;
	
		return false;
		
	}
	public static void printResultsFile(Customer[] regularArray, Customer[] preferredArray) throws IOException
	{
		PrintWriter outFileReg = new PrintWriter("customer.dat");
		PrintWriter outFilePref = new PrintWriter("preferred.dat");
		
		for (Customer variable: regularArray)
		{
			outFileReg.println(variable);
		}
		for (Customer variable: preferredArray)
		{
			outFilePref.println(variable);
		}
		
		outFileReg.close();
		outFilePref.close();
		
	}
	/**
	 * 
	 * @param any array
	 * @param Customer object c
	 * @param a character that designates whether that new customer will be platiunum or gold. If 'p', then platinum. If 'g', then gold.
	 * @return an array the new customer added onto the end of it 
	 */
	public static Customer[] addCustomer(Customer[] arr, Customer c, char choice)
	{
		Customer[] new_array = new Customer[arr.length + 1];
		
		for (int i = 0; i < arr.length; i++)
		{
			new_array[i] = arr[i];
		}
		if (choice == 'p')
		{
			new_array[new_array.length - 1] = new Platinum(c.getGuestID(), c.getFirstName(), c.getLastName(), c.getAmountSpent(), 0);
		}
		else if (choice == 'g')
		{
			new_array[new_array.length - 1] = new Gold(c.getGuestID(), c.getFirstName(), c.getLastName(), c.getAmountSpent(), 0);
		}
		return new_array;
	}
	
	/**
	 * 
	 * @param any array
	 * @param the customer that you want to get rid of 
	 * @return an array with a 1 lower size. The method will find the spot with your designated customer and boot them. 
	 */
	public static Customer[] removeCustomer(Customer[] arr, Customer c)
	{
		int newArrayIndex = 0;
		Customer[] new_array = new Customer[arr.length - 1];
		
		for (int i = 0; i < arr.length; i++)
		{
			if (!arr[i].equals(c))
			{
				new_array[newArrayIndex] = arr[i];
				newArrayIndex++;
			}
		}
		return new_array;
	}

	/**
	 * @param keyboard
	 * @return returns a newly created array for a regular customer file.
	 */
	public static Customer[] customerArrayInitializer(Scanner keyboard) throws IOException
	{
		String guestID = "";
		String firstName = "";
		String lastName = "";
		float amountSpent = 0;
		int rowRegCounter = 0;
		int inLineWordCounter = 0;
		
		//Reading in all of the user's File names
		System.out.print("Enter regular customer file: ");
		String regName = keyboard.nextLine(); //TODO: make this Scanner based
		File regFile = new File(regName);
		Scanner regular = new Scanner(regFile); System.out.println();
				
		//Second Scanner object is to preemptively determine the number of rows in a file, to make a correct array
		File rowRegFile = new File(regName);
		Scanner rowRegChecker = new Scanner(rowRegFile);
		
		
		//basically, Im just counting 4 words in the line and calling that as one row. 
		
		while (rowRegChecker.hasNext())
		{
			rowRegChecker.next();
			inLineWordCounter++;
		}
		
		rowRegCounter = inLineWordCounter / 4; //there are 4 words always in one line 
		
		Customer[] customers = new Customer[rowRegCounter]; //now this will not make an overisized array. Yay!
		
		
		while (regular.hasNext())
		{
			for (int i = 0; i < rowRegCounter; i++)
			{
				 guestID = regular.next(); 
				 firstName = regular.next();
				 lastName = regular.next();
				 amountSpent = regular.nextFloat();
				 
				 customers[i] = new Customer(guestID, firstName, lastName, amountSpent);
			}
		}
		regular.close();
		rowRegChecker.close();
		
		return customers;
	}

	/**
	 * @param keyboard
	 * @return returns a newly created array for a preferred customer file.
	 */
	public static Customer[] preferredArrayInitializer(Scanner keyboard) throws IOException
	{
		
		System.out.print("Enter preferred customer file: ");
		String prefName = keyboard.nextLine();
		File prefFile = new File(prefName); //TODO: make this Scanner based 
		Scanner pref = new Scanner(prefFile); System.out.println();
		
		
		//Second Scanner object is to preemptively determine the number of rows in a file, to make a correct array
		File rowPrefFile = new File(prefName);
		Scanner rowPrefChecker = new Scanner(rowPrefFile);
		
		int rowPrefCounter = 0;
		int inLineWordCounter = 0;
		
		//basically, Im just counting 4 words in the line and calling that as one row. 
		
		while (rowPrefChecker.hasNext())
		{
			rowPrefChecker.next();
			inLineWordCounter++;
		}
		
		rowPrefCounter = inLineWordCounter / 5; //there are 5 words always in one line 
	
		Customer[] customers = new Customer[rowPrefCounter]; //now this will not make an oversized array. Yay!
		
		String guestID = "";
		String firstName = "";
		String lastName = "";
		String percentRemoverTemp = "";
		boolean isGold = false;
		float amountSpent = 0;
		int undetermined = 0;
		
		//Reading in each segment of the file. 
		while (pref.hasNext())
		{
			for (int i = 0; i < rowPrefCounter; i++)
			{
				 guestID = pref.next();
				 firstName = pref.next();
				 lastName = pref.next();
				 amountSpent = pref.nextFloat();
				 percentRemoverTemp = pref.next();
				 
				 //the file is going to have a percent sign if the customer is gold. We want to remove that. 
				 if (percentRemoverTemp.substring(percentRemoverTemp.length() - 1, percentRemoverTemp.length()).equals("%"))
				 {
					 isGold = true;
					 undetermined = Integer.valueOf(percentRemoverTemp.substring(0, percentRemoverTemp.length() - 1));
				 }
				 //if the file doesn't have a percent sign, then we can just call that last int "bonusBucks".
				 else
				 {
					 isGold = false;
					 undetermined = Integer.valueOf(percentRemoverTemp);
				 }
				 
				
				 
				 //if line had a percent sign, the customer will be initialized to the Platinum subclass.
				 //otherwise, the customer will be placed into the gold subclass. Both will be in same array.
				 if (!isGold)
				 {
					 customers[i] = new Platinum(guestID, firstName, lastName, amountSpent, undetermined);
				 }
				 else
				 {
				 	customers[i] = new Gold(guestID, firstName, lastName, amountSpent, undetermined);
				 }
			}
		}
		
		pref.close();
		rowPrefChecker.close();
		
		return customers;
	}


}

























