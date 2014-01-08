package org.jboss.tools.teiid.reddeer.manager;

import java.io.File;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.datatools.ui.FlatFileProfile;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileFlatFilePage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileSelectPage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileWizard;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.tools.teiid.reddeer.extensions.FlatFileProfileExt;

public class ConnectionProfileManager extends ConnectionProfileWizard{

	//no private attributes which would have to be set prior to usage!!
	//only simply usable methods, or constants
	//include descriptive help!!!
	//robust methods, split class if too large
	//move differentiation to lower level - wizard steps etc.
	
	public static String FLAT_FILE_DATA_SOURCE = "Flat File Data Source";
	/**
	 * Firstly, you should create new FlatFileProfileExt and set properties to it
	 * Create connection profile to a flat file data source
	 * 
	 */
	// presun do conn profile mgr
	public void createFlatFileProfileExt(FlatFileProfileExt flatProfile) {
		ConnectionProfileSelectPage selectPage = getFirstPage();
		selectPage.setConnectionProfile(FLAT_FILE_DATA_SOURCE);
		selectPage.setName(flatProfile.getName());

		next();

		ConnectionProfileFlatFilePage flatPage = (ConnectionProfileFlatFilePage) getSecondPage();

		if (flatProfile.getFolder() != null) {
			// TODO: LabeledText
			flatPage.setHomeFolder(flatProfile.getFolder());
		} else if (flatProfile.getURI() != null){
			new SWTWorkbenchBot().text().setText(new File(flatProfile.getFolder()).getAbsolutePath());//should be absolute path!						
		}
		if (flatProfile.isValidateHomeFolder() == false) {
			// switch off validation of home folder
			new CheckBox("Validate home folder").click();
		}

		flatPage.setCharset(flatProfile.getCharset());//not null, set in constructor
		flatPage.setStyle(flatProfile.getStyle());

		finish();
	}
		
}
