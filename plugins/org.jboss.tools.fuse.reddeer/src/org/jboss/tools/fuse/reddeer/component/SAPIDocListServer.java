package org.jboss.tools.fuse.reddeer.component;

import static org.jboss.tools.fuse.reddeer.component.SAPLabels.APPLICATION_RELEASE;
import static org.jboss.tools.fuse.reddeer.component.SAPLabels.IDOC_TYPE;
import static org.jboss.tools.fuse.reddeer.component.SAPLabels.IDOC_TYPE_EXTENSION;
import static org.jboss.tools.fuse.reddeer.component.SAPLabels.SERVER;
import static org.jboss.tools.fuse.reddeer.component.SAPLabels.SYSTEM_RELEASE;;

/**
 * 
 * @author apodhrad
 *
 */
public class SAPIDocListServer extends AbstractURICamelComponent {

	public SAPIDocListServer() {
		super("sap-idoclist-server");
		addProperty(SERVER, "destination");
		addProperty(IDOC_TYPE, "idocType");
		addProperty(IDOC_TYPE_EXTENSION, "idocTypeExtension");
		addProperty(SYSTEM_RELEASE, "systemRelease");
		addProperty(APPLICATION_RELEASE, "applicationRelease");
	}

	@Override
	public String getPaletteEntry() {
		return "SAP IDoc List Server";
	}

}
