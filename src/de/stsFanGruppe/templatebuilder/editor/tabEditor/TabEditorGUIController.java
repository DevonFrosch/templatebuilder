package de.stsFanGruppe.templatebuilder.editor.tabEditor;

import java.util.HashMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.editor.EditorDaten;
import de.stsFanGruppe.templatebuilder.editor.EditorGUIController;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrplanhalt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.FirstLastList;
import de.stsFanGruppe.tools.TimeFormater;

public class TabEditorGUIController extends EditorGUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TabEditorGUIController.class);
	
	protected TabEditorGUI gui;
	protected TabEditorZeilenheaderGUI zeilenGui;
	protected FirstLastLinkedList<TabEditorTabellenZeile> zeilen = new FirstLastLinkedList<>();
	
	protected boolean richtungAufsteigend;
	
	public TabEditorGUIController(EditorDaten daten, BildfahrplanConfig config, TemplateBuilderGUI parent, boolean richtungAufsteigend)
	{
		super(daten, config, parent);
		initVariables(config, parent, richtungAufsteigend);
	}
	public TabEditorGUIController(BildfahrplanConfig config, TemplateBuilderGUI parent, boolean richtungAufsteigend)
	{
		super(config, parent);
		initVariables(config, parent, richtungAufsteigend);
	}
	private void initVariables(BildfahrplanConfig config, TemplateBuilderGUI parent, boolean richtungAufsteigend)
	{
		super.getEditorDaten().setTabEditor(this, richtungAufsteigend);
		this.gui = new TabEditorGUI(this, parent);
		this.zeilenGui = new TabEditorZeilenheaderGUI(this);
		editorDaten.addFahrtenGeladenCallback(() -> ladeFahrten());
		this.richtungAufsteigend = richtungAufsteigend;
		
		ladeFahrten();
	}
	
	public void ladeFahrten()
	{
		Streckenabschnitt str = editorDaten.getStreckenabschnitt();
		Set<Fahrt> unsortiert = editorDaten.getFahrtenCopy();
		
		if(str == null || !str.hasStrecken() || unsortiert == null)
		{
			return;
		}
		
		NavigableSet<Fahrt> fahrten = new TreeSet<>(unsortiert.stream().filter(f -> f.hasAbschnitteRichtung(richtungAufsteigend)).collect(Collectors.toSet()));
		FirstLastList<Betriebsstelle> betriebsstellen = str.getBetriebsstellen();
		
		FirstLastLinkedList<String> bsNamen = new FirstLastLinkedList<>();
		HashMap<String, Integer> bsIndexAn = new HashMap<>();
		HashMap<String, Integer> bsIndexAb = new HashMap<>();
		
		int anzahlBs = betriebsstellen.size();
		if(anzahlBs < 2)
		{
			log.warn("Zu wenig Betriebsstellen ({})!", anzahlBs);
			return;
		}
		
		// Erster Zug
		{
			Betriebsstelle bs = betriebsstellen.first();
			
			bsNamen.add(bs.getName()+" (ab)");
			bsIndexAb.put(bs.getName(), 0);
		}
		
		
		for(int i=1; i < anzahlBs-1; i++)
		{
			Betriebsstelle bs = betriebsstellen.get(i);

			bsNamen.add(bs.getName()+" (an)");
			bsNamen.add(bs.getName()+" (ab)");
			bsIndexAn.put(bs.getName(), i*2-1);
			bsIndexAb.put(bs.getName(), i*2);
		}
		
		// Letzter Zug
		{
			Betriebsstelle bs = betriebsstellen.last();
			
			bsNamen.add(bs.getName()+" (an)");
			bsIndexAn.put(bs.getName(), (anzahlBs-1)*2-1);
		}
		
		zeilenGui.setRows(bsNamen.toArray(new String[bsNamen.size()]));
		
		String[] fahrtNamen = fahrten.stream().map(f -> f.getName()).toArray(String[]::new);
		HashMap<String, Integer> fahrtIndex = new HashMap<>();
		for(int j=0; j < fahrtNamen.length; j++)
		{
			fahrtIndex.put(fahrtNamen[j], j);
		}
		
		gui.setTableModel(new DefaultTableModel(fahrtNamen, bsNamen.size()));
		
		for(Fahrt fahrt: fahrten)
		{
			String fahrtName = fahrt.getName();
			NavigableSet<Fahrplanhalt> halte = fahrt.getFahrplanhalte();
			
			double letzterKm = -1;
			
			if(halte == null || halte.size() < 1)
			{
				break;
			}
			
			for(Fahrplanhalt fh: halte)
			{
				String b = fh.getGleisabschnitt().getParent().getParent().getName();
				
				// Ankünfte außerhalb der Tabelle ignorieren
				if(fh.getAnkunft().isPresent() && bsIndexAn.get(b) != null)
				{
					String an = TimeFormater.doubleToString(fh.getAnkunft().getAsDouble());
					gui.setTableValueAt(an, bsIndexAn.get(b), fahrtIndex.get(fahrtName));
				}
				if(fh.getAbfahrt().isPresent() && bsIndexAb.get(b) != null)
				{
					String ab = TimeFormater.doubleToString(fh.getAbfahrt().getAsDouble());
					gui.setTableValueAt(ab, bsIndexAb.get(b), fahrtIndex.get(fahrtName));
				}
			}
		}
	}
	
	public TabEditorGUI getTabEditorGUI()
	{
		return this.gui;
	}
	public TabEditorZeilenheaderGUI getZeilenheaderGUI()
	{
		return this.zeilenGui;
	}
	public JTableHeader getTableHeader()
	{
		return this.gui.getTableHeader();
	}
	
	public int getColumnCount()
	{
		return 0;
	}
	public String getColumnName(int index)
	{
		return "";
	}
	public void getColumn(int index)
	{
		
	}
	public void addColumn()
	{
		
	}
	public void insertColumn(int index)
	{
		
	}
	public void removeColumn(int index)
	{
		
	}
	public void removeAllColumns()
	{
		
	}
	
	public int getRowCount()
	{
		return zeilen.size();
	}
	public String getRowName(int index)
	{
		return zeilen.get(index).getName();
	}
	public Betriebsstelle getRowBetriebsstelle(int index)
	{
		return zeilen.get(index).getBetriebsstelle();
	}
	public void addRow(Betriebsstelle bs, boolean isAnkunft)
	{
		new TabEditorTabellenZeile(bs, richtungAufsteigend, isAnkunft);
	}
	public void insertRow(int index)
	{
		
	}
	public void removeRow(int index)
	{
		
	}
	public void removeAllRows()
	{
		
	}
	
	public void configChanged()
	{
		// TODO Auto-generated method stub
		
	}
}
