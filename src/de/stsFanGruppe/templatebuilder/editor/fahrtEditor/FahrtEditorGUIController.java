package de.stsFanGruppe.templatebuilder.editor.fahrtEditor;

import java.awt.event.ActionEvent;
import java.util.NavigableSet;
import javax.swing.table.DefaultTableModel;
import de.stsFanGruppe.templatebuilder.editor.EditorDaten;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
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
	
	protected boolean ignoreNextComboBoxUpdate = false;
	
	public FahrtEditorGUIController(EditorDaten editorDaten, Runnable onClose)
	{
		NullTester.test(editorDaten);
		NullTester.test(onClose);
		this.editorDaten = editorDaten;
		this.onClose = onClose;
		
		this.gui = new FahrtEditorGUI(this, editorDaten.getName());
		
		initFahrtNamen();
		editorDaten.registerFahrtenGeladenCallback(this::initFahrtNamen);
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
		
		Streckenabschnitt strecke = editorDaten.getStreckenabschnitt();
		NavigableSet<Fahrplanhalt> halte = fahrt.getFahrplanhalte();
		
		if(halte.size() < 2)
		{
			log.warn("ladeFahrplan: Zu wenig Fahrplanhalte: {}", halte.size());
			return;
		}
		
		String[][] inhalte = new String[(halte.size()-1)*2][2];
		
		int j = 0;
		{
			Fahrplanhalt halt = halte.first();
			inhalte[j][0] = halt.getBetriebsstelle().getName() + " (ab)";
			inhalte[j][1] = TimeFormater.optionalDoubleToString(halt.getAbfahrt());
			j++;
		}
		
		for(Fahrplanhalt halt : halte)
		{
			if(halt == halte.first() || halt == halte.last())
			{
				continue;
			}
			
			String haltName = halt.getBetriebsstelle().getName();
			inhalte[j][0] =  haltName + " (an)";
			inhalte[j][1] = TimeFormater.optionalDoubleToString(halt.getAnkunft());
			j++;
			inhalte[j][0] =  haltName + " (ab)";
			inhalte[j][1] = TimeFormater.optionalDoubleToString(halt.getAbfahrt ());
			j++;
		}
		{
			Fahrplanhalt halt = halte.last();

			inhalte[j][0] = halt.getBetriebsstelle().getName() + " (an)";
			inhalte[j][1] = TimeFormater.optionalDoubleToString(halt.getAnkunft());
		}
		
		DefaultTableModel model = new DefaultTableModel(inhalte, gui.SPALTEN_ÜBERSCHRIFTEN);
		
		gui.setTableModel(model);
	}
	
	public synchronized void comboBoxSelectionChanged(ActionEvent event)
	{
		log.debug("comboBoxChanged()");
		
		ladeFahrplan(editorDaten.getFahrt(gui.getSelectedFahrt()));
	}
	
	protected void close()
	{
		gui.close();
		gui = null;
		onClose.run();
	}
}
