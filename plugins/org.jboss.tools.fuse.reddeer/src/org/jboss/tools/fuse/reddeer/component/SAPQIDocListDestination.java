package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class SAPQIDocListDestination extends AbstractURICamelComponent {

	@Override
	public String getPaletteEntry() {
		return "SAP Queued IDoc List Destination";
	}

	@Override
	public String getUri() {
		return "sap-qidoclist-destination:destination:queue:idocType:idocTypeExtension:systemRelease:applicationRelease";
	}

}
