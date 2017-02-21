package de.stsFanGruppe.templatebuilder.editor.tabEditor;

import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.editor.EditorGUIController;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.tools.FirstLastLinkedList;

public class TabEditorGUIController extends EditorGUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TabEditorGUIController.class);
	
	protected TabEditorGUI gui;
	protected FirstLastLinkedList<TabEditorTabellenZeile> zeilen = new FirstLastLinkedList<>();
	
	protected boolean richtungAufsteigend;
	
	public TabEditorGUIController(BildfahrplanConfig config, TemplateBuilderGUI parent, boolean richtungAufsteigend)
	{
		super(config, parent);
		
		this.gui = new TabEditorGUI(this, parent);
		this.richtungAufsteigend = richtungAufsteigend;
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
