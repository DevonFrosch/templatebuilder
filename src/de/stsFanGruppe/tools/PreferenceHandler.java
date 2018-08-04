package de.stsFanGruppe.tools;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringJoiner;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

public class PreferenceHandler
{
	protected static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PreferenceHandler.class);;
	protected Preferences prefs;
	
	public PreferenceHandler(Class<?> reference)
	{
		NullTester.test(reference);
		log.trace("Neuer PreferenceHandler ({})", reference);
		prefs = Preferences.userNodeForPackage(reference);
	}
	
	// Hilfsfunktionen für Getter/Setter
	// String
	public void setString(String logName, String configName, String value)
	{
		prefs.put(configName, value);
		log.trace("Config: {} = {}", logName, value);
	}
	
	public String getString(String configName, String defaultValue)
	{
		return prefs.get(configName, defaultValue);
	}
	
	// boolean
	public void setBoolean(String logName, String configName, boolean value)
	{
		prefs.putBoolean(configName, value);
		log.trace("Config: {} = {}", logName, value);
	}
	
	public boolean getBoolean(String configName, boolean defaultValue)
	{
		return prefs.getBoolean(configName, defaultValue);
	}
	
	// int
	public void setInt(String logName, String configName, int value)
	{
		prefs.putInt(configName, value);
		log.trace("Config: {} = {}", logName, value);
	}
	
	public int getInt(String configName, int defaultValue)
	{
		return prefs.getInt(configName, defaultValue);
	}
	
	// double
	public void setDouble(String logName, String configName, double value)
	{
		prefs.putDouble(configName, value);
		log.trace("Config: {} = {}", logName, value);
	}
	
	public double getDouble(String configName, double defaultValue)
	{
		return prefs.getDouble(configName, defaultValue);
	}
	
	// int[]
	public void setIntArray(String logName, String configName, int[] value)
	{
		StringJoiner arr = new StringJoiner(",");
		for(int val : value)
		{
			arr.add(String.valueOf(val));
		}
		setString(logName, configName, arr.toString());
	}
	
	public int[] getIntArray(String configName, int[] defaultValue)
	{
		String value = getString(configName, null);
		
		if(value == null)
		{
			// Kein Eintrag vorhanden
			return defaultValue;
		}
		
		String[] strArray = value.split(",");
		
		if(strArray.length == 0)
		{
			// Leeres Array, kein gültiger Wert
			return defaultValue;
		}
		int[] rgba = new int[strArray.length];
		try
		{
			for(int i = 0; i < strArray.length; i++)
			{
				rgba[i] = Integer.parseInt(strArray[i]);
			}
		}
		catch(NumberFormatException e)
		{
			log.error("NumberFormatException beim Lesen von " + configName, e);
		}
		return rgba;
	}
	
	// Color = int[4]
	public void setColor(String logName, String configName, Color value)
	{
		int[] rgba = {value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha()};
		setIntArray(logName, configName, rgba);
	}
	
	public Color getColor(String configName, Color defaultValue)
	{
		int[] def = {defaultValue.getRed(), defaultValue.getGreen(), defaultValue.getBlue(), defaultValue.getAlpha()};
		int[] rgba = getIntArray(configName, def);
		
		if(rgba.length < 4)
		{
			return defaultValue;
		}
		
		return new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
	}
	
	public boolean speichertest()
	{
		String key = "test/speichertest";
		Preferences prefs = Preferences.userNodeForPackage(PreferenceHandler.class);
		try
		{
			boolean oldValue = prefs.getBoolean(key, false);
			prefs.putBoolean(key, !oldValue);
			prefs.flush();
			return true;
		}
		catch(BackingStoreException e)
		{
			log.info("speichertest: Storage-Fehler", e);
		}
		return false;
	}
	
	/**
	 * Stellt sicher, dass alle Einstellungen dauerhaft gespeichert sind.
	 * 
	 * @return false, falls ein Fehler aufgetreten ist, sonst true
	 */
	public boolean schreibeEinstellungen()
	{
		try
		{
			prefs.flush();
			return true;
		}
		catch(BackingStoreException e)
		{
			log.error("schreibeEinstellungen: Storage-Fehler", e);
		}
		return false;
	}
	
	/**
	 * Speichert die Einstellungen im OutputStream als Preferences-XML
	 * 
	 * @param os Stream, auf den das XML geschrieben wird
	 * @return false, falls ein Fehler aufgetreten ist, sonst true
	 */
	public boolean exportXML(OutputStream os)
	{
		try
		{
			prefs.exportSubtree(os);
			return true;
		}
		catch(IOException | BackingStoreException e)
		{
			log.error("exportXML: Exception beim Export", e);
		}
		return false;
	}
	
	/**
	 * Läd die Einstellungen aus dem InputStream als Preferences-XML
	 * 
	 * @param is Stream, von dem das XML gelesen wird
	 * @return false, falls ein Fehler aufgetreten ist, sonst true
	 */
	public boolean importXML(InputStream is)
	{
		try
		{
			Preferences.importPreferences(is);
			return true;
		}
		catch(IOException | InvalidPreferencesFormatException e)
		{
			log.error("importXML: Exception beim Import", e);
		}
		return false;
	}
	
	public String toString()
	{
		return prefs.toString();
	}
}
