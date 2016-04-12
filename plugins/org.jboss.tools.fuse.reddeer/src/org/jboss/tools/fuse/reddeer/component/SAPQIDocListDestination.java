package org.jboss.tools.fuse.reddeer.component;

import static org.jboss.tools.fuse.reddeer.component.SAPLabels.APPLICATION_RELEASE;
import static org.jboss.tools.fuse.reddeer.component.SAPLabels.DESTINATION;
import static org.jboss.tools.fuse.reddeer.component.SAPLabels.IDOC_TYPE;
import static org.jboss.tools.fuse.reddeer.component.SAPLabels.IDOC_TYPE_EXTENSION;
import static org.jboss.tools.fuse.reddeer.component.SAPLabels.QUEUE;
import static org.jboss.tools.fuse.reddeer.component.SAPLabels.SYSTEM_RELEASE;

/**
 * 
 * @author apodhrad
 *
 */
public class SAPQIDocListDestination extends AbstractURICamelComponent {

	public SAPQIDocListDestination() {
		super("sap-qidoclist-destination");
		addProperty(DESTINATION, "destination");
		addProperty(QUEUE, "queue");
		addProperty(IDOC_TYPE, "idocType");
		addProperty(IDOC_TYPE_EXTENSION, "idocTypeExtension");
		addProperty(SYSTEM_RELEASE, "systemRelease");
		addProperty(APPLICATION_RELEASE, "applicationRelease");
	}

	@Override
	public String getPaletteEntry() {
		return "SAP Queued IDoc List Destination";
	}

}
