package org.got5.roo;

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
	
	protected void activate(ComponentContext context) {
	}

	protected void deactivate(ComponentContext context) {
	}
	
	@CliAvailabilityIndicator("tapestry setup")
	public boolean isCreateTapestryApplication() {
		return operations.isCreateTapestryApplicationAvailable(); 
	}
	
	@CliCommand(value = "tapestry setup")
	public void createTapestryApplication(
		@CliOption(key = "name", mandatory = false, help = "Name of your application", unspecifiedDefaultValue="App") String name, 
		@CliOption(key = "tapestryVersion", mandatory = false, help = "Tapestry Version", unspecifiedDefaultValue="5.3.1") String version) {
		
		log.info("Create Tapestry Structure");
		
		operations.createTapestryApplication(name, version);
		
	}
	
	@CliAvailabilityIndicator("tapestry page")
	public boolean isCreatePageAvailable() {
		//TODO
		return true; 
	}
	
	@CliCommand(value = "tapestry page")
	public void createPageAvailable(){
		log.info("Create Tapestry Page");
		//TODO
	}

	@CliAvailabilityIndicator("tapestry component")
	public boolean isCreateComponentAvailable() {
		//TODO
		return true; 
	}
	
	@CliCommand(value = "tapestry component")
	public void createComponentAvailable(){
		log.info("Create Tapestry Component");
		//TODO
	}
	
	@CliAvailabilityIndicator("tapestry service")
	public boolean isCreateServiceAvailable() {
		//TODO
		return true; 
	}
	
	@CliCommand(value = "tapestry service")
	public void createServiceAvailable(){
		log.info("Create Tapestry Service");
		//TODO
	}
}