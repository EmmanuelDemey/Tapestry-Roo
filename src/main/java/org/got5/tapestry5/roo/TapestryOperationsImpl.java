package org.got5.tapestry5.roo;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.classpath.PhysicalTypeCategory;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.TypeManagementService;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetailsBuilder;
import org.springframework.roo.classpath.details.ImportMetadata;
import org.springframework.roo.classpath.details.ImportMetadataBuilder;
import org.springframework.roo.classpath.details.MethodMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotatedJavaType;
import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.ClassAttributeValue;
import org.springframework.roo.classpath.details.annotations.StringAttributeValue;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.classpath.operations.AbstractOperations;
import org.springframework.roo.model.CustomData;
import org.springframework.roo.model.DataType;
import org.springframework.roo.model.ImportRegistrationResolverImpl;
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
	private static final JavaType[] SETUP_PARAMETERS = {new JavaType("org.apache.tapestry5.ioc.MappedConfiguration", 0, DataType.TYPE, null, Arrays.asList(new JavaType("java.util.String"),new JavaType("java.util.String")))};
	@Reference private ProjectOperations projectOperation;
	@Reference private PathResolver pathResolver;
	@Reference private TypeManagementService typeManagementService;
	@Reference private TapestryReparse reparse;
	
	public boolean isCreateTapestryApplicationAvailable() {
		/*return projectOperation.isProjectAvailable(projectOperation.getFocusedModuleName()) && 
			!(isTapestryDependencyAvailable());*/ 
		return true;
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
	public void createTapestryApplication(String name, String version) {
		
		
		TapestryMetadata metadata = new TapestryMetadata();
		
		metadata.setApplicationName(name);
		
		if(!projectOperation.isProjectAvailable(projectOperation.getFocusedModuleName())) createMavenStructure();
		
		addTapestryDependency(version);
		
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
		
		/*
		cidBuilder.add(new ImportMetadata() {
			
			public CustomData getCustomData() {
				return null;
			}
			
			public int getModifier() {
				return 0;
			}
			
			public String getDeclaredByMetadataId() {
				return declaredByMetadataId;
			}
			
			public boolean isStatic() {
				return false;
			}
			
			public boolean isAsterisk() {
				return false;
			}
			
			public JavaType getImportType() {
				return null;
			}
			
			public JavaPackage getImportPackage() {
				return new JavaPackage("org.apache.tapestry5.SymbolConstants");
			}
		});*/
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
	
	private String getPathFromTopPackage(String path){
		String topPackage = projectOperation.getFocusedTopLevelPackage().getFullyQualifiedPackageName();
		return topPackage.replace(".", "/")+"/"+path;
	}
}