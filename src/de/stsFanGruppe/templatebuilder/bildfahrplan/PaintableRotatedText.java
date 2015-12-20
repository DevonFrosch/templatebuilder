package de.stsFanGruppe.templatebuilder.bildfahrplan;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class PaintableRotatedText implements Paintable
{
	protected int x1;
	protected int x2;
	protected int y1;
	protected int y2;
	protected String beschriftung;
	protected int offset;
	
	protected Color c;
	
	public PaintableRotatedText(int x1, int y1, int x2, int y2, Color c, String beschriftung, int offset)
	{
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.c = c;
		this.beschriftung = beschriftung;
		this.offset = offset;
	}
	
	@Override
	public void paint(Graphics2D g)
	{
		g.setColor(c);
		
		FontMetrics f = g.getFontMetrics();
		int stringWidth = f.stringWidth(beschriftung);
		
		// Koordinatensystem drehen
		AffineTransform neu = g.getTransform();
		AffineTransform alt = (AffineTransform) neu.clone();
		neu.translate((x1 + x2) / 2, (y2 + y1) / 2);
		neu.rotate(Math.atan((1.0 * y2 - y1) / (x2 - x1)));
		g.setTransform(neu);
		
		// Text einzeichnen
		g.drawString(beschriftung, -stringWidth / 2, offset);
		
		// Koordinatensystem zurücksetzen
		g.setTransform(alt);
	}
}
