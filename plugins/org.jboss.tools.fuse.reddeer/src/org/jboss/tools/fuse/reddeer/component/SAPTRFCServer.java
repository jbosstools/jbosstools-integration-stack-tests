package org.jboss.tools.fuse.reddeer.component;


/**
 * 
 * @author apodhrad
 *
 */
public class SAPTRFCServer extends AbstractURICamelComponent {
	@Override
	public String getPaletteEntry() {
		return "SAP tRFC Server";
	}

	@Override
	public String getUri() {
		return "sap-trfc-server:server:rfc";
	}

}
