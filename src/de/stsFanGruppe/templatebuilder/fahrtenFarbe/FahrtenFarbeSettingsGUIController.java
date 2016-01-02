package de.stsFanGruppe.templatebuilder.fahrtenFarbe;

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
		gui.saveEnabled = config.schreibTest();
	}
	
	public FahrtenFarbeConfig getConfig()
	{
		return config;
	}

	
}
