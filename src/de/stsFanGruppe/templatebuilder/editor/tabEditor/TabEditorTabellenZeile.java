package de.stsFanGruppe.templatebuilder.editor.tabEditor;

import java.util.LinkedList;
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Gleis;
import de.stsFanGruppe.tools.NullTester;

public class TabEditorTabellenZeile implements Comparable<TabEditorTabellenZeile>
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TabEditorTabellenZeile.class);
	
	protected Betriebsstelle bs;
	protected boolean richtungAufsteigend;
	protected boolean isAnkunft;
	
	public TabEditorTabellenZeile(Betriebsstelle bs, boolean richtungAufsteigend, boolean isAnkunft)
	{
		NullTester.test(bs);
		
		this.bs = bs;
		this.richtungAufsteigend = richtungAufsteigend;
		this.isAnkunft = isAnkunft;
	}
	
	public Betriebsstelle getBetriebsstelle()
	{
		return bs;
	}
	public boolean isAbfahrt()
	{
		return !isAnkunft;
	}
	public boolean isAnkunft()
	{
		return isAnkunft;
	}
	public String getName()
	{
		return bs.getName();
	}
	public String getDS100()
	{
		return bs.getDS100();
	}
	
	public int compareTo(TabEditorTabellenZeile other)
	{
		int richtungsFaktor = ((richtungAufsteigend) ? 1 : -1);
		int comp = this.bs.compareByKM(other.bs);
		
		if(comp != 0)
		{
			return comp * richtungsFaktor;
		}
		else if(this.isAnkunft == other.isAnkunft)
		{
			return 0;
		}
		else if(this.isAnkunft)
		{
			return (- richtungsFaktor);
		}
		else
		{
			return richtungsFaktor;
		}
	}
	
	public static void main(String args[])
	{
		testCompareTo();
	}
	public static void testCompareTo()
	{
		boolean richtung = true;
		TabEditorTabellenZeile o = new TabEditorTabellenZeile(getBs(1), richtung, true);
		test(1, o.compareTo(new TabEditorTabellenZeile(getBs(0), richtung, true)));
		test(1, o.compareTo(new TabEditorTabellenZeile(getBs(0), richtung, false)));
		test(0, o.compareTo(new TabEditorTabellenZeile(getBs(1), richtung, true)));
		test(-1, o.compareTo(new TabEditorTabellenZeile(getBs(1), richtung, false)));
		test(-1, o.compareTo(new TabEditorTabellenZeile(getBs(2), richtung, true)));
		test(-1, o.compareTo(new TabEditorTabellenZeile(getBs(2), richtung, false)));
		
		o = new TabEditorTabellenZeile(getBs(1), richtung, false);
		test(1, o.compareTo(new TabEditorTabellenZeile(getBs(0), richtung, true)));
		test(1, o.compareTo(new TabEditorTabellenZeile(getBs(0), richtung, false)));
		test(1, o.compareTo(new TabEditorTabellenZeile(getBs(1), richtung, true)));
		test(0, o.compareTo(new TabEditorTabellenZeile(getBs(1), richtung, false)));
		test(-1, o.compareTo(new TabEditorTabellenZeile(getBs(2), richtung, true)));
		test(-1, o.compareTo(new TabEditorTabellenZeile(getBs(2), richtung, false)));
		
		richtung = false;
		o = new TabEditorTabellenZeile(getBs(1), richtung, true);
		test(-1, o.compareTo(new TabEditorTabellenZeile(getBs(0), richtung, true)));
		test(-1, o.compareTo(new TabEditorTabellenZeile(getBs(0), richtung, false)));
		test(0, o.compareTo(new TabEditorTabellenZeile(getBs(1), richtung, true)));
		test(1, o.compareTo(new TabEditorTabellenZeile(getBs(1), richtung, false)));
		test(1, o.compareTo(new TabEditorTabellenZeile(getBs(2), richtung, true)));
		test(1, o.compareTo(new TabEditorTabellenZeile(getBs(2), richtung, false)));
		
		o = new TabEditorTabellenZeile(getBs(1), richtung, false);
		test(-1, o.compareTo(new TabEditorTabellenZeile(getBs(0), richtung, true)));
		test(-1, o.compareTo(new TabEditorTabellenZeile(getBs(0), richtung, false)));
		test(-1, o.compareTo(new TabEditorTabellenZeile(getBs(1), richtung, true)));
		test(0, o.compareTo(new TabEditorTabellenZeile(getBs(1), richtung, false)));
		test(1, o.compareTo(new TabEditorTabellenZeile(getBs(2), richtung, true)));
		test(1, o.compareTo(new TabEditorTabellenZeile(getBs(2), richtung, false)));

		log.info("Erfolgreich getestet!");
	}
	private static void test(int soll, int ist) throws AssertionError
	{
		if(soll != ist)
		{
			throw new AssertionError(soll+" != "+ist);
		}
	}
	private static Betriebsstelle getBs(double km)
	{
		LinkedList<Gleis> gleise = new LinkedList<Gleis>();
		gleise.add(new Gleis("", km));
		return new Betriebsstelle("", gleise);
	}
}
