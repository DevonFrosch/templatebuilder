package de.stsFanGruppe.templatebuilder.config;

import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.FahrtDarstellungConfig;
import de.stsFanGruppe.tools.PreferenceHandler;

public class GeneralConfig extends ConfigController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GeneralConfig.class);
	
	public static final String CONFIG_LAST_OPEN_FILE_PATH = "general/lastOpenFilePath";
	
	public static final String DEFAULT_LAST_OPEN_FILE_PATH = null;
	
	// Andere Configs
	protected BildfahrplanConfig bildfahrplanConfig;
	
	public GeneralConfig()
	{
		this(new BildfahrplanConfig());
	}
	
	public GeneralConfig(BildfahrplanConfig bildfahrplanConfig)
	{
		log.trace("Neue GeneralConfig()");
		this.prefs = new PreferenceHandler(BildfahrplanConfig.class);
		this.bildfahrplanConfig = bildfahrplanConfig;

		assert prefs != null;
	}
	
	public String getLastOpenFilePath()
	{
		return prefs.getString(CONFIG_LAST_OPEN_FILE_PATH, DEFAULT_LAST_OPEN_FILE_PATH);
	}
	
	public void setLastOpenFilePath(String lastOpenFilePath)
	{
		prefs.setString("lastOpenFilePath", CONFIG_LAST_OPEN_FILE_PATH, lastOpenFilePath);
		notifyChange();
	}
	
	public BildfahrplanConfig getBildfahrplanConfig()
	{
		return bildfahrplanConfig;
	}
	
	public FahrtDarstellungConfig getFahrtDarstellungConfig()
	{
		return bildfahrplanConfig.getFahrtDarstellungConfig();
	}
}
