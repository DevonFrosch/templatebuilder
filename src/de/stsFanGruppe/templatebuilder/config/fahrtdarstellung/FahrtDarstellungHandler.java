package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung;

import de.stsFanGruppe.templatebuilder.zug.FahrtDarstellung;

public class FahrtDarstellungHandler
{
	FahrtDarstellungConfig config;
	FahrtDarstellung[] darstellungen = null;
	
	public FahrtDarstellungHandler(FahrtDarstellungConfig config)
	{
		this.config = config;
		ladeConfig();
		config.registerChangeHandler(() -> ladeConfig());
	}
	
	public FahrtDarstellung getFahrtDarstellung(String zugname)
	{
		assert darstellungen != null;
		
		for(FahrtDarstellung dar: darstellungen)
		{
			if(dar.getFilter().testZugname(zugname, dar.getMuster()))
			{
				return dar;
			}
		};
		
		return config.getStandardFahrtDarstellung();
	}
	
	protected void ladeConfig()
	{
		darstellungen = config.getFahrtDarstellungen();
	}
}
