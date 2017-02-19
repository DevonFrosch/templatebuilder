package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum FahrtFilter
{
	BEGINNT_MIT("beginnt mit") {
		public boolean testZugname(String zugname, String muster)
		{
			return zugname.startsWith(muster);
		}
	},
	BEGINNT_NICHT_MIT("beginnt nicht mit") {
		public boolean testZugname(String zugname, String muster)
		{
			return !zugname.startsWith(muster);
		}
	},
	ENTHAELT("enthält") {
		public boolean testZugname(String zugname, String muster)
		{
			return zugname.contains(muster);
		}
	},
	ENTHAELT_NICHT("enthält nicht") {
		public boolean testZugname(String zugname, String muster)
		{
			return !zugname.contains(muster);
		}
	},
	GLEICH("ist gleich") {
		public boolean testZugname(String zugname, String muster)
		{
			return zugname.equals(muster);
		}
	},
	ENDET_MIT("endet mit") {
		public boolean testZugname(String zugname, String muster)
		{
			return zugname.endsWith(muster);
		}
	},
	ENDET_NICHT_MIT("endet nicht mit") {
		public boolean testZugname(String zugname, String muster)
		{
			return !zugname.endsWith(muster);
		}
	},
	GROESSER_ALS("Zugnummer größer als") {
		public boolean testZugname(String zugname, String muster)
		{
			try
			{
				return getZugnummer(zugname) > Integer.parseInt(muster);
			}
			catch(NumberFormatException|NullPointerException e)
			{
				return false;
			}
		}
	},
	KLEINER_ALS("Zugnummer kleiner als") {
		public boolean testZugname(String zugname, String muster)
		{
			try
			{
				return getZugnummer(zugname) < Integer.parseInt(muster);
			}
			catch(NumberFormatException|NullPointerException e)
			{
				return false;
			}
		}
	},
	GERADE("Zugnummer gerade") {
		public boolean testZugname(String zugname, String muster)
		{
			try
			{
				return getZugnummer(zugname) % 2 == 0;
			}
			catch(NumberFormatException|NullPointerException e)
			{
				return false;
			}
		}
	},
	UNGERADE("Zugnummer ungerade") {
		public boolean testZugname(String zugname, String muster)
		{
			try
			{
				return getZugnummer(zugname) % 2 == 1;
			}
			catch(NumberFormatException|NullPointerException e)
			{
				return false;
			}
		}
	};
	
	private String printName;
	public abstract boolean testZugname(String zugname, String muster);
	
	private FahrtFilter(String printName)
	{
		this.printName = printName;
	}
	
	public String toString()
	{
		return printName;
	}
	
	protected int getZugnummer(String zugname) throws NumberFormatException, NullPointerException
	{
		if(zugname == null)
		{
			throw new NullPointerException("zugname ist null.");
		}
		
		Matcher matcher = Pattern.compile("\\d+").matcher(zugname);
		if(!matcher.find())
		{
			throw new NumberFormatException("zugname enthält keine Ziffern.");
		}
		return Integer.parseInt(matcher.group());
	}
}
