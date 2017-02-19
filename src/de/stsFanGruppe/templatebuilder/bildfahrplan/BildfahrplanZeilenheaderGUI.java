package de.stsFanGruppe.templatebuilder.bildfahrplan;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.NullTester;

public class BildfahrplanZeilenheaderGUI extends JComponent
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanZeilenheaderGUI.class);
	
	protected BildfahrplanGUI gui;
	protected BildfahrplanGUIController controller;

	protected Object paintLock = new Object();
	protected FirstLastLinkedList<Paintable> paintables = null;
	boolean paint = true;
	
	/**
	 * Anzeigen der GUI mit einer festen Zeilenzahl
	 * @param gui
	 */
	public BildfahrplanZeilenheaderGUI(BildfahrplanGUI gui, BildfahrplanGUIController controller)
	{
		NullTester.test(gui);
		NullTester.test(controller);
		
		this.gui = gui;
		this.controller = controller;
	}
	
	public void setPaintables(FirstLastLinkedList<Paintable> paintables)
	{
		synchronized(paintLock)
		{
			this.paintables = paintables;
		}
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
		
		if(!paint || paintables == null)
		{
			return;
		}
		
		// Arbeitskopie
		FirstLastLinkedList<Paintable> ps;
		synchronized(paintLock)
		{
			ps = paintables.clone();
		}
		
		if(ps.isEmpty())
		{
			return;
		}
		
		for(Paintable p : ps)
		{
			p.paint(g);
		}
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension(controller.getZeilenHeaderBreite(), (int) gui.getPreferredSize().getHeight());
	}
}
