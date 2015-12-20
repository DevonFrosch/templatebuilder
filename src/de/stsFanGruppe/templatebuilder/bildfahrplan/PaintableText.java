package de.stsFanGruppe.templatebuilder.bildfahrplan;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class PaintableText implements Paintable
{
	protected int x;
	protected int y;
	protected String beschriftung;
	
	protected Color c;
	
	public PaintableText(int x, int y, Color c, String beschriftung)
	{
		this.x = x;
		this.y = y;
		this.c = c;
		this.beschriftung = beschriftung;
	}
	
	@Override
	public void paint(Graphics2D g)
	{
		g.setColor(c);
		
		FontMetrics f = g.getFontMetrics();
		int stringWidth = f.stringWidth(beschriftung);
		
		// Text einzeichnen
		g.drawString(beschriftung, x - stringWidth / 2, y);
	}
}
