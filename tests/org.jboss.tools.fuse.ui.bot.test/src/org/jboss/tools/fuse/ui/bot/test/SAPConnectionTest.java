package org.jboss.tools.fuse.ui.bot.test;

import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.fuse.reddeer.view.SAPConnectionView;
import org.jboss.tools.fuse.reddeer.view.SAPConnectionView.TestDestinationConnection;
import org.jboss.tools.fuse.reddeer.view.SAPConnectionView.TestServerConnection;
import org.jboss.tools.fuse.reddeer.view.SAPDestinationProperties;
import org.jboss.tools.fuse.reddeer.view.SAPServerProperties;
import org.jboss.tools.runtime.reddeer.impl.SAPDestination;
import org.jboss.tools.runtime.reddeer.impl.SAPServer;
import org.jboss.tools.runtime.reddeer.requirement.SAPRequirement;
import org.jboss.tools.runtime.reddeer.requirement.SAPRequirement.SAP;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@SAP
@CleanWorkspace
@RunWith(RedDeerSuite.class)
public class SAPConnectionTest {

	@InjectRequirement
	private SAPRequirement sapRequirement;

	private SAPDestination sapDestination;
	private SAPServer sapServer;

	@BeforeClass
	public static void openPropertiesView() {
		new WorkbenchShell().maximize();
		new PropertiesView().open();
	}

	@Before
	public void initSapVariables() {
		sapDestination = sapRequirement.getConfig().getDestination();
		sapServer = sapRequirement.getConfig().getServer();
	}

	@Test
	public void sapConnectionTest() {
		String destination = "destinationTest";
		String server = "serverTest";

		SAPConnectionView sapConnectionView = new SAPConnectionView();
		sapConnectionView.open();
		sapConnectionView.newDestination(destination);

		SAPDestinationProperties sapDestinationProperties = sapConnectionView.openDestinationProperties(destination);
		sapDestinationProperties.getSAPApplicationServerText().setText(sapDestination.getAshost());
		sapDestinationProperties.getSAPSystemNumberText().setText(sapDestination.getSysnr());
		sapDestinationProperties.getSAPClientText().setText(sapDestination.getClient());
		sapDestinationProperties.getLogonUserText().setText(sapDestination.getUser());
		sapDestinationProperties.getLogonPasswordText().setText(sapDestination.getPassword());
		sapDestinationProperties.getLogonLanguageText().setText(sapDestination.getLang());

		TestDestinationConnection testDestinationConnection = sapConnectionView.openDestinationTest(destination);
		testDestinationConnection.test();
		String expected = "Connection test for destination '" + destination + "' succeeded.";
		new WaitUntil(new ContainsText(testDestinationConnection.getResultText(), expected));
		testDestinationConnection.clear();
		testDestinationConnection.close();

		sapConnectionView.open();
		sapConnectionView.newServer(server);

		SAPServerProperties sapServerProperties = sapConnectionView.openServerProperties(server);
		sapServerProperties.getGatewayHostText().setText(sapServer.getGwhost());
		sapServerProperties.getGatewayPortText().setText("3300");
		sapServerProperties.getProgramIDText().setText(sapServer.getProgid());
		sapServerProperties.getRepositoryDestinationText().setText(destination);
		sapServerProperties.getConnectionCountText().setText(sapServer.getConnectionCount());

		TestServerConnection testServerConnection = sapConnectionView.openServerTest(server);
		testServerConnection.start();
		new WaitUntil(new ContainsText(testServerConnection.getResultText(), "Server state: STARTED"));
		new WaitUntil(new ContainsText(testServerConnection.getResultText(), "Server state: ALIVE"));
		testServerConnection.stop();
		new WaitUntil(new ContainsText(testServerConnection.getResultText(), "Server state: STOPPED"));
		testServerConnection.clear();
		testServerConnection.close();
	}
	
	// TODO Write test for exporting, wait for https://issues.jboss.org/browse/FUSETOOLS-1374

	private class ContainsText implements WaitCondition {

		private Text text;
		private String actualText;
		private String expectedText;

		public ContainsText(Text text, String expectedText) {
			this.text = text;
			this.expectedText = expectedText;
		}

		@Override
		public boolean test() {
			actualText = text.getText();
			return actualText.contains(expectedText);
		}

		@Override
		public String description() {
			return "Expected '" + expectedText + "' but was '" + actualText + "'";
		}

	}

}
