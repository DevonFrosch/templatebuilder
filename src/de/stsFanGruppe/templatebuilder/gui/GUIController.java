package de.stsFanGruppe.templatebuilder.gui;

public abstract class GUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GUIController.class);
	
	public int parsePositiveIntField(String name, String input) throws NumberFormatException
	{
		int number = parseIntField(name, input);
		if(number == 0) {
			throw new NumberFormatException("Wert ist 0");
		}
		return number;
	}
	
	public int parseIntField(String name, String input) throws NumberFormatException
	{
		assert name != null;
		
		if(input == null || input.trim().isEmpty())
		{
			throw new NumberFormatException("Leerer String");
		}
		
		// throws NumberFormatException
		int hpsInt = Integer.parseInt(input);
		
		if(hpsInt < 0)
		{
			throw new NumberFormatException("Wert kleiner 0");
		}
		return hpsInt;
	}
}
