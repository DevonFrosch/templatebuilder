package de.stsFanGruppe.templatebuilder.config;

import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.PreferenceHandler;

public abstract class ConfigController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ConfigController.class);
	
	protected Map<Object, Runnable> callbacks = new HashMap<>();
	protected int callbackCounter = 0;
	protected PreferenceHandler prefs = null;
	
	protected boolean isTransaction = false;
	
	// Change-Handler
	public Object registerChangeHandler(Runnable callback)
	{
		NullTester.test(callback);
		Object handlerID = Integer.valueOf(callbackCounter++);
		log.debug("registerChangeHandler (ID {}) auf {}", handlerID, this.getClass().getName());
		callbacks.put(handlerID, callback);
		return handlerID;
	}
	
	public boolean unregisterChangeHandler(Object handlerID)
	{
		if(handlerID == null)
		{
			return false;
		}
		log.debug("unregisterChangeHandler (ID {})", handlerID);
		return callbacks.remove(handlerID) != null;
	}
	
	public void startTransaction()
	{
		this.isTransaction = true;
	}
	
	public void endTransaction()
	{
		this.isTransaction = false;
		notifyChange();
	}
	
	protected void notifyChange()
	{
		if(isTransaction)
		{
			return;
		}
		log.trace("notifyChange");
		callbacks.forEach((k, v) -> v.run());
	}
	
	public void importSettings(Window windowParent, GUI messageParent)
	{
		JFileChooser loadFileChooser = new JFileChooser();
		loadFileChooser.setDialogTitle("Einstellungen laden");
		loadFileChooser.setFileFilter(new FileNameExtensionFilter("XML-Einstellungen (*.xml)", "xml"));
		loadFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		if(loadFileChooser.showOpenDialog(windowParent) == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				InputStream is = new FileInputStream(new File(loadFileChooser.getSelectedFile().getPath()));
				if(!importXML(is))
				{
					messageParent.infoMessage("Fehler beim Laden der Einstellungen!");
					is.close();
				}
				is.close();
			}
			catch(FileNotFoundException e)
			{
				messageParent.infoMessage("Datei nicht gefunden!");
			}
			catch(IOException e)
			{
				log.error("Einstellungen importieren", e);
				messageParent.infoMessage("Fehler beim Laden der Einstellungen!");
			}
		}
	}
	
	public void exportSettings(Window windowParent)
			throws IOException
	{
		JFileChooser saveFileChooser = new JFileChooser();
		saveFileChooser.setDialogTitle("Einstellungen speichern");
		saveFileChooser.setFileFilter(new FileNameExtensionFilter("XML-Einstellungen (*.xml)", "xml"));
		saveFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		if(saveFileChooser.showSaveDialog(windowParent) == JFileChooser.APPROVE_OPTION)
		{
			OutputStream os = new FileOutputStream(new File(saveFileChooser.getSelectedFile().getPath()));
			try
			{
				exportXML(os);
			}
			finally
			{
				os.close();
			}
		}
	}
	
	public boolean schreibeEinstellungen()
	{
		return prefs.schreibeEinstellungen();
	}
	
	public boolean importXML(InputStream is)
	{
		return prefs.importXML(is);
	}
	
	public void exportXML(OutputStream os) throws IOException
	{
		prefs.exportXML(os);
	}
	
	public boolean speichertest()
	{
		NullTester.test(prefs);
		return prefs.speichertest();
	}
}
