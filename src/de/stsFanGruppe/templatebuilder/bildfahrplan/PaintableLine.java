package de.stsFanGruppe.templatebuilder.bildfahrplan;

import java.awt.*;

public class PaintableLine implements Paintable
{
	protected int x1;
	protected int x2;
	protected int y1;
	protected int y2;
	
	protected Color c;
	protected Stroke s;
	
	public PaintableLine(int x1, int y1, int x2, int y2, Color c)
	{
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.c = c;
		this.s = new BasicStroke(1);
	}
	
	public PaintableLine(int x1, int y1, int x2, int y2, Color c, Stroke s)
	{		
		this(x1, y1, x2, y2, c);
		this.s = s;
	}
	
	@Override
	public void paint(Graphics2D g)
	{
		g.setStroke(s);
		g.setColor(c);
		g.drawLine(x1, y1, x2, y2);
	}
}
