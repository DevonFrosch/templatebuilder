package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum FahrtFilter
{
	BEGINNT_MIT("beginnt mit")
	{
		public boolean testZugname(String zugname, String muster)
		{
			return zugname.toLowerCase().startsWith(muster.toLowerCase());
		}
	},
	BEGINNT_NICHT_MIT("beginnt nicht mit")
	{
		public boolean testZugname(String zugname, String muster)
		{
			return !zugname.toLowerCase().startsWith(muster.toLowerCase());
		}
	},
	ENTHAELT("enthält")
	{
		public boolean testZugname(String zugname, String muster)
		{
			return zugname.toLowerCase().contains(muster.toLowerCase());
		}
	},
	ENTHAELT_NICHT("enthält nicht")
	{
		public boolean testZugname(String zugname, String muster)
		{
			return !zugname.toLowerCase().contains(muster.toLowerCase());
		}
	},
	GLEICH("ist gleich")
	{
		public boolean testZugname(String zugname, String muster)
		{
			return zugname.toLowerCase().equals(muster.toLowerCase());
		}
	},
	ENDET_MIT("endet mit")
	{
		public boolean testZugname(String zugname, String muster)
		{
			return zugname.toLowerCase().endsWith(muster.toLowerCase());
		}
	},
	ENDET_NICHT_MIT("endet nicht mit")
	{
		public boolean testZugname(String zugname, String muster)
		{
			return !zugname.toLowerCase().endsWith(muster.toLowerCase());
		}
	},
	GROESSER_ALS("Zugnummer größer als")
	{
		public boolean testZugname(String zugname, String muster)
		{
			try
			{
				return getZugnummer(zugname) > Integer.parseInt(muster);
			}
			catch(NumberFormatException | NullPointerException e)
			{
				return false;
			}
		}
	},
	KLEINER_ALS("Zugnummer kleiner als")
	{
		public boolean testZugname(String zugname, String muster)
		{
			try
			{
				return getZugnummer(zugname) < Integer.parseInt(muster);
			}
			catch(NumberFormatException | NullPointerException e)
			{
				return false;
			}
		}
	},
	GERADE("Zugnummer gerade")
	{
		public boolean testZugname(String zugname, String muster)
		{
			try
			{
				return getZugnummer(zugname) % 2 == 0;
			}
			catch(NumberFormatException | NullPointerException e)
			{
				return false;
			}
		}
	},
	UNGERADE("Zugnummer ungerade")
	{
		public boolean testZugname(String zugname, String muster)
		{
			try
			{
				return getZugnummer(zugname) % 2 == 1;
			}
			catch(NumberFormatException | NullPointerException e)
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
