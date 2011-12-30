package org.got5.tapestry5.roo;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;


@Component
@Service
public class TapestryReparseImpl implements TapestryReparse {
	
	
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
	
	}

	public void addInfo(TapestryMetadata meta) {
		this.data = meta;
		
	}
	
}
