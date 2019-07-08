package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung;

import de.stsFanGruppe.templatebuilder.zug.FahrtDarstellung;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.FirstLastList;

public class FahrtDarstellungHandler
{
	protected static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FahrtDarstellungHandler.class);;
	
	FahrtDarstellungConfig config;
	FahrtDarstellung[] darstellungen = null;
	FahrtDarstellung zugsucheDarstellung = null;
	FahrtDarstellung templatesucheDarstellung = null;
	FahrtDarstellung[] hervorhebungsDarstellungen = null;
	
	private Object changeHandlerId = null;
	
	public FahrtDarstellungHandler(FahrtDarstellungConfig config)
	{
		this.config = config;
		ladeConfig();
		log.debug("FahrtDarstellungHandler registerChangeHandler");
		this.changeHandlerId = config.registerChangeHandler(() -> ladeConfig());
	}
	
	public FahrtDarstellung[] getFahrtDarstellungen(String zugName, String templateName, String templateTid)
	{
		assert darstellungen != null;
		
		FirstLastList<FahrtDarstellung> liste = new FirstLastLinkedList<>();
		
		if(zugsucheDarstellung != null && zugsucheDarstellung.testZugname(zugName))
		{
			liste.add(zugsucheDarstellung);
		}
		else if(templatesucheDarstellung != null && (templateName != null && templatesucheDarstellung.testZugname(templateName) 
				|| templateTid != null && templatesucheDarstellung.testZugname(templateTid)))
		{
			liste.add(templatesucheDarstellung);
		}
		else 
		{
			FahrtDarstellung ziel = null;
			
			for(FahrtDarstellung dar : darstellungen)
			{
				if(dar.testZugname(zugName))
				{
					ziel = dar;
					break;
				}
			}
			
			if(ziel != null)
			{
				liste.add(ziel);
			}
			else
			{
				liste.add(config.getStandardFahrtDarstellung());
			}
		}
		
		FahrtDarstellung hervorhebung = null;
		
		for(FahrtDarstellung dar : hervorhebungsDarstellungen)
		{
			if(dar.testZugname(zugName))
			{
				hervorhebung = dar;
				break;
			}
		}
		
		if(hervorhebung != null)
		{
			liste.add(hervorhebung);
		}
		
		return liste.toArray(new FahrtDarstellung[liste.size()]);
	}
	
	protected void ladeConfig()
	{
		darstellungen = config.getFahrtDarstellungen();
		zugsucheDarstellung = config.getZugsucheFahrtDarstellung();
		templatesucheDarstellung = config.getTemplatesucheFahrtDarstellung();
		hervorhebungsDarstellungen = config.getFahrtHervorhebungsDarstellungen();
	}
}
