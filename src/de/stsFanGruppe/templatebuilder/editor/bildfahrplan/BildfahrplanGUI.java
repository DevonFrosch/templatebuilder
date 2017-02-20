package de.stsFanGruppe.templatebuilder.editor.bildfahrplan;

import java.awt.*;
import javax.swing.JComponent;
import de.stsFanGruppe.templatebuilder.editor.EditorGUI;
import de.stsFanGruppe.templatebuilder.editor.EditorGUIController;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.Paintable;

public class BildfahrplanGUI extends JComponent implements EditorGUI
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
		NullTester.test(controller);
		NullTester.test(parent);
		
		this.controller = controller;
		this.parent = parent;
	}
	public EditorGUIController getController()
	{
		return controller;
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
			forceRepaint = false;
			controller.recalculate(g.getFontMetrics());
		}
		
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
}
