package de.stsFanGruppe.templatebuilder.fahrtenFarbe;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import de.stsFanGruppe.bibliothek.FahrtenFarbe;
import de.stsFanGruppe.templatebuilder.config.*;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.tools.NullTester;
import javax.swing.table.DefaultTableModel;

public class FahrtenFarbeSettingsGUI extends JDialog implements GUI
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsGUI.class);
	
	FahrtenFarbeSettingsGUIController controller;
	FahrtenFarbeConfig config;
	FahrtenFarbe fahrtenFarbe;
	
	boolean saveEnabled = false;
	final JPanel contentPanel = new JPanel();
	JLabel lblDescription;
	JTable table;
	
	public final String[] columnName = {"Zugname", "Linienfarbe", "Linienstärke", "Linienart"};
	
	public static void main(String[] args)
	{
		try
		{
			FahrtenFarbeSettingsGUI dialog = new FahrtenFarbeSettingsGUI(new FahrtenFarbeSettingsGUIController(new FahrtenFarbeConfig(), () -> {}),
					null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public FahrtenFarbeSettingsGUI(FahrtenFarbeSettingsGUIController controller, Window parent)
	{
		super(parent);
		NullTester.test(controller);
		this.controller = controller;
		this.config = controller.getConfig();
		controller.setSettingsGui(this);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 500, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			FormLayout fl_panel = new FormLayout(new ColumnSpec[] {
					FormSpecs.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("default:grow"),
					FormSpecs.RELATED_GAP_COLSPEC,
					FormSpecs.DEFAULT_COLSPEC,
					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,},
				new RowSpec[] {
					FormSpecs.RELATED_GAP_ROWSPEC,
					RowSpec.decode("default:grow"),
					FormSpecs.RELATED_GAP_ROWSPEC,
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.UNRELATED_GAP_ROWSPEC,
					RowSpec.decode("default:grow"),
					FormSpecs.RELATED_GAP_ROWSPEC,});
			contentPanel.setLayout(fl_panel);
			{
				JLabel lblDiscription = new JLabel();
				contentPanel.add(lblDiscription, "2, 2, 3, 1");
				lblDiscription
						.setText("<html>Für eine bessere Darstellung können Linien hier nach festgelegten Kriterien hervorgehoben werden.</html>");
			}
			{
				
				Object[][] data = 
				{
					{"Kathy", "Smith", "Doe", new Integer(5)},
					{"John", "Doe", "Doe", new Integer(3)},
					{"Sue", "Black", "Doe", new Integer(2)},
					{"Jane", "White", "Doe", new Integer(20)},
					{"Joe", "Brown", "Doe", new Integer(10)}
				};
						
				DefaultTableModel tableModel = new DefaultTableModel();
				
				tableModel.setDataVector(data, columnName);			
				
				table = new JTable(tableModel);
				table.getTableHeader().setReorderingAllowed(false);
				//TableColumn linienArtColumn = table.getColumnModel().getColumn(2);
				//linienArt.setUpLinienArtColumn(table, linienArtColumn);
				
				JScrollPane scrollPane = new JScrollPane(table);
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
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
		{
			JPanel standardConfigLabel = new JPanel();
			contentPanel.add(standardConfigLabel, "2, 6, 3, 1, fill, fill");
			FormLayout fl_standardConfigLabel = new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("default:grow"),
					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
					FormSpecs.DEFAULT_COLSPEC,},
				new RowSpec[] {
					RowSpec.decode("default:grow"),
					FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
					FormSpecs.DEFAULT_ROWSPEC,});
			standardConfigLabel.setLayout(fl_standardConfigLabel);
			{
				JLabel lblStandardfarbe = new JLabel("Standardfarbe");
				standardConfigLabel.add(lblStandardfarbe, "1, 1, left, default");
			}
			{
				JComboBox comboBox = new JComboBox<float[]>(config.getComboBoxListe());
				standardConfigLabel.add(comboBox, "3, 1, fill, default");
				
//				for (int i = 0; i < linienArt.getLinienArtenSammlung().size(); i++)
//				{
//					comboBox.addItem(linienArt.getLinienArtenSammlung().get(i));
//				}
			}
		}
		
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				buttonPane.setLayout(new FormLayout(
						new ColumnSpec[] {FormSpecs.UNRELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
								FormSpecs.DEFAULT_COLSPEC, ColumnSpec.decode("3dlu:grow"), FormSpecs.DEFAULT_COLSPEC,
								FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
								FormSpecs.DEFAULT_COLSPEC, FormSpecs.UNRELATED_GAP_COLSPEC,},
						new RowSpec[] {FormSpecs.LINE_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.UNRELATED_GAP_ROWSPEC,}));
				{
					JButton speichernButton = new JButton("Speichern");
					speichernButton.setActionCommand("save");
					speichernButton.addActionListener((ActionEvent arg0) -> controller.actionButton(arg0));
					buttonPane.add(speichernButton, "2, 2, left, top");
				}
				{
					JButton ladenButton = new JButton("Laden");
					ladenButton.setActionCommand("load");
					ladenButton.addActionListener((ActionEvent arg0) -> controller.actionButton(arg0));
					buttonPane.add(ladenButton, "4, 2, left, top");
				}
				{
					JButton okButton = new JButton("OK");
					okButton.setEnabled(saveEnabled);
					okButton.setActionCommand("ok");
					okButton.addActionListener((ActionEvent arg0) -> controller.actionButton(arg0));
					buttonPane.add(okButton, "6, 2, left, top");
					getRootPane().setDefaultButton(okButton);
				}
				{
					JButton cancelButton = new JButton("Abbrechen");
					cancelButton.setEnabled(saveEnabled);
					cancelButton.setActionCommand("cancel");
					cancelButton.addActionListener((ActionEvent arg0) -> controller.actionButton(arg0));
					buttonPane.add(cancelButton, "8, 2, left, top");
				}
				{
					JButton applyButton = new JButton("Anwenden");
					applyButton.setEnabled(saveEnabled);
					applyButton.setActionCommand("apply");
					applyButton.addActionListener((ActionEvent arg0) -> controller.actionButton(arg0));
					buttonPane.add(applyButton, "10, 2, left, top");
				}
			}
		}
		loadSettings();
		setVisible(true);
	}
	
	public void loadSettings()
	{
		log.info("FahrtenFarbe einlesen");
		
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
}
