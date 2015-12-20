package de.stsFanGruppe.templatebuilder.bildfahrplan;

import java.awt.*;
import java.util.Set;

import javax.swing.JComponent;

import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.TimeFormater;

public class BildfahrplanZeilenheaderGUI extends JComponent
{
	protected BildfahrplanGUI gui;
	protected BildfahrplanConfig config;
	
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
		NullTester.test(gui);
		NullTester.test(controller);
		
		this.gui = gui;
		this.config = controller.getConfig();
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
		
		g.setColor(config.getBfpZeitenFarbe());
		
		if(!paint || gui.streckenabschnitt == null)
		{
			return;
		}
		int minZeit = (int) config.getMinZeit();
		int maxZeit = (int) config.getMaxZeit();
		int zeitIntervall = config.getZeitIntervall();
		int zeit = minZeit;
		
		if(zeit % 10 != 0){
			zeit = zeit - (zeit % 10);
		}
		while(zeit <= maxZeit)
		{
			paintZeiten(g, zeit);
			zeit += zeitIntervall;
		}
	}
	/**
	 * Die Breite wird aus der Config-Klasse ausgelesen. 
	 * Die Höhe ist die Höhe der GUI. 
	 */
	public Dimension getPreferredSize()
	{
		return new Dimension( config.getZeilenHeaderBreite(), (int) gui.getPreferredSize().getHeight());
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
					
		// Schriftbreite und -höhe erkennen
		String zeitAngabe = TimeFormater.doubleToString(zeit);
		FontMetrics f = g.getFontMetrics();
		int stringWidth = f.stringWidth(zeitAngabe);
		if(stringWidth < zeitBreite){
			zeitBreite = stringWidth;
		}
		stringHeight = f.getHeight();
		
		// Text einzeichnen
		g.drawString(zeitAngabe, x1 , y + (stringHeight/3));	
	}
}
