package de.stsFanGruppe.tools;

import java.util.OptionalDouble;

public class TimeFormater
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TimeFormater.class);
	
	public static OptionalDouble stringToOptionalDouble(String input) throws NumberFormatException
	{
		if(isEmpty(input))
		{
			return OptionalDouble.empty();
		}
		
		// String hat genau ein :
		if(!input.contains(":") || (input.indexOf(':') != input.lastIndexOf(':')))
		{
			log.error("strinToDouble: Zahl hat nicht genau ein Doppelpunkt");
			throw new NumberFormatException("Zeit falsch formatiert");
		}
		
		String[] ts = input.split(":");
		try
		{
			int stunden = Integer.parseInt(ts[0]);
			int minuten = Integer.parseInt(ts[1]);
			if(stunden < 0 || minuten < 0 || minuten >= 60)
			{
				log.error("strinToDouble: Stunde- oder Minutenwert auﬂerhalb des zul‰ssigen Bereichs");
				throw new NumberFormatException("Zeit falsch formatiert");
			}
			return OptionalDouble.of((stunden * 60) + minuten);
		}
		catch(NumberFormatException e)
		{
			log.error("strinToDouble: NumberFormatException", e);
			throw new NumberFormatException("Zeit falsch formatiert");
		}
	}
	public static double stringToDouble(String input) throws NumberFormatException
	{
		OptionalDouble output = stringToOptionalDouble(input);
		if(output.isPresent())
		{
			return output.getAsDouble();
		}
		
		log.error("strinToDouble: String ist leer");
		throw new NumberFormatException("Zeit falsch formatiert");
	}
	public static String optionalDoubleToString(OptionalDouble input)
	{
		if(input.isPresent())
		{
			return doubleToString(input.getAsDouble());
		}
		return "";
	}
	public static String doubleToString(double input)
	{
		int stunden = ((int) input) / 60;
		
		return stunden+":"+doubleToMinute(input);
	}
	
	public static String doubleToMinute(double input)
	{
		int minuten = ((int) input) % 60;
		
		if(minuten < 10)
		{
			return "0" + minuten;
		}
		return String.valueOf(minuten);
	}
	
	private static boolean isEmpty(String str)
	{
		return str == null || str.isEmpty();
	}
}
