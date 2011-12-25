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
	@Reference private ComponentOperations components;
	@Reference private MixinOperations mixins;
	@Reference private PageOperations page;
	@Reference private ServiceOperations services; 
	
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
		
		log.info("Create Tapestry Structure");
		
		operations.createTapestryApplication(name, version);
		
	}
	
	@CliAvailabilityIndicator("tapestry page")
	public boolean isCreatePageAvailable() {
		return !operations.isCreateTapestryApplicationAvailable();
	}
	
	@CliCommand(value = "tapestry page")
	public void createPage(){
		
		log.info("Create Tapestry Page");
		
		page.createTapestryPage();
	}

	@CliAvailabilityIndicator("tapestry component")
	public boolean isCreateComponentAvailable() {
		return !operations.isCreateTapestryApplicationAvailable();
	}
	
	@CliCommand(value = "tapestry component")
	public void createComponent(){
		
		log.info("Create Tapestry Component");
		
		components.createTapestryComponent();
	}
	
	@CliAvailabilityIndicator("tapestry service")
	public boolean isCreateServiceAvailable() {
		return !operations.isCreateTapestryApplicationAvailable();
	}
	
	@CliCommand(value = "tapestry service")
	public void createService(){
		
		log.info("Create Tapestry Service");
		
		services.createTapestryService();
	}
	
	@CliAvailabilityIndicator("tapestry mixin")
	public boolean isCreateMixinAvailable() {
		return !operations.isCreateTapestryApplicationAvailable();
	}
	
	@CliCommand(value = "tapestry mixin")
	public void createMixin(){
		
		log.info("Create Tapestry Mixin");
		
		mixins.createTapestryMixin();
	}
}