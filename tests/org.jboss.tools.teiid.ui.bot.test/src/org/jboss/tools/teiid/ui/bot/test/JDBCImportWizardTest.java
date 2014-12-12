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
		new ServerManager().getServersViewExt().refreshServer(teiidServer.getName());
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
		
		Assert.assertTrue(new GuidesView().canPreviewData(null, new String[]{MODEL_PROJECT, model+".xmi", "SMALLA"}));
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
		
		Assert.assertTrue(new GuidesView().canPreviewData(null, new String[]{MODEL_PROJECT, model+".xmi", "STATUS"}));
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
		
		Assert.assertTrue(new GuidesView().canPreviewData(null, new String[]{MODEL_PROJECT, model+".xmi", "AUTHORS"}));
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
		
		Assert.assertTrue(new GuidesView().canPreviewData(null, new String[]{MODEL_PROJECT, model+".xmi", "salesforce", "AccountFeed"}));
	}

	//TODO class DatasourcesTest - create CP to VDB, create src model from it, new vdb- execute
}
