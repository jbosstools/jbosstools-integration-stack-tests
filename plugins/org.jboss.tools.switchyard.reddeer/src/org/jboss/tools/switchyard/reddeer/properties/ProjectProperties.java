package org.jboss.tools.switchyard.reddeer.properties;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * 
 * @author apodhrad
 *
 */
public class ProjectProperties {

	private final Logger log = Logger.getLogger(ProjectProperties.class);

	public ProjectPropertiesJavaBuildPath selectJavaBuildPath() {
		log.info("Select 'Java Build Path' page.");
		new DefaultTreeItem("Java Build Path").select();
		return new ProjectPropertiesJavaBuildPath();
	}
}
