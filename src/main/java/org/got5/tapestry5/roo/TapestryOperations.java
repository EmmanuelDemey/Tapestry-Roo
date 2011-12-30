package org.got5.tapestry5.roo;

import java.util.Map;

public interface TapestryOperations {
	
	boolean isCreateTapestryApplicationAvailable();

	void createTapestryApplication(String name, String version, Map<String, Boolean> dependencies, boolean override);
	
	void createCrud();
	
}