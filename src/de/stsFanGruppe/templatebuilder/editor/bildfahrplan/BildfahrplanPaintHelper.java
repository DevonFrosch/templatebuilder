package de.stsFanGruppe.templatebuilder.editor.bildfahrplan;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.zug.FahrtDarstellung;
import de.stsFanGruppe.tools.NullTester;

public class BildfahrplanPaintHelper
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanPaintHelper.class);
	
	protected BildfahrplanConfig config;
	protected BildfahrplanGUI gui;
	
	protected boolean isCalculated = false;
	
	protected int minHoehe;
	protected int zeichenHoehe;
	protected double minZeit;
	protected double diffZeit;
	
	protected int minBreite;
	protected int zeichenBreite;
	protected double diffKm;
	
	public BildfahrplanPaintHelper(BildfahrplanConfig config, BildfahrplanGUI gui)
	{
		NullTester.test(config);
		NullTester.test(gui);
		
		this.config = config;
		this.gui = gui;
		this.diffKm = 0;
	}
	public void setDiffKm(double diffKm)
	{
		this.diffKm = diffKm;
	}
	
	public void readConfig()
	{
		log.trace("readConfig()");
		// getZeitPos
		minHoehe = config.getMarginTop();
		zeichenHoehe = config.getMarginTop() + config.getMarginBottom();
		minZeit = config.getMinZeit();
		diffZeit = config.getMaxZeit() - config.getMinZeit();
		
		// getWegPos
		minBreite = config.getMarginLeft();
		zeichenBreite = config.getMarginLeft() + config.getMarginRight();
		
		isCalculated = true;
	}

	public int getZeitPos(double zeit)
	{
		if(!isCalculated)
		{
			readConfig();
		}
		
		// Offset, da nicht mit 0:00 angefangen wird
		zeit -= minZeit;
		
		return (int) ((zeit / diffZeit * (gui.getHeight() - zeichenHoehe)) + minHoehe);
	}
	public int getWegPos(double km)
	{
		if(!isCalculated)
		{
			readConfig();
		}
		
		return (int) ((km / diffKm * (gui.getWidth() - zeichenBreite)) + minBreite);
	}
	
	public void paintLine(Graphics2D g, int x1, int y1, int x2, int y2, Color c)
	{
		this.paintLine(g, x1, y1, x2, y2, c, new BasicStroke(1));
	}
	public void paintLine(Graphics2D g, int x1, int y1, int x2, int y2, FahrtDarstellung d)
	{
		this.paintLine(g, x1, y1, x2, y2, d.getFarbe(), d.getTyp().getStroke(d.getBreite()));
	}
	public void paintLine(Graphics2D g, int x1, int y1, int x2, int y2, Color c, Stroke s)
	{
		g.setStroke(s);
		g.setColor(c);
		g.drawLine(x1, y1, x2, y2);
	}
	
	public void paintText(Graphics2D g, int x, int y, Color c, String beschriftung)
	{
		g.setColor(c);
		
		FontMetrics f = g.getFontMetrics();
		int stringWidth = f.stringWidth(beschriftung);
		
		// Text einzeichnen
		g.drawString(beschriftung, x, y);
	}
	public void paintRotatedText(Graphics2D g, int x1, int y1, int x2, int y2, Color c, String beschriftung, int offset)
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
