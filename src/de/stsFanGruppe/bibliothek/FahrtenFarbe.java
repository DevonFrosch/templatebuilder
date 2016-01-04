package de.stsFanGruppe.bibliothek;

import java.awt.Color;
import java.awt.List;
import java.util.ArrayList;

import de.stsFanGruppe.tools.NullTester;

public class FahrtenFarbe{
	private String suchKriterium;
	private Color linienFarbe;
	private int linienStaerke;
	private float[] linienArt;
	
	private ArrayList<FahrtenFarbe> fahrtenFarbeList = new ArrayList<FahrtenFarbe>();
	
	public FahrtenFarbe(String suchKriterium, Color linienFarbe, int linienStaerke, float[] linienArt)
	{
		NullTester.test(suchKriterium);
		NullTester.test(linienFarbe);
		NullTester.test(linienStaerke);
		NullTester.test(linienArt);
		this.suchKriterium = suchKriterium;
		this.linienFarbe = linienFarbe;
		this.linienStaerke = linienStaerke;
		this.linienArt = linienArt;
	}	
	
	public FahrtenFarbe(ArrayList<FahrtenFarbe> fahrtenFarbeList)
	{
		this.fahrtenFarbeList.addAll(fahrtenFarbeList);
	}
	
	public String getSuchKriterium() {
		return suchKriterium;
	}
	public void setSuchKriterium(String suchKriterium) {
		this.suchKriterium = suchKriterium;
	}
	public Color getLinienFarbe() {
		return linienFarbe;
	}
	public void setLinienFarbe(Color linienFarbe) {
		this.linienFarbe = linienFarbe;
	}
	public int getLinienStaerke() {
		return linienStaerke;
	}
	public void setLinienStärke(int linienStärke) {
		this.linienStaerke = linienStärke;
	}
	public float[] getLinienArt() {
		return linienArt;
	}
	public void setLinienArt(float[] linienArt) {
		this.linienArt = linienArt;
	}
	
	public String colorToString(Color color)
	{
		String c = color.toString();
		return c;
	}
}
