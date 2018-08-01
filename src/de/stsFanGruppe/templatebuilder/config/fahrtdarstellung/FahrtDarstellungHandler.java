package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung;

import de.stsFanGruppe.templatebuilder.zug.FahrtDarstellung;

public class FahrtDarstellungHandler
{
	protected static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FahrtDarstellungHandler.class);;
	
	FahrtDarstellungConfig config;
	FahrtDarstellung[] darstellungen = null;
	FahrtDarstellung suchDarstellung = null;
	
	public FahrtDarstellungHandler(FahrtDarstellungConfig config)
	{
		this.config = config;
		ladeConfig();
		config.registerChangeHandler(() -> ladeConfig());
	}
	
	public FahrtDarstellung getFahrtDarstellung(String zugname)
	{
		assert darstellungen != null;
		
		if(suchDarstellung != null && suchDarstellung.testZugname(zugname))
		{
			return suchDarstellung;
		}
		
		for(FahrtDarstellung dar : darstellungen)
		{
			if(dar.testZugname(zugname))
			{
				return dar;
			}
		}
		
		return config.getStandardFahrtDarstellung();
	}
	
	protected void ladeConfig()
	{
		
		darstellungen = config.getFahrtDarstellungen();
		suchDarstellung = config.getGesuchteFahrtDarstellung();
	}
}
