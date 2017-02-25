package de.stsFanGruppe.templatebuilder.editor.streckenEditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import de.stsFanGruppe.templatebuilder.editor.EditorDaten;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.tools.NullTester;
import com.jgoodies.forms.layout.FormSpecs;

public class StreckenEditorGUI extends JDialog implements GUI
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StreckenEditorGUI.class);
	
	protected EditorDaten editorDaten;
	protected StreckenEditorGUIController controller;
	boolean saveEnabled = false;

	protected final JPanel contentPanel = new JPanel();
	public JTable table;
	public static final String[] columnNames = {"km", "Bestriebsstelle"};
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			StreckenEditorGUI dialog = new StreckenEditorGUI(
					new StreckenEditorGUIController(new EditorDaten(), () -> {}), null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public StreckenEditorGUI(StreckenEditorGUIController controller, Window parent)
	{
		super(parent);
		NullTester.test(controller);
		this.controller = controller;
		controller.setSettingsGUI(this);
		
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				controller.close();
			}
		});
		setBounds(0, 0, 500, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			FormLayout fl_panel = new FormLayout(
					new ColumnSpec[] {FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
							FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,},
					new RowSpec[] {FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
							FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.UNRELATED_GAP_ROWSPEC,
							RowSpec.decode("default:grow"), FormSpecs.RELATED_GAP_ROWSPEC,});
			contentPanel.setLayout(fl_panel);
			{
				JLabel lblDiscription = new JLabel();
				contentPanel.add(lblDiscription, "2, 2, 3, 1");
				lblDiscription.setText("<html>Hier kannst du die Strecke bearbeiten.</html>");
			}
			{
				table = new JTable();
				table.getTableHeader().setReorderingAllowed(false);
				
				JScrollPane scrollPane = new JScrollPane(table);
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				contentPanel.add(scrollPane, "2, 4");
			}
			{
				JPanel buttonTablePane = new JPanel();
				contentPanel.add(buttonTablePane, "4, 4, default, top");
				buttonTablePane.setLayout(new FormLayout(new ColumnSpec[] {FormSpecs.DEFAULT_COLSPEC,},
						new RowSpec[] {FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,}));
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
			{
				JPanel buttonPane = new JPanel();
				contentPanel.add(buttonPane, "2, 6, 3, 1, right, center");
				{
					JButton btnSaveRoute = new JButton("Speichern");
					btnSaveRoute.setActionCommand("ok");
					btnSaveRoute.addActionListener((ActionEvent arg0) -> controller.actionButton(arg0));
					buttonPane.add(btnSaveRoute, "1,1");
				}
				{
					JButton btnSaveRoute = new JButton("Abbrechen");
					btnSaveRoute.setActionCommand("cancel");
					btnSaveRoute.addActionListener((ActionEvent arg0) -> controller.actionButton(arg0));
					buttonPane.add(btnSaveRoute, "3,1");
				}
			}
		}
		controller.ladeStrecken();
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
