package org.jboss.tools.fuse.ui.bot.test;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
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

/**
 * Tests relevant for SAP Tooling
 * 
 * @author apodhrad
 */
@SAP
@CleanWorkspace
@RunWith(RedDeerSuite.class)
public class SAPConnectionTest {

	@InjectRequirement
	private SAPRequirement sapRequirement;

	private SAPDestination sapDestination;
	private SAPServer sapServer;

	/**
	 * Prepares test environment
	 */
	@BeforeClass
	public static void setupOpenPropertiesView() {
		new WorkbenchShell().maximize();
		new PropertiesView().open();
	}

	/**
	 * Prepares test environment
	 */
	@Before
	public void setupInitSapVariables() {
		sapDestination = sapRequirement.getConfig().getDestination();
		sapServer = sapRequirement.getConfig().getServer();
	}

	/**
	 * <p>
	 * Tests SAP Connections view.
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>open SAP Connections view</li>
	 * <li>define a new destination</li>
	 * <li>check connection to the destination</li>
	 * <li>create a new server definition in SAP Connections view</li>
	 * <li>start the server</li>
	 * <li>check status of the server</li>
	 * <li>stop the server</li>
	 * <li>check status of the server</li>
	 * </ol>
	 */
	@Test
	public void testSAPConnection() {
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

	private class ContainsText extends AbstractWaitCondition {

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
