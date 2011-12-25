package org.got5.roo;

import java.util.ArrayList;
import java.util.List;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.project.Property;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Element;


@Component
@Service
public class TapestryOperationsImpl implements TapestryOperations {
	
	@Reference private ProjectOperations projectOperation;
	
	public boolean isCreateTapestryApplicationAvailable() {
		return !(isTapestryDependencyAvailable() && isTapestryStructureAvailable());
	}
	
	public boolean isTapestryStructureAvailable() {
		return false;
	}
	
	public boolean isTapestryDependencyAvailable() {
		return false;
	}

	public void createTapestryApplication(String name, String version) {
		
		if(!projectOperation.isProjectAvailable(projectOperation.getFocusedModuleName())) createMavenStructure();
		
		createTapestryStructure(name);
		
		addTapestryDependency(version);
		
	}

	private void createMavenStructure(){
	}
	
	private void createTapestryStructure(String name){
		
		//TODO create Tapestry folders
		if(isTapestryStructureAvailable()) return;
		
		//TODO create AppModule with an contributeApplicationDefaults method, PRODUCTION_MODE to true
		
	}
	
	private void addTapestryDependency(String version){
		
		if(isTapestryDependencyAvailable()) return;
		
		final List<Dependency> requiredDependencies = new ArrayList<Dependency>();
		
		Element configuration = XmlUtils.getConfiguration(getClass());
		
		final List<Element> dependencies = XmlUtils.findElements("/configuration/maven/dependencies/dependency", configuration);
		
		for(Element dependency : dependencies){
			requiredDependencies.add(new Dependency(dependency));
		}
		projectOperation.addProperty(projectOperation.getFocusedModuleName(), new Property("tapestry.version", version));
		
		projectOperation.addDependencies(projectOperation.getFocusedModuleName(), requiredDependencies);
	}
}