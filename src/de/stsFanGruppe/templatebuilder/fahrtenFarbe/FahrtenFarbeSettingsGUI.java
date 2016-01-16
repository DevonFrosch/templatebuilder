package de.stsFanGruppe.templatebuilder.fahrtenFarbe;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import de.stsFanGruppe.templatebuilder.config.*;
import de.stsFanGruppe.templatebuilder.fahrtenFarbe.FahrtenFarbeConfig.LineType;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.TableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import de.stsFanGruppe.tools.TableModel.*;

public class FahrtenFarbeSettingsGUI extends JDialog implements GUI
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsGUI.class);
	
	FahrtenFarbeSettingsGUIController controller;
	FahrtenFarbeConfig config;
	TableModel ownTableModel;
	
	boolean saveEnabled = false;
	final JPanel contentPanel = new JPanel();
	JLabel lblDescription;
	JTable table;
	
	JPanel panelStandardFarbeVorschau;
	JTextField txtStandardLinienStaerke;
	JComboBox comboBoxLinienArt;
	ArrayList<Color> testFarben = TableModel.testFarben;
	
	public final String[] columnName = {"Zugname", "Linienfarbe", "Linienstärke", "Linienart"};
	private JTextField textField;
	
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
						.setText("<html>Für eine bessere Darstellung können Linien hier nach festgelegten Kriterien hervorgehoben werden.</html>");
			}
			{
				// TODO Nach Testphase entfernen
				Object[][] data = {{"Kathy", "", new Integer(5), comboBoxLinienArt}, {"John", "", new Integer(5), comboBoxLinienArt},
						{"Sue", "", new Integer(5), comboBoxLinienArt}, {"Jane", "", new Integer(5), comboBoxLinienArt},
						{"Joe", "", new Integer(5), comboBoxLinienArt}};
						
				DefaultTableModel tableModel = new DefaultTableModel();
				tableModel.setDataVector(data, columnName);
				
				table = new JTable(tableModel);
				table.getTableHeader().setReorderingAllowed(false);
				// Einstellung für die 2. Spalte (Farbe):
				table.getColumnModel().getColumn(1).setCellRenderer(new BackgroundTableCellRenderer());
				// Einstellung des Editors für die letzte Spalte (Linienart):
				table.getColumnModel().getColumn(3).setCellEditor(new LinienArtCellEditor());
				table.addMouseListener(new CellMouseClickForBackgroundColor());			
				
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
			contentPanel.add(standardConfigLabel, "2, 6, 3, 1, fill, fill");
			FormLayout fl_standardConfigLabel = new FormLayout(
					new ColumnSpec[] {ColumnSpec.decode("default:grow"), FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("default:grow"),},
					new RowSpec[] {RowSpec.decode("default:grow"), FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
							FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,});
			standardConfigLabel.setLayout(fl_standardConfigLabel);
			/*
			 * Standardlinienfarbe
			 */
			{
				// Label für Standardlinienfarbe für die Beschriftung
				{
					JLabel lblDefaultFarbeString = new JLabel();
					lblDefaultFarbeString.setText("Linienfarbe");
					standardConfigLabel.add(lblDefaultFarbeString, "1,1");
				}
				// Panel für Standardlinienfarbe
				{
					JPanel panelStandardLinienFarbe = new JPanel();
					FlowLayout flowLayout = (FlowLayout) panelStandardLinienFarbe.getLayout();
					flowLayout.setAlignment(FlowLayout.RIGHT);
					standardConfigLabel.add(panelStandardLinienFarbe, "3, 1, right, default");
					{
						// FlowLayout & Button werden im Panel hinzugefügt
						{
							panelStandardFarbeVorschau = new JPanel();
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
			 * Standardlinienstärke
			 */
			{
				{
					JLabel lblDefaultLinienStaerkeString = new JLabel();
					lblDefaultLinienStaerkeString.setText("Linienstärke");
					standardConfigLabel.add(lblDefaultLinienStaerkeString, "1,3");
				}
				{
					txtStandardLinienStaerke = new JTextField();
					standardConfigLabel.add(txtStandardLinienStaerke, "3, 3, right, default");
					// FIXME Textfeld darf nur zahlen zurückgeben
					txtStandardLinienStaerke.setColumns(1);
				}
			}
			/*
			 * Standardlinienart
			 */
			{
				{
					JLabel lblStandardLinienart = new JLabel();
					lblStandardLinienart.setText("Linienart");
					standardConfigLabel.add(lblStandardLinienart, "1, 5, left, default");
				}
				{
					// FIXME Nach Auswahl eines Eintrags werden alle Linien
					// ausgewählt.
					standardConfigLabel.add(getComboBoxLinienArt(), "3, 5, fill, default");
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
		}
		loadSettings();
		setVisible(true);
	}
	
	public JComboBox getComboBoxLinienArt()
	{
		comboBoxLinienArt = new JComboBox(LineType.values());
		comboBoxLinienArt.setRenderer(new LineRenderer());
		comboBoxLinienArt.setEditable(false);
		comboBoxLinienArt.setSelectedItem(0);
		
		return comboBoxLinienArt;
	}
	
	public void loadSettings()
	{
		log.info("Standardwerte für FahrtenFarbe einlesen");
		panelStandardFarbeVorschau.setBackground(config.getDefaultLinienFarbe());
		txtStandardLinienStaerke.setText(config.getDefaultLinienStaerkeToString());
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

	public ArrayList<Color> getTestFarben()
	{
		return testFarben;
	}
}
