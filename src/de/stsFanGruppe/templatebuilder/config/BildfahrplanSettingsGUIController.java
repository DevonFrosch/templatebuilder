package de.stsFanGruppe.templatebuilder.config;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.JColorChooser;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.FahrtDarstellungConfig;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.FahrtDarstellungSettingsGUI;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.FahrtDarstellungSettingsGUIController;
import de.stsFanGruppe.templatebuilder.gui.GUIController;
import de.stsFanGruppe.tools.GUILocker;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.TimeFormater;

public class BildfahrplanSettingsGUIController extends GUIController
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
		
		// Ohne GUI kˆnnen wir nichts machen
		if(gui == null)
		{
			log.error("farbButton(): Keine GUI gesetzt!");
			return;
		}
		
		Color c = null;
		switch(event.getActionCommand())
		{
			case "zeitenFarbe":
				c = JColorChooser.showDialog(gui, "Farbe w‰hlen", gui.panelBfpZeitenFarbeVorschau.getBackground());
				if(c != null)
				{
					gui.panelBfpZeitenFarbeVorschau.setBackground(c);
				}
				break;
			case "betriebsstellenFarbe":
				c = JColorChooser.showDialog(gui, "Farbe w‰hlen", gui.panelBfpBetriebsstellenFarbeVorschau.getBackground());
				if(c != null)
				{
					gui.panelBfpBetriebsstellenFarbeVorschau.setBackground(c);
				}
				break;
			case "fahrtenFarbe":
				c = JColorChooser.showDialog(gui, "Farbe w‰hlen", gui.panelBfpFahrtenFarbeVorschau.getBackground());
				if(c != null)
				{
					gui.panelBfpFahrtenFarbeVorschau.setBackground(c);
				}
				break;
			case "hintergrundFarbe":
				c = JColorChooser.showDialog(gui, "Farbe w‰hlen", gui.panelBfpHintergrundFarbeVorschau.getBackground());
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
		
		// Ohne GUI kˆnnen wir nichts machen
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
				ladeDaten();
				break;
			case "format":
				FahrtDarstellungSettingsGUIController ffsgc = new FahrtDarstellungSettingsGUIController(
						new FahrtDarstellungConfig(), () -> {GUILocker.unlock(FahrtDarstellungSettingsGUI.class);}
				);
				FahrtDarstellungSettingsGUI ffsg = new FahrtDarstellungSettingsGUI(ffsgc, gui);
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
					gui.errorMessage("Fehler beim Lesen der Einstellungen");
					return;
				}
				
				if(!config.schreibeEinstellungen())
				{
					log.error("Fehler beim Speichern der Einstellungen");
					gui.errorMessage("Fehler beim Speichern der Einstellungen");
					return;
				}
				
				// OK: Fenster schlieﬂen
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
	
	public void ladeDaten()
	{
		if(!config.speichertest())
		{
			return;
		}
		config.schreibeEinstellungen();
		
		ladeTabBildfahrplan();
		ladeTabFarben();
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

	public void ladeTabBildfahrplan()
	{
		
		gui.sliderHoeheProStunde.setValue(config.getHoeheProStunde());
		gui.inputMinZeit.setText(getMinZeit());
		gui.inputMaxZeit.setText(getMaxZeit());
		gui.inputSchachtelung.setText(String.valueOf(config.getSchachtelung()));
		gui.inputSchachtelung.setEnabled(config.getSchachtelung() != 0);
		gui.chckbxSchachtelung.setSelected(config.getSchachtelung() != 0);
		
		gui.setZeigeZugnamen(config.getZeigeZugnamen());
		gui.chckbxZugnamenKommentare.setSelected(config.getZeigeZugnamenKommentare());
		gui.chckbxZeigeZeiten.setSelected(config.getZeigeZeiten());
		gui.setRichtung(config.getZeigeRichtung());
	}
	protected void speichereTabBildfahrplan() throws NumberFormatException
	{
		config.setHoeheProStunde(gui.getHoeheProStunde());
		
		if(gui.getAutoSizeSelected())
		{
			config.enableAutoSize();
		}
		else
		{
			config.setZeiten(gui.getMinZeit(), gui.getMaxZeit());
		}
		
		config.setSchachtelung(gui.getSchachtelung());
		
		config.setZeigeZugnamen(gui.rdbtngrpZeigeZugnamen.getSelection().getActionCommand());
		config.setZeigeZugnamenKommentare(gui.chckbxZugnamenKommentare.isSelected());
		config.setZeigeZeiten(gui.chckbxZeigeZeiten.isSelected());
		config.setZeigeRichtung(gui.rdbtngrpZeigeRichtung.getSelection().getActionCommand());
	}
	
	protected void ladeTabFarben()
	{
		gui.panelBfpZeitenFarbeVorschau.setBackground(config.getZeitenFarbe());
		gui.panelBfpBetriebsstellenFarbeVorschau.setBackground(config.getBetriebsstellenFarbe());
		gui.panelBfpFahrtenFarbeVorschau.setBackground(config.getFahrtenFarbe());
		gui.panelBfpHintergrundFarbeVorschau.setBackground(config.getHintergrundFarbe());
	}
	protected void speichereTabFarben()
	{
		config.setZeitenFarbe(gui.panelBfpZeitenFarbeVorschau.getBackground());
		config.setBetriebsstellenFarbe(gui.panelBfpBetriebsstellenFarbeVorschau.getBackground());
		config.setFahrtenFarbe(gui.panelBfpFahrtenFarbeVorschau.getBackground());
		config.setHintergrundFarbe(gui.panelBfpHintergrundFarbeVorschau.getBackground());
	}
	
	public boolean speichertest()
	{
		return config.speichertest();
	}
	
	protected void close()
	{
		gui.close();
        gui = null;
        onClose.run();
	}
}
