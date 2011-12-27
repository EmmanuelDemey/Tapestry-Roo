package org.got5.tapestry5.roo;

import java.util.List;

public class TapestryMetadata {
	
	private String ApplicationName;
	private List<String> Modules;
	
	public TapestryMetadata() {
		super();
	}

	public String getApplicationName() {
		return ApplicationName;
	}

	public void setApplicationName(String applicationName) {
		ApplicationName = applicationName;
	}

	public List<String> getModules() {
		return Modules;
	}

	public void setModules(List<String> modules) {
		Modules = modules;
	}
	
}
