package de.stsFanGruppe.templatebuilder.startup;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import de.stsFanGruppe.templatebuilder.config.GeneralConfig;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUIController;

public class Startup
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Startup.class);
	
	// Versionsinformationen
	public final static String version = "0.3.11";
	public final static boolean dev = true;
	
	public final static String updateUrl = "https://sts-fan-gruppe.de/spielzeug/templatebuilder/version.txt";
	
	public static void main(String[] args)
	{
		String v = version;
		if(dev)
			v += " (dev)";
		String update = testForUpdate();
		
		log.info("Templatebuilder gestartet.");
		log.info("Version {}", v);
		
		if(update != null)
		{
			log.info("Update auf Version '{}' verfügbar.", update);
		}
		
		try
		{
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		}
		catch(ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex)
		{
			log.info("Setzten des L&F fehlgeschlagen", ex);
		}
		
		EventQueue.invokeLater(() -> {
			try
			{
				new TemplateBuilderGUIController(new GeneralConfig(), version, dev, update);
			}
			catch(Exception e)
			{
				log.error("Nicht abgefangene Exception", e);
			}
		});
	}
	
	public static String testForUpdate()
	{
		try
		{
			InputStream inputStream = new URI(updateUrl)
					.toURL()
					.openConnection()
					.getInputStream();
	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
	        
            String zeile = bufferedReader.readLine();
            
            if(!zeile.equals(version))
            {
            	return zeile;
            }
		}
		catch(IOException | URISyntaxException e)
		{}
		
		return null;
	}
}
