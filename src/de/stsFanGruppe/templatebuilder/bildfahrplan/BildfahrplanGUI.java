package de.stsFanGruppe.templatebuilder.bildfahrplan;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Collection;
import javax.swing.JComponent;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;
import de.stsFanGruppe.tools.NullTester;

public class BildfahrplanGUI extends JComponent
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanGUI.class);
	
	protected BildfahrplanGUIController controller;
	protected TemplateBuilderGUI parent;
	
	protected Object paintLock = new Object();
	protected Collection<Paintable> paintables = null;
	boolean paint = true;
	protected boolean forceRepaint = true;
	
	Dimension lastSize = null;
	
	public BildfahrplanGUI(BildfahrplanGUIController controller, TemplateBuilderGUI parent)
	{
		NullTester.test(parent);
		
		this.controller = controller;
		controller.setBildfahrplanGUI(this);
		
		this.parent = parent;
	}
	
	public void setPaintables(Collection<Paintable> paintables)
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
	
	public void errorMessage(String text)
	{
		if(parent == null)
		{
			log.error(text);
		}
		parent.errorMessage(text);
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
		
		if(forceRepaint || lastSize == null || !lastSize.equals(getSize()))
		{
			controller.recalculate(g.getFontMetrics());
			forceRepaint = false;
		}
		
		if(!paint || paintables == null)
		{
			return;
		}
		
		synchronized(paintables)
		{
			if(paintables.isEmpty())
			{
				return;
			}
			
			for(Paintable p : paintables)
			{
				p.paint(g);
			}
		}
	}
}
