package de.stsFanGruppe.templatebuilder.gui.bildfahrplan;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.tree.*;
import de.stsFanGruppe.tools.NullTester;
import java.awt.Component;
import java.awt.event.*;
import java.awt.BorderLayout;

public class BildfahrplanGUI
{
	protected BildfahrplanGUIController controller;
	BildfahrplanZeichner bildfahrplanZeichner;
	private boolean initialized = false;
	
	private JFrame frmTemplatebauer;
	private JScrollPane scrollPane;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			javax.swing.UIManager.setLookAndFeel( javax.swing.UIManager.getSystemLookAndFeelClassName() );
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex)
		{
			java.util.logging.Logger.getLogger(BildfahrplanTestGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		
		EventQueue.invokeLater(() -> {
			try
			{
				BildfahrplanGUIController controller = new BildfahrplanGUIController();
				BildfahrplanGUI window = new BildfahrplanGUI(controller);
				window.frmTemplatebauer.setVisible(true);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */
	public BildfahrplanGUI()
	{
		this(new BildfahrplanGUIController());
	}
	
	public BildfahrplanGUI(BildfahrplanGUIController controller)
	{
		NullTester.test(controller);
		this.controller = controller;
		initialize();
		controller.setBildfahrplanGUI(this);
	}
	
	public BildfahrplanZeichner getBildfahrplanZeichner()
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
		frmTemplatebauer.setBounds(100, 100, 450, 300);
		frmTemplatebauer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmTemplatebauer.setJMenuBar(menuBar);
		
		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);
		
		JMenuItem mntmNeu = new JMenuItem("Neu...");
		mntmNeu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnDatei.add(mntmNeu);
		
		JMenuItem mntmffnen = new JMenuItem("\u00D6ffnen...");
		mntmffnen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnDatei.add(mntmffnen);
		
		JSeparator separator_2 = new JSeparator();
		mnDatei.add(separator_2);
		
		JMenuItem mntmSpeichern = new JMenuItem("Speichern");
		mntmSpeichern.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnDatei.add(mntmSpeichern);
		
		JMenuItem mntmSpeichernUnter = new JMenuItem("Speichern unter...");
		mntmSpeichernUnter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnDatei.add(mntmSpeichernUnter);
		
		JSeparator separator = new JSeparator();
		mnDatei.add(separator);
		
		JMenu mnImportexport = new JMenu("Import/Export");
		mnDatei.add(mnImportexport);
		
		JMenuItem mntmImportAusJtraingraph = new JMenuItem("Import aus JTrainGraph");
		mntmImportAusJtraingraph.addActionListener((ActionEvent arg0) -> controller.menuImportJTG());
		mnImportexport.add(mntmImportAusJtraingraph);
		
		JSeparator separator_1 = new JSeparator();
		mnDatei.add(separator_1);
		
		JMenuItem mntmSchlieen = new JMenuItem("Schlie\u00DFen");
		mntmSchlieen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mnDatei.add(mntmSchlieen);
		
		JMenu mnBearbeiten = new JMenu("Bearbeiten");
		menuBar.add(mnBearbeiten);
		
		JMenu mnStrecken = new JMenu("Strecken");
		menuBar.add(mnStrecken);
		
		JMenu mnZge = new JMenu("Z\u00FCge");
		menuBar.add(mnZge);
		
		JMenu mnAnsicht = new JMenu("Ansicht");
		menuBar.add(mnAnsicht);
		
		JMenuItem mntmBildfahrplan = new JMenuItem("Bildfahrplan");
		mnAnsicht.add(mntmBildfahrplan);
		
		JMenu mnEinstellungen = new JMenu("Einstellungen");
		menuBar.add(mnEinstellungen);
		
		JMenuItem mntmOptionen = new JMenuItem("Optionen");
		mnEinstellungen.add(mntmOptionen);
		
		JMenu mnHilfe = new JMenu("?");
		menuBar.add(mnHilfe);
		
		JMenuItem mntmHilfe = new JMenuItem("Hilfe");
		mntmHilfe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mnHilfe.add(mntmHilfe);
		
		JMenuItem mntmber = new JMenuItem("\u00DCber...");
		mnHilfe.add(mntmber);
		
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
		splitPane.setRightComponent(tabbedPane);
		
		scrollPane = new JScrollPane();
		tabbedPane.addTab("New tab", null, scrollPane, null);
		
		bildfahrplanZeichner = new BildfahrplanZeichner(new BildfahrplanConfig(300, 600), this.controller);
		scrollPane.setViewportView(bildfahrplanZeichner);
		bildfahrplanZeichner.setLayout(null);
		
		JToolBar toolBar = new JToolBar();
		frmTemplatebauer.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton button = new JButton("");
		toolBar.add(button);
		
		initialized = true;
	}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	public void errorMessage(String text)
	{
		JOptionPane.showMessageDialog(null, text, "Fehler", JOptionPane.ERROR_MESSAGE);
	}
	
	private static void log(String text)
	{
		System.out.println("BildfahrplanGUI: "+text);
	}
}
