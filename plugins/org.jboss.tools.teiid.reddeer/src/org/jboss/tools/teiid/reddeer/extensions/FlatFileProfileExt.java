package org.jboss.tools.teiid.reddeer.extensions;

import org.jboss.reddeer.eclipse.datatools.ui.FlatFileProfile;

public class FlatFileProfileExt extends FlatFileProfile {

	//FlatFileProfile:
	//name = connection profile name
	//home folder | URI
	//validate home folder - may cause problems if switched on
	
	private String URI;//file location
	private boolean validateHomeFolder = false;
	
	public void setURI(String URI){
		this.URI = URI;
	}
	
	public String getURI(){
		return this.URI;
	}
	
	public boolean isValidateHomeFolder() {
		return validateHomeFolder;
	}

	public void setValidateHomeFolder(boolean validateHomeFolder) {
		this.validateHomeFolder = validateHomeFolder;
	}
	
	public FlatFileProfileExt(){
		if (super.getCharset() == null){
			this.setCharset("UTF-8");
		}
		if (super.getStyle() == null){
			this.setStyle("CSV");
		}
	}
	
	/*public FlatFileProfile createWithFolder(String name, String folder){
		FlatFileProfile flatProfile = new FlatFileProfile();
		flatProfile.setName(name);
		flatProfile.setFolder(folder);
		flatProfile.setCharset("UTF-8");
		flatProfile.setStyle("CSV");
		return flatProfile;
	}
	
	public FlatFileProfileExt createWithURI(String name, String URI){
		FlatFileProfileExt flatProfile = new FlatFileProfileExt();
		flatProfile.setName(name);
		flatProfile.setURI(URI);
		flatProfile.setCharset("UTF-8");
		flatProfile.setStyle("CSV");
		return flatProfile;
	}*/
	
	/*public FlatFileProfileExt create(){
		FlatFileProfileExt flatProfile = new FlatFileProfileExt();
		//copy all attributes from parent, return extended profile 
		if (super.getName() != null){
			flatProfile.setName(super.getName());
		}
		if (super.getCharset() != null){
			flatProfile.setCharset(super.getCharset());
		} else {
			
		}
		if (super.getFolder() != null){
			flatProfile.setFolder(super.getFolder());
		}
		if (super.getStyle() != null){
			flatProfile.setStyle(super.getStyle());
		}
		if (URI != null){
			flatProfile.setURI(URI);
		}
		
		return flatProfile;
	}*/

	
	
	
	
	
	
}
