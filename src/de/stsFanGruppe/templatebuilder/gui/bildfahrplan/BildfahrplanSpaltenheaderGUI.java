package de.stsFanGruppe.templatebuilder.gui.bildfahrplan;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.Map;

import javax.swing.JComponent;

import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;



public class BildfahrplanSpaltenheaderGUI extends JComponent {
	
	protected BildfahrplanGUI gui;
	protected BildfahrplanConfig config;
	protected BildfahrplanGUIController controller;
	protected TemplateBuilderGUI parent;
	
	protected Streckenabschnitt streckenabschnitt;
	protected Map<Betriebsstelle, Double> streckenKm;
	
	protected boolean changed = true;
	protected boolean paint = true;
	protected boolean firstPaint = true;
	
	JComponent component;
	int        columns;

	/**
	 * Anzeigen der GUI mit einer festen Spaltenanzahl
	 * @param component
	 * @param columns
	 */
	public BildfahrplanSpaltenheaderGUI(JComponent component)
	{
		this.component = component;
	}
	
	public void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		// wir nehmen mal an, dass wir Graphics2D haben, sonst wird's schwierig...
		Graphics2D g = (Graphics2D) graphics;
		
		System.setProperty("swing.aatext", "true");
		System.setProperty("awt.useSystemAAFontSettings", "lcd");
		
		if(!paint || this.streckenabschnitt == null || this.streckenKm == null )
		{
			return;
		}
		
		Boolean ersteLinie = true;
		double linienVerschiebung = 0;
		
		for(Betriebsstelle bs: streckenabschnitt.getBetriebsstellen())
		{
			double km = bs.getMaxKm();
			
			if(km != 0 && ersteLinie)
			{
				linienVerschiebung = linienVerschiebung - km;	
			}
			km = km + linienVerschiebung;
			
			writeBetriebsstelle(g, km, bs.getName());
			
			ersteLinie = false;
		}	
		
	}
	/**
	 * "Schreibt" den Text in den Header
	 * @param g Variable für die Grafik.
	 * @param km Die x-Koordinaten, wo der Text hingeschrieben werden soll.
	 * @param bs Die Bezeichnung der Betriebsstelle.
	 */
	protected void writeBetriebsstelle(Graphics2D g, double km, String bs)
	{
		assert config != null;
		assert g != null;
		
		// Anti-Aliasing an
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		int x1 = gui.getWegPos(km);
		int x2 = x1;
		int y1 = gui.getZeitPos(0);
		int y2 = y1+5;
		
		// Linie zeichnen
		g.drawLine(x1, y1, x2, y2);
		
		// Schriftbreite erkennen
		FontMetrics f = g.getFontMetrics();
		int stringWidth = f.stringWidth(bs);
		
		// Koordinatensystem drehen
		AffineTransform neu = g.getTransform();
		AffineTransform alt = (AffineTransform)neu.clone(); 
		neu.translate((x1 + x2) / 2, (y2 + y1) / 2);
		neu.rotate(Math.atan((1.0 * y2 - y1) / (x2 - x1)));
		g.setTransform(neu);
		
		// Text einzeichnen
		g.drawString(bs, stringWidth / 2, 10);
	}
}
