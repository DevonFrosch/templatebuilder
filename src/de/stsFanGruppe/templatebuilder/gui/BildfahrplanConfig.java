package de.stsFanGruppe.templatebuilder.gui;

import javax.swing.JPanel;

public class BildfahrplanConfig
{
	int margin_right = 20;
	int margin_left = 20;
	int margin_top = 20;
	int margin_bottom = 20;
	
	int hoeheProStunde = 1000;
	double minZeit = 360;
	double maxZeit = 470;
	
	public BildfahrplanConfig(double minZeit, double maxZeit)
	{
		this.setMinZeit(minZeit);
		this.setMaxZeit(maxZeit);
	}
	
	public double getMinZeit()
	{
		return minZeit;
	}
	public void setMinZeit(double zeit)
	{
		if(zeit < 0)
		{
			throw new IllegalArgumentException("Zeit muss größer gleich 0 sein.");
		}
		this.minZeit = zeit;
	}
	public double getMaxZeit()
	{
		return maxZeit;
	}
	public void setMaxZeit(double zeit)
	{
		if(zeit < 0)
		{
			throw new IllegalArgumentException("Zeit muss größer gleich 0 sein.");
		}
		this.maxZeit = zeit;
	}
	
	public int getPanelHeight()
	{
		return (int) ((maxZeit - minZeit) / 60 * hoeheProStunde) + margin_top + margin_bottom;
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
