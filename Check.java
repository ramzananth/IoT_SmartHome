package com.rest.client;


public class Check {
	public static boolean isNumber(String k)
	{
		try{
			int ki=Integer.parseInt(k);
			return true;
		}
		catch(NumberFormatException nfe)
		{
			System.out.println("You didn't enter a numerical value. \nPlease restart the system and enter a numerical value !!");
			System.exit(0);
			return false;
		}
		
	}
	public static void stopTheProgram()
	{
		System.out.println("You didn't choose YES to continue !!\nGoodbye !!");
	}
}
