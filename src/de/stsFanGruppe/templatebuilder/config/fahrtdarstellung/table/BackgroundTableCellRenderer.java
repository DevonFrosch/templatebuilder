package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Anhand der ArrayList (aktuell) testFarben erhält jede Zelle in seiner Zeile die Hintergrundfarbe definiert,
 * die ihm gegeben wird.
 * 
 * Um dies aufzurufen, wird an der Tabelle die Spalte gesucht und die Zelldarstellung neu gesetzt:
 * table.getColumnModel().getColumn(n).setCellRenderer(new BackgroundTableCellRenderer());
 */
public class BackgroundTableCellRenderer implements TableCellRenderer
{
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Color color = (Color) value;
		
		// TODO: Performance testen
		Component comp = new JPanel();
		comp.setBackground(color);
		return comp;
	}
}
