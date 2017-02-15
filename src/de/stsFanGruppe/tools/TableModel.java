package de.stsFanGruppe.tools;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUI;
import de.stsFanGruppe.templatebuilder.config.FahrtenFarbeSettingsGUI;
import de.stsFanGruppe.templatebuilder.config.FahrtenFarbeConfig.LineType;

public class TableModel
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsGUI.class);
	
	static FahrtenFarbeSettingsGUI gui;
	public static ArrayList<Color> testFarben = new ArrayList<Color>()
	{
		{
			add(Color.BLACK);
			add(Color.RED);
			add(Color.BLUE);
			add(Color.BLACK);
			add(Color.BLACK);
		}
	};
	/**
	 * Anhand der ArrayList (aktuell) testFarben erhält jede Zelle in seiner Zeile die Hintergrundfarbe definiert,
	 * die ihm gegeben wird.
	 * 
	 * Um dies aufzurufen, wird an der Tabelle die Spalte gesucht und die Zelldarstellung neu gesetzt:
	 * table.getColumnModel().getColumn(n).setCellRenderer(new BackgroundTableCellRenderer());
	 */
	public static class BackgroundTableCellRenderer extends JTable implements TableCellRenderer
	{
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Color color = null;
			try
			{
				if(row >= 0 && testFarben != null) color = testFarben.get(row);
			}
			catch(IndexOutOfBoundsException e)
			{
			//	log.error("Farbenspalte: Farbe konnte nicht gefunden werden - " + e.getMessage() + color);
			}
			setBackground(color);
			return this;
		}
	}
	
	/**
	 * Zelleneditoreinstellung für die Spalte, die eine Combobox erhalten soll, wo die Linienarten gezeichnet werden.
	 *
	 * Um dies aufzurufen, wird an der Tabelle die Spalte gesucht und die Zelleditordarstellung neu gesetzt:
	 * table.getColumnModel().getColumn(n).setCellEditor(new LinienArtCellEditor());
	 */
	public static class LinienArtCellEditor extends AbstractCellEditor implements TableCellEditor
	{
		JComboBox<LineType> comboBoxLinienArt;
		
		public LinienArtCellEditor()
		{
			// FIXME Combobox werden derzeit doppelt im Code geschrieben. Die
			// JComboBox sollte möglich einmal definiert werden.
			// Erstellt eine Combobox mit den Linientypen.
			comboBoxLinienArt = new JComboBox<LineType>(LineType.values());
			comboBoxLinienArt.setRenderer(new LineRenderer());
			comboBoxLinienArt.setEditable(false);
			comboBoxLinienArt.setSelectedItem(null);
		}
		
		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int colIndex)
		{
//			//Setzt das Modell für die Tabelle
//			if(isSelected)
//			{
//				comboBoxLinienArt.setSelectedItem(value);
//				TableModel model = (TableModel) table.getModel();
//				model.setValueAt(value, rowIndex, colIndex);
//			}
			return comboBoxLinienArt;
		}
		
		@Override
		public Object getCellEditorValue()
		{
			return comboBoxLinienArt.getSelectedItem();
		}
	}
	/**
	 * Ändern die Darstellung der Combobox so, dass Linien eingezeichnet werden.
	 *
	 * Um dies aufzurufen, wird an der Combobox die Darstellung neu gesetzt:
	 * combobox.setRenderer(new LineRenderer());
	 */
	public static class LineRenderer extends JPanel implements ListCellRenderer<LineType>
	{
		private LineType value;
		
		@Override
		public Component getListCellRendererComponent(JList<? extends LineType> list, LineType value, int index, boolean isSelected, boolean cellHasFocus)
		{
			if(value instanceof LineType)
			{
				setLineType((LineType) value);
			}
			else
			{
				setLineType(null);
			}
			return this;
		}
		
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			if(value != null)
			{
				g2.setStroke(value.getStroke());
				g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
			}
		}
		
		private void setLineType(LineType value)
		{
			this.value = value;
		}
		
		@Override
		public Dimension getPreferredSize()
		{
			return new Dimension(20, 20);
		}
	}
	
	/**
	 * MouseListener für die Tabelle: Fahrtenfarbekategoriesieren
	 * Es wird nur der mouseClicked(..) mit einer funktionierenden Methode ausgeführt.
	 *
	 * Wenn eine Zelle in der 2. Spalte ausgewählt wird, öffnet sich ein Editor zum Ändern der Farbe.
	 */
	public static class CellMouseClickForBackgroundColor extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			JTable target = (JTable) e.getSource();
			int row = target.getSelectedRow();
			int column = target.getSelectedColumn();
			if(column == 1)
			{
				Color c = null;
				c = JColorChooser.showDialog(gui, "Farbe wählen", testFarben.get(row));
				if(c != null)
				{
					testFarben.set(row, c);
				}
			}
		}
	}

	public void setValueAt(Object value, int rowIndex, int colIndex)
	{
		// TODO Auto-generated method stub
		
	}
}
