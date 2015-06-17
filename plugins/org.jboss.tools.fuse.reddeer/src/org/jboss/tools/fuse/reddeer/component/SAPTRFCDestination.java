package org.jboss.tools.fuse.reddeer.component;


/**
 * 
 * @author apodhrad
 *
 */
public class SAPTRFCDestination extends AbstractURICamelComponent {

	@Override
	public String getPaletteEntry() {
		return "SAP tRFC Destination";
	}

	@Override
	public String getUri() {
		return "sap-trfc-destination:destination:rfc";
	}

}
