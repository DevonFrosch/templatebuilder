package de.stsFanGruppe.templatebuilder.gui.settings;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.tools.NullTester;
import javax.swing.event.ChangeEvent;

public class SettingsGUI extends JDialog
{
	SettingsGUIController controller;
	
	final JPanel contentPanel = new JPanel();
	JTextField inputMinZeit;
	JTextField inputMaxZeit;
	JTextField inputHoeheProStunde;
	JSlider sliderHoeheProStunde;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			SettingsGUI dialog = new SettingsGUI(new SettingsGUIController(new BildfahrplanConfig()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public SettingsGUI(SettingsGUIController controller)
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
				tabbedPane.addTab("Bildfahrplan", null, panel, null);
				panel.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.UNRELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormFactory.UNRELATED_GAP_COLSPEC,},
					new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,}));
				{
					JLabel lblHheProStunde = new JLabel("H\u00F6he pro Stunde");
					panel.add(lblHheProStunde, "2, 2, fill, fill");
				}
				{
					JPanel panel_1 = new JPanel();
					panel.add(panel_1, "4, 2, fill, fill");
					panel_1.setLayout(new FormLayout(new ColumnSpec[] {
							FormFactory.UNRELATED_GAP_COLSPEC,
							ColumnSpec.decode("default:grow"),
							FormFactory.RELATED_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,},
						new RowSpec[] {
							FormFactory.DEFAULT_ROWSPEC,}));
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
						panel_1.add(sliderHoeheProStunde, "2, 1, left, top");
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
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							ColumnSpec.decode("default:grow"),},
						new RowSpec[] {
							FormFactory.DEFAULT_ROWSPEC,}));
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
				}
			}
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
				applyButton.setActionCommand("OK");
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
	
	public void errorMessage(String text)
	{
		JOptionPane.showMessageDialog(null, text, "Fehler", JOptionPane.ERROR_MESSAGE);
	}
	
	private static void log(String text)
	{
		System.out.println("SettingsGUI: "+text);
	}
}
