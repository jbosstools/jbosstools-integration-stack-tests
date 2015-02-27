package org.jboss.tools.switchyard.reddeer.properties;

import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * 
 * @author apodhrad
 *
 */
public class ProjectProperties extends PreferencePage {
	
	public ProjectPropertiesJavaBuildPath selectJavaBuildPath() {
		log.info("Select 'Java Build Path' page.");
		new DefaultTreeItem("Java Build Path").select();
		return new  ProjectPropertiesJavaBuildPath();
	}
}
