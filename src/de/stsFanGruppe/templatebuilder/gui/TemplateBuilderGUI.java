package de.stsFanGruppe.templatebuilder.gui;

import javax.swing.*;
import javax.swing.tree.*;
import de.stsFanGruppe.templatebuilder.gui.bildfahrplan.BildfahrplanGUIController;
import de.stsFanGruppe.templatebuilder.gui.bildfahrplan.BildfahrplanSpaltenheaderGUI;
import de.stsFanGruppe.templatebuilder.gui.bildfahrplan.BildfahrplanZeilenheaderGUI;
import de.stsFanGruppe.templatebuilder.gui.bildfahrplan.BildfahrplanGUI;
import de.stsFanGruppe.tools.NullTester;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.BorderLayout;

public class TemplateBuilderGUI implements GUI
{
	protected TemplateBuilderGUIController controller;
	BildfahrplanGUI bildfahrplanZeichner;
	private boolean initialized = false;
	
	private JFrame frmTemplatebauer;

	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */
	public TemplateBuilderGUI(TemplateBuilderGUIController controller)
	{
		NullTester.test(controller);
		this.controller = controller;
		controller.setGUI(this);
		initialize();
	}
	
	public BildfahrplanGUI getBildfahrplanZeichner()
	{
		NullTester.test(bildfahrplanZeichner);
		return bildfahrplanZeichner;
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		assert !initialized;
		assert controller != null;
		
		frmTemplatebauer = new JFrame();
		frmTemplatebauer.setTitle("TemplateBauer");
		frmTemplatebauer.setMinimumSize(new Dimension(450, 300));
		frmTemplatebauer.setBounds(100, 100, 1000, 600);
		// Fenster maximieren
		frmTemplatebauer.setExtendedState(frmTemplatebauer.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frmTemplatebauer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmTemplatebauer.setJMenuBar(menuBar);
		
		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);
		
		JMenuItem mntmNeu = new JMenuItem("Neu...");
		mntmNeu.setEnabled(false);
		mntmNeu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnDatei.add(mntmNeu);
		
		JMenuItem mntmffnen = new JMenuItem("\u00D6ffnen...");
		mntmffnen.setEnabled(false);
		mntmffnen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnDatei.add(mntmffnen);
		
		JSeparator separator_2 = new JSeparator();
		mnDatei.add(separator_2);
		
		JMenuItem mntmSpeichern = new JMenuItem("Speichern");
		mntmSpeichern.setEnabled(false);
		mntmSpeichern.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnDatei.add(mntmSpeichern);
		
		JMenuItem mntmSpeichernUnter = new JMenuItem("Speichern unter...");
		mntmSpeichernUnter.setEnabled(false);
		mntmSpeichernUnter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnDatei.add(mntmSpeichernUnter);
		
		JSeparator separator = new JSeparator();
		mnDatei.add(separator);
		
		JMenu mnImportexport = new JMenu("Import/Export");
		mnDatei.add(mnImportexport);
		
		JMenuItem mntmImportAusJtraingraph = new JMenuItem("Import aus JTrainGraph");
		mntmImportAusJtraingraph.setActionCommand("importFromJTG");
		mntmImportAusJtraingraph.addActionListener((ActionEvent arg0) -> controller.menuAction(arg0));
		mnImportexport.add(mntmImportAusJtraingraph);
		
		JMenuItem mntmExportNachJtraingraph = new JMenuItem("Export nach JTrainGraph");
		mntmExportNachJtraingraph.setActionCommand("exportToJTG");
		mntmExportNachJtraingraph.addActionListener((ActionEvent arg0) -> controller.menuAction(arg0));
		mnImportexport.add(mntmExportNachJtraingraph);
		
		JSeparator separator_1 = new JSeparator();
		mnDatei.add(separator_1);
		
		JMenuItem mntmSchliessen = new JMenuItem("Schlie\u00DFen");
		mntmSchliessen.setEnabled(false);
		mntmSchliessen.setActionCommand("close");
		mntmSchliessen.addActionListener((ActionEvent arg0) -> controller.menuAction(arg0));
		mntmSchliessen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mnDatei.add(mntmSchliessen);
		
		JMenu mnBearbeiten = new JMenu("Bearbeiten");
		mnBearbeiten.setEnabled(false);
		menuBar.add(mnBearbeiten);
		
		JMenu mnStrecken = new JMenu("Strecken");
		mnStrecken.setEnabled(false);
		menuBar.add(mnStrecken);
		
		JMenu mnZge = new JMenu("Z\u00FCge");
		mnZge.setEnabled(false);
		menuBar.add(mnZge);
		
		JMenu mnAnsicht = new JMenu("Ansicht");
		menuBar.add(mnAnsicht);
		
		JCheckBoxMenuItem mntmBildfahrplan = new JCheckBoxMenuItem("Bildfahrplan");
		mntmBildfahrplan.setSelected(true);
		mntmBildfahrplan.setActionCommand("bfpOptions");
		mnAnsicht.add(mntmBildfahrplan);
		
		JMenu mnEinstellungen = new JMenu("Einstellungen");
		menuBar.add(mnEinstellungen);
		
		JMenuItem mntmOptionen = new JMenuItem("Optionen");
		mntmOptionen.setActionCommand("options");
		mntmOptionen.addActionListener((ActionEvent arg0) -> controller.menuAction(arg0));
		mnEinstellungen.add(mntmOptionen);
		
		JMenu mnHilfe = new JMenu("?");
		menuBar.add(mnHilfe);
		
		JMenuItem mntmHilfe = new JMenuItem("Hilfe");
		mntmHilfe.setEnabled(false);
		mntmHilfe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mnHilfe.add(mntmHilfe);
		
		JMenuItem mntmUeber = new JMenuItem("\u00DCber...");
		mntmUeber.setActionCommand("about");
		mntmUeber.addActionListener((ActionEvent arg0) -> controller.menuAction(arg0));
		mnHilfe.add(mntmUeber);
		
		JSplitPane splitPane = new JSplitPane();
		frmTemplatebauer.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JTree tree = new JTree();
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Strecken") {
				{
					DefaultMutableTreeNode node_1;
					node_1 = new DefaultMutableTreeNode("Heerlen - D\u00FCren");
						node_1.add(new DefaultMutableTreeNode("RB 20"));
					add(node_1);
				}
			}
		));
		splitPane.setLeftComponent(tree);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		controller.setTabbedPane(tabbedPane);
		splitPane.setRightComponent(tabbedPane);
		
		JScrollPane scrollPane = new JScrollPane();
		tabbedPane.addTab("New tab", null, scrollPane, null);
		
		BildfahrplanGUIController bfpController = new BildfahrplanGUIController(controller.getConfig());
		bildfahrplanZeichner = new BildfahrplanGUI(bfpController, this);
		controller.setBildfahrplanController(bfpController);
		
		scrollPane.setViewportView(bildfahrplanZeichner);
		scrollPane.setColumnHeaderView(new BildfahrplanSpaltenheaderGUI(bildfahrplanZeichner, bfpController));
		scrollPane.setRowHeaderView(new BildfahrplanZeilenheaderGUI(bildfahrplanZeichner, bfpController));
		bildfahrplanZeichner.setLayout(null);
		
		JToolBar toolBar = new JToolBar();
		frmTemplatebauer.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton button = new JButton("");
		toolBar.add(button);
		
		initialized = true;
	}
	
	public void setVisible(boolean arg0)
	{
		this.frmTemplatebauer.setVisible(arg0);
	}

	public void errorMessage(String text, String titel)
	{
		JOptionPane.showMessageDialog(frmTemplatebauer, text, titel, JOptionPane.ERROR_MESSAGE);
	}
	public void warningMessage(String text, String titel)
	{
		JOptionPane.showMessageDialog(frmTemplatebauer, text, titel, JOptionPane.WARNING_MESSAGE);
	}
	public void infoMessage(String text, String titel)
	{
		JOptionPane.showMessageDialog(frmTemplatebauer, text, titel, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private static void log(String text)
	{
		System.out.println("TemplateBuilderGUI: "+text);
	}
}
