package de.stsFanGruppe.templatebuilder.gui.bildfahrplan;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.Map;

import javax.swing.JComponent;

import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;



public class BildfahrplanSpaltenheaderGUI extends JComponent {
	
	protected BildfahrplanGUI gui;
	
	protected Streckenabschnitt streckenabschnitt;
	protected Map<Betriebsstelle, Double> streckenKm;
	
	protected boolean changed = true;
	protected boolean paint = true;
	protected boolean firstPaint = true;
	
	JComponent component;
	int        columns;

	/**
	 * Anzeigen der GUI mit einer festen Spaltenanzahl
	 * @param gui
	 */
	public BildfahrplanSpaltenheaderGUI(BildfahrplanGUI gui)
	{
		this.gui = gui;
		this.setPreferredSize(new Dimension(100, 40));
	}
	
	public void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		// wir nehmen mal an, dass wir Graphics2D haben, sonst wird's schwierig...
		Graphics2D g = (Graphics2D) graphics;
		
		// Anti-Aliasing an
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		System.setProperty("swing.aatext", "true");
		System.setProperty("awt.useSystemAAFontSettings", "lcd");
		
		g.setBackground(Color.GREEN);
		
		if(!paint || this.streckenabschnitt == null || this.streckenKm == null )
		{
			return;
		}
		
		Boolean ersteLinie = true;
		double linienVerschiebung = 0;
		double km = 0; 
		
		for(Betriebsstelle bs: streckenabschnitt.getBetriebsstellen())
		{
			km = bs.getMaxKm();
			
			if(km != 0 && ersteLinie)
			{
				linienVerschiebung = linienVerschiebung - km;	
			}
			km = km + linienVerschiebung;
			
			paintBetriebsstelle(g, km, bs.getName());
			
			ersteLinie = false;
		}
		g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
		System.out.println("Höhe: " + getHeight() + "Breite: " + getWidth());
	}
	/**
	 * "Schreibt" den Text in den Header
	 * @param g Variable für die Grafik.
	 * @param km Die x-Koordinaten, wo der Text hingeschrieben werden soll.
	 * @param bs Die Bezeichnung der Betriebsstelle.
	 */
	protected void paintBetriebsstelle(Graphics2D g, double km, String bs)
	{
		assert gui != null;
		assert g != null;
		
		int x = gui.getWegPos(km);
		int y1 = this.getHeight();
		int y2 = y1-5;
		
		// Linie zeichnen
		g.drawLine(x, y1, x, y2);
		
		// Schriftbreite erkennen
		FontMetrics f = g.getFontMetrics();
		int stringWidth = f.stringWidth(bs);
		
		// Koordinatensystem drehen
		AffineTransform neu = g.getTransform();
		AffineTransform alt = (AffineTransform)neu.clone(); 
		neu.translate(x, (y2 + y1) / 2);
		g.setTransform(neu);
		
		// Text einzeichnen
		g.drawString(bs, stringWidth / 2, 10);
		
		// Koordinatensystem zurücksetzen
		g.setTransform(alt);
	}
}
