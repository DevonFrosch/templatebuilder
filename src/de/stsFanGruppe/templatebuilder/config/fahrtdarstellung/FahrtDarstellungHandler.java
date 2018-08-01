package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung;

import de.stsFanGruppe.templatebuilder.zug.FahrtDarstellung;

public class FahrtDarstellungHandler
{
	protected static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FahrtDarstellungHandler.class);;
	
	FahrtDarstellungConfig config;
	FahrtDarstellung[] darstellungen = null;
	FahrtDarstellung zugsucheDarstellung = null;
	FahrtDarstellung templatesucheDarstellung = null;
	
	public FahrtDarstellungHandler(FahrtDarstellungConfig config)
	{
		this.config = config;
		ladeConfig();
		config.registerChangeHandler(() -> ladeConfig());
	}
	
	public FahrtDarstellung getFahrtDarstellung(String zugname, String templatename)
	{
		assert darstellungen != null;
		
		if(zugsucheDarstellung != null && zugsucheDarstellung.testZugname(zugname))
		{
			return zugsucheDarstellung;
		}
		if(templatesucheDarstellung != null && templatename != null && templatesucheDarstellung.testZugname(templatename))
		{
			return templatesucheDarstellung;
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
		zugsucheDarstellung = config.getZugsucheFahrtDarstellung();
		templatesucheDarstellung = config.getTemplatesucheFahrtDarstellung();
	}
}
