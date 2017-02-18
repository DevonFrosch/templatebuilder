package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.*;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUI;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.linetype.JLineTypeComboBox;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.linetype.LineTypeRenderer;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.linetype.LineType;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.table.BackgroundTableCellRenderer;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.table.FahrtDarstellungTable;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.table.LineTypeCellEditor;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.tools.NullTester;
import javax.swing.border.BevelBorder;

public class FahrtDarstellungSettingsGUI extends JDialog implements GUI
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsGUI.class);
	
	FahrtDarstellungSettingsGUIController controller;
	FahrtDarstellungConfig config;
	public static final String[] UEBERSCHRIFTEN = {"Zugname", "Farbe", "Breite [px]", "Typ"};
	
	final JPanel contentPanel = new JPanel();
	FahrtDarstellungTable table;
	
	private JPanel vorschauStandardFarbe;
	private JTextField inputStandardBreite;
	private JComboBox<LineType> inputStandardLineType;
	
	public static void main(String[] args)
	{
		try
		{
			FahrtDarstellungSettingsGUI dialog = new FahrtDarstellungSettingsGUI(new FahrtDarstellungSettingsGUIController(new FahrtDarstellungConfig(), () -> {}), null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public FahrtDarstellungSettingsGUI(FahrtDarstellungSettingsGUIController controller, Window parent)
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
				contentPanel.add(lblDiscription, "2, 2, 3, 1, fill, fill");
				lblDiscription
						.setText("<html>Für eine bessere Darstellung können Linien hier nach festgelegten Kriterien hervorgehoben werden.</html>");
			}
			{
				table = new FahrtDarstellungTable(UEBERSCHRIFTEN, 0);
				table.setFillsViewportHeight(true);
				table.getTableHeader().setReorderingAllowed(false);
				
				// Einstellung für die 2. Spalte (Farbe):
				table.getColumnModel().getColumn(1).setCellRenderer(new BackgroundTableCellRenderer());
				
				// Einstellung für die 4. Spalte (Linientyp):
				table.getColumnModel().getColumn(3).setCellRenderer(new LineTypeRenderer());
				table.getColumnModel().getColumn(3).setCellEditor(new LineTypeCellEditor());
				
				table.addMouseListener(controller.getMouseListener());
				
				JScrollPane scrollPane = new JScrollPane(table);
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				contentPanel.add(scrollPane, "2, 4, fill, fill");
			}
			{
				JPanel buttonTablePane = new JPanel();
				contentPanel.add(buttonTablePane, "4, 4, fill, top");
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
				// Label für Standardlinienfarbe für die Beschriftung
				{
					JLabel lblDefaultFarbeString = new JLabel();
					lblDefaultFarbeString.setText("Linienfarbe");
					standardConfigLabel.add(lblDefaultFarbeString, "2, 2, right, default");
				}
				// Panel für Standardlinienfarbe
				{
					JPanel panelStandardLinienFarbe = new JPanel();
					FlowLayout flowLayout = (FlowLayout) panelStandardLinienFarbe.getLayout();
					flowLayout.setAlignment(FlowLayout.RIGHT);
					standardConfigLabel.add(panelStandardLinienFarbe, "4, 2, left, fill");
					{
						// FlowLayout & Button werden im Panel hinzugefügt
						{
							vorschauStandardFarbe = new JPanel();
							vorschauStandardFarbe.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
							FlowLayout fl_panelStandardFarbeVorschau = (FlowLayout) vorschauStandardFarbe.getLayout();
							fl_panelStandardFarbeVorschau.setVgap(10);
							fl_panelStandardFarbeVorschau.setHgap(10);
							panelStandardLinienFarbe.add(vorschauStandardFarbe);
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
			 * Standardlinienstärke
			 */
			{
				{
					JLabel lblDefaultLinienStaerkeString = new JLabel();
					lblDefaultLinienStaerkeString.setText("Linienstärke");
					standardConfigLabel.add(lblDefaultLinienStaerkeString, "2, 4, right, default");
				}
				{
					inputStandardBreite = new JTextField();
					standardConfigLabel.add(inputStandardBreite, "4, 4, fill, default");
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
					inputStandardLineType = new JLineTypeComboBox();
					standardConfigLabel.add(inputStandardLineType, "4, 6, fill, fill");
				}
			}
		}
		/*
		 * Buttonsfläche
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
	
	public Color getStandardFarbe()
	{
		return vorschauStandardFarbe.getBackground();
	}
	public void setStandardFarbe(Color farbe)
	{
		vorschauStandardFarbe.setBackground(farbe);
	}
	public String getStandardBreite()
	{
		return inputStandardBreite.getText();
	}
	public int getStandardBreiteInt()
	{
		try
		{
			return Integer.parseInt(getStandardBreite());
		}
		catch(NumberFormatException e)
		{
			return FahrtDarstellungConfig.DEFAULT_STANDARD_LINIEN_STAERKE;
		}
	}
	public void setStandardBreite(String breite)
	{
		inputStandardBreite.setText(breite);
	}
	public LineType getStandardLineType()
	{
		int typIndex = inputStandardLineType.getSelectedIndex();
		return LineType.values()[typIndex];
	}
	public void setStandardLineType(LineType lineType)
	{
		inputStandardLineType.setSelectedItem(lineType);
	}
	
	public void loadSettings()
	{
		log.info("Standardwerte für FahrtenFarbe einlesen");
		vorschauStandardFarbe.setBackground(config.getStandardLinienFarbe());
		inputStandardBreite.setText(config.getStandardLinienStärke()+"");
		inputStandardLineType.setSelectedItem(config.getStandardLinienTyp());
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
