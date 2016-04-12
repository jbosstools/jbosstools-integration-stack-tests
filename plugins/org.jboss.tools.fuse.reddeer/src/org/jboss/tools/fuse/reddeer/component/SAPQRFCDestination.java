package org.jboss.tools.fuse.reddeer.component;

import static org.jboss.tools.fuse.reddeer.component.SAPLabels.DESTINATION;
import static org.jboss.tools.fuse.reddeer.component.SAPLabels.QUEUE;
import static org.jboss.tools.fuse.reddeer.component.SAPLabels.RFC;

/**
 * 
 * @author apodhrad
 *
 */
public class SAPQRFCDestination extends AbstractURICamelComponent {

	public SAPQRFCDestination() {
		super("sap-qrfc-destination");
		addProperty(DESTINATION, "destination");
		addProperty(QUEUE, "queue");
		addProperty(RFC, "rfc");
	}

	@Override
	public String getPaletteEntry() {
		return "SAP qRFC Destination";
	}

}
