package de.stsFanGruppe.templatebuilder.hafas;

public class Station
{
	public final String name;
	public final String externalId;
	public final String externalStationNr;
	public final String type;
	public final Coordinate position;
	
	public Station(String name, String externalId, String externalStationNr, String type, Coordinate position)
	{
		this.name = name;
		this.externalId = externalId;
		this.externalStationNr = externalStationNr;
		this.type = type;
		this.position = position;
	}
	
	public String toString()
	{
		return "'"+name+"' (nr="+externalStationNr+", pos="+position+")";
	}
}
