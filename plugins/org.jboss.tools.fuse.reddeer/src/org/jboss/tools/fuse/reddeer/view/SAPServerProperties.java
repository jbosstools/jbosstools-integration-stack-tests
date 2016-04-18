package org.jboss.tools.fuse.reddeer.view;

import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.tools.common.reddeer.widget.CLabeledText;

/**
 * 
 * @author apodhrad
 *
 */
public class SAPServerProperties extends PropertiesView {

	public void selectMandatory() {
		activate();
		selectTab("Mandatory");
	}

	public void selectOptional() {
		activate();
		selectTab("Optional");
	}

	public void selectSNC() {
		activate();
		selectTab("SNC");
	}

	public Text getGatewayHostText() {
		return new CLabeledText("Gateway Host:");
	}

	public Text getGatewayPortText() {
		return new CLabeledText("Gatway Port:");
	}

	public Text getProgramIDText() {
		return new CLabeledText("Program ID:");
	}

	public Text getRepositoryDestinationText() {
		return new CLabeledText("Repository Destination:");
	}

	public Text getConnectionCountText() {
		return new CLabeledText("Connection Count:");
	}

}
