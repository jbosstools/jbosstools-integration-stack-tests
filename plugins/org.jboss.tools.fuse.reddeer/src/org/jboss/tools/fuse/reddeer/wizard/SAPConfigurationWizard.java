package org.jboss.tools.fuse.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.CCombo;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ccombo.LabeledCCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.fuse.reddeer.dialog.SAPTestDestinationDialog;
import org.jboss.tools.fuse.reddeer.dialog.SAPTestServerDialog;

/**
 * 
 * @author apodhrad
 *
 */
public class SAPConfigurationWizard extends WizardDialog {

	public static final String TITLE = "Edit SAP Connection Configuration";

	public void activate() {
		new DefaultShell(TITLE);
	}

	public void selectDestination(String name) {
		activate();
		new DefaultTreeItem("SAP Connection Configuration", "Destination Data Store", name).select();
	}

	public void addDestination(String name) {
		activate();
		getAddDestinationBTN().click();

		new DefaultShell("Create Destination");
		new LabeledText("Please provide a destination name").setText(name);
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Create Destination"));
	}

	public void deleteDestination(String name) {
		selectDestination(name);
		getDeleteBTN().click();
	}

	public List<String> getDestinations() {
		activate();
		List<String> destinations = new ArrayList<>();
		for (TreeItem item : new DefaultTreeItem("SAP Connection Configuration", "Destination Data Store").getItems()) {
			destinations.add(item.getText());
		}
		return destinations;
	}

	public void addServer(String name) {
		activate();
		getAddServerBTN().click();

		new DefaultShell("Create Server");
		new LabeledText("Please provide a server name").setText(name);
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Create Server"));
	}

	public void selectServer(String name) {
		activate();
		new DefaultTreeItem("SAP Connection Configuration", "Server Data Store", name).select();
	}

	public List<String> getServers() {
		activate();
		List<String> destinations = new ArrayList<>();
		for (TreeItem item : new DefaultTreeItem("SAP Connection Configuration", "Server Data Store").getItems()) {
			destinations.add(item.getText());
		}
		return destinations;
	}

	public void deleteServer(String name) {
		selectServer(name);
		getDeleteBTN().click();
	}

	public void selectTab(String tab) {
		new DefaultCTabItem(tab).activate();
	}

	public Button getAddDestinationBTN() {
		return new PushButton("Add Destination");
	}

	public Button getAddServerBTN() {
		return new PushButton("Add Server");
	}

	public SAPTestDestinationDialog openDestinationTestDialog(String destination) {
		selectDestination(destination);
		getTestBTN().click();
		return new SAPTestDestinationDialog();
	}

	public SAPTestServerDialog openServerTestDialog(String server) {
		selectServer(server);
		getTestBTN().click();
		return new SAPTestServerDialog();
	}

	public Button getTestBTN() {
		return new PushButton("Test");
	}

	public Button getDeleteBTN() {
		return new PushButton("Delete");
	}

	public Text getEditSAPDestinationandServerDataStoresTXT() {
		return new LabeledText("Edit SAP Destination and Server Data Stores");
	}

	public Text getRepositoryMapTXT() {
		return new LabeledText("Repository Map:");
	}

	public Text getMaximumStartupDelayTXT() {
		return new LabeledText("Maximum Startup Delay:");
	}

	public Text getMinimumWorkerThreadCountTXT() {
		return new LabeledText("Minimum Worker Thread Count:");
	}

	public Text getWorkerThreadCountTXT() {
		return new LabeledText("Worker Thread Count:");
	}

	public Text getSAPRouterStringTXT() {
		return new LabeledText("SAP Router String:");
	}

	public CheckBox getEnableRFCTraceCHB() {
		return new CheckBox("Enable RFC Trace?");
	}

	public Text getConnectionCountTXT() {
		return new LabeledText("Connection Count:");
	}

	public Text getProgramIDTXT() {
		return new LabeledText("Program ID:");
	}

	public Text getGatewayPortTXT() {
		return new LabeledText("Gateway Port:");
	}

	public Text getGatewayHostTXT() {
		return new LabeledText("Gateway Host:");
	}

	public CheckBox getUseRFC_METADATA_GETCHB() {
		return new CheckBox("Use RFC_METADATA_GET?");
	}

	public CheckBox getTurnOnSNCModeCHB() {
		return new CheckBox("Turn On SNC Mode?");
	}

	public CheckBox getTurnOnSNCModeforRepositoryDestinationCHB() {
		return new CheckBox("Turn On SNC Mode for Repository Destination?");
	}

	public Text getRepositoryLogonPasswordTXT() {
		return new LabeledText("Repository Logon Password:");
	}

	public Text getRepositoryLogonUserTXT() {
		return new LabeledText("Repository Logon User:");
	}

	public Text getRepositoryDestinationTXT() {
		return new LabeledText("Repository Destination:");
	}

	public Text getSNCPartnerNameTXT() {
		return new LabeledText("SNC Partner Name:");
	}

	public Text getSNCNameTXT() {
		return new LabeledText("SNC Name:");
	}

	public Text getSNCLibraryPathTXT() {
		return new LabeledText("SNC Library Path:");
	}

	public Text getConnectionPoolMaxGetClientTimeTXT() {
		return new LabeledText("Connection Pool Max Get Client Time:");
	}

	public Text getConnectionPoolExpireCheckPeriodTXT() {
		return new LabeledText("Connection Pool Expire Check Period:");
	}

	public Text getConnectionPoolExpirationTimeTXT() {
		return new LabeledText("Connection Pool Expiration Time:");
	}

	public Text getConnectionPoolCapacityTXT() {
		return new LabeledText("Connection Pool Capacity:");
	}

	public Text getConnectionPoolPeakLimitTXT() {
		return new LabeledText("Connection Pool Peak Limit:");
	}

	public Button getDenyUseofInitialPasswords() {
		return new CheckBox("Deny Use of Initial Passwords?");
	}

	public CheckBox getReqeustSSOTicketCHB() {
		return new CheckBox("Reqeust SSO Ticket?");
	}

	public Text getInitialCodepage() {
		return new LabeledText("Initial Codepage:");
	}

	public CheckBox getEnableLogonCheckCHB() {
		return new CheckBox("Enable Logon Check?");
	}

	public Text getLogonLanguageTXT() {
		return new LabeledText("Logon Language:");
	}

	public Text getSAPX509LoginTicketTXT() {
		return new LabeledText("SAP X509 Login Ticket:");
	}

	public Text getSAPSSOLogonTicketTXT() {
		return new LabeledText("SAP SSO Logon Ticket:");
	}

	public Text getLogonPasswordTXT() {
		return new LabeledText("Logon Password:");
	}

	public Text getLogonUserAliasTXT() {
		return new LabeledText("Logon User Alias:");
	}

	public Text getLogonUserTXT() {
		return new LabeledText("Logon User:");
	}

	public Text getSAPApplicationServerGroupTXT() {
		return new LabeledText("SAP Application Server Group:");
	}

	public Text getSAPSystemIDTXT() {
		return new LabeledText("SAP System ID:");
	}

	public Text getSAPMessageServerPortTXT() {
		return new LabeledText("SAP Message Server Port:");
	}

	public Text getSAPMessageServerTXT() {
		return new LabeledText("SAP Message Server:");
	}

	public Text getSAPSystemNumberTXT() {
		return new LabeledText("SAP System Number:");
	}

	public Text getSAPClientTXT() {
		return new LabeledText("SAP Client:");
	}

	public Text getSAPApplicationServerTXT() {
		return new LabeledText("SAP Application Server:");
	}

	public CCombo getSAPApplicationTypeCMB() {
		return new LabeledCCombo("SAP Authentication Type:");
	}

	public CCombo getSelectCPICTraceCMB() {
		return new LabeledCCombo("Select CPIC Trace:");
	}

	public CCombo getSNCLevelOfSecurityCMB() {
		return new LabeledCCombo("SNC Level of Security:");
	}

}
