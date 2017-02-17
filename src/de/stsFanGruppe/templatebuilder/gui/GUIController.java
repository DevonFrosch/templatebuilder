package de.stsFanGruppe.templatebuilder.gui;

public abstract class GUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GUIController.class);
	
	protected static int parseIntField(String name, String input) throws NumberFormatException
	{
		assert name != null;
		
		if(input == null || input.trim().isEmpty())
		{
			log.error("{}: Leerer String", name);
			throw new NumberFormatException();
		}
		try
		{
			int hpsInt = Integer.parseInt(input);
			if(hpsInt < 0)
			{
				log.error("{}: Wert kleiner 0", name);
				throw new NumberFormatException();
			}
			return hpsInt;
		}
		catch(NumberFormatException e)
		{
			log.error(name+": NumberformatException", e);
			throw new NumberFormatException();
		}
	}
}
