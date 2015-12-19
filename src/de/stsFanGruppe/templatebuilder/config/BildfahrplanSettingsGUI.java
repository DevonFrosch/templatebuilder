package de.stsFanGruppe.templatebuilder.config;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.tools.NullTester;
import javax.swing.event.ChangeEvent;
import com.jgoodies.forms.layout.FormSpecs;

public class BildfahrplanSettingsGUI extends JDialog implements GUI
{
	BildfahrplanSettingsGUIController controller;
	
	final JPanel contentPanel = new JPanel();
	JTextField inputMinZeit;
	JTextField inputMaxZeit;
	JTextField inputHoeheProStunde;
	JSlider sliderHoeheProStunde;
	JCheckBox chckbxAuto;
	JTextField inputSchachtelung;
	ButtonGroup rdbtngrpZeigeZugnamen;
	JCheckBox chckbxZugnamenKommentare;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			BildfahrplanSettingsGUI dialog = new BildfahrplanSettingsGUI(new BildfahrplanSettingsGUIController(new BildfahrplanConfig()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public BildfahrplanSettingsGUI(BildfahrplanSettingsGUIController controller)
	{
		NullTester.test(controller);
		this.controller = controller;
		controller.setSettingsGUI(this);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			contentPanel.add(tabbedPane, BorderLayout.CENTER);
			{
				JPanel panel = new JPanel();
				tabbedPane.addTab("Allgemein", null, panel, null);
				panel.setLayout(new FormLayout(new ColumnSpec[] {},
					new RowSpec[] {}));
			}
			{
				JPanel panel = new JPanel();
				tabbedPane.addTab("Bildfahrplan", null, panel, null);
				panel.setLayout(new FormLayout(new ColumnSpec[] {
						FormSpecs.UNRELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.UNRELATED_GAP_COLSPEC,},
					new RowSpec[] {
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,}));
				{
					JLabel lblHheProStunde = new JLabel("H\u00F6he pro Stunde");
					panel.add(lblHheProStunde, "2, 2, fill, fill");
				}
				{
					JPanel panel_1 = new JPanel();
					panel.add(panel_1, "4, 2, fill, fill");
					panel_1.setLayout(new FormLayout(new ColumnSpec[] {
							com.jgoodies.forms.layout.FormSpecs.UNRELATED_GAP_COLSPEC,
							ColumnSpec.decode("default:grow"),
							com.jgoodies.forms.layout.FormSpecs.RELATED_GAP_COLSPEC,
							com.jgoodies.forms.layout.FormSpecs.DEFAULT_COLSPEC,},
						new RowSpec[] {
							com.jgoodies.forms.layout.FormSpecs.DEFAULT_ROWSPEC,}));
					{
						inputHoeheProStunde = new JTextField();
						panel_1.add(inputHoeheProStunde, "4, 1, left, center");
						inputHoeheProStunde.setColumns(10);
					}
					{
						sliderHoeheProStunde = new JSlider();
						sliderHoeheProStunde.addChangeListener((ChangeEvent arg0) -> { inputHoeheProStunde.setText(sliderHoeheProStunde.getValue()+""); });
						sliderHoeheProStunde.setSnapToTicks(true);
						sliderHoeheProStunde.setPaintTicks(true);
						sliderHoeheProStunde.setMinorTickSpacing(50);
						sliderHoeheProStunde.setMinimum(50);
						sliderHoeheProStunde.setMaximum(1000);
						sliderHoeheProStunde.setValue(controller.getHoeheProStunde());
						panel_1.add(sliderHoeheProStunde, "2, 1, left, center");
					}
				}
				{
					JLabel lblDargestellteZeit = new JLabel("Dargestellte Zeit");
					panel.add(lblDargestellteZeit, "2, 4, fill, fill");
				}
				{
					JPanel panelZeit = new JPanel();
					panel.add(panelZeit, "4, 4, fill, default");
					panelZeit.setLayout(new FormLayout(new ColumnSpec[] {
							ColumnSpec.decode("default:grow"),
							com.jgoodies.forms.layout.FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
							com.jgoodies.forms.layout.FormSpecs.DEFAULT_COLSPEC,
							com.jgoodies.forms.layout.FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
							ColumnSpec.decode("default:grow"),
							com.jgoodies.forms.layout.FormSpecs.RELATED_GAP_COLSPEC,
							com.jgoodies.forms.layout.FormSpecs.DEFAULT_COLSPEC,},
						new RowSpec[] {
							com.jgoodies.forms.layout.FormSpecs.DEFAULT_ROWSPEC,}));
					{
						inputMinZeit = new JTextField();
						inputMinZeit.setText(controller.getMinZeit());
						panelZeit.add(inputMinZeit, "1, 1, fill, top");
						inputMinZeit.setColumns(10);
					}
					{
						JLabel lblZeitTrenner = new JLabel("-");
						panelZeit.add(lblZeitTrenner, "3, 1, left, center");
					}
					{
						inputMaxZeit = new JTextField();
						inputMaxZeit.setText(controller.getMaxZeit());
						panelZeit.add(inputMaxZeit, "5, 1, fill, top");
						inputMaxZeit.setColumns(10);
					}
					{
						chckbxAuto = new JCheckBox("auto");
						chckbxAuto.addActionListener((ActionEvent arg0) -> {
							inputMinZeit.setEnabled(!chckbxAuto.isSelected());
							inputMaxZeit.setEnabled(!chckbxAuto.isSelected());
						});
						panelZeit.add(chckbxAuto, "7, 1");
					}
				}
				{
					JLabel lblSchachtelung = new JLabel("Schachtelung");
					panel.add(lblSchachtelung, "2, 6, right, default");
				}
				{
					JPanel panel_1 = new JPanel();
					panel.add(panel_1, "4, 6, fill, fill");
					panel_1.setLayout(new FormLayout(new ColumnSpec[] {
							FormSpecs.UNRELATED_GAP_COLSPEC,
							ColumnSpec.decode("default:grow"),
							FormSpecs.RELATED_GAP_COLSPEC,
							FormSpecs.DEFAULT_COLSPEC,},
						new RowSpec[] {
							FormSpecs.DEFAULT_ROWSPEC,}));
					{
						inputSchachtelung = new JTextField();
						panel_1.add(inputSchachtelung, "4, 1, center, center");
						inputSchachtelung.setColumns(10);
					}
					{
						JSlider sliderSchachtelung = new JSlider();
						sliderSchachtelung.setValue(24);
						sliderSchachtelung.addChangeListener((ChangeEvent arg0) -> { inputSchachtelung.setText(sliderSchachtelung.getValue()+""); });
						sliderSchachtelung.setPaintTicks(true);
						sliderSchachtelung.setSnapToTicks(true);
						sliderSchachtelung.setMinorTickSpacing(1);
						sliderSchachtelung.setMinimum(1);
						sliderSchachtelung.setMaximum(24);
						sliderSchachtelung.setValue(controller.getSchachtelung());
						panel_1.add(sliderSchachtelung, "2, 1, left, center");
					}
				}
				{
					JLabel lblZeigeZugnamen = new JLabel("Zeige Zugnamen");
					panel.add(lblZeigeZugnamen, "2, 8");
				}
				{
					JPanel panel_1 = new JPanel();
					panel.add(panel_1, "4, 8, fill, fill");
					rdbtngrpZeigeZugnamen = new ButtonGroup();
					JRadioButton[] rds = new JRadioButton[3];
					
					rds[0] = new JRadioButton("nie");
					rdbtngrpZeigeZugnamen.add(rds[0]);
					rds[0].setActionCommand("nie");
					panel_1.add(rds[0]);
					
					rds[2] = new JRadioButton("automatisch");
					rdbtngrpZeigeZugnamen.add(rds[2]);
					rds[2].setActionCommand("automatisch");
					panel_1.add(rds[2]);
					
					rds[1] = new JRadioButton("immer");
					rdbtngrpZeigeZugnamen.add(rds[1]);
					rds[1].setActionCommand("immer");
					panel_1.add(rds[1]);
					
					rdbtngrpZeigeZugnamen.setSelected(rds[controller.getZeigeZugnamen()].getModel(), true);
				}
				{
					chckbxZugnamenKommentare = new JCheckBox("Zeige Kommentare in Zugnamen");
					chckbxZugnamenKommentare.setToolTipText("Kommentare sind alles ab dem ersten %-Zeichen");
					chckbxZugnamenKommentare.setSelected(controller.getZeigeZugnamenKommentare());
					panel.add(chckbxZugnamenKommentare, "4, 10");
				}
			}
			// TODO entfernen, wenn Tab Allgemein vorhanden ist
			tabbedPane.setSelectedIndex(1);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener((ActionEvent arg0) -> controller.actionButton(arg0));
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Abbrechen");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener((ActionEvent arg0) -> controller.actionButton(arg0));
				buttonPane.add(cancelButton);
			}
			{
				JButton applyButton = new JButton("Anwenden");
				applyButton.setActionCommand("Apply");
				applyButton.addActionListener((ActionEvent arg0) -> controller.actionButton(arg0));
				buttonPane.add(applyButton);
			}
		}
		setVisible(true);
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
