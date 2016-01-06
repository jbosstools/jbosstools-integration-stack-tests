package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class SAPIDocListDestination extends AbstractURICamelComponent {

	@Override
	public String getPaletteEntry() {
		return "SAP IDoc List Destination";
	}

	@Override
	public String getUri() {
		return "sap-idoclist-destination:destination:idocType:idocTypeExtension:systemRelease:applicationRelease";
	}

}
