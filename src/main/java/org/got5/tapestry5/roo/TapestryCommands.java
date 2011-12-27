package org.got5.tapestry5.roo;

import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CliOption;
import org.springframework.roo.shell.CommandMarker;

@Component 
@Service
public class TapestryCommands implements CommandMarker { 
	
	private Logger log = Logger.getLogger(getClass().getName());

	@Reference private TapestryOperations operations; 
	@Reference private TapestryReparse reparse;
	
	protected void activate(ComponentContext context) {
	}

	protected void deactivate(ComponentContext context) {
	}
	
	@CliAvailabilityIndicator("tapestry setup")
	public boolean isCreateTapestryApplication() {
		return operations.isCreateTapestryApplicationAvailable(); 
	}
	
	@CliCommand(value = "tapestry setup")
	public void createTapestry(
		@CliOption(key = "name", mandatory = false, help = "Name of your application", unspecifiedDefaultValue="App") String name, 
		@CliOption(key = "tapestryVersion", mandatory = false, help = "Tapestry Version", unspecifiedDefaultValue="5.3.1") String version) {
		
		log.info("Create Tapestry Structure for " + name);
		
		operations.createTapestryApplication(name, version);
		
	}
}