package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.filter;

public enum FahrtFilter
{
	BEGINNT_MIT("beginnt mit") {
		public boolean testZugname(String zugname, String muster)
		{
			return zugname.startsWith(muster);
		}
	},
	ENTHAELT("enthält") {
		public boolean testZugname(String zugname, String muster)
		{
			return zugname.contains(muster);
		}
	},
	IST_GLEICH("ist gleich") {
		public boolean testZugname(String zugname, String muster)
		{
			return zugname.equals(muster);
		}
	},
	ENDET_MIT("endet mit") {
		public boolean testZugname(String zugname, String muster)
		{
			return zugname.endsWith(muster);
		}
	};
	
	private String printName;
	public abstract boolean testZugname(String zugname, String muster);
	
	private FahrtFilter(String printName)
	{
		this.printName = printName;
	}
	
	public String toString()
	{
		return printName;
	}
}
