package de.stsFanGruppe.templatebuilder.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import de.stsFanGruppe.templatebuilder.editor.bildfahrplan.BildfahrplanGUI;
import de.stsFanGruppe.tools.NullTester;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.BorderLayout;

public class TemplateBuilderGUI implements GUI
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TemplateBuilderGUI.class);
	
	public static final int ANSICHT_BILDFAHRPLAN = 0;
	public static final int ANSICHT_TABELLARISCH = 1;
	
	protected TemplateBuilderGUIController controller;
	protected BildfahrplanGUI bildfahrplanZeichner;
	private boolean initialized = false;
	
	protected JCheckBoxMenuItem mntmBildfahrplan;
	protected JCheckBoxMenuItem mntmTabellarischHin;
	protected JCheckBoxMenuItem mntmTabellarischRück;
	
	protected JFrame frmTemplatebauer;
	protected TemplateBuilderTabbedPane tabs;

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
		assert controller != null;
		
		if(initialized)
		{
			throw new IllegalStateException("GUI bereits initialisiert!");
		}
		initialized = true;
		
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
		mntmNeu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mntmNeu.setActionCommand("neu");
		mntmNeu.addActionListener((ActionEvent arg0) -> controller.menuAction(arg0));
		mnDatei.add(mntmNeu);
		
		JMenuItem mntmffnen = new JMenuItem("\u00D6ffnen...");
		mntmffnen.setEnabled(false);
		mntmffnen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmffnen.setActionCommand("öffnen");
		mntmffnen.addActionListener((ActionEvent arg0) -> controller.menuAction(arg0));
		mnDatei.add(mntmffnen);
		
		JSeparator separator_2 = new JSeparator();
		mnDatei.add(separator_2);
		
		JMenuItem mntmSpeichern = new JMenuItem("Speichern");
		mntmSpeichern.setEnabled(false);
		mntmSpeichern.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmSpeichern.setActionCommand("speichern");
		mntmSpeichern.addActionListener((ActionEvent arg0) -> controller.menuAction(arg0));
		mnDatei.add(mntmSpeichern);
		
		JMenuItem mntmSpeichernUnter = new JMenuItem("Speichern unter...");
		mntmSpeichernUnter.setEnabled(false);
		mntmSpeichernUnter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmSpeichernUnter.setActionCommand("speichernUnter");
		mntmSpeichernUnter.addActionListener((ActionEvent arg0) -> controller.menuAction(arg0));
		mnDatei.add(mntmSpeichernUnter);
		
		JSeparator separator = new JSeparator();
		mnDatei.add(separator);
		
		JMenu mnImport = new JMenu("Import");
		mnDatei.add(mnImport);
		
		JMenuItem mntmImportAusJtraingraph = new JMenuItem("Import aus JTrainGraph");
		mntmImportAusJtraingraph.setActionCommand("importFromJTG");
		mntmImportAusJtraingraph.addActionListener((ActionEvent arg0) -> controller.menuAction(arg0));
		mnImport.add(mntmImportAusJtraingraph);
		
		JMenuItem mntmRegelimportAusJtraingraph = new JMenuItem("Regelimport aus JTrainGraph");
		mntmRegelimportAusJtraingraph.setActionCommand("importRulesFromJTG");
		mntmRegelimportAusJtraingraph.addActionListener((ActionEvent arg0) -> controller.menuAction(arg0));
		mnImport.add(mntmRegelimportAusJtraingraph);
		
		JMenu mnExport = new JMenu("Export");
		mnDatei.add(mnExport);
		
		JMenuItem mntmExportNachJtraingraph = new JMenuItem("Export nach JTrainGraph");
		mntmExportNachJtraingraph.setActionCommand("exportToJTG");
		mntmExportNachJtraingraph.addActionListener((ActionEvent arg0) -> controller.menuAction(arg0));
		mnExport.add(mntmExportNachJtraingraph);
		
		JSeparator separator_1 = new JSeparator();
		mnDatei.add(separator_1);
		
		JMenuItem mntmSchliessen = new JMenuItem("Schlie\u00DFen");
		mntmSchliessen.setActionCommand("exit");
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
		
		mntmBildfahrplan = new JCheckBoxMenuItem("Bildfahrplan");
		mntmBildfahrplan.setActionCommand("zeigeBildfahrplan");
		mntmBildfahrplan.addActionListener((ActionEvent arg0) -> controller.menuAction(arg0));
		mntmBildfahrplan.addActionListener((ActionEvent arg0) -> updateAnsichtAuswahl());
		mnAnsicht.add(mntmBildfahrplan);
		
		mntmTabellarischHin = new JCheckBoxMenuItem("Tabellarisch (hin)");
		mntmTabellarischHin.setActionCommand("zeigeTabEditorHin");
		mntmTabellarischHin.addActionListener((ActionEvent arg0) -> controller.menuAction(arg0));
		mntmTabellarischHin.addActionListener((ActionEvent arg0) -> updateAnsichtAuswahl());
		mnAnsicht.add(mntmTabellarischHin);
		
		mntmTabellarischRück = new JCheckBoxMenuItem("Tabellarisch (zurück)");
		mntmTabellarischRück.setActionCommand("zeigeTabEditorRück");
		mntmTabellarischRück.addActionListener((ActionEvent arg0) -> controller.menuAction(arg0));
		mntmTabellarischRück.addActionListener((ActionEvent arg0) -> updateAnsichtAuswahl());
		mnAnsicht.add(mntmTabellarischRück);
		
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
		
		if(controller.dev)
		{
			// Übersicht über bestehende Locks
			JMenuItem mntmLocks = new JMenuItem("GUI-Locks");
			mntmLocks.setActionCommand("locks");
			mntmLocks.addActionListener((ActionEvent arg0) -> controller.menuAction(arg0));
			mnHilfe.add(mntmLocks);
		}
		
		/*
		// TODO
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
		splitPane.setLeftComponent(tree); //*/
		
		tabs = new TemplateBuilderTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		tabs.addChangeListener((ChangeEvent event) -> tabChanged(event));
		
		// TODO
		// splitPane.setRightComponent(tabs);
		frmTemplatebauer.getContentPane().add(tabs, BorderLayout.CENTER);
		
		// TODO
		/*JToolBar toolBar = new JToolBar();
		frmTemplatebauer.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		toolBar.add(new JButton()); //*/
	}
	
	public JFrame getFrame()
	{
		return frmTemplatebauer;
	}
	public void setVisible(boolean arg0)
	{
		this.frmTemplatebauer.setVisible(arg0);
	}
	
	public void tabChanged(ChangeEvent e)
	{
		
	}
	
	public void updateAnsichtAuswahl()
	{
		mntmBildfahrplan.setSelected(tabs.selectedTabIsBildfahrplan());
		mntmTabellarischHin.setSelected(tabs.selectedTabIsTabEditorHin());
		mntmTabellarischRück.setSelected(tabs.selectedTabIsTabEditorRück());
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
}
