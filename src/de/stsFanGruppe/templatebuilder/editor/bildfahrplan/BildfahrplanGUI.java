package de.stsFanGruppe.templatebuilder.editor.bildfahrplan;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import de.stsFanGruppe.templatebuilder.editor.EditorGUI;
import de.stsFanGruppe.templatebuilder.editor.EditorGUIController;
import de.stsFanGruppe.templatebuilder.editor.GUIType;
import de.stsFanGruppe.tools.CalculatableLine;
import de.stsFanGruppe.tools.FahrtPaintable;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.Paintable;

public class BildfahrplanGUI extends JComponent implements EditorGUI
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanGUI.class);
	
	protected BildfahrplanGUIController controller;
	
	protected Object paintLock = new Object();
	protected FirstLastLinkedList<Paintable> paintables = null;
	private FirstLastLinkedList<FahrtPaintable> fahrtPaintables = null;
	boolean paint = true;
	protected boolean forceRepaint = true;

	
	public BildfahrplanGUI(BildfahrplanGUIController controller)
	{
		NullTester.test(controller);
		this.controller = controller;
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				controller.mouseClicked(e);
			}
		});
	}
	
	public EditorGUIController getController()
	{
		return controller;
	}
	
	public GUIType getGUIType() {
		return GUIType.BILDFAHRPLAN;
	}
	
	public void setPaintables(FirstLastLinkedList<Paintable> paintables)
	{
		synchronized(paintLock)
		{
			this.paintables = paintables;
		}
	}

	public void setFahrtPaintables(FirstLastLinkedList<FahrtPaintable> fahrtPaintables)
	{
		synchronized(paintLock)
		{
			this.fahrtPaintables = fahrtPaintables;
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
		
		if(!paint || paintables == null || fahrtPaintables == null)
		{
			return;
		}
		
		// Arbeitskopie
		FirstLastLinkedList<Paintable> ps;
		FirstLastLinkedList<FahrtPaintable> fps;
		synchronized(paintLock)
		{
			ps = paintables.clone();
			fps = fahrtPaintables.clone();
		}
		
		if(!ps.isEmpty())
		{
			for(Paintable p : ps)
			{
				p.paint(g);
			}
		}
		
		if(!fps.isEmpty())
		{
			FirstLastLinkedList<CalculatableLine> zugLinien = new FirstLastLinkedList<>();
			for(FahrtPaintable fp : fps)
			{
				zugLinien.addAll(fp.paint(g));
			}
			controller.setZugLinien(zugLinien);
		}
	}
}
