package de.stsFanGruppe.templatebuilder.strecken;

import java.util.List;
import java.util.LinkedList;

public class Kursbuchstrecke
{	
	protected String name;
	protected String nummer;
	protected List<Streckenabschnitt> streckenabschnitte;
	
	public Kursbuchstrecke(String name, String nummer, List<Streckenabschnitt> streckenabschnitte)
	{
		this.name = name;
		this.nummer = nummer;
		this.streckenabschnitte = streckenabschnitte;
	}
	public Kursbuchstrecke(String name, String nummer)
	{
		this(name, nummer, new LinkedList<>());
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getNummer()
	{
		return nummer;
	}
	public void setNummer(String nummer)
	{
		this.nummer = nummer;
	}
	public boolean hasStreckenabschnitte()
	{
		return !streckenabschnitte.isEmpty();
	}
	public List<Streckenabschnitt> getStreckenabschnitte()
	{
		return streckenabschnitte;
	}
	public void addStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		this.streckenabschnitte.add(streckenabschnitt);
	}
	public void addStreckenabschnitt(int index, Streckenabschnitt streckenabschnitt)
	{
		this.streckenabschnitte.add(index, streckenabschnitt);
	}
	public void removeStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		this.streckenabschnitte.remove(streckenabschnitt);
	}
}
