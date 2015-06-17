package org.jboss.tools.fuse.reddeer.component;


/**
 * 
 * @author apodhrad
 *
 */
public class SAPSRFCServer extends AbstractURICamelComponent {

	@Override
	public String getPaletteEntry() {
		return "SAP sRFC Server";
	}

	@Override
	public String getUri() {
		return "sap-srfc-server:server:rfc";
	}

}
