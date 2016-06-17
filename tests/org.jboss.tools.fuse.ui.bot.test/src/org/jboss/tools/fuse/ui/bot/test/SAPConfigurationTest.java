package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.XPathEvaluator;
import org.jboss.tools.fuse.reddeer.ProjectType;
import org.jboss.tools.fuse.reddeer.condition.ContainsText;
import org.jboss.tools.fuse.reddeer.dialog.SAPTestDestinationDialog;
import org.jboss.tools.fuse.reddeer.dialog.SAPTestServerDialog;
import org.jboss.tools.fuse.reddeer.editor.ConfigurationsEditor;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.reddeer.wizard.SAPConfigurationWizard;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.impl.SAPDestination;
import org.jboss.tools.runtime.reddeer.impl.SAPServer;
import org.jboss.tools.runtime.reddeer.requirement.SAPRequirement;
import org.jboss.tools.runtime.reddeer.requirement.SAPRequirement.SAP;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

/**
 * Tests relevant for SAP Tooling
 * 
 * @author apodhrad
 */
@SAP
@RunWith(RedDeerSuite.class)
@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
public class SAPConfigurationTest {

	public static final String PROJECT_NAME = "sap_config";
	public static final String CONFIGURATION_IMPL = "org.fusesource.camel.component.sap.SapConnectionConfiguration";
	public static final String DESTINATION_IMPL = "org.fusesource.camel.component.sap.model.rfc.impl.DestinationDataImpl";
	public static final String SERVER_IMPL = "org.fusesource.camel.component.sap.model.rfc.impl.ServerDataImpl";

	@InjectRequirement
	private SAPRequirement sapRequirement;

	private SAPDestination sapDestination;
	private SAPServer sapServer;

	@Parameters(name = "{0}")
	public static Collection<ProjectType> data() {
		return Arrays.asList(new ProjectType[] { ProjectType.SPRING, ProjectType.BLUEPRINT });
	}

	private ProjectType type;

	public SAPConfigurationTest(ProjectType type) {
		this.type = type;
		new WorkbenchShell().maximize();
		new CleanWorkspaceRequirement().fulfill();
		ProjectFactory.newProject(PROJECT_NAME).type(type).create();
	}

	private ConfigurationsEditor editor() {
		return new ConfigurationsEditor(PROJECT_NAME, type.getCamelContext());
	}

	/**
	 * Prepare test environment
	 */
	@Before
	public void setupInitSapVariables() {
		sapDestination = sapRequirement.getConfig().getDestination();
		sapServer = sapRequirement.getConfig().getServer();
	}

	/**
	 * Delete any existing SAP configuration
	 */
	@Before
	public void deleteSapConfiguration() {
		try {
			editor().deleteSapConfig();
		} catch (Exception e) {
			// ok
		}
	}

	/**
	 * <p>
	 * Tests adding and removing SAP destinations in Configurations editor.
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>open Configurations editor</li>
	 * <li>add 2 new destinations</li>
	 * <li>check the xml file</li>
	 * <li>remove the first destination</li>
	 * <li>check the xml file</li>
	 * <li>remove the second destination</li>
	 * <li>check the xml file</li>
	 * </ol>
	 */
	@Test
	public void testAddingAndDeletingDestinations() throws Exception {
		String destinationName1 = "myDestination1";
		String destinationName2 = "myDestination2";

		SAPConfigurationWizard wizard = editor().addSapConfig();
		wizard.addDestination(destinationName1);
		wizard.addDestination(destinationName2);

		Collection<String> destinations = wizard.getDestinations();
		assertTrue(destinations.contains(destinationName1));
		assertTrue(destinations.contains(destinationName2));
		assertEquals(2, destinations.size());

		wizard.finish();
		editor().save();

		assertXPath(CONFIGURATION_IMPL, configurationQuery() + "/@class");
		assertXPath(DESTINATION_IMPL, destinationQuery(destinationName1) + "/@class");
		assertXPath(DESTINATION_IMPL, destinationQuery(destinationName2) + "/@class");

		wizard = editor().editSapConfig();
		wizard.deleteDestination(destinationName1);

		destinations = wizard.getDestinations();
		assertTrue(destinations.contains(destinationName2));
		assertEquals(1, destinations.size());

		wizard.finish();
		editor().save();

		assertXPath(CONFIGURATION_IMPL, configurationQuery() + "/@class");
		assertXPath("", destinationQuery(destinationName1) + "/@class");
		assertXPath(DESTINATION_IMPL, destinationQuery(destinationName2) + "/@class");

		wizard = editor().editSapConfig();
		wizard.deleteDestination(destinationName2);

		destinations = wizard.getDestinations();
		assertTrue(destinations.isEmpty());

		wizard.finish();
		editor().save();

		assertXPath(CONFIGURATION_IMPL, configurationQuery() + "/@class");
		assertXPath("", destinationQuery(destinationName1) + "/@class");
		assertXPath("", destinationQuery(destinationName2) + "/@class");
	}

	/**
	 * <p>
	 * Tests adding and removing SAP destinations in Configurations editor.
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>open Configurations editor</li>
	 * <li>add 2 new destinations</li>
	 * <li>check the xml file</li>
	 * <li>remove the first destination</li>
	 * <li>check the xml file</li>
	 * <li>remove the second destination</li>
	 * <li>check the xml file</li>
	 * </ol>
	 */
	@Test
	public void testAddingAndDeletingServers() throws Exception {
		String serverName1 = "myServer1";
		String serverName2 = "myServer2";

		SAPConfigurationWizard wizard = editor().addSapConfig();
		wizard.addServer(serverName1);
		wizard.addServer(serverName2);

		Collection<String> servers = wizard.getServers();
		assertTrue(servers.contains(serverName1));
		assertTrue(servers.contains(serverName2));
		assertEquals(2, servers.size());

		wizard.finish();
		editor().save();

		assertXPath(CONFIGURATION_IMPL, configurationQuery() + "/@class");
		assertXPath(SERVER_IMPL, serverQuery(serverName1) + "/@class");
		assertXPath(SERVER_IMPL, serverQuery(serverName2) + "/@class");

		wizard = editor().editSapConfig();
		wizard.deleteServer(serverName1);

		servers = wizard.getServers();
		assertTrue(servers.contains(serverName2));
		assertEquals(1, servers.size());

		wizard.finish();
		editor().save();

		assertXPath(CONFIGURATION_IMPL, configurationQuery() + "/@class");
		assertXPath("", serverQuery(serverName1) + "/@class");
		assertXPath(SERVER_IMPL, serverQuery(serverName2) + "/@class");

		wizard = editor().editSapConfig();
		wizard.deleteServer(serverName2);

		servers = wizard.getServers();
		assertTrue(servers.isEmpty());

		wizard.finish();
		editor().save();

		assertXPath(CONFIGURATION_IMPL, configurationQuery() + "/@class");
		assertXPath("", serverQuery(serverName1) + "/@class");
		assertXPath("", serverQuery(serverName2) + "/@class");
	}

	/**
	 * <p>
	 * Tests all properties of SAP destination in Configurations editor.
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>open Configurations editor</li>
	 * <li>add a new destination</li>
	 * <li>set all properties</li>
	 * <li>save and check the xml file</li>
	 * </ol>
	 */
	@Test
	public void testDestinationProperties() throws Exception {
		String destinationName = "myDestination";
		ConfigurationsEditor editor = editor();

		SAPConfigurationWizard wizard = editor.addSapConfig();
		wizard.addDestination(destinationName);
		wizard.selectDestination(destinationName);

		/* Basic */
		wizard.selectTab("Basic");
		wizard.getSAPApplicationServerTXT().setText("sap.example.com");
		wizard.getSAPSystemNumberTXT().setText("12");
		wizard.getSAPClientTXT().setText("123");
		wizard.getLogonUserTXT().setText("admin");
		wizard.getLogonPasswordTXT().setText("admin123$");
		wizard.getLogonLanguageTXT().setText("xy");

		/* Connection */
		wizard.selectTab("Connection");
		assertEquals("12", wizard.getSAPSystemNumberTXT().getText());
		wizard.getSAPRouterStringTXT().setText("/H/xyz/S/123/W/abc");
		assertEquals("sap.example.com", wizard.getSAPApplicationServerTXT().getText());
		wizard.getSAPMessageServerTXT().setText("sap-msg.example.com");
		wizard.getSAPMessageServerPortTXT().setText("1234");
		wizard.getGatewayHostTXT().setText("gt.example.com");
		wizard.getGatewayPortTXT().setText("4321");
		wizard.getSAPSystemIDTXT().setText("AB");
		wizard.getSAPApplicationServerGroupTXT().setText("myGroup");

		/* Authentication */
		wizard.selectTab("Authentication");
		wizard.getSAPApplicationTypeCMB().setSelection("CONFIGURED_USER");
		wizard.getSAPApplicationTypeCMB().setSelection("CURRENT_USER");
		assertEquals("123", wizard.getSAPClientTXT().getText());
		assertEquals("admin", wizard.getLogonUserTXT().getText());
		wizard.getLogonUserAliasTXT().setText("superadmin");
		assertEquals("admin123$", wizard.getLogonPasswordTXT().getText());
		wizard.getSAPSSOLogonTicketTXT().setText("sso");
		wizard.getSAPX509LoginTicketTXT().setText("x509");
		assertEquals("xy", wizard.getLogonLanguageTXT().getText());

		/* Special */
		wizard.selectTab("Special");
		wizard.getEnableRFCTraceCHB().toggle(true);
		wizard.getSelectCPICTraceCMB().setSelection("Trace Level 0: No Trace");
		wizard.getSelectCPICTraceCMB().setSelection("Trace Level 1: Only Errors are Logged");
		wizard.getSelectCPICTraceCMB().setSelection("Trace Level 2: Flow and Basic Data Trace");
		wizard.getSelectCPICTraceCMB().setSelection("Trace Level 3: Flow and Complete Data Trace");
		wizard.getEnableLogonCheckCHB().toggle(true);
		wizard.getInitialCodepage().setText("init");
		wizard.getReqeustSSOTicketCHB().toggle(true);

		/* Pool */
		wizard.selectTab("Pool");
		wizard.getConnectionPoolPeakLimitTXT().setText("10");
		wizard.getConnectionPoolCapacityTXT().setText("11");
		wizard.getConnectionPoolExpirationTimeTXT().setText("12");
		wizard.getConnectionPoolExpireCheckPeriodTXT().setText("13");
		wizard.getConnectionPoolMaxGetClientTimeTXT().setText("14");

		/* SNC */
		wizard.selectTab("SNC");
		wizard.getTurnOnSNCModeCHB().toggle(true);
		wizard.getSNCPartnerNameTXT().setText("snc-partner");
		wizard.getSNCLevelOfSecurityCMB().setSelection("Security Level 1: Secure Authentication Only");
		wizard.getSNCLevelOfSecurityCMB().setSelection("Security Level 2: Data Integrity Protection");
		wizard.getSNCLevelOfSecurityCMB().setSelection("Security Level 3: Data Privacy Protection");
		wizard.getSNCLevelOfSecurityCMB().setSelection("Security Level 8: Default Protection");
		wizard.getSNCLevelOfSecurityCMB().setSelection("Security Level 9: Maximum Protection");
		wizard.getSNCNameTXT().setText("snc-name");
		wizard.getSNCLibraryPathTXT().setText("snc-path");

		/* Repository */
		wizard.selectTab("Repository");
		wizard.getRepositoryDestinationTXT().setText("repo");
		wizard.getRepositoryLogonUserTXT().setText("user");
		wizard.getRepositoryLogonPasswordTXT().setText("user123$");
		wizard.getTurnOnSNCModeforRepositoryDestinationCHB().toggle(true);
		wizard.getUseRFC_METADATA_GETCHB().toggle(true);

		wizard.finish();
		editor.save();

		assertXPath(CONFIGURATION_IMPL, configurationQuery() + "/@class");
		assertXPath(DESTINATION_IMPL, destinationQuery(destinationName) + "/@class");
		assertDestinationProperty(destinationName, "aliasUser", "superadmin");
		assertDestinationProperty(destinationName, "ashost", "sap.example.com");
		assertDestinationProperty(destinationName, "authType", "CURRENT_USER");
		assertDestinationProperty(destinationName, "client", "123");
		assertDestinationProperty(destinationName, "codepage", "init");
		assertDestinationProperty(destinationName, "cpicTrace", "3");
		assertDestinationProperty(destinationName, "denyInitialPassword", "0");
		assertDestinationProperty(destinationName, "expirationPeriod", "13");
		assertDestinationProperty(destinationName, "expirationTime", "12");
		assertDestinationProperty(destinationName, "getsso2", "1");
		assertDestinationProperty(destinationName, "group", "myGroup");
		assertDestinationProperty(destinationName, "gwhost", "gt.example.com");
		assertDestinationProperty(destinationName, "gwserv", "4321");
		assertDestinationProperty(destinationName, "lang", "xy");
		assertDestinationProperty(destinationName, "lcheck", "1");
		assertDestinationProperty(destinationName, "maxGetTime", "14");
		assertDestinationProperty(destinationName, "mshost", "sap-msg.example.com");
		assertDestinationProperty(destinationName, "msserv", "1234");
		assertDestinationProperty(destinationName, "mysapsso2", "sso");
		assertDestinationProperty(destinationName, "passwd", "admin123$");
		assertDestinationProperty(destinationName, "password", "admin123$");
		assertDestinationProperty(destinationName, "peakLimit", "10");
		assertDestinationProperty(destinationName, "pingOnCreate", "false");
		assertDestinationProperty(destinationName, "poolCapacity", "11");
		assertDestinationProperty(destinationName, "r3name", "AB");
		assertDestinationProperty(destinationName, "repositoryDest", "repo");
		assertDestinationProperty(destinationName, "repositoryPasswd", "user123$");
		assertDestinationProperty(destinationName, "repositoryRoundtripOptimization", "1");
		assertDestinationProperty(destinationName, "repositorySnc", "1");
		assertDestinationProperty(destinationName, "repositoryUser", "user");
		assertDestinationProperty(destinationName, "saprouter", "/H/xyz/S/123/W/abc");
		assertDestinationProperty(destinationName, "sncLibrary", "snc-path");
		assertDestinationProperty(destinationName, "sncMode", "1");
		assertDestinationProperty(destinationName, "sncMyname", "snc-name");
		assertDestinationProperty(destinationName, "sncPartnername", "snc-partner");
		assertDestinationProperty(destinationName, "sncQop", "9");
		assertDestinationProperty(destinationName, "sysnr", "12");
		assertDestinationProperty(destinationName, "trace", "1");
		assertDestinationProperty(destinationName, "userName", "admin");
		assertDestinationProperty(destinationName, "user", "admin");
		assertDestinationProperty(destinationName, "x509cert", "x509");
	}

	/**
	 * <p>
	 * Tests all properties of SAP server in Configurations editor.
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>open Configurations editor</li>
	 * <li>add a new server</li>
	 * <li>set all properties</li>
	 * <li>save and check the xml file</li>
	 * </ol>
	 */
	@Test
	public void testServerProperties() throws Exception {
		String serverName = "myServer";
		ConfigurationsEditor editor = editor();

		SAPConfigurationWizard wizard = editor.addSapConfig();
		wizard.addServer(serverName);
		wizard.selectServer(serverName);

		/* Mandatory */
		wizard.selectTab("Mandatory");
		wizard.getGatewayHostTXT().setText("host.example.com");
		wizard.getGatewayPortTXT().setText("3333");
		wizard.getProgramIDTXT().setText("FOO");
		wizard.getRepositoryDestinationTXT().setText("myDest");
		wizard.getConnectionCountTXT().setText("3");

		/* Optional */
		wizard.selectTab("Optional");
		wizard.getEnableRFCTraceCHB().toggle(true);
		wizard.getSAPRouterStringTXT().setText("/H/abc/S/321/W/xyz");
		wizard.getWorkerThreadCountTXT().setText("10");
		wizard.getMinimumWorkerThreadCountTXT().setText("11");
		wizard.getMaximumStartupDelayTXT().setText("12");
		wizard.getRepositoryMapTXT().setText("map");

		/* SNC */
		wizard.selectTab("SNC");
		wizard.getTurnOnSNCModeCHB().toggle(true);
		wizard.getSNCLevelOfSecurityCMB().setSelection("Security Level 1: Secure Authentication Only");
		wizard.getSNCLevelOfSecurityCMB().setSelection("Security Level 2: Data Integrity Protection");
		wizard.getSNCLevelOfSecurityCMB().setSelection("Security Level 3: Data Privacy Protection");
		wizard.getSNCLevelOfSecurityCMB().setSelection("Security Level 8: Default Protection");
		wizard.getSNCLevelOfSecurityCMB().setSelection("Security Level 9: Maximum Protection");
		wizard.getSNCNameTXT().setText("snc-name");
		wizard.getSNCLibraryPathTXT().setText("snc-path");

		wizard.finish();
		editor.save();

		assertXPath(CONFIGURATION_IMPL, configurationQuery() + "/@class");
		assertXPath(SERVER_IMPL, serverQuery(serverName) + "/@class");
		assertServerProperty(serverName, "gwhost", "host.example.com");
		assertServerProperty(serverName, "gwserv", "3333");
		assertServerProperty(serverName, "progid", "FOO");
		assertServerProperty(serverName, "connectionCount", "3");
		assertServerProperty(serverName, "saprouter", "/H/abc/S/321/W/xyz");
		assertServerProperty(serverName, "maxStartUpDelay", "12");
		assertServerProperty(serverName, "repositoryDestination", "myDest");
		assertServerProperty(serverName, "repositoryMap", "map");
		assertServerProperty(serverName, "workerThreadCount", "10");
		assertServerProperty(serverName, "workerThreadMinCount", "11");
		assertServerProperty(serverName, "sncQop", "9");
		assertServerProperty(serverName, "sncMyname", "snc-name");
		assertServerProperty(serverName, "sncLib", "snc-path");
	}

	/**
	 * <p>
	 * Tests a connection of SAP destination and server in Configurations editor.
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>open Configurations editor</li>
	 * <li>add a new destination</li>
	 * <li>set basic properties</li>
	 * <li>test the connection</li>
	 * <li>add a new server</li>
	 * <li>set mandatory properties</li>
	 * <li>test the connection</li>
	 * </ol>
	 */
	@Test
	public void testConnectionOfDestinationAndServer() {
		SAPConfigurationWizard wizard = editor().addSapConfig();
		wizard.addDestination(sapDestination.getName());
		wizard.selectDestination(sapDestination.getName());
		wizard.selectTab("Connection");
		wizard.selectTab("Basic");
		wizard.getSAPApplicationServerTXT().setText(sapDestination.getAshost());
		wizard.getSAPSystemNumberTXT().setText(sapDestination.getSysnr());
		wizard.getSAPClientTXT().setText(sapDestination.getClient());
		wizard.getLogonUserTXT().setText(sapDestination.getUser());
		wizard.getLogonPasswordTXT().setText(sapDestination.getPassword());

		SAPTestDestinationDialog destinationDialog = wizard.openDestinationTestDialog(sapDestination.getName());
		String expected = "Connection test for destination '" + sapDestination.getName() + "' succeeded.";
		assertEquals(expected, destinationDialog.test());
		destinationDialog.close();

		wizard.addServer(sapServer.getName());
		wizard.selectServer(sapServer.getName());
		wizard.getGatewayHostTXT().setText(sapServer.getGwhost());
		wizard.getGatewayPortTXT().setText(sapServer.getGwport());
		wizard.getProgramIDTXT().setText(sapServer.getProgid());
		wizard.getRepositoryDestinationTXT().setText(sapServer.getDestination());
		wizard.getConnectionCountTXT().setText(sapServer.getConnectionCount());

		SAPTestServerDialog serverDialog = wizard.openServerTestDialog(sapServer.getName());
		expected = "Connection test for destination '" + sapDestination.getName() + "' succeeded.";
		serverDialog.clear();
		serverDialog.start();
		new WaitUntil(new ContainsText(serverDialog.getResultText(), "Server state: STARTED"));
		new WaitUntil(new ContainsText(serverDialog.getResultText(), "Server state: ALIVE"));
		serverDialog.stop();
		new WaitUntil(new ContainsText(serverDialog.getResultText(), "Server state: STOPPED"));
		serverDialog.clear();
		assertTrue(serverDialog.getResultText().getText().trim().equals(""));
		serverDialog.close();

		wizard.finish();
		editor().save();

		// TODO Model and execute a route with SAP destination or server
	}

	private void assertDestinationProperty(String destination, String property, String expected) throws IOException {
		assertXPath(expected, destinationQuery(destination) + "/property[@name='" + property + "']/@value");
	}

	private void assertServerProperty(String server, String property, String expected) throws IOException {
		assertXPath(expected, serverQuery(server) + "/property[@name='" + property + "']/@value");
	}

	private void assertXPath(String expected, String query) throws IOException {
		File contextFile = new CamelProject(PROJECT_NAME).getCamelContextFile(type.getCamelContext());
		XPathEvaluator xpath = new XPathEvaluator(contextFile);
		assertEquals(expected, xpath.evaluateString(query));
	}

	private String configurationQuery() {
		return "/" + type.getRootElement() + "/bean[@id='sap-configuration']";
	}

	private String destinationQuery() {
		return configurationQuery() + "/" + "property[@name='destinationDataStore']";
	}

	private String destinationQuery(String name) {
		return destinationQuery() + "/map/entry[@key='" + name + "']/bean";
	}

	private String serverQuery() {
		return configurationQuery() + "/" + "property[@name='serverDataStore']";
	}

	private String serverQuery(String name) {
		return serverQuery() + "/map/entry[@key='" + name + "']/bean";
	}

}
