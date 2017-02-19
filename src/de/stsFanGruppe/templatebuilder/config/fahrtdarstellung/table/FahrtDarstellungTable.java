package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.table;

import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.FahrtDarstellungSettingsGUI;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.filter.FahrtFilter;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.linetype.LineType;
import de.stsFanGruppe.templatebuilder.zug.FahrtDarstellung;

public class FahrtDarstellungTable extends JTable
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FahrtDarstellungTable.class);
	
	public FahrtDarstellungTable()
	{
		super();
	}
	public FahrtDarstellungTable(int numRows, int numColumns)
	{
		super(numRows, numColumns);
	}
	public FahrtDarstellungTable(Object[] columnNames, int numRows)
	{
		super(new DefaultTableModel(columnNames, numRows));
	}
	public FahrtDarstellungTable(Object[][] rowData, Object[] columnNames)
	{
		super(rowData, columnNames);
	}
	public FahrtDarstellungTable(TableModel dm)
	{
		super(dm);
	}
	
	public FahrtFilter getFilterAt(int row)
	{
		return (FahrtFilter) this.getValueAt(row, FahrtDarstellungSettingsGUI.REGELN_FILTER_SPALTE);
	}
	public void setFilterAt(FahrtFilter filter, int row)
	{
		this.setValueAt(filter, row, FahrtDarstellungSettingsGUI.REGELN_FILTER_SPALTE);
	}
	
	public String getMusterAt(int row)
	{
		return (String) this.getValueAt(row, FahrtDarstellungSettingsGUI.REGELN_MUSTER_SPALTE);
	}
	public void setMusterAt(String muster, int row)
	{
		this.setValueAt(muster, row, FahrtDarstellungSettingsGUI.REGELN_MUSTER_SPALTE);
	}
	
	public Color getFarbeAt(int row)
	{
		return (Color) this.getValueAt(row, FahrtDarstellungSettingsGUI.REGELN_FARBE_SPALTE);
	}
	public void setFarbeAt(Color farbe, int row)
	{
		this.setValueAt(farbe, row, FahrtDarstellungSettingsGUI.REGELN_FARBE_SPALTE);
	}
	
	public int getBreiteAt(int row) throws NumberFormatException
	{
		// throws NumberFormatException
		int breite = Integer.parseInt((String) this.getValueAt(row, FahrtDarstellungSettingsGUI.REGELN_BREITE_SPALTE));
		if(breite < 0)
		{
			throw new NumberFormatException("Number below 0");
		}
		return breite;
	}
	public void setBreiteAt(int breite, int row)
	{
		this.setValueAt(Integer.toString(breite), row, FahrtDarstellungSettingsGUI.REGELN_BREITE_SPALTE);
	}
	public void setBreiteAt(String breite, int row)
	{
		this.setValueAt(breite, row, FahrtDarstellungSettingsGUI.REGELN_BREITE_SPALTE);
	}
	
	public LineType getLineTypeAt(int row)
	{
		return (LineType) this.getValueAt(row, FahrtDarstellungSettingsGUI.REGELN_TYP_SPALTE);
	}
	public void setLineTypeAt(LineType lineType, int row)
	{
		this.setValueAt(lineType, row, FahrtDarstellungSettingsGUI.REGELN_TYP_SPALTE);
	}
	
	public FahrtDarstellung getRow(int row) throws NumberFormatException
	{
		return new FahrtDarstellung(getFilterAt(row), getMusterAt(row), getFarbeAt(row), getBreiteAt(row), getLineTypeAt(row));
	}
	public void setRow(FahrtDarstellung darstellung, int row)
	{
		this.setFilterAt(darstellung.getFilter(), row);
		this.setMusterAt(darstellung.getMuster(), row);
		this.setFarbeAt(darstellung.getFarbe(), row);
		this.setBreiteAt(darstellung.getBreite(), row);
		this.setLineTypeAt(darstellung.getTyp(), row);
	}
	
	public void addRow(FahrtDarstellung darstellung)
	{
		DefaultTableModel model = (DefaultTableModel) getModel();
		model.addRow(darstellung.toArray());
	}
	public void insertRow(int row, FahrtDarstellung darstellung)
	{
		DefaultTableModel model = (DefaultTableModel) getModel();
		model.insertRow(row, darstellung.toArray());
	}
	public void removeRow(int row)
	{
		DefaultTableModel model = (DefaultTableModel) getModel();
		model.removeRow(row);
	}
	public void removeAllRows()
	{
		DefaultTableModel model = (DefaultTableModel) getModel();
		for(int i=0; i < model.getRowCount(); i++)
		{
			model.removeRow(0);
		}
	}
	
	public void clearSelection()
	{
		getSelectionModel().clearSelection();
	}
}
