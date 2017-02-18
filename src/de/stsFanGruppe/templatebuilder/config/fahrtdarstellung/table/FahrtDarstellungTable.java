package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.table;

import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
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
	
	public String getNameAt(int row)
	{
		return (String) this.getValueAt(row, 0);
	}
	public void setNameAt(String name, int row)
	{
		this.setValueAt(name, row, 0);
	}
	
	public Color getFarbeAt(int row)
	{
		return (Color) this.getValueAt(row, 1);
	}
	public void setFarbeAt(Color farbe, int row)
	{
		this.setValueAt(farbe, row, 1);
	}
	
	public int getBreiteAt(int row) throws NumberFormatException
	{
		// throws NumberFormatException
		int breite = Integer.parseInt((String) this.getValueAt(row, 2));
		if(breite < 0)
		{
			throw new NumberFormatException("Number below 0");
		}
		return breite;
	}
	public void setBreiteAt(int breite, int row)
	{
		this.setValueAt(breite+"", row, 2);
	}
	public void setBreiteAt(String breite, int row)
	{
		this.setValueAt(breite, row, 2);
	}
	
	public LineType getLineTypeAt(int row)
	{
		return (LineType) this.getValueAt(row, 3);
	}
	public void setLineTypeAt(LineType lineType, int row)
	{
		this.setValueAt(lineType, row, 3);
	}
	
	public FahrtDarstellung getRow(int row) throws NumberFormatException
	{
		return new FahrtDarstellung(getNameAt(row), getFarbeAt(row), getBreiteAt(row), getLineTypeAt(row));
	}
	public void setRow(FahrtDarstellung darstellung, int row)
	{
		this.setNameAt(darstellung.getName(), row);
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
