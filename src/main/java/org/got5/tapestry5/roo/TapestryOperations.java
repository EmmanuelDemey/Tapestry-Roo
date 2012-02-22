package org.got5.tapestry5.roo;

import java.util.Map;

import org.springframework.roo.model.JavaType;

public interface TapestryOperations {
	
	boolean isCreateTapestryApplicationAvailable();

	void createTapestryApplication(String name, String version, Map<String, Boolean> dependencies, boolean override);
	
	void createTapestryPage(String name, String subpackage, JavaType parentPage);

	void createTapestryMixin(String name, String subpackage, JavaType parentComponent);
	
	boolean isTapestryCreatePageAvailable();

	void createTapestryComponent(String name, String subpackage,
			JavaType parentComponentClass);
}