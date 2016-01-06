package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class SAPSRFCDestination extends AbstractURICamelComponent {

	@Override
	public String getPaletteEntry() {
		return "SAP sRFC Destination";
	}

	@Override
	public String getUri() {
		return "sap-srfc-destination:destination:rfc";
	}

}
