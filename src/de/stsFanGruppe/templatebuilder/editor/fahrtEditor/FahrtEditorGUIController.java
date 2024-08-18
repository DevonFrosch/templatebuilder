package de.stsFanGruppe.templatebuilder.editor.fahrtEditor;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.OptionalDouble;

import javax.swing.event.TableModelEvent;

import de.stsFanGruppe.templatebuilder.editor.EditorDaten;
import de.stsFanGruppe.templatebuilder.zug.Fahrplanhalt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.TimeFormater;

public class FahrtEditorGUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FahrtEditorGUIController.class);
	
	private static FahrtEditorGUIController instance;
	
	protected EditorDaten editorDaten;
	protected FahrtEditorGUI gui;
	private Object fahrtenGeladenCallbackId;
	private Fahrt aktuelleFahrt;
	
	protected FahrtEditorGUIController(EditorDaten editorDaten)
	{
		instance = this;
		NullTester.test(editorDaten);
		this.editorDaten = editorDaten;
		
		this.gui = new FahrtEditorGUI(this, editorDaten.getName());
		
		initFahrtNamen();
		fahrtenGeladenCallbackId = editorDaten.registerFahrtenGeladenCallback(this::initFahrtNamen);
	}
	
	protected FahrtEditorGUIController(EditorDaten editorDaten, Fahrt fahrt)
	{
		this(editorDaten);
		gui.comboBoxZugname.setSelectedItem(fahrt.getName());
		ladeFahrplan(fahrt);
	}

	/**
	 * Öffnet den Dialog ohne Fahrt.
	 * Falls der Dialog schon offen ist, setzt ihn in den Fokus und entfernt die Fahrt.
	 * 
	 * Für den Aufruf über das Menü.
	 */
	public static void openOrFocus(EditorDaten editorDaten)
	{
		if(instance == null)
		{
			new FahrtEditorGUIController(editorDaten);
		}
		instance.ladeFahrplan(null);
		instance.gui.requestFocus();
	}
	
	/**
	 * Öffnet den Dialog mit der gewählten Fahrt.
	 * Falls der Dialog schon offen ist, setzt ihn in den Fokus und überschreibt die Fahrt.
	 * 
	 * Für den Aufruf über Doppelklick auf einen Fahrtabschnitt.
	 */
	public static void openOrFocus(EditorDaten editorDaten, Fahrt fahrt)
	{
		if(instance == null)
		{
			new FahrtEditorGUIController(editorDaten);
		}
		instance.ladeFahrplan(fahrt);
		instance.gui.requestFocus();
	}
	
	public void initFahrtNamen()
	{
		gui.setComboBoxItems(editorDaten.getFahrten().stream().map(Fahrt::getName).toArray(String[]::new));
	}
	
	public void ladeFahrplan(Fahrt fahrt)
	{
		if(fahrt == null)
		{
			gui.clearFahrt();
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
			// Wert kopieren wegen Lambda-Funktion...
			int localRow = row;
			
			editorDaten.updateFahrt(aktuelleFahrt.getFahrtId(), (fahrt) -> {
				int anzahlHalte = fahrt.getFahrplanhalte().size();
				Fahrplanhalt halt = fahrt.getFahrplanhalt(localRow);
				switch(column)
				{
					case 1:
						try
						{
							OptionalDouble zeit = TimeFormater.stringToOptionalDouble(value);
							
							if(localRow > 0 && !zeit.isPresent())
							{
								gui.errorMessage("Die Abfahrt in Zeile " + (localRow+1) + " darf nicht leer sein!");
								return fahrt;
							}
							
							halt.setAnkunft(zeit);
							log.debug("Setze Ankunft auf {}", value);
						}
						catch(NumberFormatException e)
						{
							log.error("tableChanged: Kein gültiges Zeitformat in Ankunft, Wert war '{}'", value);
							gui.errorMessage("Die Abfahrt in Zeile " + (localRow+1) + " ist keine gültige Zeit!");
							return fahrt;
						}
						catch(IllegalArgumentException e)
						{
							log.error("tableChanged: Kann an nicht ändern", e);
							gui.errorMessage("Die Ankunft in Zeile " + (localRow+1) + " ist nicht gültig! " + e.getMessage());
							return fahrt;
						}
						break;
					case 2:
						try
						{
							OptionalDouble zeit = TimeFormater.stringToOptionalDouble(value);
							
							if(localRow < anzahlHalte - 1 && !zeit.isPresent())
							{
								gui.errorMessage("Die Abfahrt in Zeile " + (localRow+1) + " darf nicht leer sein!");
								return fahrt;
							}
							
							halt.setAbfahrt(zeit);
							log.debug("Setze Abfahrt auf {}", value);
						}
						catch(NumberFormatException e)
						{
							log.error("tableChanged: Kein gültiges Zeitformat in Abfahrt, Wert war '{}'", value);
							gui.errorMessage("Die Abfahrt in Zeile " + (localRow+1) + " ist keine gültige Zeit!");
							return fahrt;
						}
						catch(IllegalArgumentException e)
						{
							log.error("tableChanged: Kann ab nicht ändern", e);
							gui.errorMessage("Die Abfahrt in Zeile " + (localRow+1) + " ist nicht gültig! " + e.getMessage());
							return fahrt;
						}
						break;
					default:
						log.error("tableChanged: Column {} darf nicht geändert werden", column);
				}
				return fahrt;
			});
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
		instance = null;
	}
}
