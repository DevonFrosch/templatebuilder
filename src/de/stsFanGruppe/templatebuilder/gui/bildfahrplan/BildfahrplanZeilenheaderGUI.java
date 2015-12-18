package de.stsFanGruppe.templatebuilder.gui.bildfahrplan;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.Set;

import javax.swing.JComponent;

import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrplanhalt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.TimeFormater;
import sun.awt.TimedWindowEvent;

public class BildfahrplanZeilenheaderGUI extends JComponent {
	protected BildfahrplanGUI gui;
	protected BildfahrplanConfig config;
	protected TimeFormater timeFormat;
	
	protected Streckenabschnitt streckenabschnitt;
	protected Set<Fahrt> fahrten;
	
	protected boolean changed = true;
	protected boolean paint = true;
	protected boolean firstPaint = true;
	
	protected int stringHeight = 0;
	protected int zeitAbstand = 5;
	protected int zeitBreite = 0;
	
	/**
	 * Anzeigen der GUI mit einer festen Zeilenzahl
	 * @param gui
	 */
	public BildfahrplanZeilenheaderGUI(BildfahrplanGUI gui, BildfahrplanGUIController controller)
	{
		this.gui = gui;
		this.config = controller.getConfig();
		this.timeFormat = new TimeFormater();
		controller.setBildfahrplanZeilenheaderGUI(this);
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
		
		g.setColor(config.getZeitFarbe());
		
		if(!paint || gui.streckenabschnitt == null)
		{
			return;
		}
		int minZeit = (int) config.getMinZeit();
		int maxZeit = (int) config.getMaxZeit();
		int zeitIntervall = config.getZeitIntervall();
		int zeit = (int) minZeit;
		
		if(zeit % 10 != 0){
			zeit = zeit - (zeit % 10);
		}
		while(zeit <= maxZeit)
		{
			paintZeiten(g, zeit);
			zeit = zeit + zeitIntervall;
		}
	}
	/**
	 * Die Breite wird an Hand der Schriftbreite errechnet in der Methode paintZeiten. 
	 * Die Höhe ist die Höhe der GUI. 
	 */
	public Dimension getPreferredSize()
	{
		return new Dimension( 32, (int) gui.getPreferredSize().getHeight());
	}
	/**
	 * "Schreibt" den Text in den Header
	 * @param g Variable für die Grafik.
	 * @param zeit Die x-Koordinaten, wo der Text hingeschrieben werden soll.
	 * @param bs Die Bezeichnung der Betriebsstelle.
	 */
	protected void paintZeiten(Graphics2D g, double zeit)
	{
		assert gui != null;
		assert g != null;
		
		int x1 = 5;
		int x2 = gui.getWidth();
		int y = gui.getZeitPos(zeit);
					
		// Koordinatensystem drehen
		AffineTransform neu = g.getTransform();
		AffineTransform alt = (AffineTransform)neu.clone(); 
		neu.translate((x1 + x2) / 2, (y) / 2);
		g.setTransform(neu);
		
		// Schriftbreite und -höhe erkennen
		String zeitAngabe = timeFormat.doubleToString(zeit);
		FontMetrics f = g.getFontMetrics();
		int stringWidth = f.stringWidth(zeitAngabe);
		if(stringWidth < zeitBreite){
			zeitBreite = stringWidth;
		}
		stringHeight = f.getHeight();
		
		// Linie zeichnen
		g.drawLine(x1 + stringWidth, y, x2, y);
		
		// Koordinatensystem zurücksetzen
		g.setTransform(alt);
		
		// Text einzeichnen
		g.drawString(zeitAngabe, x1 , y + (stringHeight/3));	
	}
}
