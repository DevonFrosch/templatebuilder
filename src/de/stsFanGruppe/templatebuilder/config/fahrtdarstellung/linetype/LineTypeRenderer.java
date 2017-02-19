package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.linetype;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * Ändern die Darstellung der Combobox so, dass Linien eingezeichnet werden.
 *
 * Um dies aufzurufen, wird an der Combobox die Darstellung neu gesetzt:
 * combobox.setRenderer(new LineTypeRenderer());
 */
public class LineTypeRenderer extends JPanel implements ListCellRenderer<LineType>, TableCellRenderer
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LineTypeRenderer.class);
	private LineType value;
	
	public Component getListCellRendererComponent(JList<? extends LineType> list, LineType value, int index, boolean isSelected, boolean cellHasFocus)
	{
		this.value = value;
		
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
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
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