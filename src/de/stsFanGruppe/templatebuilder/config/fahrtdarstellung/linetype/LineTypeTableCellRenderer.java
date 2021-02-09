package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.linetype;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Ändern die Darstellung der Combobox so, dass Linien eingezeichnet werden.
 * Um dies aufzurufen, wird an der Combobox die Darstellung neu gesetzt:
 * combobox.setRenderer(new LineTypeComboBoxRenderer());
 */
public class LineTypeTableCellRenderer extends DefaultTableCellRenderer
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LineTypeTableCellRenderer.class);
	private LineType value;
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
		this.value = (LineType) value;
		
		if(isSelected)
		{
			this.setBackground(table.getSelectionBackground());
			this.setForeground(table.getSelectionForeground());
		}
		else
		{
			this.setBackground(table.getBackground());
			this.setForeground(table.getForeground());
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
