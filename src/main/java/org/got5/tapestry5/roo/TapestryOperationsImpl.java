package org.got5.tapestry5.roo;

import static org.springframework.roo.classpath.customdata.CustomDataKeys.PERSISTENT_TYPE;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.classpath.PhysicalTypeCategory;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.classpath.TypeManagementService;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetailsBuilder;
import org.springframework.roo.classpath.details.FieldMetadataBuilder;
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
import org.springframework.roo.support.util.Assert;
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
	@Reference private TapestryReparse reparse;
	@Reference private TypeLocationService typeLocationService;
	
	public boolean isCreateTapestryApplicationAvailable() {
		/*return projectOperation.isProjectAvailable(projectOperation.getFocusedModuleName()) && 
			!(isTapestryDependencyAvailable());*/ 
		return projectOperation.isProjectAvailable(projectOperation.getFocusedModuleName());
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
	public void createTapestryApplication(String name, String version, Map<String, Boolean> dependencies, boolean override) {
		
		
		TapestryMetadata metadata = new TapestryMetadata();
		
		metadata.setApplicationName(name);
		
		addTapestryDependency(version, dependencies, override);
		
		createTapestryStructure(name);
		
	}

	/**
	 * TODO
	 */
	private void createMavenStructure(){
	}
	
	/**
	 * TODO
	 * @param name
	 */
	private void createTapestryStructure(String name){
		
		Assert.notNull(name, "Your Tapestry Application should have a name.");
		
		createWebApp();
		createJavaPackage();
		createResourcesPackage();
		
		createLayoutComponent();
		
		createAppModule(name);
		
		createAppProperties(name);
		
		createOrUpdateWebXml(name);
		
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
		final String appenginePath = projectOperation .getPathResolver().getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "WEB-INF/"+name.toLowerCase()+".properties");
		
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
	private void addTapestryDependency(String version,Map<String, Boolean> dep, boolean override){
		
		log.info("Adding Tapestry Dependencies");
		if(isTapestryDependencyAvailable()) return;
		
		if(override){
			/*for(Dependency depremove : projectOperation.getFocusedModule().getDependencies()){
				log.info("Remove Dependency : " + depremove.getGroupId() + " " + depremove.getArtifactId());
				projectOperation.removeDependency(projectOperation.getFocusedModuleName(), depremove);
			}*/
			/*for(Repository repremove : projectOperation.getFocusedModule().getPluginRepositories()){
				log.info("Remove Plugin Repository : " + repremove.getName());
				projectOperation.removeRepository(projectOperation.getFocusedModuleName(), repremove);
			}
			for(Plugin pluginremove : projectOperation.getFocusedModule().getBuildPlugins()){
				log.info("Remove Plugin : " + pluginremove.getArtifactId());
				projectOperation.removeBuildPlugin(projectOperation.getFocusedModuleName(), pluginremove);
			}
			for(Repository repositoryremove : projectOperation.getFocusedModule().getRepositories()){
				log.info("Remove Repository : " + repositoryremove.getUrl());
				projectOperation.removeRepository(projectOperation.getFocusedModuleName(), repositoryremove);
			}*/
		}
		
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

	public void createCrud() {
		int modifier = Modifier.PUBLIC;

		ClassOrInterfaceTypeDetailsBuilder cidBuilder=null;
		String declaredByMetadataId = null;
		//Create Index Page
		copyFile(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_RESOURCES, "/"+getPathFromTopPackage("pages")+"/Index.tml"), getDocumentTemplate("crud/Index.tml"));
		declaredByMetadataId = PhysicalTypeIdentifier.createIdentifier(new JavaType(projectOperation.getFocusedTopLevelPackage()+".pages.Index"), pathResolver.getFocusedPath(Path.SRC_MAIN_JAVA));
		cidBuilder = new ClassOrInterfaceTypeDetailsBuilder(declaredByMetadataId, modifier, new JavaType(projectOperation.getFocusedTopLevelPackage()+".pages.Index"), PhysicalTypeCategory.CLASS) ;
		typeManagementService.createOrUpdateTypeOnDisk(cidBuilder.build());
		
		//Create Layout Component
		createLayoutComponent();
		//copyFile(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_RESOURCES, "/"+getPathFromTopPackage("components")+"/Layout.tml"), getDocumentTemplate("crud/Layout.tml"));
		copyFile(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_RESOURCES, "/"+getPathFromTopPackage("components")+"/Layout.tml"), getDocumentTemplate("crud/Layout.tml"));
		
		List<String> listEntity = new ArrayList<String>();
		for (ClassOrInterfaceTypeDetails entityDetails : typeLocationService.findClassesOrInterfaceDetailsWithTag(PERSISTENT_TYPE)) {
			log.info(entityDetails.getName().getSimpleTypeName().toString());
			listEntity.add(entityDetails.getName().getSimpleTypeName().toString());
			declaredByMetadataId = PhysicalTypeIdentifier.createIdentifier(new JavaType(projectOperation.getFocusedTopLevelPackage()+".pages.Edit_"+entityDetails.getName().getSimpleTypeName().toString()), pathResolver.getFocusedPath(Path.SRC_MAIN_JAVA));
			cidBuilder = new ClassOrInterfaceTypeDetailsBuilder(declaredByMetadataId, modifier, new JavaType(projectOperation.getFocusedTopLevelPackage()+".pages.Edit_"+entityDetails.getName().getSimpleTypeName().toString()), PhysicalTypeCategory.CLASS) ;
			typeManagementService.createOrUpdateTypeOnDisk(cidBuilder.build());
			copyFile(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_RESOURCES, "/"+getPathFromTopPackage("pages")+"/Edit_"+entityDetails.getName().getSimpleTypeName().toString()+".tml"), getDocumentTemplate("crud/Edit.tml"));
			
			declaredByMetadataId = PhysicalTypeIdentifier.createIdentifier(new JavaType(projectOperation.getFocusedTopLevelPackage()+".pages.Display_"+entityDetails.getName().getSimpleTypeName().toString()), pathResolver.getFocusedPath(Path.SRC_MAIN_JAVA));
			cidBuilder = new ClassOrInterfaceTypeDetailsBuilder(declaredByMetadataId, modifier, new JavaType(projectOperation.getFocusedTopLevelPackage()+".pages.Display_"+entityDetails.getName().getSimpleTypeName().toString()), PhysicalTypeCategory.CLASS) ;
			typeManagementService.createOrUpdateTypeOnDisk(cidBuilder.build());
			copyFile(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_RESOURCES, "/"+getPathFromTopPackage("pages")+"/Display_"+entityDetails.getName().getSimpleTypeName().toString()+".tml"), getDocumentTemplate("crud/Display.tml"));
		}
		
		declaredByMetadataId = PhysicalTypeIdentifier.createIdentifier(new JavaType(projectOperation.getFocusedTopLevelPackage()+".components.Menu"), pathResolver.getFocusedPath(Path.SRC_MAIN_JAVA));
		cidBuilder = new ClassOrInterfaceTypeDetailsBuilder(declaredByMetadataId, modifier, new JavaType(projectOperation.getFocusedTopLevelPackage()+".components.Menu"), PhysicalTypeCategory.CLASS) ;
		
		List<AnnotationMetadataBuilder> annotations = new ArrayList<AnnotationMetadataBuilder>();
		annotations.add(new AnnotationMetadataBuilder(new JavaType("org.apache.tapestry5.annotations.Property")));
		
		FieldMetadataBuilder field = new FieldMetadataBuilder(declaredByMetadataId, Modifier.PRIVATE, annotations, new JavaSymbolName("row"), JavaType.STRING);
		cidBuilder.addField(field);
		
		field = new FieldMetadataBuilder(declaredByMetadataId, Modifier.PRIVATE, annotations, new JavaSymbolName("data"), JavaType.listOf(JavaType.STRING));
		cidBuilder.addField(field);
		
		InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
		bodyBuilder.appendFormalLine("return \"Edit_\"+row.toLowerCase();");
		MethodMetadataBuilder method = new MethodMetadataBuilder(declaredByMetadataId, Modifier.PUBLIC, new JavaSymbolName("getEdit"), JavaType.STRING, bodyBuilder);
		cidBuilder.addMethod(method);
		
		bodyBuilder = new InvocableMemberBodyBuilder();
		bodyBuilder.appendFormalLine("return row.toLowerCase();");
		method = new MethodMetadataBuilder(declaredByMetadataId, Modifier.PUBLIC, new JavaSymbolName("getLink"), JavaType.STRING, bodyBuilder);
		cidBuilder.addMethod(method);
		
		bodyBuilder = new InvocableMemberBodyBuilder();
		bodyBuilder.appendFormalLine("return \"Display_\"+row.toLowerCase();");
		method = new MethodMetadataBuilder(declaredByMetadataId, Modifier.PUBLIC, new JavaSymbolName("getDisplay"), JavaType.STRING, bodyBuilder);
		cidBuilder.addMethod(method);
		
		bodyBuilder = new InvocableMemberBodyBuilder();
		bodyBuilder.appendFormalLine("return row.toUpperCase();");
		method = new MethodMetadataBuilder(declaredByMetadataId, Modifier.PUBLIC, new JavaSymbolName("getTitle"), JavaType.STRING, bodyBuilder);
		cidBuilder.addMethod(method);
		
		bodyBuilder = new InvocableMemberBodyBuilder();
		bodyBuilder.appendFormalLine("data = new ArrayList<String>();");
		for(String entity : listEntity){
			bodyBuilder.appendFormalLine("data.add(\""+entity.toLowerCase()+"\");");
		}
		final ImportMetadataBuilder newImport = new ImportMetadataBuilder(declaredByMetadataId, 0, new JavaPackage("java.util.ArrayList"), new JavaType("java.util.ArrayList"), false, false);
		cidBuilder.add(newImport.build());
		method = new MethodMetadataBuilder(declaredByMetadataId, Modifier.PUBLIC, new JavaSymbolName("init"), JavaType.VOID_PRIMITIVE, bodyBuilder);
		method.addAnnotation(new AnnotationMetadataBuilder(new JavaType("org.apache.tapestry5.annotations.SetupRender"), null));
		cidBuilder.addMethod(method);
		
		
		typeManagementService.createOrUpdateTypeOnDisk(cidBuilder.build());

		copyFile(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_RESOURCES, "/"+getPathFromTopPackage("components")+"/Menu.tml"), getDocumentTemplate("crud/Menu.tml"));
		
	}
	
	private void copyFile(String dest, Document content){
		fileManager.createOrUpdateTextFileIfRequired(dest, XmlUtils.nodeToString(content), false);
	}
}