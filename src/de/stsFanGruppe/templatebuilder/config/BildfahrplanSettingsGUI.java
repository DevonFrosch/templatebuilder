package de.stsFanGruppe.templatebuilder.config;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.TimeFormater;
import javax.swing.event.ChangeEvent;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

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
	
	ButtonGroup rdbtngrpZeigeZugnamen;
	JRadioButton radioZeigeZugnamenNie;
	JRadioButton radioZeigeZugnamenImmer;
	JRadioButton radioZeigeZugnamenAuto;
	
	JCheckBox chckbxZugnamenKommentare;
	JCheckBox chckbxZeigeZeiten;
	
	ButtonGroup rdbtngrpZeigeRichtung;
	JRadioButton radioZeigeRichtungKeine;
	JRadioButton radioZeigeRichtungHin;
	JRadioButton radioZeigeRichtungZurueck;
	JRadioButton radioZeigeRichtungBeide;
	
	JTextArea ignorierteZuegeTextArea;
	JTextArea ignorierteTemplatesTextArea;
	
	// Farben
	JPanel panelBfpZeitenFarbeVorschau;
	JPanel panelBfpBetriebsstellenFarbeVorschau;
	JPanel panelBfpHintergrundFarbeVorschau;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			new BildfahrplanSettingsGUI(new BildfahrplanSettingsGUIController(new BildfahrplanConfig(), () -> {}), null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public BildfahrplanSettingsGUI(BildfahrplanSettingsGUIController controller, Window parent)
	{
		super(parent, "Einstellungen");
		NullTester.test(controller);
		this.controller = controller;
		this.config = controller.getConfig();
		controller.setSettingsGUI(this);
		
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				controller.close();
			}
		});
		
		setBounds(100, 100, 450, 525);
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
				panelAllgemein.setLayout(new FormLayout(new ColumnSpec[] {}, new RowSpec[] {}));
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
				panelBfp.setLayout(
						new FormLayout(new ColumnSpec[] {
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
				{
					JPanel panelZeiten = new JPanel();
					panelZeiten.setBorder(BorderFactory.createTitledBorder("Zeiten"));
					panelBfp.add(panelZeiten, "2, 2, fill, fill");
					panelZeiten.setLayout(new FormLayout(
							new ColumnSpec[] {FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("right:default"), FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
									ColumnSpec.decode("default:grow"), FormSpecs.RELATED_GAP_COLSPEC,},
							new RowSpec[] {FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.UNRELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
									FormSpecs.UNRELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,}));
					{
						JLabel lblHheProStunde = new JLabel("H\u00F6he pro Stunde");
						panelZeiten.add(lblHheProStunde, "2, 2");
					}
					{
						JPanel panelHoeheProStunde = new JPanel();
						panelZeiten.add(panelHoeheProStunde, "4, 2");
						panelHoeheProStunde.setLayout(new FormLayout(
								new ColumnSpec[] {com.jgoodies.forms.layout.FormSpecs.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
										com.jgoodies.forms.layout.FormSpecs.RELATED_GAP_COLSPEC, com.jgoodies.forms.layout.FormSpecs.DEFAULT_COLSPEC,},
								new RowSpec[] {com.jgoodies.forms.layout.FormSpecs.DEFAULT_ROWSPEC,}));
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
							sliderHoeheProStunde.addChangeListener((ChangeEvent arg0) -> {
								inputHoeheProStunde.setText(sliderHoeheProStunde.getValue() + "");
							});
							sliderHoeheProStunde.setSnapToTicks(true);
							sliderHoeheProStunde.setPaintTicks(true);
							sliderHoeheProStunde.setMinorTickSpacing(100);
							sliderHoeheProStunde.setMinimum(100);
							sliderHoeheProStunde.setMaximum(2000);
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
						panelZeit
								.setLayout(
										new FormLayout(
												new ColumnSpec[] {ColumnSpec.decode("default:grow"), FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
														FormSpecs.DEFAULT_COLSPEC, FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
														FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,},
												new RowSpec[] {FormSpecs.DEFAULT_ROWSPEC,}));
						{
							inputMinZeit = new JTextField();
							panelZeit.add(inputMinZeit, "1, 1");
							inputMinZeit.setColumns(10);
						}
						{
							JLabel lblZeitTrenner = new JLabel("-");
							panelZeit.add(lblZeitTrenner, "3, 1");
						}
						{
							inputMaxZeit = new JTextField();
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
							FormSpecs.RELATED_GAP_ROWSPEC,
							RowSpec.decode("default:grow"),
							FormSpecs.RELATED_GAP_ROWSPEC,}));
					{
						JLabel lblZeigeZugnamen = new JLabel("Zeige Zugnamen");
						panelDarstellung.add(lblZeigeZugnamen, "2, 2");
					}
					{
						rdbtngrpZeigeZugnamen = new ButtonGroup();
						
						JPanel panelZeigeZugnamen = new JPanel();
						panelDarstellung.add(panelZeigeZugnamen, "4, 2, left, fill");
						FlowLayout fl_panelZeigeZugnamen = (FlowLayout) panelZeigeZugnamen.getLayout();
						fl_panelZeigeZugnamen.setHgap(0);
						fl_panelZeigeZugnamen.setVgap(0);
						
						radioZeigeZugnamenNie = new JRadioButton("nie");
						rdbtngrpZeigeZugnamen.add(radioZeigeZugnamenNie);
						radioZeigeZugnamenNie.setActionCommand("nie");
						panelZeigeZugnamen.add(radioZeigeZugnamenNie);
						
						radioZeigeZugnamenAuto = new JRadioButton("automatisch");
						rdbtngrpZeigeZugnamen.add(radioZeigeZugnamenAuto);
						radioZeigeZugnamenAuto.setActionCommand("automatisch");
						panelZeigeZugnamen.add(radioZeigeZugnamenAuto);
						
						radioZeigeZugnamenImmer = new JRadioButton("immer");
						rdbtngrpZeigeZugnamen.add(radioZeigeZugnamenImmer);
						radioZeigeZugnamenImmer.setActionCommand("immer");
						panelZeigeZugnamen.add(radioZeigeZugnamenImmer);
					}
					{
						chckbxZugnamenKommentare = new JCheckBox("Zeige Kommentare in Zugnamen");
						panelDarstellung.add(chckbxZugnamenKommentare, "4, 4");
						chckbxZugnamenKommentare.setToolTipText("Kommentare sind alles ab dem ersten %-Zeichen");
					}
					{
						chckbxZeigeZeiten = new JCheckBox("Zeige Zeiten an Halten");
						panelDarstellung.add(chckbxZeigeZeiten, "4, 6");
					}
					{
						JLabel lblZeigeRichtung = new JLabel("Zeige Richtung");
						panelDarstellung.add(lblZeigeRichtung, "2, 8");
					}
					{
						rdbtngrpZeigeRichtung = new ButtonGroup();
						
						JPanel panelZeigeRichtung = new JPanel();
						panelDarstellung.add(panelZeigeRichtung, "4, 8, left, fill");
						FlowLayout flowLayout = (FlowLayout) panelZeigeRichtung.getLayout();
						flowLayout.setVgap(0);
						flowLayout.setHgap(0);
						{
							radioZeigeRichtungKeine = new JRadioButton("keine");
							rdbtngrpZeigeRichtung.add(radioZeigeRichtungKeine);
							radioZeigeRichtungKeine.setActionCommand("keine");
							panelZeigeRichtung.add(radioZeigeRichtungKeine);
						}
						{
							radioZeigeRichtungHin = new JRadioButton("hin");
							rdbtngrpZeigeRichtung.add(radioZeigeRichtungHin);
							radioZeigeRichtungHin.setActionCommand("hin");
							panelZeigeRichtung.add(radioZeigeRichtungHin);
						}
						{
							radioZeigeRichtungZurueck = new JRadioButton("zur\u00FCck");
							rdbtngrpZeigeRichtung.add(radioZeigeRichtungZurueck);
							radioZeigeRichtungZurueck.setActionCommand("zurueck");
							panelZeigeRichtung.add(radioZeigeRichtungZurueck);
						}
						{
							
							radioZeigeRichtungBeide = new JRadioButton("beide");
							rdbtngrpZeigeRichtung.add(radioZeigeRichtungBeide);
							radioZeigeRichtungBeide.setActionCommand("beide");
							panelZeigeRichtung.add(radioZeigeRichtungBeide);
						}
					}
				}
			}
			{
				JPanel panelIgnorieren = new JPanel();
				tabbedPane.addTab("Ignorieren", null, panelIgnorieren, null);
				panelIgnorieren.setLayout(new FormLayout(new ColumnSpec[] {
						FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_COLSPEC,},
					new RowSpec[] {
						FormSpecs.LINE_GAP_ROWSPEC,
						RowSpec.decode("default:grow"),
						FormSpecs.LINE_GAP_ROWSPEC,
						RowSpec.decode("default:grow"),
						FormSpecs.LINE_GAP_ROWSPEC,}));
				{
					JPanel panelIgnorierteZuege = new JPanel();
					panelIgnorierteZuege.setBorder(new TitledBorder(null, "Ignorierte Z\u00FCge", TitledBorder.LEADING, TitledBorder.TOP, null, null));
					panelIgnorieren.add(panelIgnorierteZuege, "2, 2, fill, fill");
					panelIgnorierteZuege.setLayout(new FormLayout(new ColumnSpec[] {
							FormSpecs.RELATED_GAP_COLSPEC,
							ColumnSpec.decode("default:grow"),
							FormSpecs.RELATED_GAP_COLSPEC,},
						new RowSpec[] {
							FormSpecs.LINE_GAP_ROWSPEC,
							RowSpec.decode("default:grow"),
							FormSpecs.LINE_GAP_ROWSPEC,}));
					{
						JScrollPane ignorierteZuegePane = new JScrollPane();
						panelIgnorierteZuege.add(ignorierteZuegePane, "2, 2, fill, fill");
						{
							ignorierteZuegeTextArea = new JTextArea();
							ignorierteZuegePane.setViewportView(ignorierteZuegeTextArea);
						}
					}
				}
				{
					JPanel panelIgnorierteTemplates = new JPanel();
					panelIgnorierteTemplates.setBorder(new TitledBorder(null, "Ignorierte Templates", TitledBorder.LEADING, TitledBorder.TOP, null, null));
					panelIgnorieren.add(panelIgnorierteTemplates, "2, 4, fill, fill");
					panelIgnorierteTemplates.setLayout(new FormLayout(new ColumnSpec[] {
							FormSpecs.RELATED_GAP_COLSPEC,
							ColumnSpec.decode("default:grow"),
							FormSpecs.RELATED_GAP_COLSPEC,},
						new RowSpec[] {
							FormSpecs.LINE_GAP_ROWSPEC,
							RowSpec.decode("default:grow"),
							FormSpecs.LINE_GAP_ROWSPEC,}));
					{
						JScrollPane ignorierteTemplatesPane = new JScrollPane();
						panelIgnorierteTemplates.add(ignorierteTemplatesPane, "2, 2, fill, fill");
						{
							ignorierteTemplatesTextArea = new JTextArea();
							ignorierteTemplatesPane.setViewportView(ignorierteTemplatesTextArea);
						}
					}
				}
			}
			{
				JPanel panelFarben = new JPanel();
				tabbedPane.addTab("Farben", null, panelFarben, null);
				tabbedPane.setEnabledAt(5, true);
				panelFarben.setLayout(
						new FormLayout(new ColumnSpec[] {FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormSpecs.RELATED_GAP_COLSPEC,},
								new RowSpec[] {FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
										FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,}));
				{
					JPanel panelFarbenBfp = new JPanel();
					panelFarbenBfp.setBorder(BorderFactory.createTitledBorder("Bildfahrplan"));
					panelFarben.add(panelFarbenBfp, "2, 2, fill, fill");
					panelFarbenBfp.setLayout(new FormLayout(
							new ColumnSpec[] {FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("right:default"), FormSpecs.UNRELATED_GAP_COLSPEC,
									ColumnSpec.decode("left:default:grow"), FormSpecs.RELATED_GAP_COLSPEC,},
							new RowSpec[] {FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
									FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,}));
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
								panelBfpZeitenFarbeVorschau.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
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
								panelBfpBetriebsstellenFarbeVorschau.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
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
							JLabel lblBfpHintergrundFarbe = new JLabel("Hintergrund");
							panelFarbenBfp.add(lblBfpHintergrundFarbe, "2, 6");
						}
						{
							JPanel panelBfpHintergrundFarbe = new JPanel();
							panelFarbenBfp.add(panelBfpHintergrundFarbe, "4, 6");
							{
								panelBfpHintergrundFarbeVorschau = new JPanel();
								panelBfpHintergrundFarbeVorschau.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
								FlowLayout flowLayout = (FlowLayout) panelBfpHintergrundFarbeVorschau.getLayout();
								flowLayout.setHgap(10);
								flowLayout.setVgap(10);
								panelBfpHintergrundFarbe.add(panelBfpHintergrundFarbeVorschau);
							}
							JButton btnBfpHintergrundFarbe = new JButton("w\u00E4hlen...");
							panelBfpHintergrundFarbe.add(btnBfpHintergrundFarbe);
							btnBfpHintergrundFarbe.setActionCommand("hintergrundFarbe");
							btnBfpHintergrundFarbe.addActionListener((ActionEvent arg0) -> controller.farbButton(arg0));
						}
					}
					{
						JPanel panelZugregeln = new JPanel();
						panelZugregeln.setBorder(BorderFactory.createTitledBorder("Zugregeln"));
						panelFarben.add(panelZugregeln, "2, 4, fill, fill");
						panelZugregeln.setLayout(new FormLayout(
								new ColumnSpec[] {FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormSpecs.RELATED_GAP_COLSPEC,},
								new RowSpec[] {FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,}));
						{
							JButton btnZugregeln = new JButton("Zugformatierungsregeln...");
							btnZugregeln.setActionCommand("format");
							btnZugregeln.addActionListener((ActionEvent arg0) -> controller.actionButton(arg0));
							panelZugregeln.add(btnZugregeln, "2, 2, left, top");
						}
					}
				}
			}
			
			// TODO entfernen, wenn Tab Allgemein vorhanden ist
			tabbedPane.setSelectedIndex(3);
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				buttonPane.setLayout(new FormLayout(
						new ColumnSpec[] {FormSpecs.UNRELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
								FormSpecs.DEFAULT_COLSPEC, ColumnSpec.decode("3dlu:grow"), FormSpecs.DEFAULT_COLSPEC, FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
								FormSpecs.DEFAULT_COLSPEC, FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.UNRELATED_GAP_COLSPEC,},
						new RowSpec[] {FormSpecs.LINE_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.UNRELATED_GAP_ROWSPEC,}));
				
				boolean saveEnabled = controller.speichertest();
				
				{
					JButton speichernButton = new JButton("Speichern");
					speichernButton.setEnabled(saveEnabled);
					speichernButton.setActionCommand("save");
					speichernButton.addActionListener((ActionEvent arg0) -> controller.actionButton(arg0));
					buttonPane.add(speichernButton, "2, 2, left, top");
				}
				{
					JButton ladenButton = new JButton("Laden");
					ladenButton.setEnabled(saveEnabled);
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
		controller.ladeDaten();
		setVisible(true);
	}
	
	public int getHoeheProStunde()
	{
		try
		{
			return controller.parseIntField("H�he pro Stunde", inputHoeheProStunde.getText());
		}
		catch(NumberFormatException e)
		{
			log.error("NumberFormatException bei hoeheProStunde: {}", e.getMessage());
			errorMessage("H�he pro Stunde: Nur positive ganze Zahlen erlaubt.");
			throw e;
		}
	}
	
	public boolean getAutoSizeSelected()
	{
		return chckbxAuto.isSelected();
	}
	
	public double getMinZeit() throws NumberFormatException
	{
		try
		{
			return TimeFormater.stringToDouble(inputMinZeit.getText());
		}
		catch(NumberFormatException e)
		{
			log.error("NumberFormatException bei minZeit: {}", e.getMessage());
			errorMessage("Dargestellte Zeit: Nur Zeiten im Format hh:mm (z.B. 12:01) erlaubt.");
			throw e;
		}
	}
	
	public double getMaxZeit() throws NumberFormatException
	{
		try
		{
			return TimeFormater.stringToDouble(inputMaxZeit.getText());
		}
		catch(NumberFormatException e)
		{
			log.error("NumberFormatException bei maxZeit: {}", e.getMessage());
			errorMessage("Dargestellte Zeit: Nur Zeiten im Format hh:mm (z.B. 12:01) erlaubt.");
			throw e;
		}
	}
	
	public void setZeigeZugnamen(int zeigeZugnamen)
	{
		switch(zeigeZugnamen)
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
				log.error("ZeigeZugnamen: Ung\u00FCltiger Wert {}", config.getZeigeZugnamen());
		}
	}
	
	public void setRichtung(int richtung)
	{
		switch(config.getZeigeRichtung())
		{
			case 0:
				rdbtngrpZeigeRichtung.setSelected(radioZeigeRichtungKeine.getModel(), true);
				break;
			case 1:
				rdbtngrpZeigeRichtung.setSelected(radioZeigeRichtungHin.getModel(), true);
				break;
			case 2:
				rdbtngrpZeigeRichtung.setSelected(radioZeigeRichtungZurueck.getModel(), true);
				break;
			case 3:
				rdbtngrpZeigeRichtung.setSelected(radioZeigeRichtungBeide.getModel(), true);
				break;
			default:
				log.error("ZeigeRichtung: Ung\u00FCltiger Wert {}", config.getZeigeRichtung());
		}
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
