package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung;

import javax.swing.JComboBox;

public class JLineTypeComboBox extends JComboBox<LineType>
{
	public JLineTypeComboBox()
	{
		super(LineType.values());
		setRenderer(new LineRenderer());
		setEditable(false);
		setSelectedItem(0);
	}
	
	public JLineTypeComboBox(int selectedItem)
	{
		this();
		setSelectedItem(selectedItem);
	}
}
