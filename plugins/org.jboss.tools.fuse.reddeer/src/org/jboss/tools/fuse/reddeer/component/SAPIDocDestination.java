package org.jboss.tools.fuse.reddeer.component;

/**
 * 
 * @author apodhrad
 *
 */
public class SAPIDocDestination extends AbstractURICamelComponent {

	@Override
	public String getPaletteEntry() {
		return "SAP IDoc Destination";
	}

	@Override
	public String getUri() {
		return "sap-idoc-destination:destination:idocType:idocTypeExtension:systemRelease:applicationRelease";
	}

}
