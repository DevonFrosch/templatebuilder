package de.stsFanGruppe.templatebuilder.config.fahrtenfarbe;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.JColorChooser;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUIController;
import de.stsFanGruppe.templatebuilder.gui.GUIController;
import de.stsFanGruppe.tools.NullTester;

public class FahrtenFarbeSettingsGUIController extends GUIController
{
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
	}
	
	public FahrtenFarbeConfig getConfig()
	{
		return config;
	}
	
	/**
	 * Liest den ActionCommand vom event aus und führt dann etwas aus.
	 * 
	 * @param event
	 */
	public void tableButtonAction(ActionEvent event)
	{
		assert config != null;
		
		// Ohne GUI können wir nichts machen
		if(gui == null)
		{
			log.error("actionButton(): Keine GUI gesetzt!");
			return;
		}
		// Tabellenmodell wird ausgelesen
		DefaultTableModel model = (DefaultTableModel) gui.table.getModel();
		// Selectionmodell wird ausgelesen
		ListSelectionModel selectionModel = gui.table.getSelectionModel();
		// Erhalte ausgewählte Zeilen
		int[] rows = gui.table.getSelectedRows();
		
		switch(event.getActionCommand())
		{
			// Zeile(n) nach oben verschieben
			case "moveUpRow":
				if(rows[0] == 0)
				{
					gui.infoMessage("Erste Zeile kann nicht nach oben verschoben werden.", "Info: Zeile nach oben verschieben");
				}
				else
				{
					selectionModel.clearSelection();
					for(int i = 0; i < rows.length; i++)
					{
						// FIXME Farbe wird noch nicht nach oben bewegt.
						model.moveRow(rows[i], rows[i], rows[i] - 1);
						gui.table.addRowSelectionInterval(rows[i] - 1, rows[i] - 1);
					}
				}
				break;
			// Zeile(n) nach unten verschieben
			case "moveDownRow":
				selectionModel.clearSelection();
				for(int i = 0; i < rows.length; i++)
				{
					// FIXME Farbe wird noch nicht nach unten bewegt.
					model.moveRow(rows[i], rows[i], rows[i] + 1);
					gui.table.addRowSelectionInterval(rows[i] + 1, rows[i] + 1);
				}
				break;
			/* Zeile(n) hinzufügen
			 * Zeile(n) wird/ werden nach ausgewählten Zeilen hinzugefügt oder nach der letzten Zeile
			 */
			case "addRow":
				if(rows.length > 0)
				{
					for(int i = 0; i < rows.length; i++)
					{
						model.insertRow(rows[i] + i + 1, getDefaultRowData());
						gui.testFarben.add(rows[i] + i + 1, gui.panelStandardFarbeVorschau.getBackground());
					}
					gui.table.clearSelection();
					break;
				}
				else
				{
					gui.testFarben.add(gui.panelStandardFarbeVorschau.getBackground());
					model.addRow(getDefaultRowData());
					break;
				}
			/* Zeile(n) löschen
			 * Wenn keine Zeile ausgewählt, erscheint Fehlerfenster.
			 */
			case "removeRow":
				if(rows.length < 0)
				{
					gui.errorMessage("Zeile auswählen, die gelöscht werden soll", "Zeile löschen fehlgeschlagen!");
					break;
				}
				else
				{
					for(int i = 0; i < rows.length; i++)
					{
						gui.testFarben.remove(rows[i] - i);
						model.removeRow(rows[i] - i);
					}
					break;
				}
		}
		
	}
	
	public void farbButton(ActionEvent event)
	{
		assert config != null;
		
		// Ohne GUI können wir nichts machen
		if(gui == null)
		{
			log.error("farbButton(): Keine GUI gesetzt!");
			return;
		}
		
		Color c = null;
		switch(event.getActionCommand())
		{
			case "zeitenFarbe":
				c = JColorChooser.showDialog(gui, "Farbe wählen", gui.panelStandardFarbeVorschau.getBackground());
				if(c != null)
				{
					gui.panelStandardFarbeVorschau.setBackground(c);
				}
				break;
			default:
				log.error("farbButton: actionCommand nicht erkannt: {}", event.getActionCommand());
		}
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
					speichereStandards();
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
	
	protected Object[] getDefaultRowData()
	{
		return new Object[]{"", gui.getDefaultLineColor(), gui.getDefaultLineWidth(), gui.getDefaultLineType()};
	}
	
	public void speichereStandards() throws NumberFormatException
	{
		// StandardLinienFarbe
		config.setStandardLinienFarbe(gui.getDefaultLineColor());
		
		// StandardLinienStärke
		try
		{
			config.setStandardLinienStärke(parseIntField("Linienstärke", gui.getDefaultLineWidth()));
		}
		catch(NumberFormatException e)
		{
			gui.errorMessage("Linienstärke: Nur positive ganze Zahlen erlaubt.");
			throw e;
		}
		
		// StandardLinienTyp
		config.setStandardLinienTyp(gui.getDefaultLineType());
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
