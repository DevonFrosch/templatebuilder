package de.stsFanGruppe.templatebuilder.editor.streckenEditor;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.NavigableSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import de.stsFanGruppe.templatebuilder.editor.EditorDaten;
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Gleis;
import de.stsFanGruppe.templatebuilder.strecken.Gleisabschnitt;
import de.stsFanGruppe.templatebuilder.strecken.Strecke;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrplanhalt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.FirstLastList;
import de.stsFanGruppe.tools.NullTester;

/**
 * Alle Funktionalitäten für die GUI {@link StreckenEditorGUI} sind in dieser Klasse definiert. <br>
 * {@link #tableButtonAction(ActionEvent)} definiert alle Funktionen zu den Button für die Tabelle. <br>
 * {@link #actionButton(ActionEvent)} definiert alle Funktionen zu den Button für die GUI selbst. <br>
 *
 */
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
	 * Liest den Event vom Button für die Tabelle aus und führt dann die jeweilige Aktion aus: <br><br>
	 * 
	 * <code>moveUpRow</code> verschiebt die ausgewählte(n) Zeile(n) um eine Zeile nach oben. <br>
	 * <code>moveDownRow</code> verschiebt die ausgewählte(n) Zeile(n) um eine Zeile nach unten. <br>
	 * <code>addRow</code> fügt hinter jeder markierten Zeile eine neue Zeile hinzu. <br>
	 * <code>removeRow</code> löscht alle markierten Zeilen.
	 * @param event Event vom gedrückten Button.
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
		Object[] defaultRowdata = {20, 100, ""};
		
		switch(event.getActionCommand())
		{
			/*
			 * Zeile(n) hinzufügen 
			 * Zeile(n) wird/ werden nach ausgewählten Zeilen hinzugefügt oder nach der letzten Zeile
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
			 * Tabelle sortieren
			 * Sortiert die Tabelle nach km.
			 */
			
			//TODO BT013 Funktion für das Sortieren der Tabelle einbinden
			
			/*
			 * Zeile(n) löschen 
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
						if(gui.table.getValueAt(rows[i], 0).toString() == "0") {
							model.removeRow(rows[i] - i);
						} else {
							gui.warningMessage(gui.table.getValueAt(rows[i], 2).toString() + " hat noch " + gui.table.getValueAt(rows[i], 0).toString() + " Züge, die hier halten! \n Um diese Betriebsstelle löschen zu können, darf kein Zug in dieser Betriebsstelle ein Fahrplaneintrag haben.");
						}
					}
					break;
				}
		}
		
	}
	
	/**
	 * Event zu den Fensterbutton <br><br>
	 * 
	 * <code>cancel</code> schließt das Fenster. <br>
	 * <code>save</code> führt {@link #speichereStrecken()} aus und schließt das Fenster anschließend.
	 * @param event
	 */
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
			case "save":
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
				if(event.getActionCommand() == "save")
				{
					close();
					return;
				}
				break;
			default:
				log.error("actionButton: actionCommand nicht erkannt: {}", event.getActionCommand());
		}
	}
	
	/**
	 * Beim Öffnen der GUI wird die Strecke aus den editorDaten geladen und in der Tabelle eingefügt.
	 */
	public void ladeStrecken()
	{
		assert editorDaten != null;
		
		//Erstelle model für Tabelle mit fester Datentyp für die einzelnen Spalten
		DefaultTableModel model = new DefaultTableModel(gui.columnNames, 0) {
			@Override
			public Class<?> getColumnClass(int column)
			{
				switch(column)
				{
					case 0:
						return Long.class;
					case 1:
						return Double.class;
					case 2:
						return String.class;
					default:
						return Object.class;
				}
			}
		};
		
		Streckenabschnitt streckenabschnitt = editorDaten.getStreckenabschnitt();
		
		if(streckenabschnitt != null)
		{
			int i = 0;
			for(Betriebsstelle bs : streckenabschnitt.getBetriebsstellen())
			{	
				long anzahlZuegeProBetriebstelle = 0;
				for(Fahrt fahrt : editorDaten.getFahrten())
				{
					anzahlZuegeProBetriebstelle += fahrt.getFahrplanhalte().stream().filter(fph -> fph.getBetriebsstelle() == bs).count();
				}
				
				double km = editorDaten.getStreckenKm(bs);
				String name = bs.getName();
				Object[] row = {anzahlZuegeProBetriebstelle, km, name};
				i++;
				
				model.addRow(row);
			}
		}
		gui.table.setModel(model);
	}
	
	
	/**
	 * Speichert die Betriebsstellen in die Streckenabschnitte und diese wird dann sortiert nach km in den {@link EditorDaten} gespeichert.
	 * @throws NullPointerException
	 */
	public void speichereStrecken() throws NullPointerException
	{
		log.info("Speichere Strecken");
		
		TableModel model = gui.table.getModel();
		FirstLastList<Betriebsstelle> betriebsstellen = new FirstLastLinkedList<>();
		
		for(int i = 0; i < model.getRowCount(); i++)
		{
			String stName = model.getValueAt(i, 1).toString();
			Betriebsstelle betriebsstelle = new Betriebsstelle(stName);
			log.info(model.getValueAt(i, 1).toString() + " - " + model.getValueAt(i, 0).toString());
			
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
		editorDaten.getBildfahrplan().configChanged();
		
	}
	
	/**
	 * Schließt das Streckeneditorfenster
	 */
	protected void close()
	{
		gui.close();
		gui = null;
		onClose.run();
	}
	
	/**
	 * Erstellt einen Namen von der ersten Betriebstellle zur letzten Betriebsstelle
	 * @param anfang Erste Betriebstelle
	 * @param ende Letzte Beriebstelle
	 * @return Der Name der ersten Betriebsstelle und letzten Betriebsstelle werden mit einen Minus "-" hintereinander geschrieben.
	 */
	private static String makeName(Betriebsstelle anfang, Betriebsstelle ende)
	{
		assert anfang != null;
		assert ende != null;
		
		// Anfang - Ende
		return anfang.getName() + " - " + ende.getName();
	}
	
	/**
	 * Prüft, ob es String str leer ist.
	 * @param str Der String, der geprüft werden soll.
	 * @return boolean - True, wenn String leer ist.
	 */
	private static boolean isEmpty(String str)
	{
		return str == null || str.isEmpty();
	}
}
