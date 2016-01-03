package de.stsFanGruppe.templatebuilder.fahrtenFarbe;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import de.stsFanGruppe.templatebuilder.config.*;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.tools.NullTester;
import javax.swing.border.TitledBorder;
import com.jgoodies.forms.factories.DefaultComponentFactory;

public class FahrtenFarbeSettingsGUI extends JDialog implements GUI
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsGUI.class);
	
	FahrtenFarbeSettingsGUIController controller;
	FahrtenFarbeConfig config;
	boolean saveEnabled = false;
	
	final JPanel contentPanel = new JPanel();
	
	JLabel lblDescription;
	
	JTable table;
	
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
			FormLayout fl_panel = new FormLayout(
				new ColumnSpec[] 
					{
						FormSpecs.RELATED_GAP_COLSPEC, 
						FormSpecs.DEFAULT_COLSPEC, 
						FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, 
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
					},
				new RowSpec[] 
					{
						FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.UNRELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,
					}
				);
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
					{"Kathy", "Smith", "Snowboarding", new Integer(5), new Boolean(false)},
					{"John", "Doe", "Rowing", new Integer(3), new Boolean(true)},
					{"Sue", "Black", "Knitting", new Integer(2), new Boolean(false)},
					{"Jane", "White", "Speed reading", new Integer(20), new Boolean(true)},
					{"Joe", "Brown", "Pool", new Integer(10), new Boolean(false)}
				};
						
				String[] columnName = {"Zugname", "Linienfarbe", "Linienstärke", "Linienart"};
				
				table = new JTable(data, columnName);
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
					buttonTablePane.add(btnMoveUp, "1, 2");
				}
				{
					JButton btnMoveDown = new JButton("Unten");
					buttonTablePane.add(btnMoveDown, "1, 4");
				}
				{
					JButton btnAddRow = new JButton("+");
					buttonTablePane.add(btnAddRow, "1, 6");
				}
				{
					JButton btnRemoveRow = new JButton("-");
					buttonTablePane.add(btnRemoveRow, "1, 8");
				}
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
