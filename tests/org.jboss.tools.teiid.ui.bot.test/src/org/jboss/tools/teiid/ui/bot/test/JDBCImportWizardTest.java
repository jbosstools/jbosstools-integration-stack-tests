package org.jboss.tools.teiid.ui.bot.test;

import java.util.Properties;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for importing relational models from various sources
 * 
 * @author lfabriko
 * 
 */
@Perspective(name = "Teiid Designer")
public class JDBCImportWizardTest extends SWTBotTestCase {

	public static final String MODEL_PROJECT = "jdbcImportTest";

	private static TeiidBot teiidBot = new TeiidBot();

	@BeforeClass
	public static void before(){
		new ShellMenu("Project", "Build Automatically").select();
		new ModelExplorerManager().createProject(MODEL_PROJECT);
	}
	
	@Test
	public void db2Test(){
		String model = "DB2Model";
		String cpProps = teiidBot.toAbsolutePath("resources/db/db2_bqt2.properties");
		String cpName = "DB2 Profile";
		Properties iProps = new Properties();
		iProps.setProperty("itemList", "BQT2/TABLE/SMALLA,BQT2/TABLE/SMALLB");
		
		new ConnectionProfileManager().createCPWithDriverDefinition(cpName, cpProps);
		new ImportManager().importFromDatabase(MODEL_PROJECT, model, cpName, iProps);
		
		teiidBot.checkResource(MODEL_PROJECT, model+".xmi", "SMALLA");
		teiidBot.checkResource(MODEL_PROJECT, model+".xmi", "SMALLB");
	}
	
	//@Test
	public void derbyTest(){
		
	}
	
	//@Test
	public void genericJDBCTest(){
		//hsql for dv6
		String model = "GenericModel"; 
		String cpProps = teiidBot.toAbsolutePath("resources/db/dv6-ds1.properties");
		String cpName = "Generic cp";
		Properties iProps = new Properties();
		iProps.setProperty("itemList", "PUBLIC/PUBLIC/TABLE/SHIP_VIA, PUBLIC/PUBLIC/TABLE/STATUS");
		
		new ConnectionProfileManager().createCPWithDriverDefinition(cpName, cpProps);
		new ImportManager().importFromDatabase(MODEL_PROJECT, model, cpName, iProps);
		
		teiidBot.checkResource(MODEL_PROJECT, model+".xmi", "SHIP_VIA");
		teiidBot.checkResource(MODEL_PROJECT, model+".xmi", "STATUS");
	}
	
	//@Test
	public void hsqlTest(){
		String model = "HSQLModel"; 
		String cpProps = teiidBot.toAbsolutePath("resources/db/hsqldb.properties");
		String importProps = teiidBot.toAbsolutePath("resources/importWizard/hsql-employees.properties");
		String cpName = "HSQLDB cp";
		new ConnectionProfileManager().createCPWithDriverDefinition(cpName, cpProps);
		new ImportManager().importFromDatabase(MODEL_PROJECT, model, cpName, teiidBot.getProperties(importProps));
		
		teiidBot.checkResource(MODEL_PROJECT, model+".xmi", "CUSTOMER");
		teiidBot.checkResource(MODEL_PROJECT, model+".xmi", "ORDER");
	}//^customer, order
	
	//@Test
	public void informixTest(){
		//no testing db available
	}
	
	//@Test
	public void ingresTest(){
		//driver? --> TODO add to my git repo, create some branch with just drivers...
		
	}
	
	//@Test
	public void maxDBTest(){
		//no testing db available
	}
	
	//@Test
	public void modeshapeTest(){
		
	}
	
	//@Test
	public void mysqlTest(){
		///home/lfabriko/Work/repos/dataservices/teiid-test-artifacts/scenario-deploy-artifacts/PassOne/datasource-ds/QT_sqls2005ds_Push-ds.xml
	}
	
	//@Test
	public void oracleTest(){
		String model = "OracleModel";
		String cpProps = teiidBot.toAbsolutePath("resources/db/oracle_books.properties");
		
		String cpName = "Oracle cp";
		new ConnectionProfileManager().createCPWithDriverDefinition(cpName, cpProps);
		Properties iProps = new Properties();
		iProps.setProperty("itemList", "BOOKS/TABLE/AUTHORS,BOOKS/TABLE/PUBLISHERS");
		new ImportManager().importFromDatabase(MODEL_PROJECT, model, cpName, iProps);
		
		teiidBot.checkResource(MODEL_PROJECT, model+".xmi", "AUTHORS");
		teiidBot.checkResource(MODEL_PROJECT, model+".xmi", "PUBLISHERS");
	}
	
	//@Test
	public void postgresqlTest(){
		
	}
	
	//@Test
	public void sqlserverTest(){
		String model = "SQLModel";
		String cpProps = teiidBot.toAbsolutePath("resources/db/sqlserver_books.properties");
		
		Properties iProps = new Properties();//empty
		
		String cpName = "Books sql server";
		new ConnectionProfileManager().createCPWithDriverDefinition(cpName, cpProps);
		new ImportManager().importFromDatabase(MODEL_PROJECT, model, cpName, iProps);
		
		teiidBot.checkResource(MODEL_PROJECT, model+".xmi", "AUTHORS");
		teiidBot.checkResource(MODEL_PROJECT, model+".xmi", "PUBLISHERS");
	}//UC authors, publishers
	
	//@Test
	public void sqliteTest(){
		//no db available
	}

	
	//@Test
	public void salesforceTest(){//this is not JDBC
		String model = "SFModel";
		String cpProps = teiidBot.toAbsolutePath("resources/db/salesforce.properties");
		String importProps = teiidBot.toAbsolutePath("resources/importWizard/sf.properties");
		String cpName =  "SF profile";
		new ConnectionProfileManager().createCPSalesForce(cpName, cpProps);
		new ImportManager().importFromSalesForce(MODEL_PROJECT, model, cpName, teiidBot.getProperties(importProps));
		
		teiidBot.checkResource(MODEL_PROJECT, model+".xmi", "salesforce", "AccountFeed");
		teiidBot.checkResourceNotPresent(MODEL_PROJECT, model+".xmi", "salesforce", "Account");
		teiidBot.checkResourceNotPresent(MODEL_PROJECT, model+".xmi", "salesforce", "Apex Class");
	}//salesforce, AccountFeed

}
