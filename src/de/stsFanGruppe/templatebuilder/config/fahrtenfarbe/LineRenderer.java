package de.stsFanGruppe.templatebuilder.config.fahrtenfarbe;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * Ändern die Darstellung der Combobox so, dass Linien eingezeichnet werden.
 *
 * Um dies aufzurufen, wird an der Combobox die Darstellung neu gesetzt:
 * combobox.setRenderer(new LineRenderer());
 */
public class LineRenderer extends JPanel implements ListCellRenderer<LineType>
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LineRenderer.class);
	private LineType value;
	
	public Component getListCellRendererComponent(JList<? extends LineType> list, LineType value, int index, boolean isSelected, boolean cellHasFocus)
	{
		this.value = value;
		
		if(isSelected)
		{
			// TODO Farbeffekte bei Auswahl			
		}
		
		return this;
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if(value != null)
		{
			g2.setStroke(value.getStroke());
			g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
			
			// TODO Hintergrundfarbe an Hintergrund, Selektion, ... anpassen
		}
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension(20, 20);
	}
}