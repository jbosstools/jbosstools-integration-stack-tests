package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class SAPQIDocDestination extends AbstractURICamelComponent {

	@Override
	public String getPaletteEntry() {
		return "SAP Queued IDoc Destination";
	}

	@Override
	public String getUri() {
		return "sap-qidoc-destination:destination:queue:idocType:idocTypeExtension:systemRelease:applicationRelease";
	}

}
