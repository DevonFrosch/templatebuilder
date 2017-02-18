package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.table;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class DoNothingCellEditor extends DefaultCellEditor
{

	public DoNothingCellEditor(JCheckBox arg0)
	{
		super(arg0);
	}

	public DoNothingCellEditor(JComboBox arg0)
	{
		super(arg0);
	}

	public DoNothingCellEditor(JTextField arg0)
	{
		super(arg0);
	}
	
}
