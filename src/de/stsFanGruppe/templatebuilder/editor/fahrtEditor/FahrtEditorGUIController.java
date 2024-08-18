package de.stsFanGruppe.templatebuilder.editor.fahrtEditor;

import java.awt.event.ActionEvent;
import java.util.Collection;

import javax.swing.event.TableModelEvent;

import de.stsFanGruppe.templatebuilder.editor.EditorDaten;
import de.stsFanGruppe.templatebuilder.zug.Fahrplanhalt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.TimeFormater;

public class FahrtEditorGUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FahrtEditorGUIController.class);
	
	protected EditorDaten editorDaten;
	protected FahrtEditorGUI gui;
	private Runnable onClose;
	private Object fahrtenGeladenCallbackId;
	private Fahrt aktuelleFahrt;
	
	public FahrtEditorGUIController(EditorDaten editorDaten, Runnable onClose)
	{
		NullTester.test(editorDaten);
		NullTester.test(onClose);
		this.editorDaten = editorDaten;
		this.onClose = onClose;
		
		this.gui = new FahrtEditorGUI(this, editorDaten.getName());
		
		initFahrtNamen();
		fahrtenGeladenCallbackId = editorDaten.registerFahrtenGeladenCallback(this::initFahrtNamen);
	}
	
	/**
	 * Für den Aufruf über Doppelklick auf einen Fahrtabschnitt. Hier soll die ausgewählte Fahrt direkt geladen werden.
	 * @param editorDaten
	 * @param onClose
	 * @param fahrt
	 */
	public FahrtEditorGUIController(EditorDaten editorDaten, Runnable onClose, Fahrt fahrt)
	{
		NullTester.test(editorDaten);
		NullTester.test(onClose);
		this.editorDaten = editorDaten;
		this.onClose = onClose;
		
		this.gui = new FahrtEditorGUI(this, editorDaten.getName());
		
		initFahrtNamen();
		fahrtenGeladenCallbackId = editorDaten.registerFahrtenGeladenCallback(this::initFahrtNamen);
		gui.comboBoxZugname.setSelectedItem(fahrt.getName());
		ladeFahrplan(fahrt);
	}
	
	public void initFahrtNamen()
	{
		gui.setComboBoxItems(editorDaten.getFahrten().stream().map(Fahrt::getName).toArray(String[]::new));
	}
	
	public void ladeFahrplan(Fahrt fahrt)
	{
		if(fahrt == null)
		{
			return;
		}
		aktuelleFahrt = fahrt;
		
		Collection<Fahrplanhalt> halte = fahrt.getFahrplanhalte();
		
		String[][] inhalte = new String[halte.size()][3];
		
		int j = 0;
		for(Fahrplanhalt halt : halte)
		{
			String an = TimeFormater.optionalDoubleToString(halt.getAnkunft());
			String ab = TimeFormater.optionalDoubleToString(halt.getAbfahrt());
			
			// TODO: Entscheiden, ob Ankünfte am ersten und Abfahrten am letzten Halt angezeigt werden sollen
			// Zur Entwicklung ist es sinnvoller, alles anzuzeigen
			/*
			if(j == 0 && halte.size() >= 2)
			{
				an = "";
			}
			if(j == halte.size() - 1 && halte.size() >= 2)
			{
				ab = "";
			}
			*/
			
			inhalte[j][0] = halt.getBetriebsstelle().getName();
			inhalte[j][1] = an;
			inhalte[j][2] = ab;
			j++;
		}
		
		gui.updateSelectedFahrt(fahrt, inhalte, (event) -> tableChanged(event));
	}
	
	protected void tableChanged(TableModelEvent event)
	{
		if(aktuelleFahrt == null)
		{
			return;
		}
		int column = event.getColumn();
		
		for(int row = event.getFirstRow(); row <= event.getLastRow(); row++)
		{
			String value = gui.getTableValueAt(row, column);
			
			// TODO: editorDaten aktualisieren und alle Ansichten neu laden
		}
	}
	
	public synchronized void comboBoxSelectionChanged(ActionEvent event)
	{
		log.debug("comboBoxChanged()");
		
		ladeFahrplan(editorDaten.getFahrt(gui.getSelectedFahrt()));
	}
	
	protected void close()
	{
		editorDaten.unregisterFahrtenGeladenCallback(fahrtenGeladenCallbackId);
		gui.close();
		gui = null;
		onClose.run();
	}
}
