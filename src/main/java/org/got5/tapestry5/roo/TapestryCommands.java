package org.got5.tapestry5.roo;

import java.util.HashMap;
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
	public void createTapestry(
		@CliOption(key = "name", mandatory = false, help = "Name of your application", unspecifiedDefaultValue="App") String name, 
		@CliOption(key = "tapestryVersion", mandatory = false, help = "Tapestry Version", unspecifiedDefaultValue="5.3.1") String version, 
		@CliOption(key = "withTapestryUpload", mandatory = false, help = "Include Tapestry5-upload", unspecifiedDefaultValue="false", specifiedDefaultValue = "true") Boolean t5upload, 
		@CliOption(key = "withTapestryTest", mandatory = false, help = "Include Tapestry5-test", unspecifiedDefaultValue="false", specifiedDefaultValue = "true") Boolean t5test, 
		@CliOption(key = "withTapestrySpring", mandatory = false, help = "Include Tapestry5-spring", unspecifiedDefaultValue="false", specifiedDefaultValue = "true") Boolean t5spring, 
		@CliOption(key = "withTapestryHibernate", mandatory = false, help = "Include Tapestry5-hibernate", unspecifiedDefaultValue="false", specifiedDefaultValue = "true") Boolean t5hibernate, 
		@CliOption(key = "withTapestryJpa", mandatory = false, help = "Include Tapestry5-JPA", unspecifiedDefaultValue="false", specifiedDefaultValue = "true") Boolean t5jpa, 
		@CliOption(key = "overridePom", mandatory = false, help = "Override the Spring Roo POM", unspecifiedDefaultValue="false", specifiedDefaultValue = "true") Boolean override) {
		
		log.info("Create Tapestry Structure for " + name);
		
		HashMap<String, Boolean> dependencies = new HashMap<String, Boolean>();
		dependencies.put("tapestry-upload", t5upload);
		dependencies.put("tapestry-test", t5test);
		dependencies.put("tapestry-spring", t5spring);
		dependencies.put("tapestry-hibernate", t5hibernate);
		dependencies.put("tapestry-jpa", t5jpa);
		
		operations.createTapestryApplication(name, version, dependencies, override);
		
	}
}