package de.stsFanGruppe.templatebuilder.gui;

import javax.swing.JPanel;

public class BildfahrplanConfig
{
	int margin_right = 20;
	int margin_left = 20;
	int margin_top = 20;
	int margin_bottom = 20;
	
	public BildfahrplanConfig()
	{
		
	}

	int zeichnenBreite(JPanel p)
	{
		assert p != null;
		return p.getWidth() - margin_left - margin_right;
	}
	int zeichnenHoehe(JPanel p)
	{
		assert p != null;
		return p.getHeight() - margin_top - margin_bottom;
	}
	
	public String toString()
	{
		return "{margin: r="+margin_right+", l="+margin_left+", t="+margin_top+", b="+margin_bottom+"}";
	}
}
