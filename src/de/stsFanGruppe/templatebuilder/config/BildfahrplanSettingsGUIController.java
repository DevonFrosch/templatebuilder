package de.stsFanGruppe.templatebuilder.config;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.JColorChooser;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.TimeFormater;

public class BildfahrplanSettingsGUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsGUIController.class);
	
	private BildfahrplanSettingsGUI gui;
	private BildfahrplanConfig config;
	private Runnable onClose;
	
	public BildfahrplanSettingsGUIController(BildfahrplanConfig config, Runnable onClose)
	{
		NullTester.test(config);
		NullTester.test(onClose);
		this.config = config;
		this.onClose = onClose;
	}
	
	public void setSettingsGUI(BildfahrplanSettingsGUI gui)
	{
		NullTester.test(gui);
		this.gui = gui;
		gui.saveEnabled = config.schreibTest();
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
		
		// Ohne GUI können wir nichts machen
		if(gui == null)
		{
			log.error("farbButton(): Keine GUI gesetzt!");
			return;
		}
		
		Color c = null;
		switch(event.getActionCommand())
		{
			case "zeitenFarbe":
				c = JColorChooser.showDialog(gui, "Farbe wählen", gui.panelBfpZeitenFarbeVorschau.getBackground());
				if(c != null)
				{
					gui.panelBfpZeitenFarbeVorschau.setBackground(c);
				}
				break;
			case "betriebsstellenFarbe":
				c = JColorChooser.showDialog(gui, "Farbe wählen", gui.panelBfpBetriebsstellenFarbeVorschau.getBackground());
				if(c != null)
				{
					gui.panelBfpBetriebsstellenFarbeVorschau.setBackground(c);
				}
				break;
			case "fahrtenFarbe":
				c = JColorChooser.showDialog(gui, "Farbe wählen", gui.panelBfpFahrtenFarbeVorschau.getBackground());
				if(c != null)
				{
					gui.panelBfpFahrtenFarbeVorschau.setBackground(c);
				}
				break;
			case "hintergrundFarbe":
				c = JColorChooser.showDialog(gui, "Farbe wählen", gui.panelBfpHintergrundFarbeVorschau.getBackground());
				if(c != null)
				{
					gui.panelBfpHintergrundFarbeVorschau.setBackground(c);
				}
				break;
			default:
				log.error("farbButton: actionCommand nicht erkannt: {}", event.getActionCommand());
		}
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
			case "save":
				config.exportSettings(gui, gui);
				break;
			case "load":
				config.importSettings(gui, gui);
				// Einstellungen neu laden
				gui.loadSettings();
				break;
			case "cancel":
				close();
				return;
			case "apply":
			case "ok":
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
					gui.errorMessage("Fehler beim Speichern der Einstellungen");
					return;
				}
				
				if(!config.schreibeEinstellungen())
				{
					log.error("Fehler beim Speichern der Einstellungen");
					gui.errorMessage("Fehler beim Speichern der Einstellungen");
					return;
				}
				
				// OK: Fenster schließen
				if(event.getActionCommand() == "ok")
				{
					close();
			        return;
				}
				break;
			default:
				log.error("actionButton: actionCommand nicht erkannt: {}", event.getActionCommand());
		}
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
		// Höhe pro Stunde
		config.setHoeheProStunde(parseIntField("Höhe pro Stunde", gui.inputHoeheProStunde.getText()));
		
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
		if(gui.chckbxSchachtelung.isSelected())
		{
			config.setSchachtelung(parseIntField("Schachtelung", gui.inputSchachtelung.getText()));
		}
		else
		{
			config.setSchachtelung(0);
		}
		
		// ZeigeZugnamen
		config.setZeigeZugnamen(gui.rdbtngrpZeigeZugnamen.getSelection().getActionCommand());
		
		// ZeigeZugnamenKommentare
		config.setZeigeZugnamenKommentare(gui.chckbxZugnamenKommentare.isSelected());
		
		// ZeigeZugnamenKommentare
		config.setZeigeZeiten(gui.chckbxZeigeZeiten.isSelected());
		
		// ZeigeRichtung
		config.setZeigeRichtung(gui.rdbtngrpZeigeRichtung.getSelection().getActionCommand());
	}
	protected void speichereTabFarben()
	{
		// BfpZeiten
		config.setZeitenFarbe(gui.panelBfpZeitenFarbeVorschau.getBackground());
		
		// BfpBetriebsstellen
		config.setBetriebsstellenFarbe(gui.panelBfpBetriebsstellenFarbeVorschau.getBackground());
		
		// BfpFahrten
		config.setFahrtenFarbe(gui.panelBfpFahrtenFarbeVorschau.getBackground());
		
		// BfpHintergrund
		config.setHintergrundFarbe(gui.panelBfpHintergrundFarbeVorschau.getBackground());
	}
	protected void close()
	{
		gui.close();
        gui = null;
        onClose.run();
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
