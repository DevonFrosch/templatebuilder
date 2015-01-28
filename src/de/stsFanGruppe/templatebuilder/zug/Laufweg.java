package de.stsFanGruppe.templatebuilder.zug;

import java.util.List;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;

public class Laufweg
{
	protected String name;
	protected List<Streckenabschnitt> streckenabschnitte;
	
	public Laufweg(String name, List<Streckenabschnitt> streckenabschnitte)
	{
		super();
		this.name = name;
		this.streckenabschnitte = streckenabschnitte;
	}

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
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
