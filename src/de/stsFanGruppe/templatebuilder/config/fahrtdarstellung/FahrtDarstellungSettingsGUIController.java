package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import javax.swing.JColorChooser;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUIController;
import de.stsFanGruppe.templatebuilder.gui.GUIController;
import de.stsFanGruppe.templatebuilder.zug.FahrtDarstellung;
import de.stsFanGruppe.tools.NullTester;

public class FahrtDarstellungSettingsGUIController extends GUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsGUIController.class);
	
	private FahrtDarstellungSettingsGUI gui;
	private FahrtDarstellungConfig config;
	private Runnable onClose;
	
	public FahrtDarstellungSettingsGUIController(FahrtDarstellungConfig config, Runnable onClose)
	{
		NullTester.test(config);
		NullTester.test(onClose);
		this.config = config;
		this.onClose = onClose;
	}
	
	public void setSettingsGui(FahrtDarstellungSettingsGUI gui)
	{
		NullTester.test(gui);
		this.gui = gui;
	}
	
	public FahrtDarstellungConfig getConfig()
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
		// Selectionmodell wird ausgelesen
		ListSelectionModel selectionModel = gui.table.getSelectionModel();
		// Erhalte ausgewählte Zeilen
		int[] rows = gui.table.getSelectedRows();
		Arrays.sort(rows);
		
		switch(event.getActionCommand())
		{
			// Zeile(n) nach oben verschieben
			case "moveUpRow":
				if(rows.length <= 0 || rows[0] <= 0)
				{
					// TODO: Visuelles Feedback ohne Bestätigung
					log.debug("tableButtonAction(moveUpRow): Keine oder oberste Zeile markiert.");
					break;
				}
				
				selectionModel.clearSelection();
				
				for(int i = 0; i < rows.length; i++)
				{
					FahrtDarstellung tauschPartner = gui.table.getRow(rows[i] - 1);
					gui.table.setRow(gui.table.getRow(rows[i]), rows[i] - 1);
					gui.table.setRow(tauschPartner, rows[i]);
					gui.table.addRowSelectionInterval(rows[i] - 1, rows[i] - 1);
				}
				
				break;
			// Zeile(n) nach unten verschieben
			case "moveDownRow":
				if(rows.length <= 0 || rows[rows.length-1] >= gui.table.getRowCount()-1)
				{
					// TODO: Visuelles Feedback ohne Bestätigung
					log.debug("tableButtonAction(moveDownRow): Keine oder unterste Zeile markiert.");
					break;
				}
				
				selectionModel.clearSelection();
				
				for(int i = 0; i < rows.length; i++)
				{
					FahrtDarstellung tauschPartner = gui.table.getRow(rows[i] + 1);
					gui.table.setRow(gui.table.getRow(rows[i]), rows[i] + 1);
					gui.table.setRow(tauschPartner, rows[i]);
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
						gui.table.insertRow(rows[i] + i + 1, getDefaultRowData());
					}
					gui.table.clearSelection();
				}
				else
				{
					gui.table.addRow(getDefaultRowData());
				}
				break;
			/* Zeile(n) löschen
			 * Wenn keine Zeile ausgewählt, erscheint Fehlerfenster.
			 */
			case "removeRow":
				if(rows.length < 0)
				{
					// TODO: Visuelles Feedback ohne Bestätigung
					log.debug("tableButtonAction(removeRow): Keine Zeile markiert");
					break;
				}
				
				for(int i = 0; i < rows.length; i++)
				{
					gui.table.removeRow(rows[i] - i);
				}
				
				break;
			default:
				log.error("tableButtonAction: actionCommand nicht erkannt: {}", event.getActionCommand());
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
				c = JColorChooser.showDialog(gui, "Farbe wählen", gui.getStandardFarbe());
				if(c != null)
				{
					gui.setStandardFarbe(c);
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
	
	protected FahrtDarstellung getDefaultRowData()
	{
		return new FahrtDarstellung("", gui.getStandardFarbe(), gui.getStandardBreiteInt(), gui.getStandardLineType());
	}
	public MouseListener getMouseListener()
	{
		return new MouseAdapter() {
			public void mouseClicked(MouseEvent e)
			{
				JTable target = (JTable) e.getSource();
				int row = target.getSelectedRow();
				int column = target.getSelectedColumn();
				if(column == 1)
				{
					Color alt = (Color) gui.table.getValueAt(row, column);
					Color neu = JColorChooser.showDialog(gui, "Farbe wählen", alt);
					if(neu != null)
					{
						gui.table.setValueAt(neu, row, column);
					}
				}
				if(column == 3)
				{
					gui.table.editCellAt(row, column);
				}
			}
		};
	}
	
	public void speichereStandards() throws NumberFormatException
	{
		// StandardLinienFarbe
		config.setStandardLinienFarbe(gui.getStandardFarbe());
		
		// StandardLinienStärke
		try
		{
			config.setStandardLinienStärke(parseIntField("Linienstärke", gui.getStandardBreite()));
		}
		catch(NumberFormatException e)
		{
			gui.errorMessage("Linienstärke: Nur positive ganze Zahlen erlaubt.");
			throw e;
		}
		
		// StandardLinienTyp
		config.setStandardLinienTyp(gui.getStandardLineType());
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
