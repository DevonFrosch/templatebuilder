package de.stsFanGruppe.templatebuilder.streckenConfig;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import de.stsFanGruppe.templatebuilder.bildfahrplan.BildfahrplanGUIController;
import de.stsFanGruppe.templatebuilder.fahrtenFarbe.FahrtenFarbeConfig;
import de.stsFanGruppe.templatebuilder.fahrtenFarbe.FahrtenFarbeSettingsGUIController;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.tools.FirstLastList;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.TableModel;

import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.jgoodies.forms.layout.FormSpecs;

public class BildfahrplanSettingsStreckenGUI extends JDialog implements GUI
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsStreckenGUI.class);
	
	protected static Streckenabschnitt streckenabschnitt;
	private BildfahrplanGUIController bildfahrplanController = null;
	protected BildfahrplanSettingsStreckenGUIController controller;
	protected BildfahrplanStreckenConfig config;
	boolean saveEnabled = false;
	
	protected static Map<Betriebsstelle, Double> streckenKm;
	
	JLabel lblDescription;
	JTable table;
	
	public static final String[] columnName = {"km", "Bestriebsstelle"};
	private JTextField textField;
	
	final JPanel contentPanel = new JPanel();
		
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			BildfahrplanSettingsStreckenGUI dialog = new BildfahrplanSettingsStreckenGUI(new BildfahrplanSettingsStreckenGUIController(new BildfahrplanStreckenConfig(), () -> {}), null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public BildfahrplanSettingsStreckenGUI(BildfahrplanSettingsStreckenGUIController controller, Window parent)
	{
		super(parent);
		NullTester.test(controller);
		this.controller = controller;
		this.config = controller.getConfig();
		controller.setSettingsGUI(this);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 500, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			FormLayout fl_panel = new FormLayout(
					new ColumnSpec[] {FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormSpecs.RELATED_GAP_COLSPEC,
							FormSpecs.DEFAULT_COLSPEC, FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,},
					new RowSpec[] {FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormSpecs.RELATED_GAP_ROWSPEC,
							FormSpecs.DEFAULT_ROWSPEC, FormSpecs.UNRELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
							FormSpecs.RELATED_GAP_ROWSPEC,});
			contentPanel.setLayout(fl_panel);
			{
				JLabel lblDiscription = new JLabel();
				contentPanel.add(lblDiscription, "2, 2, 3, 1");
				lblDiscription
						.setText("<html>Hier kannst du die Strecke bearbeiten.</html>");
			}
			{				
				table = new JTable(tableModel());
				table.getTableHeader().setReorderingAllowed(false);
				
				table.setDefaultRenderer( Point.class, new PointRender());
				
				JScrollPane scrollPane = new JScrollPane(table);
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				contentPanel.add(scrollPane, "2, 4");
			}
			{
				JPanel buttonTablePane = new JPanel();
				contentPanel.add(buttonTablePane, "4, 4, default, top");
				buttonTablePane.setLayout(new FormLayout(new ColumnSpec[] {FormSpecs.DEFAULT_COLSPEC,},
						new RowSpec[] {FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
								FormSpecs.DEFAULT_ROWSPEC, FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,}));
				{
					JButton btnMoveUp = new JButton("Oben");
					btnMoveUp.setActionCommand("moveUpRow");
					btnMoveUp.addActionListener((ActionEvent arg0) -> controller.tableButtonAction(arg0));
					buttonTablePane.add(btnMoveUp, "1, 2");
				}
				{
					JButton btnMoveDown = new JButton("Unten");
					btnMoveDown.setActionCommand("moveDownRow");
					btnMoveDown.addActionListener((ActionEvent arg0) -> controller.tableButtonAction(arg0));
					buttonTablePane.add(btnMoveDown, "1, 4");
				}
				{
					JButton btnAddRow = new JButton("+");
					btnAddRow.setActionCommand("addRow");
					btnAddRow.addActionListener((ActionEvent arg0) -> controller.tableButtonAction(arg0));
					buttonTablePane.add(btnAddRow, "1, 6");
				}
				{
					JButton btnRemoveRow = new JButton("-");
					btnRemoveRow.setActionCommand("removeRow");
					btnRemoveRow.addActionListener((ActionEvent arg0) -> controller.tableButtonAction(arg0));
					buttonTablePane.add(btnRemoveRow, "1, 8");
				}
			}
		}
		loadSettings();
		setVisible(true);
	}
	
	private DefaultTableModel tableModel() {
		
		DefaultTableModel model = new DefaultTableModel(columnName, 0){
            @Override
            public Class<?> getColumnClass( int column ) {
                switch( column ){
                    case 0: return Integer.class;
                    case 1: return String.class;
                    default: return Object.class;
                }
            }
        };
        
		if(streckenabschnitt == null)
		{
			return model;
		}
		
		Streckenabschnitt streckenabschnitt = bildfahrplanController.getStreckenabschnitt();
        
		for(Betriebsstelle bs: streckenabschnitt.getBetriebsstellen())
		{
			double km = streckenKm.get(bs);
			String name = bs.getName();
			Object[] row = {km, name};
			
			model.addRow(row);
		}
		
		return model;
	}

	public void close()
	{
		dispose();
        setVisible(false);
        controller = null;
	}
	
	public void errorMessage(String text, String titel)
	{
		JOptionPane.showMessageDialog(contentPanel, text, titel, JOptionPane.ERROR_MESSAGE);
	}
	public void warningMessage(String text, String titel)
	{
		JOptionPane.showMessageDialog(contentPanel, text, titel, JOptionPane.WARNING_MESSAGE);
	}
	public void infoMessage(String text, String titel)
	{
		JOptionPane.showMessageDialog(contentPanel, text, titel, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Beim Öffnen der GUI werden die Strecken für die Tabelle ausgelesen und korrekt dargestellt.
	 */
	public void loadSettings() {
		// TODO Auto-generated method stub
		
	}
	public class PointRender extends DefaultTableCellRenderer{
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
	    public Component getTableCellRendererComponent( JTable table, Object value, 
	            boolean isSelected, boolean hasFocus, int row, int column ) {
	        Point point = (Point)value;
	        String text = point.x + " / " + point.y;
	        return super.getTableCellRendererComponent( table, text, isSelected,
	            hasFocus,  row, column );
	    }
	}
}

