package de.stsFanGruppe.templatebuilder.config;

import java.awt.event.ActionEvent;
import javax.swing.JColorChooser;
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
	
	public BildfahrplanConfig getConfig()
	{
		return config;
	}
	
	public String getMinZeit()
	{
		return TimeFormater.doubleToString(config.getMinZeit());
	}
	public String getMaxZeit()
	{
		return TimeFormater.doubleToString(config.getMaxZeit());
	}
	
	public void farbButton(ActionEvent event)
	{
		assert config != null;
		
		// Ohne GUI k�nnen wir nichts machen
		if(gui == null)
		{
			log.error("farbButton(): Keine GUI gesetzt!");
			return;
		}
		
		switch(event.getActionCommand())
		{
			case "zeitenFarbe":
				gui.panelBfpZeitenFarbeVorschau.setBackground(JColorChooser.showDialog(gui, "Farbe w�hlen", gui.panelBfpZeitenFarbeVorschau.getBackground()));
				break;
			case "betriebsstellenFarbe":
				gui.panelBfpBetriebsstellenFarbeVorschau.setBackground(JColorChooser.showDialog(gui, "Farbe w�hlen", gui.panelBfpBetriebsstellenFarbeVorschau.getBackground()));
				break;
			case "fahrtenFarbe":
				gui.panelBfpFahrtenFarbeVorschau.setBackground(JColorChooser.showDialog(gui, "Farbe w�hlen", gui.panelBfpFahrtenFarbeVorschau.getBackground()));
				break;
			default:
				log.error("farbButton: actionCommand nicht erkannt: {}", event.getActionCommand());
		}
	}
	public void actionButton(ActionEvent event)
	{
		assert config != null;
		
		// Ohne GUI k�nnen wir nichts machen
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
					speichereTabAllgemein();
					speichereTabStreckeneditor();
					speichereTabZugeditor();
					speichereTabBildfahrplan();
					speichereTabFarben();
				}
				catch(NumberFormatException e)
				{
					return;
				}
				
				// Apply: Fenster nicht schlie�en
				if(event.getActionCommand() == "Apply")
				{
					return;
				}
				break;
			default:
				log.error("actionButton: actionCommand nicht erkannt: {}", event.getActionCommand());
		}
        gui.close();
        gui = null;
	}
	protected void speichereTabAllgemein()
	{
		
	}
	protected void speichereTabStreckeneditor()
	{
		
	}
	protected void speichereTabZugeditor()
	{
		
	}
	protected void speichereTabBildfahrplan()
	{
		// H�he pro Stunde
		config.setHoeheProStunde(parseIntField("H�he pro Stunde", gui.inputHoeheProStunde.getText()));
		
		// Dargestellte Zeit + Autosizing
		if(gui.chckbxAuto.isSelected())
		{
			config.enableAutoSize();
		}
		else
		{
			double minZeit, maxZeit;
			try
			{
				minZeit = TimeFormater.stringToDouble(gui.inputMinZeit.getText());
				maxZeit = TimeFormater.stringToDouble(gui.inputMaxZeit.getText());
			}
			catch(NumberFormatException e)
			{
				log.error("Dargestellte Zeit: NumberformatException", e);
				gui.errorMessage("Dargestellte Zeit: Nur positive ganze Zahlen erlaubt.");
				// Ball weiterwerfen
				throw e;
			}
			config.setZeiten(minZeit, maxZeit);
		}
		
		// Schachtelung
		config.setSchachtelung(parseIntField("Schachtelung", gui.inputSchachtelung.getText()));
		
		// ZeigeZugnamen
		config.setZeigeZugnamen(gui.rdbtngrpZeigeZugnamen.getSelection().getActionCommand());
		
		// ZeigeZugnamenKommentare
		config.setZeigeZugnamenKommentare(gui.chckbxZugnamenKommentare.isSelected());
		
		// ZeigeZugnamenKommentare
		config.setZeigeZeiten(gui.chckbxZeigeZeiten.isSelected());
	}
	protected void speichereTabFarben()
	{
		// BfpZeiten
		config.setBfpZeitenFarbe(gui.panelBfpZeitenFarbeVorschau.getBackground());
		
		// BfpZeiten
		config.setBfpBetriebsstellenFarbe(gui.panelBfpBetriebsstellenFarbeVorschau.getBackground());
		
		// BfpZeiten
		config.setBfpFahrtenFarbe(gui.panelBfpFahrtenFarbeVorschau.getBackground());
	}
	
	protected int parseIntField(String name, String input)
	{
		if(input == null || input.trim().isEmpty())
		{
			log.error("{}: Leerer String", name);
			gui.errorMessage(name+": Dargestellte Zeit: Feld ist leer");
			throw new NumberFormatException();
		}
		try
		{
			int hpsInt = Integer.parseInt(input);
			if(hpsInt < 0)
			{
				log.error("{}: Wert kleiner 0", name);
				gui.errorMessage(name+": Nur positive ganze Zahlen erlaubt.");
				throw new NumberFormatException();
			}
			return hpsInt;
		}
		catch(NumberFormatException e)
		{
			log.error(name+": NumberformatException", e);
			gui.errorMessage(name+": Nur positive ganze Zahlen erlaubt.");
			throw new NumberFormatException();
		}
	}
}
