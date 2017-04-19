package org.jboss.tools.fuse.reddeer.view;

import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.text.LabeledText;

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
		return new LabeledText("Gateway Host:");
	}

	public Text getGatewayPortText() {
		return new LabeledText("Gateway Port:");
	}

	public Text getProgramIDText() {
		return new LabeledText("Program ID:");
	}

	public Text getRepositoryDestinationText() {
		return new LabeledText("Repository Destination:");
	}

	public Text getConnectionCountText() {
		return new LabeledText("Connection Count:");
	}

}
