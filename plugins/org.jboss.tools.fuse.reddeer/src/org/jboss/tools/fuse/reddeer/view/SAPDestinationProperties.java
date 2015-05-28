package org.jboss.tools.fuse.reddeer.view;

import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.tools.fuse.reddeer.widget.CLabeledText;

/**
 * 
 * @author apodhrad
 *
 */
public class SAPDestinationProperties extends PropertiesView {

	public void selectBasic() {
		activate();
		selectTab("Basic");
	}

	public void selectConnection() {
		activate();
		selectTab("Connection");
	}

	public Text getSAPApplicationServerText() {
		return new CLabeledText("SAP Application Server:");
	}

	public Text getSAPSystemNumberText() {
		return new CLabeledText("SAP System Number:");
	}

	public Text getSAPClientText() {
		return new CLabeledText("SAP Client:");
	}

	public Text getLogonUserText() {
		return new CLabeledText("Logon User:");
	}

	public Text getLogonPasswordText() {
		return new CLabeledText("Logon Password:");
	}

	public Text getLogonLanguageText() {
		return new CLabeledText("Logon Language:");
	}

}
