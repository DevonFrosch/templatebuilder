package de.stsFanGruppe.templatebuilder.hafas;

public class StationBuilder
{
	public String name;
	public String externalId;
	public String externalStationNr;
	public String type;
	public double x;
	public double y;
	
	public Station build()
	{
		return new Station(name, externalId, externalStationNr, type, new Coordinate(x, y));
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getExternalId()
	{
		return externalId;
	}
	public void setExternalId(String externalId)
	{
		this.externalId = externalId;
	}
	
	public String getExternalStationNr()
	{
		return externalStationNr;
	}
	public void setExternalStationNr(String externalStationNr)
	{
		this.externalStationNr = externalStationNr;
	}
	
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	
	public double getX()
	{
		return x;
	}
	public void setX(double x)
	{
		this.x = x;
	}
	
	public double getY()
	{
		return y;
	}
	public void setY(double y)
	{
		this.y = y;
	}
}
