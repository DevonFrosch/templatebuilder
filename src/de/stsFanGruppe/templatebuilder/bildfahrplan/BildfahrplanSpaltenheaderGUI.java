package de.stsFanGruppe.templatebuilder.bildfahrplan;

import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.JComponent;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.tools.NullTester;

public class BildfahrplanSpaltenheaderGUI extends JComponent
{
	protected BildfahrplanGUI gui;
	protected BildfahrplanConfig config;
	
	protected Streckenabschnitt streckenabschnitt;
	
	protected boolean changed = true;
	protected boolean paint = true;
	protected boolean firstPaint = true;
	protected int stringHeight = 0;
	
	/**
	 * Anzeigen der GUI mit einer festen Spaltenanzahl
	 * @param gui
	 */
	public BildfahrplanSpaltenheaderGUI(BildfahrplanGUI gui, BildfahrplanGUIController controller)
	{
		NullTester.test(gui);
		NullTester.test(controller);
		
		this.gui = gui;
		this.config = controller.getConfig();
		controller.setBildfahrplanSpaltenHeaderGUI(this);
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
		
		// Prüft, ob Werte den Wert null haben
		if(!paint || gui.streckenabschnitt == null)
		{
			return;
		}
		
		// Holt sich die Farbe für die Schrift und der Linie
		g.setColor(config.getBfpBetriebsstellenFarbe());
		
		boolean ersteLinie = true;
		double linienVerschiebung = 0;
		double km = 0;
		int bsZaehler = 0;
		
		for(Betriebsstelle bs: gui.streckenabschnitt.getBetriebsstellen())
		{
			km = bs.getMaxKm();
			
			if(km != 0 && ersteLinie)
			{
				linienVerschiebung -= km;	
			}
			km += linienVerschiebung;
			
			paintBetriebsstelle(g, km, bs.getName(), bsZaehler);
			
			ersteLinie = false;
			bsZaehler++;
		}
	}
	public Dimension getPreferredSize()
	{
		return new Dimension((int) gui.getPreferredSize().getWidth(), config.spaltenHeaderHoehe(stringHeight));
	}
	/**
	 * "Schreibt" den Text in den Header
	 * @param g Variable für die Grafik.
	 * @param km Die x-Koordinaten, wo der Text hingeschrieben werden soll.
	 * @param bs Die Bezeichnung der Betriebsstelle.
	 */
	protected void paintBetriebsstelle(Graphics2D g, double km, String bs, int zaehler)
	{
		assert gui != null;
		assert g != null;
		
		int x = gui.getWegPos(km);
		int y1 = this.getHeight();
		int y2 = y1 - config.getLineHeight();
		
		// Linie zeichnen
		g.drawLine(x, y1, x, y2);
		
		// Koordinatensystem drehen
		AffineTransform neu = g.getTransform();
		AffineTransform alt = (AffineTransform)neu.clone();
		neu.translate(x, (y2 + y1) / 2);
		g.setTransform(neu);
		
		// Schriftbreite und -höhe erkennen
		FontMetrics f = g.getFontMetrics();
		int stringWidth = f.stringWidth(bs);
		stringHeight = f.getHeight();
		
		// Koordinatensystem zurücksetzen
		g.setTransform(alt);
		
		//weitere Variablen
		int offsetX = config.getOffsetX();
		
		int textY = (config.getTextMarginTop()
				+ (stringHeight + config.getOffsetY()) * (zaehler % config.getZeilenAnzahl()))
				+ (stringHeight/2);
		
		// Text einzeichnen
		if(x < stringWidth)
		{
			g.drawString(bs, x - offsetX, textY);
		}
		else if((stringWidth + x) > gui.getWidth())
		{
			g.drawString(bs, x + offsetX - stringWidth , textY);
		}
		else
		{
			g.drawString(bs, x - (stringWidth / 2), textY);
		}
		
	}
}
