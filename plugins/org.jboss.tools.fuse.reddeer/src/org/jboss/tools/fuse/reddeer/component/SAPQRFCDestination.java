package org.jboss.tools.fuse.reddeer.component;


/**
 * 
 * @author apodhrad
 *
 */
public class SAPQRFCDestination extends AbstractURICamelComponent {

	@Override
	public String getPaletteEntry() {
		return "SAP qRFC Destination";
	}

	@Override
	public String getUri() {
		return "sap-qrfc-destination:destination:queue:rfc";
	}

}
