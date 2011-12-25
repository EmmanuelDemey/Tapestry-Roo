package org.got5.roo;

public interface TapestryOperations {
	
	boolean isCreateTapestryApplicationAvailable();

	void createTapestryApplication(String name, String version);
}