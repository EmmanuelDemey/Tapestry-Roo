package org.got5.tapestry5.roo;

public interface TapestryOperations {
	
	boolean isCreateTapestryApplicationAvailable();

	void createTapestryApplication(String name, String version);
}