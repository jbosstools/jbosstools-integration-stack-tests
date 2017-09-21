package org.jboss.tools.switchyard.reddeer.properties;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.eclipse.ui.dialogs.PropertyDialog;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * 
 * @author apodhrad
 *
 */
public class ProjectProperties extends PropertyDialog {

	public ProjectProperties(String projetName) {
		super(projetName);
	}

	private final Logger log = Logger.getLogger(ProjectProperties.class);

	public ProjectPropertiesJavaBuildPath selectJavaBuildPath() {
		log.info("Select 'Java Build Path' page.");
		new DefaultTreeItem("Java Build Path").select();
		return new ProjectPropertiesJavaBuildPath();
	}
}
