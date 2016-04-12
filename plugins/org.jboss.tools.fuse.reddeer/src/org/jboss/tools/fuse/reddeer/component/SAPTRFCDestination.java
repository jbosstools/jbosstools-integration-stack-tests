package org.jboss.tools.fuse.reddeer.component;

import static org.jboss.tools.fuse.reddeer.component.SAPLabels.DESTINATION;
import static org.jboss.tools.fuse.reddeer.component.SAPLabels.RFC;

/**
 * 
 * @author apodhrad
 *
 */
public class SAPTRFCDestination extends AbstractURICamelComponent {

	public SAPTRFCDestination() {
		super("sap-trfc-destination");
		addProperty(DESTINATION, "destination");
		addProperty(RFC, "rfc");
	}

	@Override
	public String getPaletteEntry() {
		return "SAP tRFC Destination";
	}

}
