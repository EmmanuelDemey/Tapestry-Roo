package org.got5.tapestry5.roo;

import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

@Component
@Service
public class TapestryReparseImpl implements TapestryReparse {
	
	private Logger log = Logger.getLogger(getClass().getName());
	
	TapestryMetadata data = new TapestryMetadata();
	
	public void reparse(){
	}
	
	
	public TapestryMetadata getData() {
		return data;
	}


	public void setData(TapestryMetadata data) {
		this.data = data;
	}


	public void showInfo() {
		log.info("Application Name " + data.getApplicationName());
	}

	public void addInfo(TapestryMetadata meta) {
		this.data = meta;
		
	}
	
}
