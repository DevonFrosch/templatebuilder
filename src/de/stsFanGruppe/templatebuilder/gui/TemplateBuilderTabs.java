package de.stsFanGruppe.templatebuilder.gui;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import de.stsFanGruppe.templatebuilder.editor.EditorDaten;
import de.stsFanGruppe.templatebuilder.editor.EditorGUI;
import de.stsFanGruppe.templatebuilder.editor.bildfahrplan.BildfahrplanGUI;
import de.stsFanGruppe.templatebuilder.editor.bildfahrplan.BildfahrplanGUIController;
import de.stsFanGruppe.templatebuilder.editor.tabEditor.TabEditorGUI;
import de.stsFanGruppe.templatebuilder.editor.tabEditor.TabEditorGUIController;
import de.stsFanGruppe.tools.ButtonTabComponent;
import de.stsFanGruppe.tools.ClassTester;
import de.stsFanGruppe.tools.NullTester;

public class TemplateBuilderTabs
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TemplateBuilderTabs.class);
	
	protected JTabbedPane pane;
	
	public TemplateBuilderTabs(JTabbedPane pane)
	{
		NullTester.test(pane);
		this.pane = pane;
	}
	
	protected int addTab(String name, Icon icon, String toolTip, Component view, Component columnHeader, Component rowHeader)
	{
		assert view != null;
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane.setViewportView(view);
		if(columnHeader != null)
		{
			scrollPane.setColumnHeaderView(columnHeader);
		}
		if(rowHeader != null)
		{
			scrollPane.setRowHeaderView(rowHeader);
		}
		
		if(name == null)
		{
			name = "Leer";
		}
		
		int tabIndex = 0;
		synchronized(this)
		{
			pane.addTab(name, icon, scrollPane, toolTip);
			tabIndex = pane.getTabCount() - 1;
			pane.setTabComponentAt(tabIndex, new ButtonTabComponent(pane, (int index) -> {return true;}));
		}
		return tabIndex;
	}
	public int getSelectedTab()
	{
		return pane.getSelectedIndex();
	}
	public void setSelectedTab(int index)
	{
		pane.setSelectedIndex(index);
	}
	
	public Component getSelectedGUI()
	{
		return getGUIAt(getSelectedTab());
	}
	public Component getGUIAt(int index)
	{
		if(index < 0)
		{
			return null;
		}
		JScrollPane scrollPane = (JScrollPane) pane.getComponentAt(index);
		if(scrollPane == null || scrollPane.getViewport() == null)
		{
			return null;
		}
		return scrollPane.getViewport().getView();
	}
	
	public int addBildfahrplanTab(String name, Icon icon, String toolTip, BildfahrplanGUIController bfpController)
	{
		return addTab(name, icon, toolTip, bfpController.getBildfahrplanGUI(),
				bfpController.getBildfahrplanSpaltenHeaderGUI(), bfpController.getBildfahrplanZeilenHeaderGUI());
	}
	public int addTabEditorTab(String name, Icon icon, String toolTip, TabEditorGUIController teController)
	{
		return addTab(name, icon, toolTip, teController.getTabEditorGUI(), teController.getTableHeader(), teController.getZeilenheaderGUI());
	}
	
	public boolean selectedTabIsEditor()
	{
		boolean test = getSelectedGUI() instanceof EditorGUI;
		return test;
	}
	public boolean selectedTabIsBildfahrplan()
	{
		return ClassTester.isAssignableFrom(getSelectedGUI(), BildfahrplanGUI.class);
	}
	public boolean selectedTabIsTabEditor()
	{
		return ClassTester.isAssignableFrom(getSelectedGUI(), TabEditorGUI.class);
	}
	public boolean selectedTabIsTabEditorHin()
	{
		if(selectedTabIsTabEditor())
		{
			try
			{
				return ((TabEditorGUI) getSelectedGUI()).isRichtungAufsteigend();
			}
			catch(ClassCastException e)
			{
				log.warn("ClassCastException", e);
			}
		}
		return false;
	}
	public boolean selectedTabIsTabEditorRück()
	{
		if(selectedTabIsTabEditor())
		{
			try
			{
				return ! ((TabEditorGUI) getSelectedGUI()).isRichtungAufsteigend();
			}
			catch(ClassCastException e)
			{
				log.warn("ClassCastException", e);
			}
		}
		return false;
	}
	
	public int getTabIndexOf(Component gui)
	{
		for(int i=0; i < pane.getTabCount(); i++)
		{
			if(gui.equals(getGUIAt(i)))
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Holt das EditorDaten-Objekt des aktuellen Tabs.
	 * @return Das EditorDaten-Objekt oder null falls der Tab keine EditorDaten hat
	 */
	public EditorDaten getSelectedEditorDaten()
	{
		try
		{
			Component com = getSelectedGUI();
			
			if(com == null || !(com instanceof EditorGUI))
			{
				return null;
			}
			
			EditorGUI bfpGUI = (EditorGUI) com;
			return bfpGUI.getController().getEditorDaten();
		}
		catch(ClassCastException e)
		{
			log.warn("ClassCastException", e);
		}
		
		return null;
	}
}
