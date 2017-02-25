package de.stsFanGruppe.templatebuilder.editor.streckenEditor;

import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import de.stsFanGruppe.templatebuilder.editor.EditorDaten;
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Gleis;
import de.stsFanGruppe.templatebuilder.strecken.Gleisabschnitt;
import de.stsFanGruppe.templatebuilder.strecken.Strecke;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.FirstLastList;
import de.stsFanGruppe.tools.NullTester;

public class StreckenEditorGUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StreckenEditorGUIController.class);
	
	protected EditorDaten editorDaten;
	private StreckenEditorGUI gui;
	private Runnable onClose;
	
	public StreckenEditorGUIController(EditorDaten editorDaten, Runnable onClose)
	{
		NullTester.test(editorDaten);
		NullTester.test(onClose);
		this.editorDaten = editorDaten;
		this.onClose = onClose;
	}
	
	public void setSettingsGUI(StreckenEditorGUI gui)
	{
		NullTester.test(gui);
		this.gui = gui;
	}
	
	/**
	 * Liest den ActionCommand vom event aus und führt dann etwas aus.
	 * 
	 * @param event
	 */
	public void tableButtonAction(ActionEvent event)
	{
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
		Object[] defaultRowdata = {"", ""};
		
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
					model.moveRow(rows[i], rows[i], rows[i] + 1);
					gui.table.addRowSelectionInterval(rows[i] + 1, rows[i] + 1);
				}
				break;
			/*
			 * Zeile(n) hinzufügen Zeile(n) wird/ werden nach ausgewählten
			 * Zeilen hinzugefügt oder nach der letzten Zeile
			 */
			case "addRow":
				if(rows.length > 0)
				{
					for(int i = 0; i < rows.length; i++)
					{
						model.insertRow(rows[i] + i + 1, defaultRowdata);
					}
					gui.table.clearSelection();
					break;
				}
				else
				{
					model.addRow(defaultRowdata);
					break;
				}
				/*
				 * Zeile(n) löschen Wenn keine Zeile ausgewählt, erscheint
				 * Fehlerfenster.
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
						model.removeRow(rows[i] - i);
					}
					break;
				}
		}
		
	}
	
	public void actionButton(ActionEvent event)
	{
		// Ohne GUI können wir nichts machen
		if(gui == null)
		{
			log.error("actionButton(): Keine GUI gesetzt!");
			return;
		}
		
		switch(event.getActionCommand())
		{
			case "cancel":
				close();
				return;
			case "apply":
			case "ok":
				try
				{
					speichereStrecken();
				}
				catch(NumberFormatException e)
				{
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
	
	public void ladeStrecken()
	{
		assert editorDaten != null;
		
		DefaultTableModel model = new DefaultTableModel(StreckenEditorGUI.columnNames, 0) {
			@Override
			public Class<?> getColumnClass(int column)
			{
				switch(column)
				{
					case 0:
						return Double.class;
					case 1:
						return String.class;
					default:
						return Object.class;
				}
			}
		};
		
		Streckenabschnitt streckenabschnitt = editorDaten.getStreckenabschnitt();
		
		if(streckenabschnitt != null)
		{
			for(Betriebsstelle bs : streckenabschnitt.getBetriebsstellen())
			{
				double km = editorDaten.getStreckenKm(bs);
				String name = bs.getName();
				Object[] row = {km, name};
				
				model.addRow(row);
			}
		}
		gui.table.setModel(model);
	}
	
	public void speichereStrecken() throws NullPointerException
	{
		log.info("Speichere Strecken");
		
		TableModel model = gui.table.getModel();
		FirstLastList<Betriebsstelle> betriebsstellen = new FirstLastLinkedList<>();
		
		for(int i = 0; i < model.getRowCount(); i++)
		{
			String stName = model.getValueAt(i, 1).toString();
			Betriebsstelle betriebsstelle = new Betriebsstelle(stName);
			
			// Ortsangabe
			String km = model.getValueAt(i, 0).toString();
			km = km.replace(',', '.').substring(0, km.indexOf('.') + 2);
			if(isEmpty(km))
			{
				throw new NullPointerException("Keine Km-Angabe für Station " + stName);
			}
			
			Gleis gleis = new Gleis(stName, Double.parseDouble(km));
			Gleisabschnitt gleisabschnitt = gleis.getGleisabschnitte().first();
			
			betriebsstelle.addGleis(gleis);
			betriebsstellen.add(betriebsstelle);
		}
		
		betriebsstellen.sort((Betriebsstelle a, Betriebsstelle b) -> a.compareByKM(b));
		
		String strName = makeName(betriebsstellen.first(), betriebsstellen.last());
		
		// Strecken erzeugen
		Streckenabschnitt streckenabschnitt = new Streckenabschnitt(strName);
		for(int i = 0; i <= betriebsstellen.size() - 2; i++)
		{
			Betriebsstelle anfang = betriebsstellen.get(i);
			Betriebsstelle ende = betriebsstellen.get(i + 1);
			streckenabschnitt.addStrecke(new Strecke(makeName(anfang, ende), anfang, ende));
		}
		editorDaten.ladeStreckenabschnitt(streckenabschnitt);
	}
	
	protected void close()
	{
		gui.close();
		gui = null;
		onClose.run();
	}
	
	private static String makeName(Betriebsstelle anfang, Betriebsstelle ende)
	{
		assert anfang != null;
		assert ende != null;
		
		// Anfang - Ende
		return anfang.getName() + " - " + ende.getName();
	}
	
	private static boolean isEmpty(String str)
	{
		return str == null || str.isEmpty();
	}
}
