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

public class TemplateBuilderTabbedPane extends JTabbedPane
{
	public TemplateBuilderTabbedPane()
	{
		super();
	}
	public TemplateBuilderTabbedPane(int tabLayoutPolicy)
	{
		super(tabLayoutPolicy);
	}
	public TemplateBuilderTabbedPane(int tabPlacement, int tabLayoutPolicy)
	{
		super(tabPlacement, tabLayoutPolicy);
	}
	
	public int addTab(String name, Icon icon, String toolTip, Component view, Component columnHeader, Component rowHeader)
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
		
		boolean keinName = (name == null);
		if(keinName)
		{
			name = "Neuer Fahrplan";
		}
		
		int tabIndex = 0;
		synchronized(this)
		{
			addTab(name, icon, scrollPane, toolTip);
			tabIndex = getTabCount() - 1;
			setTabComponentAt(tabIndex, new ButtonTabComponent(this, (int index) -> {return true;}));
			
			if(keinName)
			{
				setTitleAt(tabIndex, "Neuer Fahrplan ("+tabIndex+")");
			}
		}
		return tabIndex;
	}
	public Component getSelectedTab()
	{
		if(getTabCount() == 0)
		{
			return null;
		}
		
		return getComponentAt(getSelectedIndex());
	}
	
	public int addBildfahrplanTab(String name, Icon icon, String toolTip, BildfahrplanGUIController bfpController)
	{
		return addTab(name, icon, toolTip, bfpController.getBildfahrplanGUI(),
				bfpController.getBildfahrplanSpaltenHeaderGUI(), bfpController.getBildfahrplanZeilenHeaderGUI());
	}
	public int addTabEditorTab(String name, Icon icon, String toolTip, TabEditorGUIController teController)
	{
		return addTab(name, icon, toolTip, teController.getTabEditorGUI(), null, null);
	}
	
	public boolean selectedTabIsEditor()
	{
		return ClassTester.isAssignableFrom(getSelectedTab(), EditorGUI.class);
	}
	public boolean selectedTabIsBildfahrplan()
	{
		return ClassTester.isAssignableFrom(getSelectedTab(), BildfahrplanGUI.class);
	}
	public boolean selectedTabIsTabEditor()
	{
		return ClassTester.isAssignableFrom(getSelectedTab(), TabEditorGUI.class);
	}
	public boolean selectedTabIsTabEditorHin()
	{
		if(selectedTabIsTabEditor())
		{
			try
			{
				return ((TabEditorGUI) getSelectedTab()).isRichtungAufsteigend();
			}
			catch(ClassCastException e)
			{}
		}
		return false;
	}
	public boolean selectedTabIsTabEditorRück()
	{
		if(selectedTabIsTabEditor())
		{
			try
			{
				return ! ((TabEditorGUI) getSelectedTab()).isRichtungAufsteigend();
			}
			catch(ClassCastException e)
			{}
		}
		return false;
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
			
			if(com == null || !com.getClass().isAssignableFrom(EditorGUI.class))
			{
				return null;
			}
			
			EditorGUI bfpGUI = (EditorGUI) com;
			return bfpGUI.getController().getEditorDaten();
		}
		catch(ClassCastException e)
		{}
		
		return null;
	}
	
	public Component getSelectedGUI()
	{
		JScrollPane scrollPane = (JScrollPane) getSelectedTab();
		if(scrollPane == null || scrollPane.getViewport() == null)
		{
			return null;
		}
		return scrollPane.getViewport().getView();
	}
}
