package de.stsFanGruppe.templatebuilder.gui.settings;

import java.awt.event.ActionEvent;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.TimeFormater;

public class BildfahrplanSettingsGUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsGUIController.class);
	
	private BildfahrplanSettingsGUI gui;
	private BildfahrplanConfig config;
	
	public BildfahrplanSettingsGUIController(BildfahrplanConfig config)
	{
		NullTester.test(config);
		this.config = config;
	}
	
	public void setSettingsGUI(BildfahrplanSettingsGUI gui)
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
	public int getSchachtelung()
	{
		return config.getSchachtelung();
	}
	public int getZeigeZugnamen()
	{
		return config.getZeigeZugnamen();
	}
	public boolean getZeigeZugnamenKommentare()
	{
		return config.getZeigeZugnamenKommentare();
	}
	
	public void actionButton(ActionEvent event)
	{
		assert config != null;
		
		// Ohne GUI können wir nichts machen
		if(gui == null)
		{
			log.error("actionButton(): Keine GUI gesetzt!");
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
					log.error("HöheProStunde / Exception", e);
					gui.errorMessage("Hoehe pro Stunde: "+e.getMessage());
				}
				
				try
				{
					if(gui.chckbxAuto.isSelected())
					{
						config.enableAutoSize();
					}
					else
					{
						double minZeit = TimeFormater.stringToDouble(gui.inputMinZeit.getText());
						double maxZeit = TimeFormater.stringToDouble(gui.inputMaxZeit.getText());
						config.setZeiten(minZeit, maxZeit);
					}
				}
				catch(NumberFormatException e)
				{
					log.error("Dargestellte Zeit / Exception", e);
					gui.errorMessage("Dargestellte Zeit: "+e.getMessage());
				}
				
				try
				{
					int sch = parseIntField(gui.inputSchachtelung.getText());
					config.setSchachtelung(sch);
				}
				catch(NumberFormatException e)
				{
					log.error("Schachtelung / Exception", e);
					gui.errorMessage("Schachtelung: "+e.getMessage());
				}
				
				config.setZeigeZugnamen(gui.rdbtngrpZeigeZugnamen.getSelection().getActionCommand());
				
				// ZugnamenKommentare
				config.setZeigeZugnamenKommentare(gui.chckbxZugnamenKommentare.isSelected());
				
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
			log.error("IntField: Leerer String");
			throw new NumberFormatException("Feld ist leer.");
		}
		try
		{
			int hpsInt = Integer.parseInt(input);
			if(hpsInt < 0)
			{
				log.error("IntField: Wert kleiner 0");
				throw new NumberFormatException("Nur positive ganze Zahlen erlaubt.");
			}
			return hpsInt;
		}
		catch(NumberFormatException e)
		{
			log.error("IntField: NumberformatException", e);
			throw new NumberFormatException("Nur positive ganze Zahlen erlaubt.");
		}
	}
}
