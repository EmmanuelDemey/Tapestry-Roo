package org.got5.roo;

import java.util.ArrayList;
import java.util.List;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.classpath.operations.AbstractOperations;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.LogicalPath;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.project.Property;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Element;


@Component
@Service
public class TapestryOperationsImpl extends AbstractOperations implements TapestryOperations {
	
	@Reference private ProjectOperations projectOperation;
	@Reference private FileManager fileManager;
	@Reference private PathResolver pathResolver;
	
	public boolean isCreateTapestryApplicationAvailable() {
		return !(isTapestryDependencyAvailable() && isTapestryStructureAvailable());
	}
	
	public boolean isTapestryStructureAvailable() {
		//TODO
		return false;
	}
	
	public boolean isTapestryDependencyAvailable() {
		//TODO
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
		
		//TODO create Tapestry folders src/main/java and resources, pages, services, components and mixins packages
		final LogicalPath webappPath = pathResolver.getFocusedPath(Path.SRC_MAIN_WEBAPP);
		final LogicalPath resourcesPath = pathResolver.getFocusedPath(Path.SRC_MAIN_RESOURCES);
		final LogicalPath javaPath = pathResolver.getFocusedPath(Path.SRC_MAIN_JAVA);
		
		if(isTapestryStructureAvailable()) return;
		copyDirectoryContents("templates/webapp/*.*", pathResolver.getIdentifier(webappPath, ""), false);
		copyDirectoryContents("templates/template/*.*", pathResolver.getIdentifier(resourcesPath, ""), false);
		copyDirectoryContents("templates/java/*.*", pathResolver.getIdentifier(javaPath, ""), false);
		
		//TODO create AppModule with an contributeApplicationDefaults method, PRODUCTION_MODE to true
		
		//TODO create app.properties
		
		//TODO create web.xml
		
		//TODO update Layout package
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