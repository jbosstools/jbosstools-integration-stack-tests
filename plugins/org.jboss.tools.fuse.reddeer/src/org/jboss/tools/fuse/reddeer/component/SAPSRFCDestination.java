package org.jboss.tools.fuse.reddeer.component;

import static org.jboss.tools.fuse.reddeer.component.SAPLabels.DESTINATION;
import static org.jboss.tools.fuse.reddeer.component.SAPLabels.RFC;

/**
 * 
 * @author apodhrad
 *
 */
public class SAPSRFCDestination extends AbstractURICamelComponent {

	public SAPSRFCDestination() {
		super("sap-srfc-destination");
		addProperty(DESTINATION, "destination");
		addProperty(RFC, "rfc");
	}

	@Override
	public String getPaletteEntry() {
		return "SAP sRFC Destination";
	}

}
