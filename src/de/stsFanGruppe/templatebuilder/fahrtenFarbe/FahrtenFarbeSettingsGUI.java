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

public class FahrtenFarbeSettingsGUI extends JDialog implements GUI
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsGUI.class);
	
	FahrtenFarbeSettingsGUIController controller;
	FahrtenFarbeConfig config;
	boolean saveEnabled = false;
	
	final JPanel contentPanel = new JPanel();
	
	JLabel lblDescription;
	JTable table;
	JPanel panelDescription;

	public static void main(String[] args)
	{
		try
		{
			FahrtenFarbeSettingsGUI dialog = new FahrtenFarbeSettingsGUI(new FahrtenFarbeSettingsGUIController(new FahrtenFarbeConfig(), () -> {}), null);
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
		setBounds(100, 100, 450, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		{
			{ //Rahmen
				panelDescription = new JPanel();
				panelDescription.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Linienfarbe konfigurieren", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
				panelDescription.setPreferredSize(new Dimension(getWidth() - 20, getHeight() - 100));
				panelDescription.setLayout(new GridLayout(3, 5));
				contentPanel.add(panelDescription, "2, 2, fill, fill");
			}
			{ //Beschreibung
				JLabel lblDescription = new JLabel("<html>F\u00FCr eine bessere Darstellung k\u00F6nnen Linien hier nach festgelegten Kriterien hervorgehoben werden.</html>");
				panelDescription.add(lblDescription);
			}
			{ //Tabelle
				Object[][] data = {
					    {"Kathy", "Smith",
					     "Snowboarding"},
					};
				String[] columnNamens = {"Suchkriterium", "Farbe", "Linienart"};
				table = new JTable(data, columnNamens);
				panelDescription.add(table.getTableHeader(), BorderLayout.NORTH);
				panelDescription.add(table, BorderLayout.CENTER);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				buttonPane.setLayout(new FormLayout(new ColumnSpec[] {
						FormSpecs.UNRELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC,
						ColumnSpec.decode("3dlu:grow"),
						FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.UNRELATED_GAP_COLSPEC,},
					new RowSpec[] {
						FormSpecs.LINE_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.UNRELATED_GAP_ROWSPEC,}));
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

	public void loadSettings() {
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
