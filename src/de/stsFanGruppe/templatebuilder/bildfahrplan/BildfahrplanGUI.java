package de.stsFanGruppe.templatebuilder.bildfahrplan;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.NullTester;

public class BildfahrplanGUI extends JComponent
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanGUI.class);
	
	protected BildfahrplanGUIController controller;
	protected TemplateBuilderGUI parent;
	
	protected Object paintLock = new Object();
	protected FirstLastLinkedList<Paintable> paintables = null;
	boolean paint = true;
	protected boolean forceRepaint = true;
	
	public BildfahrplanGUI(BildfahrplanGUIController controller, TemplateBuilderGUI parent)
	{
		NullTester.test(parent);
		
		this.controller = controller;
		controller.setBildfahrplanGUI(this);
		
		this.parent = parent;
	}
	
	public void setPaintables(FirstLastLinkedList<Paintable> paintables)
	{
		synchronized(paintLock)
		{
			this.paintables = paintables;
		}
	}
	
	public synchronized void recalculatePanelSize()
	{
		Dimension size = getMinimumSize();
		size.setSize(size.getWidth(), controller.getPanelHeight());
		setMinimumSize(size);
		setPreferredSize(size);
		
		// Neu malen
		revalidate();
		forceRepaint = true;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		// wir nehmen mal an, dass wir Graphics2D haben, sonst wird's schwierig...
		Graphics2D g = (Graphics2D) graphics;
		
		// Anti-Aliasing an
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		System.setProperty("swing.aatext", "true");
		System.setProperty("awt.useSystemAAFontSettings", "lcd");
		
		controller.guiRepaint();
		
		if(forceRepaint)
		{
			controller.recalculate(g.getFontMetrics());
			forceRepaint = false;
		}
		
		if(!paint || paintables == null)
		{
			return;
		}
		
		// Arbeitskopie
		FirstLastLinkedList<Paintable> ps;
		synchronized(paintLock)
		{
			ps = (FirstLastLinkedList<Paintable>) paintables.clone();
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
}
