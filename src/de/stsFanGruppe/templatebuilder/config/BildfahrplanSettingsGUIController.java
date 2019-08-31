package de.stsFanGruppe.templatebuilder.config;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.JColorChooser;
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
				try
				{
					config.exportSettings(gui);
				}
				catch(IOException e)
				{
					log.debug("Exception beim Speichern der Einstellugen", e);
					gui.infoMessage("Fehler beim Speichern der Einstellungen!");
				}
				break;
			case "load":
				config.importSettings(gui, gui);
				// Einstellungen neu laden
				ladeDaten();
				break;
			case "format":
				if(!GUILocker.lock(FahrtDarstellungSettingsGUI.class))
					break;
				FahrtDarstellungSettingsGUIController ffsgc = new FahrtDarstellungSettingsGUIController(
					config.getFahrtDarstellungConfig(), () -> {
						GUILocker.unlock(FahrtDarstellungSettingsGUI.class);
					}
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
					config.startTransaction();
					speichereTabAllgemein();
					speichereTabStreckeneditor();
					speichereTabZugeditor();
					speichereTabBildfahrplan();
					speichereTabFarben();
					config.endTransaction();
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
		gui.setSchachtelung(config.getSchachtelungTyp(), config.getSchachtelungMinuten(), config.getSchachtelungTemplate());
		
		gui.setZeigeZugnamen(config.getZeigeZugnamen());
		gui.chckbxZugnamenKommentare.setSelected(config.getZeigeZugnamenKommentare());
		gui.chckbxZeigeZeiten.setSelected(config.getZeigeZeiten());
		gui.setRichtung(config.getZeigeRichtung());
		
		gui.ignorierteZuegeTextArea.setText(config.getIgnorierteZuege());
		gui.ignorierteTemplatesTextArea.setText(config.getIgnorierteTemplates());
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
		
		config.setSchachtelungTyp(gui.getSchachtelungTyp());
		config.setSchachtelungMinuten(gui.getSchachtelungMinuten());
		config.setSchachtelungTemplate(gui.getSchachtelungTemplate());
		
		config.setZeigeZugnamen(gui.rdbtngrpZeigeZugnamen.getSelection().getActionCommand());
		config.setZeigeZugnamenKommentare(gui.chckbxZugnamenKommentare.isSelected());
		config.setZeigeZeiten(gui.chckbxZeigeZeiten.isSelected());
		config.setZeigeRichtung(gui.rdbtngrpZeigeRichtung.getSelection().getActionCommand());
		config.setIgnorierteZuege(gui.ignorierteZuegeTextArea.getText());
		config.setIgnorierteTemplates(gui.ignorierteTemplatesTextArea.getText());
	}
	
	protected void ladeTabFarben()
	{
		gui.panelBfpZeitenFarbeVorschau.setBackground(config.getZeitenFarbe());
		gui.panelBfpBetriebsstellenFarbeVorschau.setBackground(config.getBetriebsstellenFarbe());
		gui.panelBfpHintergrundFarbeVorschau.setBackground(config.getHintergrundFarbe());
	}
	
	protected void speichereTabFarben()
	{
		config.setZeitenFarbe(gui.panelBfpZeitenFarbeVorschau.getBackground());
		config.setBetriebsstellenFarbe(gui.panelBfpBetriebsstellenFarbeVorschau.getBackground());
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
