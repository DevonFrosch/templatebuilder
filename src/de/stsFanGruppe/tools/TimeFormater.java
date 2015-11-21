package de.stsFanGruppe.tools;

public class TimeFormater
{
	public static double stringToDouble(String input) throws NumberFormatException
	{
		if(isEmpty(input) || !input.contains(":") || (input.indexOf(':') != input.lastIndexOf(':')))
			throw new NumberFormatException("Zeit falsch formatiert.");
		
		String[] ts = input.split(":");
		double output;
		try
		{
			int stunden = Integer.parseInt(ts[0]);
			int minuten = Integer.parseInt(ts[1]);
			if(stunden < 0 || minuten < 0)
			{
				throw new NumberFormatException("Zeit falsch formatiert.");
			}
			output = (stunden * 60) + minuten;
		}
		catch(NumberFormatException e)
		{
			throw new NumberFormatException("Zeit falsch formatiert.");
		}
		return output;
	}
	public static String doubleToString(double input)
	{
		int stunden = ((int) input) / 60;
		int minuten = ((int) input) % 60;
		
		if(minuten < 10)
		{
			return stunden+":0"+minuten;
		}
		return stunden+":"+minuten;
	}
	
	private static boolean isEmpty(String str)
	{
		return str == null || str.isEmpty();
	}
	
	private static void log(String text)
	{
		System.out.println("TimeFormater: "+text);
	}
}
