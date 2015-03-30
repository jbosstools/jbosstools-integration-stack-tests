package org.jboss.tools.teiid.ui.bot.test;

import java.util.Properties;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.ServerManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.GuidesView;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for importing relational models from various sources
 * 
 * @author lfabriko
 */
@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING)
public class JDBCImportWizardTest {

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;


	public static final String MODEL_PROJECT = "jdbcImportTest";

	private static TeiidBot teiidBot = new TeiidBot();

	@BeforeClass
	public static void before() {

		teiidBot.uncheckBuildAutomatically();
		new ModelExplorerManager().createProject(MODEL_PROJECT);
		new ServerManager().getServersViewExt().refreshServer(
				teiidServer.getName());
	}


	@Test
	public void db2Test() {

		String model = "DB2Model";
		Properties connectionProfileProps = teiidServer.getServerConfig().getConnectionProfile("db2_bqt2").asProperties();
		String cpName = "DB2 Profile";
		Properties iProps = new Properties();
		iProps.setProperty("itemList", "BQT2/TABLE/SMALLA,BQT2/TABLE/SMALLB");

		new ConnectionProfileManager().createCPWithDriverDefinition(cpName, connectionProfileProps);
		TeiidPerspective.getInstance();
		new ImportManager().importFromDatabase(MODEL_PROJECT, model, cpName,
				iProps);

		teiidBot.assertResource(MODEL_PROJECT, model + ".xmi", "SMALLA");
		teiidBot.assertResource(MODEL_PROJECT, model + ".xmi", "SMALLB");

		Assert.assertTrue(new GuidesView().canPreviewData(null, new String[] {
				MODEL_PROJECT, model + ".xmi", "SMALLA" }));
	}

	@Test // NOK
	public void genericJDBCTest() {

		// hsql for dv6
		String model = "GenericModel";
		Properties cpProperties = teiidServer.getServerConfig().getConnectionProfile("dv6-ds1").asProperties();
		String cpName = "Generic cp";
		Properties iProps = new Properties();
		iProps.setProperty("itemList",
				"PUBLIC/PUBLIC/TABLE/SHIP_VIA, PUBLIC/PUBLIC/TABLE/STATUS");

		new ConnectionProfileManager().createCPWithDriverDefinition(cpName, cpProperties);
		TeiidPerspective.getInstance();
		new ImportManager().importFromDatabase(MODEL_PROJECT, model, cpName,
				iProps);

		teiidBot.assertResource(MODEL_PROJECT, model + ".xmi", "SHIP_VIA");
		teiidBot.assertResource(MODEL_PROJECT, model + ".xmi", "STATUS");

		/*
		 * does not work, Designer seems to hold the lock even when disconnected, 
		 * will have to investigate whether it's a bug in Designer, in the driver
		 * or someplace else
		 *  */
		Assert.assertTrue(new GuidesView().canPreviewData(null, new String[] {
				MODEL_PROJECT, model + ".xmi", "STATUS" }));
	}

	@Test
	public void oracleTest() {

		String model = "OracleModel";
		Properties cpProperties = teiidServer.getServerConfig().getConnectionProfile("oracle_books").asProperties();
		String cpName = "Oracle cp";
		new ConnectionProfileManager().createCPWithDriverDefinition(cpName, cpProperties);
		Properties iProps = new Properties();
		iProps.setProperty("itemList",
				"BOOKS/TABLE/AUTHORS,BOOKS/TABLE/PUBLISHERS");
		TeiidPerspective.getInstance();
		new ImportManager().importFromDatabase(MODEL_PROJECT, model, cpName,
				iProps);

		teiidBot.assertResource(MODEL_PROJECT, model + ".xmi", "AUTHORS");
		teiidBot.assertResource(MODEL_PROJECT, model + ".xmi", "PUBLISHERS");

		Assert.assertTrue(new GuidesView().canPreviewData(null, new String[] {
				MODEL_PROJECT, model + ".xmi", "AUTHORS" }));
	}

	@Test
	public void salesforceTest() {

		String model = "SFModel";
		Properties cpProperties = teiidServer.getServerConfig().getConnectionProfile("salesforce").asProperties();
		String importProps = teiidBot
				.toAbsolutePath("resources/importWizard/sf.properties");
		String cpName = "SF profile";
		new ConnectionProfileManager().createCPSalesForce(cpName, cpProperties);
		TeiidPerspective.getInstance();
		new ImportManager().importFromSalesForce(MODEL_PROJECT, model, cpName,
				teiidBot.getProperties(importProps));

		teiidBot.assertResource(MODEL_PROJECT, model + ".xmi", "salesforce",
				"AccountFeed");
		teiidBot.assertFailResource(MODEL_PROJECT, model + ".xmi",
				"salesforce", "Account");
		teiidBot.assertFailResource(MODEL_PROJECT, model + ".xmi",
				"salesforce", "Apex Class");
		

		Assert.assertTrue(new GuidesView().canPreviewData(null, new String[] { 
				MODEL_PROJECT, model + ".xmi", "salesforce", "Case_" }, "select * from \"SFModel\".\"salesforce\".\"Case_\""));
	}

	// TODO class DatasourcesTest - create CP to VDB, create src model from it,
	// new vdb- execute
}
