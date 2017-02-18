package de.stsFanGruppe.templatebuilder.config.fahrtenfarbe;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.*;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUI;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.tools.NullTester;
import javax.swing.border.BevelBorder;

public class FahrtenFarbeSettingsGUI extends JDialog implements GUI
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsGUI.class);
	
	
	
	FahrtenFarbeSettingsGUIController controller;
	FahrtenFarbeConfig config;
	FahrtenFarbeGUITableModel ownTableModel;
	
	final JPanel contentPanel = new JPanel();
	JLabel lblDescription;
	JTable table;
	
	JPanel panelStandardFarbeVorschau;
	JTextField txtStandardLinienStaerke;
	private JComboBox<LineType> comboBoxLinienTyp;
	
	private JTextField textField;
	
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
		super(parent, "Zugdarstellungsregeln");
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
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.RELATED_GAP_ROWSPEC,
					RowSpec.decode("default:grow"),
					FormSpecs.UNRELATED_GAP_ROWSPEC,
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.RELATED_GAP_ROWSPEC,});
			contentPanel.setLayout(fl_panel);
			{
				JLabel lblDiscription = new JLabel();
				contentPanel.add(lblDiscription, "2, 2, 3, 1");
				lblDiscription
						.setText("<html>F�r eine bessere Darstellung k�nnen Linien hier nach festgelegten Kriterien hervorgehoben werden.</html>");
			}
			{
				table = new JTable(controller.getTableModel());
				table.getTableHeader().setReorderingAllowed(false);
				
				// Einstellung f�r die 2. Spalte (Farbe):
				table.getColumnModel().getColumn(1).setCellRenderer(new FahrtenFarbeGUITableModel.BackgroundTableCellRenderer());
				
				// Einstellung des Editors f�r die letzte Spalte (Linientyp):
				// TODO: Zellen lassen sich nicht mehr editieren
				table.getColumnModel().getColumn(3).setCellRenderer(new LineRenderer());
				table.getColumnModel().getColumn(3).setCellEditor(new FahrtenFarbeGUITableModel.LineTypeCellEditor());
				
				table.addMouseListener(controller.getMouseListener());
				
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
		/*
		 * Standardeinstellung
		 */
		{
			JPanel standardConfigLabel = new JPanel();
			standardConfigLabel.setBorder(BorderFactory.createTitledBorder("Standardeinstellung"));
			contentPanel.add(standardConfigLabel, "2, 6, 3, 1, left, fill");
			FormLayout fl_standardConfigLabel = new FormLayout(new ColumnSpec[] {
					FormSpecs.RELATED_GAP_COLSPEC,
					FormSpecs.DEFAULT_COLSPEC,
					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
					ColumnSpec.decode("default:grow"),
					FormSpecs.RELATED_GAP_COLSPEC,},
				new RowSpec[] {
					FormSpecs.RELATED_GAP_ROWSPEC,
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.RELATED_GAP_ROWSPEC,
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.RELATED_GAP_ROWSPEC,
					RowSpec.decode("max(15dlu;default)"),
					RowSpec.decode("4dlu:grow"),});
			standardConfigLabel.setLayout(fl_standardConfigLabel);
			/*
			 * Standardlinienfarbe
			 */
			{
				// Label f�r Standardlinienfarbe f�r die Beschriftung
				{
					JLabel lblDefaultFarbeString = new JLabel();
					lblDefaultFarbeString.setText("Linienfarbe");
					standardConfigLabel.add(lblDefaultFarbeString, "2, 2, right, default");
				}
				// Panel f�r Standardlinienfarbe
				{
					JPanel panelStandardLinienFarbe = new JPanel();
					FlowLayout flowLayout = (FlowLayout) panelStandardLinienFarbe.getLayout();
					flowLayout.setAlignment(FlowLayout.RIGHT);
					standardConfigLabel.add(panelStandardLinienFarbe, "4, 2, left, default");
					{
						// FlowLayout & Button werden im Panel hinzugef�gt
						{
							panelStandardFarbeVorschau = new JPanel();
							panelStandardFarbeVorschau.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
							FlowLayout fl_panelStandardFarbeVorschau = (FlowLayout) panelStandardFarbeVorschau.getLayout();
							fl_panelStandardFarbeVorschau.setVgap(10);
							fl_panelStandardFarbeVorschau.setHgap(10);
							panelStandardLinienFarbe.add(panelStandardFarbeVorschau);
						}
						{
							JButton btnStandardFarbe = new JButton("w\u00E4hlen...");
							panelStandardLinienFarbe.add(btnStandardFarbe);
							btnStandardFarbe.setActionCommand("zeitenFarbe");
							btnStandardFarbe.addActionListener((ActionEvent arg0) -> controller.farbButton(arg0));
						}
					}
				}
			}
			/*
			 * Standardlinienst�rke
			 */
			{
				{
					JLabel lblDefaultLinienStaerkeString = new JLabel();
					lblDefaultLinienStaerkeString.setText("Linienst�rke");
					standardConfigLabel.add(lblDefaultLinienStaerkeString, "2, 4, right, default");
				}
				{
					txtStandardLinienStaerke = new JTextField();
					standardConfigLabel.add(txtStandardLinienStaerke, "4, 4, fill, default");
				}
			}
			/*
			 * StandardlinienTyp
			 */
			{
				{
					JLabel lblStandardLinientyp = new JLabel();
					lblStandardLinientyp.setText("Linientyp");
					standardConfigLabel.add(lblStandardLinientyp, "2, 6, right, default");
				}
				{
					comboBoxLinienTyp = new JLineTypeComboBox();
					standardConfigLabel.add(comboBoxLinienTyp, "4, 6, fill, fill");
				}
			}
		}
		/*
		 * Buttonsfl�che
		 */
		{
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
		}
		loadSettings();
		setVisible(true);
	}
	
	public Color getDefaultLineColor()
	{
		return panelStandardFarbeVorschau.getBackground();
	}
	public String getDefaultLineWidth()
	{
		return txtStandardLinienStaerke.getText();
	}
	public LineType getDefaultLineType()
	{
		int typIndex = comboBoxLinienTyp.getSelectedIndex();
		return LineType.values()[typIndex];
	}
	
	public void loadSettings()
	{
		log.info("Standardwerte f�r FahrtenFarbe einlesen");
		panelStandardFarbeVorschau.setBackground(config.getStandardLinienFarbe());
		txtStandardLinienStaerke.setText(config.getStandardLinienSt�rke()+"");
		comboBoxLinienTyp.setSelectedItem(config.getStandardLinienTyp());
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
