package de.stsFanGruppe.templatebuilder.gui.settings;

import java.awt.event.ActionEvent;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.TimeFormater;

public class SettingsGUIController
{
	private SettingsGUI gui;
	private BildfahrplanConfig config;
	
	public SettingsGUIController(BildfahrplanConfig config)
	{
		NullTester.test(config);
		this.config = config;
	}
	
	public void setSettingsGUI(SettingsGUI gui)
	{
		NullTester.test(gui);
		this.gui = gui;
	}
	
	public int getHoeheProStunde()
	{
		return config.getHoeheProStunde();
	}
	public String getMinZeit()
	{
		return TimeFormater.doubleToString(config.getMinZeit());
	}
	public String getMaxZeit()
	{
		return TimeFormater.doubleToString(config.getMaxZeit());
	}
	public void actionButton(ActionEvent event)
	{
		assert config != null;
		
		// Ohne GUI können wir nichts machen
		if(gui == null)
		{
			log("actionButton(): Keine GUI gesetzt!");
			return;
		}
		
		switch(event.getActionCommand())
		{
			case "Cancel":
				break;
			case "Apply":
			case "OK":
				try
				{
					int hps = parseIntField(gui.inputHoeheProStunde.getText());
					config.setHoeheProStunde(hps);
				}
				catch(NumberFormatException e)
				{
					gui.errorMessage("Hoehe pro Stunde: "+e.getMessage());
				}
				
				try
				{
					double minZeit = TimeFormater.stringToDouble(gui.inputMinZeit.getText());
					double maxZeit = TimeFormater.stringToDouble(gui.inputMaxZeit.getText());
					config.setMinZeit(minZeit);
					config.setMaxZeit(maxZeit);
				}
				catch(NumberFormatException e)
				{
					gui.errorMessage("Dargestellte Zeit: "+e.getMessage());
				}
				
				// Apply: Fenster nicht schließen
				if(event.getActionCommand() == "Apply")
				{
					return;
				}
				break;
			default:
		}
        gui.close();
        gui = null;
	}
	
	protected int parseIntField(String input) throws NumberFormatException
	{
		if(input.trim().isEmpty())
		{
			throw new NumberFormatException("Feld ist leer.");
		}
		try
		{
			int hpsInt = Integer.parseInt(input);
			if(hpsInt < 0)
			{
				throw new NumberFormatException("Nur positive ganze Zahlen erlaubt.");
			}
			return hpsInt;
		}
		catch(NumberFormatException e)
		{
			throw new NumberFormatException("Nur positive ganze Zahlen erlaubt.");
		}
	}
	
	private static void log(String text)
	{
		System.out.println("SettingsGUIController: "+text);
	}
}
