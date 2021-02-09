package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.linetype;

import javax.swing.JComboBox;

public class JLineTypeComboBox extends JComboBox<LineType>
{
	public JLineTypeComboBox()
	{
		super(LineType.values());
		setRenderer(new LineTypeComboBoxRenderer());
		setEditable(false);
		setSelectedItem(0);
	}
	
	public JLineTypeComboBox(int selectedItem)
	{
		this();
		setSelectedItem(selectedItem);
	}
}
