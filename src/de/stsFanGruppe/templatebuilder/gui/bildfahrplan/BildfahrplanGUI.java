package de.stsFanGruppe.templatebuilder.gui.bildfahrplan;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.tree.*;
import de.stsFanGruppe.templatebuilder.external.ImporterFramework;
import de.stsFanGruppe.templatebuilder.external.jtraingraph.JTrainGraphImporter;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.templatebuilder.zug.Linie;
import de.stsFanGruppe.tools.NullTester;
import java.awt.Component;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.util.Set;

public class BildfahrplanGUI
{
	protected BildfahrplanGUIController controller;
	private JFrame frmTemplatebauer;
	private BildfahrplanZeichner bildfahrplanZeichner;
	
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
				String file = "testdaten/v0.1m2_Test.fpl";
				//String file = "testdaten/Bildfahrplan_Eifelbahn.fpl";
				
				ImporterFramework imp = new ImporterFramework(new JTrainGraphImporter());
				Streckenabschnitt streckenabschnitt = imp.importStreckenabschnitt(file);
				System.out.println(streckenabschnitt.toXML());
				
				log("main 1");
				BildfahrplanGUIController controller = new BildfahrplanGUIController();
				log("main 2");
				BildfahrplanGUI window = new BildfahrplanGUI(controller);
				log("main 3");
				window.frmTemplatebauer.setVisible(true);
				
				log("main 4");
				controller.ladeStreckenabschnitt(streckenabschnitt);

				log("main 5");
				Set<Fahrt> fahrten = imp.importFahrten(file, streckenabschnitt, new Linie("1"));
				/*System.out.println("<fahrten>");
				fahrten.forEach((Fahrt f) -> System.out.println(f.toXML("  ")));
				System.out.println("</fahrten>");
				//*/
				log("main 6");
				controller.ladeZüge(fahrten);
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
		log("BildfahrplanGUI gesetzt");
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
		mntmImportAusJtraingraph.addActionListener((ActionEvent arg0) -> {
			
		});
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
		
		JScrollPane scrollPane = new JScrollPane();
		tabbedPane.addTab("New tab", null, scrollPane, null);
		
		bildfahrplanZeichner = new BildfahrplanZeichner(new BildfahrplanConfig(300, 600), this.controller);
		log("bildfahrplanZeichner initialisiert");
		scrollPane.setViewportView(bildfahrplanZeichner);
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
	
	private static void log(String text)
	{
		System.out.println("BildfahrplanGUI: "+text);
	}
}
