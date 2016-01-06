package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class SAPIDocListServer extends AbstractURICamelComponent {

	@Override
	public String getPaletteEntry() {
		return "SAP IDoc List Server";
	}

	@Override
	public String getUri() {
		return "sap-idoclist-server:destination:idocType:idocTypeExtension:systemRelease:applicationRelease";
	}

}
