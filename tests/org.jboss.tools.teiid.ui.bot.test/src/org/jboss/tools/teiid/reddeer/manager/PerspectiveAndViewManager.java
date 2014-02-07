package org.jboss.tools.teiid.reddeer.manager;

import org.jboss.reddeer.eclipse.ui.perspectives.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;

public class PerspectiveAndViewManager {

	public void openTeiidDesignerPerspective(){
		TeiidPerspective.getInstance().open();
	}
	
	public void openDatabaseDevelopmentPerspective(){
		new DatabaseDevelopmentPerspective().open();
	}
	
}
