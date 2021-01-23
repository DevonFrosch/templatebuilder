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
 * Alle Funktionalit�ten f�r die GUI {@link StreckenEditorGUI} sind in dieser Klasse definiert. <br>
 * {@link #tableButtonAction(ActionEvent)} definiert alle Funktionen zu den Button f�r die Tabelle. <br>
 * {@link #actionButton(ActionEvent)} definiert alle Funktionen zu den Button f�r die GUI selbst. <br>
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
	 * Liest den Event vom Button f�r die Tabelle aus und f�hrt dann die jeweilige Aktion aus: <br><br>
	 * 
	 * <code>moveUpRow</code> verschiebt die ausgew�hlte(n) Zeile(n) um eine Zeile nach oben. <br>
	 * <code>moveDownRow</code> verschiebt die ausgew�hlte(n) Zeile(n) um eine Zeile nach unten. <br>
	 * <code>addRow</code> f�gt hinter jeder markierten Zeile eine neue Zeile hinzu. <br>
	 * <code>removeRow</code> l�scht alle markierten Zeilen.
	 * @param event Event vom gedr�ckten Button.
	 */
	public void tableButtonAction(ActionEvent event)
	{
		// Ohne GUI k�nnen wir nichts machen
		if(gui == null)
		{
			log.error("actionButton(): Keine GUI gesetzt!");
			return;
		}
		
		// Tabellenmodell wird ausgelesen
		DefaultTableModel model = (DefaultTableModel) gui.table.getModel();
		
		// Selectionmodell wird ausgelesen
		ListSelectionModel selectionModel = gui.table.getSelectionModel();
		
		// Erhalte ausgew�hlte Zeilen
		int[] rows = gui.table.getSelectedRows();
		Object[] defaultRowdata = {20, 100, ""};
		
		switch(event.getActionCommand())
		{
			/*
			 * Zeile(n) hinzuf�gen 
			 * Zeile(n) wird/ werden nach ausgew�hlten Zeilen hinzugef�gt oder nach der letzten Zeile
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
			
			//TODO BT013 Funktion f�r das Sortieren der Tabelle einbinden
			
			/*
			 * Zeile(n) l�schen 
			 * Wenn keine Zeile ausgew�hlt, erscheint Fehlerfenster.
			 */
			case "removeRow":
				if(rows.length < 0)
				{
					gui.errorMessage("Zeile ausw�hlen, die gel�scht werden soll", "Zeile l�schen fehlgeschlagen!");
					break;
				}
				else
				{
					for(int i = 0; i < rows.length; i++)
					{
						if(gui.table.getValueAt(rows[i], 0).toString() == "0") {
							model.removeRow(rows[i] - i);
						} else {
							gui.warningMessage(gui.table.getValueAt(rows[i], 2).toString() + " hat noch " + gui.table.getValueAt(rows[i], 0).toString() + " Z�ge, die hier halten! \n Um diese Betriebsstelle l�schen zu k�nnen, darf kein Zug in dieser Betriebsstelle ein Fahrplaneintrag haben.");
						}
					}
					break;
				}
		}
		
	}
	
	/**
	 * Event zu den Fensterbutton <br><br>
	 * 
	 * <code>cancel</code> schlie�t das Fenster. <br>
	 * <code>save</code> f�hrt {@link #speichereStrecken()} aus und schlie�t das Fenster anschlie�end.
	 * @param event
	 */
	public void actionButton(ActionEvent event)
	{
		// Ohne GUI k�nnen wir nichts machen
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
				
				// OK: Fenster schlie�en
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
	 * Beim �ffnen der GUI wird die Strecke aus den editorDaten geladen und in der Tabelle eingef�gt.
	 */
	public void ladeStrecken()
	{
		assert editorDaten != null;
		
		//Erstelle model f�r Tabelle mit fester Datentyp f�r die einzelnen Spalten
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
				throw new NullPointerException("Keine Km-Angabe f�r Station " + stName);
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
	 * Schlie�t das Streckeneditorfenster
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
	 * Pr�ft, ob es String str leer ist.
	 * @param str Der String, der gepr�ft werden soll.
	 * @return boolean - True, wenn String leer ist.
	 */
	private static boolean isEmpty(String str)
	{
		return str == null || str.isEmpty();
	}
}
