package de.stsFanGruppe.tools;

public class TimeFormater
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TimeFormater.class);
	
	public static double stringToDouble(String input) throws NumberFormatException
	{
		// String vorhanden und hat genau ein :
		if(isEmpty(input) || !input.contains(":") || (input.indexOf(':') != input.lastIndexOf(':')))
		{
			log.error("strinToDouble: String leer oder nicht genau ein Doppelpunkt");
			throw new NumberFormatException("Zeit falsch formatiert");
		}
		
		String[] ts = input.split(":");
		double output;
		try
		{
			int stunden = Integer.parseInt(ts[0]);
			int minuten = Integer.parseInt(ts[1]);
			if(stunden < 0 || minuten < 0 || minuten >= 60)
			{
				log.error("strinToDouble: Stunde- oder Minutenwert außerhalb des zulässigen Bereichs");
				throw new NumberFormatException("Zeit falsch formatiert");
			}
			output = (stunden * 60) + minuten;
		}
		catch(NumberFormatException e)
		{
			log.error("strinToDouble: NumberFormatException", e);
			throw new NumberFormatException("Zeit falsch formatiert");
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
}
