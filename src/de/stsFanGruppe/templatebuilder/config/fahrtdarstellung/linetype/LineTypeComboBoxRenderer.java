package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.linetype;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 * Ändern die Darstellung der Combobox so, dass Linien eingezeichnet werden.
 * Um dies aufzurufen, wird an der Combobox die Darstellung neu gesetzt:
 * combobox.setRenderer(new LineTypeComboBoxRenderer());
 */
public class LineTypeComboBoxRenderer extends BasicComboBoxRenderer
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LineTypeComboBoxRenderer.class);
	private LineType value;
	
	@Override
	@SuppressWarnings("rawtypes")
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		this.value = (LineType) value;
		
		if(isSelected)
		{
			this.setBackground(list.getSelectionBackground());
			this.setForeground(list.getSelectionForeground());
		}
		else
		{
			this.setBackground(list.getBackground());
			this.setForeground(list.getForeground());
		}
		
		return this;
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if(value != null)
		{
			g2.setStroke(value.getStroke(3));
			g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
		}
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension(20, 20);
	}
}
