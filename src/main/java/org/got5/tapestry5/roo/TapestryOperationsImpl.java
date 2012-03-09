package org.got5.tapestry5.roo;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.classpath.PhysicalTypeCategory;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.classpath.TypeManagementService;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetailsBuilder;
import org.springframework.roo.classpath.details.ImportMetadataBuilder;
import org.springframework.roo.classpath.details.MethodMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotatedJavaType;
import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.ClassAttributeValue;
import org.springframework.roo.classpath.details.annotations.StringAttributeValue;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.classpath.operations.AbstractOperations;
import org.springframework.roo.model.DataType;
import org.springframework.roo.model.JavaPackage;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.LogicalPath;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.project.Property;
import org.springframework.roo.support.util.WebXmlUtils;
import org.springframework.roo.support.util.WebXmlUtils.WebXmlParam;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * TODO
 */
@Component
@Service
public class TapestryOperationsImpl extends AbstractOperations implements TapestryOperations {
	
	private static final JavaType[] SETUP_PARAMETERS = {new JavaType("org.apache.tapestry5.ioc.MappedConfiguration", 0, DataType.TYPE, null, Arrays.asList(JavaType.STRING,JavaType.STRING))};
	private Logger log = Logger.getLogger(getClass().getName());
	
	@Reference private ProjectOperations projectOperation;
	@Reference private PathResolver pathResolver;
	@Reference private TypeManagementService typeManagementService;
	@Reference private TypeLocationService typeLocationService;
	
	//Commands availability related methods
	
	public boolean isCreateTapestryApplicationAvailable() {
		return this.isProjectAvailable();
	}
	
	public boolean isProjectAvailable() {
		return projectOperation.isProjectAvailable(projectOperation.getFocusedModuleName());
	}

	public boolean isTapestryCreatePageAvailable() {
		return isProjectAvailable();
	}
	
	public boolean isTapestryDependencyAvailable() {
		for(Dependency dep : projectOperation.getFocusedModule().getDependencies()){
			if(dep.getArtifactId().equalsIgnoreCase("tapestry-core") 
					&& dep.getGroupId().equalsIgnoreCase("org.apache.tapestry"))
				return true;
		}
		
		return false;
	}

	/**
	 * TODO
	 */
	public void createTapestryApplication(String name, String version, Map<String, Boolean> dependencies) 
	{
		addTapestryDependency(version, dependencies);
		
		createTapestryStructure(name);
	}

	/**
	 * TODO
	 * @param name
	 */
	private void createTapestryStructure(String name){
		
		if(null == name){
			log.log(Level.SEVERE, "Your Tapestry Application should have a name.");	
		}else{
			createWebApp();
			createJavaPackage();
			createResourcesPackage();
			
			createLayoutComponent();
			
			createIndexPage();
			
			createAppModule(name);
			
			createAppProperties(name);
			
			createOrUpdateWebXml(name);
		}
	}
	
	private void createIndexPage() {
		int modifier = Modifier.PUBLIC;
		
		final String declaredByMetadataId = PhysicalTypeIdentifier.createIdentifier(new JavaType(projectOperation.getFocusedTopLevelPackage()+".pages.Index"), pathResolver.getFocusedPath(Path.SRC_MAIN_JAVA));
		final ClassOrInterfaceTypeDetailsBuilder cidBuilder = new ClassOrInterfaceTypeDetailsBuilder(declaredByMetadataId, modifier, new JavaType(projectOperation.getFocusedTopLevelPackage()+".pages.Index"), PhysicalTypeCategory.CLASS) ;
		
		typeManagementService.createOrUpdateTypeOnDisk(cidBuilder.build());
	}

	private void createLayoutComponent(){
		
		List<AnnotationAttributeValue<?>> config = new ArrayList<AnnotationAttributeValue<?>>();
		config.add(new StringAttributeValue(new JavaSymbolName("stylesheet"), "context:static/css/style.css"));
		
		final AnnotationMetadataBuilder IMPORT_ANNOTATION = new AnnotationMetadataBuilder(new JavaType("org.apache.tapestry5.annotations.Import"), config);
		
		final List<AnnotationMetadataBuilder> annotationBuilder = new ArrayList<AnnotationMetadataBuilder>();
		annotationBuilder.add(IMPORT_ANNOTATION);
		
		int modifier = Modifier.PUBLIC;
		
		final String declaredByMetadataId = PhysicalTypeIdentifier.createIdentifier(new JavaType(projectOperation.getFocusedTopLevelPackage()+".components.Layout"), pathResolver.getFocusedPath(Path.SRC_MAIN_JAVA));
		final ClassOrInterfaceTypeDetailsBuilder cidBuilder = new ClassOrInterfaceTypeDetailsBuilder(declaredByMetadataId, modifier, new JavaType(projectOperation.getFocusedTopLevelPackage()+".components.Layout"), PhysicalTypeCategory.CLASS) ;
		

		cidBuilder.setAnnotations(annotationBuilder);

		typeManagementService.createOrUpdateTypeOnDisk(cidBuilder.build());
	}
	
	private void createAppModule(String name){
		
		int modifier = Modifier.PUBLIC;
		
		final String declaredByMetadataId = PhysicalTypeIdentifier.createIdentifier(new JavaType(projectOperation.getFocusedTopLevelPackage()+".services."+name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase()+"Module"), pathResolver.getFocusedPath(Path.SRC_MAIN_JAVA));
		final ClassOrInterfaceTypeDetailsBuilder cidBuilder = new ClassOrInterfaceTypeDetailsBuilder(declaredByMetadataId, modifier, new JavaType(projectOperation.getFocusedTopLevelPackage()+".services."+name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase()+"Module"), PhysicalTypeCategory.CLASS) ;
		
		
		List<AnnotationAttributeValue<?>> config = new ArrayList<AnnotationAttributeValue<?>>();
		config.add(new ClassAttributeValue(new JavaSymbolName("value"), new JavaType("org.apache.tapestry5.ioc.services.SymbolProvider")));
		
		final AnnotationMetadataBuilder CONTRIBUTE_ANNOTATION = new AnnotationMetadataBuilder(new JavaType("org.apache.tapestry5.ioc.annotations.Contribute"),config);
		final AnnotationMetadataBuilder APPLICATION_DEFAULTS_ANNOTATION = new AnnotationMetadataBuilder(new JavaType("org.apache.tapestry5.ioc.services.ApplicationDefaults"));
		InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
		bodyBuilder.appendFormalLine("configuration.add(SymbolConstants.PRODUCTION_MODE, \"false\");");
		
		List<JavaSymbolName> parameterNames = new ArrayList<JavaSymbolName>();
		parameterNames.add(new JavaSymbolName("configuration"));
		
		final MethodMetadataBuilder method = new MethodMetadataBuilder(declaredByMetadataId, modifier | Modifier.STATIC, new JavaSymbolName("contributeApplicationDefaults"), JavaType.VOID_PRIMITIVE, AnnotatedJavaType.convertFromJavaTypes(SETUP_PARAMETERS), parameterNames, bodyBuilder);
		method.addAnnotation(CONTRIBUTE_ANNOTATION);
		method.addAnnotation(APPLICATION_DEFAULTS_ANNOTATION);
		
		cidBuilder.addMethod(method);
	
		final ImportMetadataBuilder newImport = new ImportMetadataBuilder(declaredByMetadataId, 0, new JavaPackage("org.apache.tapestry5.SymbolConstants"), new JavaType("org.apache.tapestry5.SymbolConstants"), false, false);
		cidBuilder.add(newImport.build());
		typeManagementService.createOrUpdateTypeOnDisk(cidBuilder.build());
	}
	
	private void createAppProperties(String name){
		final String appenginePath = projectOperation .getPathResolver().getFocusedIdentifier(Path.SRC_MAIN_RESOURCES, "WEB-INF/"+name.toLowerCase()+".properties");
		
		final boolean appenginePathExists = fileManager.exists(appenginePath);
		
		if (!appenginePathExists) {
			fileManager.createFile(appenginePath);
		}
	}
	
	private void createOrUpdateWebXml(String name) {
		final String appenginePath = projectOperation .getPathResolver().getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "WEB-INF/web.xml");
		
		final boolean appenginePathExists = fileManager.exists(appenginePath);
		
		final Document document;
		if (appenginePathExists) {
			document = XmlUtils.readXml(fileManager.getInputStream(appenginePath));
		} else {
			document = getDocumentTemplate("WEB-INF/web-template.xml");
		}
		
		WebXmlUtils.addContextParam(new WebXmlParam("tapestry.app-package", projectOperation.getFocusedTopLevelPackage().toString()), document, null);
		WebXmlUtils.addFilter(name.toLowerCase(), "org.apache.tapestry5.TapestryFilter", "/*", document, null);
		
		fileManager.createOrUpdateTextFileIfRequired(appenginePath, XmlUtils.nodeToString(document), false);
	}
	
	/**
	 * TODO
	 */
	private void createJavaPackage(){
		
		final LogicalPath javaPath = Path.SRC_MAIN_JAVA.getModulePathId(projectOperation.getFocusedModuleName());
		
		copyDirectoryContents("templates/java/pages/*", pathResolver.getIdentifier(javaPath, getPathFromTopPackage("pages")), false);
		
		copyDirectoryContents("templates/java/components/*", pathResolver.getIdentifier(javaPath, getPathFromTopPackage("components")), false);
		
		copyDirectoryContents("templates/java/mixins/*", pathResolver.getIdentifier(javaPath, getPathFromTopPackage("mixins")), false);
		
		copyDirectoryContents("templates/java/services/*", pathResolver.getIdentifier(javaPath, getPathFromTopPackage("services")), false);
	}
	
	/**
	 * TODO
	 */
	private void createResourcesPackage(){
		
		final LogicalPath resourcesPath = Path.SRC_MAIN_RESOURCES.getModulePathId(projectOperation.getFocusedModuleName());
		
		copyDirectoryContents("templates/template/pages/*", pathResolver.getIdentifier(resourcesPath, getPathFromTopPackage("pages")), false);
		
		copyDirectoryContents("templates/template/components/*", pathResolver.getIdentifier(resourcesPath, getPathFromTopPackage("components")), false);
	}

	/**
	 * TODO
	 */
	private void createWebApp(){
		
		final LogicalPath webappPath = Path.SRC_MAIN_WEBAPP.getModulePathId(projectOperation.getFocusedModuleName());
		
		copyDirectoryContents("templates/webapp/static/css/*", pathResolver.getIdentifier(webappPath, "static/css"), false);
		
		copyDirectoryContents("templates/webapp/static/js/*", pathResolver.getIdentifier(webappPath, "static/js"), false);
		
		copyDirectoryContents("templates/webapp/static/img/*", pathResolver.getIdentifier(webappPath, "static/img"), false);
	}
	
	/**
	 * TODO
	 * @param version
	 */
	private void addTapestryDependency(String version,Map<String, Boolean> dep){
		
		log.info("Adding Tapestry Dependencies");
		
		final List<Dependency> requiredDependencies = new ArrayList<Dependency>();
		
		Element configuration = XmlUtils.getConfiguration(getClass());
		
		final List<Element> dependencies = XmlUtils.findElements("/configuration/maven/dependencies/dependency", configuration);
		
		for(Element dependency : dependencies){
			requiredDependencies.add(new Dependency(dependency));
		}
		projectOperation.addProperty(projectOperation.getFocusedModuleName(), new Property("tapestry.version", version));
		
		projectOperation.addDependencies(projectOperation.getFocusedModuleName(), requiredDependencies);
		
		
		for(String key : dep.keySet()){
			
			if(dep.get(key)){
				requiredDependencies.add(new Dependency("org.apache.tapestry", key, "${tapestry.version}"));
			}
		}
		
		projectOperation.addDependencies(projectOperation.getFocusedModuleName(), requiredDependencies);
	}
	
	private String getPathFromTopPackage(String path){
		String topPackage = projectOperation.getFocusedTopLevelPackage().getFullyQualifiedPackageName();
		return topPackage.replace(".", "/")+"/"+path;
	}

	public void createTapestryPage(String name, String subpackage,
			JavaType parentPage) {

		createTapestryObject(name, subpackage, parentPage,TapestryObjectType.PAGE);
		
	}

	public void createTapestryComponent(String name, String subpackage,
			JavaType parentComponent) {
		createTapestryObject(name, subpackage, parentComponent,TapestryObjectType.COMPONENT);
	}
	
	public void createTapestryMixin(String name, String subpackage,
			JavaType parentComponent) {
		createTapestryObject(name, subpackage, parentComponent,TapestryObjectType.MIXIN);
	}
	
	private void createTapestryObject(String name, String subpackage,
			JavaType parentObject,TapestryObjectType type) {
		//Java class creation
		int modifier = Modifier.PUBLIC;
				
		
		String objectFullName;
		String typePackage = null;
		
		switch (type) {
		case PAGE:
			typePackage = "pages";
			break;
		case COMPONENT:
			typePackage = "components";
			break;
		case MIXIN:
			typePackage = "mixins";
			break;
		}
		
		
		if (StringUtils.isNotEmpty(subpackage)) {
			objectFullName = projectOperation.getFocusedTopLevelPackage()+"."+typePackage+"."+subpackage+"."+StringUtils.capitalize(name);
		}else{
			//if no subpackage provided, page is created in the default one
			objectFullName = projectOperation.getFocusedTopLevelPackage()+"."+typePackage+"."+StringUtils.capitalize(name);
		}
		
		log.info("Prepare following "+type+" full path : "+objectFullName);
		
		
		final String declaredByMetadataId = PhysicalTypeIdentifier.createIdentifier(new JavaType(objectFullName), pathResolver.getFocusedPath(Path.SRC_MAIN_JAVA));
		final ClassOrInterfaceTypeDetailsBuilder cidBuilder = new ClassOrInterfaceTypeDetailsBuilder(declaredByMetadataId, modifier, new JavaType(objectFullName), PhysicalTypeCategory.CLASS) ;

		//The inheritance is managed here
		if (parentObject != null) {
			cidBuilder.addExtendsTypes(parentObject);
		}
		
		//Class creation
		typeManagementService.createOrUpdateTypeOnDisk(cidBuilder.build());
		
		log.info("Java class creation done");
		
		//As mixins doens't have related templates, they will skip the following code 
		if (!TapestryObjectType.MIXIN.equals(type)) {
			//Related template file creation 
			String templatePath = StringUtils.replace(objectFullName, ".", "/");
			
			final String templateFullPath = projectOperation.getPathResolver().getFocusedIdentifier(Path.SRC_MAIN_RESOURCES, templatePath+".tml");
			
			//We must check if the template doesn't already exist
			final boolean templateFullPathExists = fileManager.exists(templateFullPath);
			
			if (!templateFullPathExists) {
				//template is created
				fileManager.createFile(templateFullPath);
				fileManager.createOrUpdateTextFileIfRequired(templateFullPath, TapestryUtils.createEmptyTemplateContent(), false);
				
				boolean isCreationSuccessful = fileManager.exists(templateFullPath);
				if (isCreationSuccessful) {
					log.info("Template creation done");
				}else{
					log.info("Template creation failed");
				}
			}else{
				log.info("Template file already exists, no creation");
			}
		}
	}

}