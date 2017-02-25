package de.stsFanGruppe.templatebuilder.startup;

import java.awt.EventQueue;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUIController;

public class Startup
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Startup.class);
	
	// Versionsinformationen
	public final static String version = "0.3.1";
	public final static boolean dev = true;
	
	public static void main(String[] args)
	{
		String v = version;
		if(dev) v += " (dev)";
		
		log.info("Templatebuilder gestartet.");
		log.info("Version {}", v);
		
		try
		{
			javax.swing.UIManager.setLookAndFeel( javax.swing.UIManager.getSystemLookAndFeelClassName() );
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex)
		{
			log.info("Setzten des L&F fehlgeschlagen", ex);
		}
		
		EventQueue.invokeLater(() -> {
			try
			{
				TemplateBuilderGUIController controller = new TemplateBuilderGUIController(new BildfahrplanConfig(), version, dev);
				TemplateBuilderGUI window = new TemplateBuilderGUI(controller);
				window.setVisible(true);
			}
			catch(Exception e)
			{
				log.error("Nicht abgefangene Exception", e);
			}
		});
	}
}
