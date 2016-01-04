package de.stsFanGruppe.bibliothek.linienArten;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import de.stsFanGruppe.tools.NullTester;

public class LinienArt {
	private String linienArtName;
	private float[] linienArt;
	private ArrayList linienArtenSammlung;
	
	// RänderArten
	public static final float[] DURCHGEZOGENE_LINIE = {0};
	public static final float[] GEPUNKTE_LINIE = {10, 10};
	public static final float[] GESTRICHELTE_LINIE = {30, 10};
	public static final float[] KURZ_LANG_LINIE = {10, 10, 30, 10};
	public static final float[] KURZ_KURZ_LANG_LINIE = {10, 10, 10, 10, 30, 10};
	public static final float[] KURZ_LANG_LANG_LINIE = {10, 10, 30, 10, 30, 10};
	
	public LinienArt(String linienArtName, float[] linienArt)
	{
		NullTester.test(linienArtName);
		NullTester.test(linienArt);
		this.linienArtName = linienArtName;
		this.linienArt = linienArt;
	}
	
	public String getLinienArtName() {
		return linienArtName;
	}
	public void setLinienArtName(String linienArtName) {
		this.linienArtName = linienArtName;
	}
	
	public float[] getLinienArt() {
		return linienArt;
	}
	public void setLinienArt(float[] linienArt) {
		this.linienArt = linienArt;
	}
	
	public void setUpLinienArtColumn(JTable table, TableColumn tableColumn)
	{
		JComboBox comboBox = new JComboBox();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement(DURCHGEZOGENE_LINIE);
		model.addElement(GEPUNKTE_LINIE);
		model.addElement(GESTRICHELTE_LINIE);
		model.addElement(KURZ_LANG_LINIE);
		model.addElement(KURZ_KURZ_LANG_LINIE);
		model.addElement(KURZ_LANG_LANG_LINIE);
		comboBox.setModel(model);
		tableColumn.setCellEditor(new DefaultCellEditor(comboBox));
		
        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        tableColumn.setCellRenderer(renderer);
	}
	
	public class ComboBoxTableCellRenderer extends JComboBox implements TableCellRenderer {

	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	        setSelectedItem(value);
	        return this;
	    }

	}
	
}
