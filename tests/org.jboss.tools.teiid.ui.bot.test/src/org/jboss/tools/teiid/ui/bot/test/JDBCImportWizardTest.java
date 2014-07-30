package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.ServerManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.GuidesView;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Server;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.State;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Type;
import org.jboss.tools.teiid.ui.bot.test.suite.TeiidSuite;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for importing relational models from various sources
 * 
 * @author lfabriko
 */
@Perspective(name = "Teiid Designer")
@Server(type = Type.ALL, state = State.RUNNING)
@RunWith(TeiidSuite.class)
public class JDBCImportWizardTest {

	public static final String MODEL_PROJECT = "jdbcImportTest";

	private static TeiidBot teiidBot = new TeiidBot();
	private static final String SERVER_PROPS = "swtbot.properties";
	private static final String serverName = new ServerManager().getServerName(SERVER_PROPS);

	@BeforeClass
	public static void before() {

		teiidBot.uncheckBuildAutomatically();
		new ModelExplorerManager().createProject(MODEL_PROJECT);
		new ServerManager().getServersViewExt().refreshServer(serverName);
	}
	
	@AfterClass
	public static void after() {

		new ServerManager().stopServer(serverName);
	}
	
	@Test
	public void db2Test() {

		String model = "DB2Model";
		String cpProps = teiidBot.toAbsolutePath("resources/db/db2_bqt2.properties");
		String cpName = "DB2 Profile";
		Properties iProps = new Properties();
		iProps.setProperty("itemList", "BQT2/TABLE/SMALLA,BQT2/TABLE/SMALLB");
		
		new ConnectionProfileManager().createCPWithDriverDefinition(cpName, cpProps);
		TeiidPerspective.getInstance();
		new ImportManager().importFromDatabase(MODEL_PROJECT, model, cpName, iProps);
		
		teiidBot.assertResource(MODEL_PROJECT, model+".xmi", "SMALLA");
		teiidBot.assertResource(MODEL_PROJECT, model+".xmi", "SMALLB");
		
		assertTrue(new GuidesView().canPreviewData(null, new String[]{MODEL_PROJECT, model+".xmi", "SMALLA"}));
	}
	
	@Test
	public void genericJDBCTest() {

		//hsql for dv6
		String model = "GenericModel"; 
		String cpProps = teiidBot.toAbsolutePath("resources/db/dv6-ds1.properties");
		String cpName = "Generic cp";
		Properties iProps = new Properties();
		iProps.setProperty("itemList", "PUBLIC/PUBLIC/TABLE/SHIP_VIA, PUBLIC/PUBLIC/TABLE/STATUS");
		
		new ConnectionProfileManager().createCPWithDriverDefinition(cpName, cpProps);
		TeiidPerspective.getInstance();
		new ImportManager().importFromDatabase(MODEL_PROJECT, model, cpName, iProps);
		
		teiidBot.assertResource(MODEL_PROJECT, model+".xmi", "SHIP_VIA");
		teiidBot.assertResource(MODEL_PROJECT, model+".xmi", "STATUS");
		
		assertTrue(new GuidesView().canPreviewData(null, new String[]{MODEL_PROJECT, model+".xmi", "STATUS"}));
	}
	
	@Test
	public void oracleTest() {

		String model = "OracleModel";
		String cpProps = teiidBot.toAbsolutePath("resources/db/oracle_books.properties");
		String cpName = "Oracle cp";
		new ConnectionProfileManager().createCPWithDriverDefinition(cpName, cpProps);
		Properties iProps = new Properties();
		iProps.setProperty("itemList", "BOOKS/TABLE/AUTHORS,BOOKS/TABLE/PUBLISHERS");	
		TeiidPerspective.getInstance();
		new ImportManager().importFromDatabase(MODEL_PROJECT, model, cpName, iProps);
		
		teiidBot.assertResource(MODEL_PROJECT, model+".xmi", "AUTHORS");
		teiidBot.assertResource(MODEL_PROJECT, model+".xmi", "PUBLISHERS");
		
		assertTrue(new GuidesView().canPreviewData(null, new String[]{MODEL_PROJECT, model+".xmi", "AUTHORS"}));
	}

	@Test
	@Ignore // FIXME salesforce.properties has invalid username or password
	public void salesforceTest() {

		String model = "SFModel";
		String cpProps = teiidBot.toAbsolutePath("resources/db/salesforce.properties");
		String importProps = teiidBot.toAbsolutePath("resources/importWizard/sf.properties");
		String cpName =  "SF profile";
		new ConnectionProfileManager().createCPSalesForce(cpName, cpProps);
		TeiidPerspective.getInstance();
		new ImportManager().importFromSalesForce(MODEL_PROJECT, model, cpName, teiidBot.getProperties(importProps));
		
		teiidBot.assertResource(MODEL_PROJECT, model+".xmi", "salesforce", "AccountFeed");
		teiidBot.assertFailResource(MODEL_PROJECT, model+".xmi", "salesforce", "Account");
		teiidBot.assertFailResource(MODEL_PROJECT, model+".xmi", "salesforce", "Apex Class");
		
		assertTrue(new GuidesView().canPreviewData(null, new String[]{MODEL_PROJECT, model+".xmi", "salesforce", "AccountFeed"}));
	}

	//TODO class DatasourcesTest - create CP to VDB, create src model from it, new vdb- execute
}
