package de.stsFanGruppe.templatebuilder.fahrtenFarbe;

import java.awt.Window;

import javax.swing.JPanel;

import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUI;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUIController;
import de.stsFanGruppe.tools.NullTester;

public class FahrtenFarbeSettingsGUI 
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsGUI.class);
	
	FahrtenFarbeSettingsGUIController controller;
	FahrtenFarbeConfig config;
	boolean saveEnabled = false;
	
	final JPanel contentPanel = new JPanel();

	public static void main(String[] args)
	{
		try
		{
			FahrtenFarbeSettingsGUI dialog = new FahrtenFarbeSettingsGUI(new FahrtenFarbeSettingsGUIController(new FahrtenFarbeConfig(), () -> {}), null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public FahrtenFarbeSettingsGUI(FahrtenFarbeSettingsGUIController controller, Window parent) 
	{
		super(parent);
		NullTester.test(controller);
		this.controller = controller;
		this.config = controller.getConfig();
		controller.setSettingsGui(this);
	}
}
