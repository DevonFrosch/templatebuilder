package de.stsFanGruppe.templatebuilder.editor.fahrtEditor;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.AutoComboBox;
import de.stsFanGruppe.tools.NullTester;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JComponent;

public class FahrtEditorGUI extends JFrame implements GUI
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FahrtEditorGUI.class);
	
	protected JPanel contentPanel = new JPanel();
	protected FahrtEditorGUIController controller;
	
	protected AutoComboBox comboBoxZugname;
	protected JScrollPane scrollPane;
	protected JTable table;
	private FahrtEditorFahrplanTableModel tableModel;
	protected JTextPane lblTemplateInfo;
	
	public final String[] SPALTEN_ÜBERSCHRIFTEN = new String[] {"Halt", "an", "ab"};
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try
				{
					new FahrtEditorGUI();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	private FahrtEditorGUI()
	{
		init();
	}
	
	public FahrtEditorGUI(FahrtEditorGUIController controller, String titel)
	{
		super(titel);
		
		NullTester.test(controller);
		this.controller = controller;
		
		init();
	}
	
	public void init()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				controller.close();
			}
		});
		
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblZugname = new JLabel("Zug");
		contentPanel.add(lblZugname, "2, 2, fill, default");
		
		comboBoxZugname = new AutoComboBox();
		comboBoxZugname.addActionListener(event -> controller.comboBoxSelectionChanged());
		comboBoxZugname.setEditable(true);
		contentPanel.add(comboBoxZugname, "4, 2, fill, fill");
		
		JLabel lblTemplate = new JLabel("Template");
		contentPanel.add(lblTemplate, "2, 4");
		
		lblTemplateInfo = new JTextPane();
		lblTemplateInfo.setEditable(false);
		lblTemplateInfo.setBackground(null);
		lblTemplateInfo.setBorder(null);
		contentPanel.add(lblTemplateInfo, "4, 4, fill, fill");
		
		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, "2, 6, 3, 1, fill, fill");
		
		tableModel = new FahrtEditorFahrplanTableModel(SPALTEN_ÜBERSCHRIFTEN);
		table = new JTable(tableModel);
		scrollPane.setViewportView(table);
		
		setVisible(true);
	}

	public String getSelectedFahrt()
	{
		return comboBoxZugname.getSelectedItem();
	}
	
	public void clearFahrt()
	{
		table.setModel(new DefaultTableModel());
		lblTemplateInfo.setText("");
		comboBoxZugname.setSelectedItem(null);
	}
	public void updateSelectedFahrt(Fahrt fahrt, String[][] fahrplan, TableModelListener tableUpdate)
	{
		DefaultTableModel model = new FahrtEditorFahrplanTableModel(fahrplan, SPALTEN_ÜBERSCHRIFTEN);
		model.addTableModelListener(tableUpdate);
		table.setModel(model);
		
		lblTemplateInfo.setText(fahrt.getTemplate().toString());
		comboBoxZugname.setSelectedItem(fahrt.getName());
	}
	
	public void setComboBoxItems(String[] items)
	{
		comboBoxZugname.setItems(items);
	}
	
	public String getTableValueAt(int row, int column)
	{
		return (String) table.getValueAt(row, column);
	}
	
	public void close()
	{
		dispose();
		setVisible(false);
		controller = null;
	}
	
	public FahrtEditorGUIController getController()
	{
		return controller;
	}
	
	public void errorMessage(String text, String titel)
	{
		JComponent pane = this.createSelectableTextPane(text);
		JOptionPane.showMessageDialog(contentPanel, pane, titel, JOptionPane.ERROR_MESSAGE);
	}
	
	public void warningMessage(String text, String titel)
	{
		JComponent pane = this.createSelectableTextPane(text);
		JOptionPane.showMessageDialog(contentPanel, pane, titel, JOptionPane.WARNING_MESSAGE);
	}
	
	public void infoMessage(String text, String titel)
	{
		JComponent pane = this.createSelectableTextPane(text);
		JOptionPane.showMessageDialog(contentPanel, pane, titel, JOptionPane.INFORMATION_MESSAGE);
	}
}
