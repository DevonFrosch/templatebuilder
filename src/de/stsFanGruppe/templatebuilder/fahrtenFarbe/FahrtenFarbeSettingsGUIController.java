package de.stsFanGruppe.templatebuilder.fahrtenFarbe;

import java.awt.event.ActionEvent;

import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUIController;
import de.stsFanGruppe.tools.NullTester;

public class FahrtenFarbeSettingsGUIController {
	
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsGUIController.class);
	
	private FahrtenFarbeSettingsGUI gui;
	private FahrtenFarbeConfig config;
	private Runnable onClose;

	public FahrtenFarbeSettingsGUIController(FahrtenFarbeConfig config, Runnable onClose)
	{
		NullTester.test(config);
		NullTester.test(onClose);
		this.config = config;
		this.onClose = onClose;
	}

	public void setSettingsGui(FahrtenFarbeSettingsGUI gui)
	{
		NullTester.test(gui);
		this.gui = gui;
		//gui.saveEnabled = config.schreibTest();
	}
	
	public FahrtenFarbeConfig getConfig()
	{
		return config;
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
					//speichereTabAllgemein();
					
				}
				catch(NumberFormatException e)
				{
					gui.errorMessage("Fehler beim Speichern der Einstellungen");
					return;
				}
				
				//if(!config.schreibeEinstellungen())
				//{
				//	log.error("Fehler beim Speichern der Einstellungen");
				//	gui.errorMessage("Fehler beim Speichern der Einstellungen");
				//	return;
				//}
				
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

	protected void close()
	{
		gui.close();
        gui = null;
        onClose.run();
	}
}
