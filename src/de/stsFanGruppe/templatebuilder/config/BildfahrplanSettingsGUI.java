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
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsGUI.class);
	
	BildfahrplanSettingsGUIController controller;
	BildfahrplanConfig config;
	
	final JPanel contentPanel = new JPanel();
	// Bildfahrplan
	JTextField inputMinZeit;
	JTextField inputMaxZeit;
	JTextField inputHoeheProStunde;
	JSlider sliderHoeheProStunde;
	JCheckBox chckbxAuto;
	JTextField inputSchachtelung;
	ButtonGroup rdbtngrpZeigeZugnamen;
	JCheckBox chckbxZugnamenKommentare;
	JCheckBox chckbxZeigeZeiten;
	
	// Farben 
	JPanel panelBfpZeitenFarbeVorschau;
	JPanel panelBfpBetriebsstellenFarbeVorschau;
	JPanel panelBfpFahrtenFarbeVorschau;
	
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
		this.config = controller.getConfig();
		controller.setSettingsGUI(this);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			contentPanel.add(tabbedPane, BorderLayout.CENTER);
			{
				JPanel panelAllgemein = new JPanel();
				tabbedPane.addTab("Allgemein", null, panelAllgemein, null);
				tabbedPane.setEnabledAt(0, false);
				panelAllgemein.setLayout(new FormLayout(new ColumnSpec[] {},
					new RowSpec[] {}));
			}
			{
				JPanel panelStreckeneditor = new JPanel();
				tabbedPane.addTab("Streckeneditor", null, panelStreckeneditor, null);
				tabbedPane.setEnabledAt(1, false);
			}
			{
				JPanel panelZugeditor = new JPanel();
				tabbedPane.addTab("Zugeditor", null, panelZugeditor, null);
				tabbedPane.setEnabledAt(2, false);
			}
			{
				JPanel panelBfp = new JPanel();
				tabbedPane.addTab("Bildfahrplan", null, panelBfp, null);
				tabbedPane.setEnabledAt(3, true);
				panelBfp.setLayout(new FormLayout(new ColumnSpec[] {
						FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_COLSPEC,},
					new RowSpec[] {
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,}));
				{
					JPanel panelZeiten = new JPanel();
					panelZeiten.setBorder(BorderFactory.createTitledBorder("Zeiten"));
					panelBfp.add(panelZeiten, "2, 2, fill, fill");
					panelZeiten.setLayout(new FormLayout(new ColumnSpec[] {
							FormSpecs.RELATED_GAP_COLSPEC,
							ColumnSpec.decode("right:default"),
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
							ColumnSpec.decode("default:grow"),
							FormSpecs.RELATED_GAP_COLSPEC,},
						new RowSpec[] {
							FormSpecs.RELATED_GAP_ROWSPEC,
							FormSpecs.DEFAULT_ROWSPEC,
							FormSpecs.RELATED_GAP_ROWSPEC,
							FormSpecs.DEFAULT_ROWSPEC,
							FormSpecs.RELATED_GAP_ROWSPEC,
							FormSpecs.DEFAULT_ROWSPEC,
							FormSpecs.RELATED_GAP_ROWSPEC,}));
					{
						JLabel lblHheProStunde = new JLabel("H\u00F6he pro Stunde");
						panelZeiten.add(lblHheProStunde, "2, 2");
					}
					{
						JPanel panelHoeheProStunde = new JPanel();
						panelZeiten.add(panelHoeheProStunde, "4, 2");
						panelHoeheProStunde.setLayout(new FormLayout(new ColumnSpec[] {
								com.jgoodies.forms.layout.FormSpecs.UNRELATED_GAP_COLSPEC,
								ColumnSpec.decode("default:grow"),
								com.jgoodies.forms.layout.FormSpecs.RELATED_GAP_COLSPEC,
								com.jgoodies.forms.layout.FormSpecs.DEFAULT_COLSPEC,},
							new RowSpec[] {
								com.jgoodies.forms.layout.FormSpecs.DEFAULT_ROWSPEC,}));
						{
							inputHoeheProStunde = new JTextField();
							inputHoeheProStunde.addActionListener((ActionEvent arg0) -> {
								try
								{
									sliderHoeheProStunde.setValue(Integer.parseInt(inputHoeheProStunde.getText()));
								}
								catch(NumberFormatException e)
								{}
							});
							panelHoeheProStunde.add(inputHoeheProStunde, "4, 1");
							inputHoeheProStunde.setColumns(10);
						}
						{
							sliderHoeheProStunde = new JSlider();
							sliderHoeheProStunde.addChangeListener((ChangeEvent arg0) -> { inputHoeheProStunde.setText(sliderHoeheProStunde.getValue()+""); });
							sliderHoeheProStunde.setSnapToTicks(true);
							sliderHoeheProStunde.setPaintTicks(true);
							sliderHoeheProStunde.setMinorTickSpacing(100);
							sliderHoeheProStunde.setMinimum(100);
							sliderHoeheProStunde.setMaximum(2000);
							sliderHoeheProStunde.setValue(config.getHoeheProStunde());
							panelHoeheProStunde.add(sliderHoeheProStunde, "2, 1");
						}
					}
					{
						JLabel lblDargestellteZeit = new JLabel("Dargestellte Zeit");
						panelZeiten.add(lblDargestellteZeit, "2, 4");
					}
					{
						JPanel panelZeit = new JPanel();
						panelZeiten.add(panelZeit, "4, 4");
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
							panelZeit.add(inputMinZeit, "1, 1");
							inputMinZeit.setColumns(10);
						}
						{
							JLabel lblZeitTrenner = new JLabel("-");
							panelZeit.add(lblZeitTrenner, "3, 1");
						}
						{
							inputMaxZeit = new JTextField();
							inputMaxZeit.setText(controller.getMaxZeit());
							panelZeit.add(inputMaxZeit, "5, 1");
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
						panelZeiten.add(lblSchachtelung, "2, 6");
					}
					{
						inputSchachtelung = new JTextField();
						panelZeiten.add(inputSchachtelung, "4, 6");
						inputSchachtelung.setColumns(10);
						inputSchachtelung.setText(String.valueOf(config.getSchachtelung()));
					}
				}
				{
					JPanel panelDarstellung = new JPanel();
					panelDarstellung.setBorder(BorderFactory.createTitledBorder("Darstellung"));
					panelBfp.add(panelDarstellung, "2, 4, fill, fill");
					panelDarstellung.setLayout(new FormLayout(new ColumnSpec[] {
							FormSpecs.RELATED_GAP_COLSPEC,
							ColumnSpec.decode("right:default"),
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
							ColumnSpec.decode("default:grow"),
							FormSpecs.RELATED_GAP_COLSPEC,},
						new RowSpec[] {
							FormSpecs.RELATED_GAP_ROWSPEC,
							FormSpecs.DEFAULT_ROWSPEC,
							FormSpecs.RELATED_GAP_ROWSPEC,
							FormSpecs.DEFAULT_ROWSPEC,
							FormSpecs.RELATED_GAP_ROWSPEC,
							FormSpecs.DEFAULT_ROWSPEC,
							FormSpecs.RELATED_GAP_ROWSPEC,}));
					{
						JLabel lblZeigeZugnamen = new JLabel("Zeige Zugnamen");
						panelDarstellung.add(lblZeigeZugnamen, "2, 2");
					}
					
					rdbtngrpZeigeZugnamen = new ButtonGroup();
					
					JPanel panelZeigeZugnamen = new JPanel();
					panelDarstellung.add(panelZeigeZugnamen, "4, 2, left, fill");
					FlowLayout fl_panelZeigeZugnamen = (FlowLayout) panelZeigeZugnamen.getLayout();
					fl_panelZeigeZugnamen.setHgap(0);
					fl_panelZeigeZugnamen.setVgap(0);
					
					JRadioButton radioZeigeZugnamenNie = new JRadioButton("nie");
					rdbtngrpZeigeZugnamen.add(radioZeigeZugnamenNie);
					radioZeigeZugnamenNie.setActionCommand("nie");
					panelZeigeZugnamen.add(radioZeigeZugnamenNie);
					
					JRadioButton radioZeigeZugnamenAuto = new JRadioButton("automatisch");
					rdbtngrpZeigeZugnamen.add(radioZeigeZugnamenAuto);
					radioZeigeZugnamenAuto.setActionCommand("automatisch");
					panelZeigeZugnamen.add(radioZeigeZugnamenAuto);
					
					JRadioButton radioZeigeZugnamenImmer = new JRadioButton("immer");
					rdbtngrpZeigeZugnamen.add(radioZeigeZugnamenImmer);
					radioZeigeZugnamenImmer.setActionCommand("immer");
					panelZeigeZugnamen.add(radioZeigeZugnamenImmer);
					
					switch(config.getZeigeZugnamen())
					{
						case 0:
							rdbtngrpZeigeZugnamen.setSelected(radioZeigeZugnamenNie.getModel(), true);
							break;
						case 1:
							rdbtngrpZeigeZugnamen.setSelected(radioZeigeZugnamenImmer.getModel(), true);
							break;
						case 2:
							rdbtngrpZeigeZugnamen.setSelected(radioZeigeZugnamenAuto.getModel(), true);
							break;
						default:
							log.error("ZeigeZugnamen: Ung�ltiger Wert {}", config.getZeigeZugnamen());
					}
					
					{
						chckbxZugnamenKommentare = new JCheckBox("Zeige Kommentare in Zugnamen");
						panelDarstellung.add(chckbxZugnamenKommentare, "4, 4");
						chckbxZugnamenKommentare.setToolTipText("Kommentare sind alles ab dem ersten %-Zeichen");
						chckbxZugnamenKommentare.setSelected(config.getZeigeZugnamenKommentare());
					}
					{
						chckbxZeigeZeiten = new JCheckBox("Zeige Zeiten an Halten");
						panelDarstellung.add(chckbxZeigeZeiten, "4, 6");
						chckbxZeigeZeiten.setSelected(config.getZeigeZeiten());
					}
				}
			}
			{
				JPanel panelFarben = new JPanel();
				tabbedPane.addTab("Farben", null, panelFarben, null);
				tabbedPane.setEnabledAt(4, true);
				panelFarben.setLayout(new FormLayout(new ColumnSpec[] {
						FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_COLSPEC,},
					new RowSpec[] {
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,}));
				{
					JPanel panelFarbenBfp = new JPanel();
					panelFarbenBfp.setBorder(BorderFactory.createTitledBorder("Bildfahrplan"));
					panelFarben.add(panelFarbenBfp, "2, 2, fill, fill");
					panelFarbenBfp.setLayout(new FormLayout(new ColumnSpec[] {
							FormSpecs.RELATED_GAP_COLSPEC,
							ColumnSpec.decode("right:default"),
							FormSpecs.UNRELATED_GAP_COLSPEC,
							ColumnSpec.decode("left:default:grow"),
							FormSpecs.RELATED_GAP_COLSPEC,},
						new RowSpec[] {
							FormSpecs.RELATED_GAP_ROWSPEC,
							FormSpecs.DEFAULT_ROWSPEC,
							FormSpecs.RELATED_GAP_ROWSPEC,
							FormSpecs.DEFAULT_ROWSPEC,
							FormSpecs.RELATED_GAP_ROWSPEC,
							FormSpecs.DEFAULT_ROWSPEC,
							FormSpecs.RELATED_GAP_ROWSPEC,}));
					{
						JLabel lblBfpZeitenFarbe = new JLabel("Zeiten");
						panelFarbenBfp.add(lblBfpZeitenFarbe, "2, 2");
					}
					{
						{
							JPanel panelBfpZeitenFarbe = new JPanel();
							panelFarbenBfp.add(panelBfpZeitenFarbe, "4, 2");
							{
								panelBfpZeitenFarbeVorschau = new JPanel();
								panelBfpZeitenFarbeVorschau.setBackground(config.getBfpFahrtenFarbe());
								FlowLayout fl_panelBfpZeitenFarbeVorschau = (FlowLayout) panelBfpZeitenFarbeVorschau.getLayout();
								fl_panelBfpZeitenFarbeVorschau.setVgap(10);
								fl_panelBfpZeitenFarbeVorschau.setHgap(10);
								panelBfpZeitenFarbe.add(panelBfpZeitenFarbeVorschau);
							}
							JButton btnBfpZeitenFarbe = new JButton("w\u00E4hlen...");
							panelBfpZeitenFarbe.add(btnBfpZeitenFarbe);
							btnBfpZeitenFarbe.setActionCommand("zeitenFarbe");
							btnBfpZeitenFarbe.addActionListener((ActionEvent arg0) -> controller.farbButton(arg0));
						}
					}
					{
						{
							JLabel lblBfpBetriebsstellenFarbe = new JLabel("Betriebsstellen");
							panelFarbenBfp.add(lblBfpBetriebsstellenFarbe, "2, 4");
						}
						{
							JPanel panelBfpBetriebsstellenFarbe = new JPanel();
							panelFarbenBfp.add(panelBfpBetriebsstellenFarbe, "4, 4");
							{
								panelBfpBetriebsstellenFarbeVorschau = new JPanel();
								panelBfpBetriebsstellenFarbeVorschau.setBackground(config.getBfpBetriebsstellenFarbe());
								FlowLayout flowLayout = (FlowLayout) panelBfpBetriebsstellenFarbeVorschau.getLayout();
								flowLayout.setHgap(10);
								flowLayout.setVgap(10);
								panelBfpBetriebsstellenFarbe.add(panelBfpBetriebsstellenFarbeVorschau);
							}
							JButton btnBfpBetriebsstellenFarbe = new JButton("w\u00E4hlen...");
							panelBfpBetriebsstellenFarbe.add(btnBfpBetriebsstellenFarbe);
							btnBfpBetriebsstellenFarbe.setActionCommand("betriebsstellenFarbe");
							btnBfpBetriebsstellenFarbe.addActionListener((ActionEvent arg0) -> controller.farbButton(arg0));
						}
					}
					{
						{
							JLabel lblBfpFahrtenFarbe = new JLabel("Z\u00FCge");
							panelFarbenBfp.add(lblBfpFahrtenFarbe, "2, 6");
						}
						{
							JPanel panelBfpFahrtenFarbe = new JPanel();
							panelFarbenBfp.add(panelBfpFahrtenFarbe, "4, 6");
							{
								panelBfpFahrtenFarbeVorschau = new JPanel();
								panelBfpFahrtenFarbeVorschau.setBackground(config.getBfpFahrtenFarbe());
								FlowLayout flowLayout = (FlowLayout) panelBfpFahrtenFarbeVorschau.getLayout();
								flowLayout.setHgap(10);
								flowLayout.setVgap(10);
								panelBfpFahrtenFarbe.add(panelBfpFahrtenFarbeVorschau);
							}
							JButton btnBfpFahrtenFarbe = new JButton("w\u00E4hlen...");
							panelBfpFahrtenFarbe.add(btnBfpFahrtenFarbe);
							btnBfpFahrtenFarbe.setActionCommand("fahrtenFarbe");
							{
								JPanel panelZugregeln = new JPanel();
								panelZugregeln.setBorder(BorderFactory.createTitledBorder("Zugregeln"));
								panelFarben.add(panelZugregeln, "2, 4, fill, fill");
								panelZugregeln.setLayout(new FormLayout(new ColumnSpec[] {
										FormSpecs.RELATED_GAP_COLSPEC,
										ColumnSpec.decode("default:grow"),
										FormSpecs.RELATED_GAP_COLSPEC,},
									new RowSpec[] {
										FormSpecs.RELATED_GAP_ROWSPEC,
										FormSpecs.DEFAULT_ROWSPEC,
										FormSpecs.RELATED_GAP_ROWSPEC,}));
								{
									JButton btnZugregeln = new JButton("Zugformatierungsregeln...");
									btnZugregeln.setEnabled(false);
									panelZugregeln.add(btnZugregeln, "2, 2, left, top");
								}
							}
							btnBfpFahrtenFarbe.addActionListener((ActionEvent arg0) -> controller.farbButton(arg0));
						}
					}
				}
			}
		
			// TODO entfernen, wenn Tab Allgemein vorhanden ist
			tabbedPane.setSelectedIndex(3);
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