package de.stsFanGruppe.templatebuilder.editor.fahrtEditor;

import java.util.Collection;
import java.util.HashMap;
import java.util.OptionalDouble;

import javax.swing.event.TableModelEvent;

import de.stsFanGruppe.templatebuilder.editor.EditorDaten;
import de.stsFanGruppe.templatebuilder.editor.bildfahrplan.BildfahrplanGUIController;
import de.stsFanGruppe.templatebuilder.zug.Fahrplanhalt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.TimeFormater;

public class FahrtEditorGUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FahrtEditorGUIController.class);
	
	private static HashMap<EditorDaten, FahrtEditorGUIController> instance = new HashMap<>();
	
	protected EditorDaten editorDaten;
	protected FahrtEditorGUI gui;
	private Object fahrtenGeladenCallbackId;
	private Object editorDatenCloseCallbackId;
	private Fahrt aktuelleFahrt;
	
	protected FahrtEditorGUIController(EditorDaten editorDaten)
	{
		instance.put(editorDaten, this);
		NullTester.test(editorDaten);
		this.editorDaten = editorDaten;
		
		this.gui = new FahrtEditorGUI(this, "Fahrplaneditor: " + editorDaten.getName());
		
		initFahrtNamen();
		fahrtenGeladenCallbackId = editorDaten.registerFahrtenGeladenCallback(this::initFahrtNamen);
		editorDatenCloseCallbackId = editorDaten.registerCloseCallback(this::close);
	}
	
	protected FahrtEditorGUIController(EditorDaten editorDaten, Fahrt fahrt)
	{
		this(editorDaten);
		gui.comboBoxZugname.setSelectedItem(fahrt.getName());
		ladeFahrplan(fahrt);
	}

	/**
	 * �ffnet den Dialog ohne Fahrt.
	 * Falls der Dialog schon offen ist, setzt ihn in den Fokus und entfernt die Fahrt.
	 * 
	 * F�r den Aufruf �ber das Men�.
	 */
	public static void openOrFocus(EditorDaten editorDaten)
	{
		if(!instance.containsKey(editorDaten))
		{
			new FahrtEditorGUIController(editorDaten);
		}
		instance.get(editorDaten).ladeFahrplan(null);
		instance.get(editorDaten).gui.requestFocus();
	}
	
	/**
	 * �ffnet den Dialog mit der gew�hlten Fahrt.
	 * Falls der Dialog schon offen ist, setzt ihn in den Fokus und �berschreibt die Fahrt.
	 * 
	 * F�r den Aufruf �ber Doppelklick auf einen Fahrtabschnitt.
	 */
	public static void openOrFocus(EditorDaten editorDaten, Fahrt fahrt)
	{
		if(!instance.containsKey(editorDaten))
		{
			new FahrtEditorGUIController(editorDaten);
		}
		instance.get(editorDaten).ladeFahrplan(fahrt);
		instance.get(editorDaten).gui.requestFocus();
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
			
			// TODO: Entscheiden, ob Ank�nfte am ersten und Abfahrten am letzten Halt angezeigt werden sollen
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
						}
						catch(NumberFormatException e)
						{
							log.error("tableChanged: Kein g�ltiges Zeitformat in Ankunft, Wert war '{}'", value);
							gui.errorMessage("Die Abfahrt in Zeile " + (localRow+1) + " ist keine g�ltige Zeit!");
							return fahrt;
						}
						catch(IllegalArgumentException e)
						{
							log.error("tableChanged: Kann an nicht �ndern", e);
							gui.errorMessage("Die Ankunft in Zeile " + (localRow+1) + " ist nicht g�ltig! " + e.getMessage());
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
						}
						catch(NumberFormatException e)
						{
							log.error("tableChanged: Kein g�ltiges Zeitformat in Abfahrt, Wert war '{}'", value);
							gui.errorMessage("Die Abfahrt in Zeile " + (localRow+1) + " ist keine g�ltige Zeit!");
							return fahrt;
						}
						catch(IllegalArgumentException e)
						{
							log.error("tableChanged: Kann ab nicht �ndern", e);
							gui.errorMessage("Die Abfahrt in Zeile " + (localRow+1) + " ist nicht g�ltig! " + e.getMessage());
							return fahrt;
						}
						break;
					default:
						log.error("tableChanged: Column {} darf nicht ge�ndert werden", column);
				}
				return fahrt;
			});
		}
	}
	
	public synchronized void comboBoxSelectionChanged()
	{
		ladeFahrplan(editorDaten.getFahrt(gui.getSelectedFahrt()));
	}
	
	public void jumpToZug()
	{
		if(aktuelleFahrt == null || editorDaten == null)
		{
			return;
		}
		
		double ab = aktuelleFahrt.getFahrplanhalte().first().getAbfahrt().orElse(-1.0);
		double an = aktuelleFahrt.getFahrplanhalte().last().getAnkunft().orElse(-1.0);
		
		if(an < 0.0 || ab < 0.0)
		{
			log.warn("jumpToZug: Fahrzeiten ung�ltig, an={}, ab={}", an, ab);
			return;
		}
		
		BildfahrplanGUIController bfp = editorDaten.getBildfahrplan();
		
		if(bfp == null)
		{
			log.info("jumpToZug: Kein Bildfahrplan offen");
			return;
		}
		
		bfp.jumpToZug(ab, an);
		
		bfp.highlightFahrt(aktuelleFahrt);
	}
	
	protected void close()
	{
		editorDaten.unregisterFahrtenGeladenCallback(fahrtenGeladenCallbackId);
		editorDaten.unregisterCloseCallback(editorDatenCloseCallbackId);
		gui.close();
		gui = null;
		instance.remove(editorDaten);
	}
}
