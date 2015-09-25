package org.jboss.tools.teiid.ui.bot.test;

import java.util.Properties;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfilesConstants;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.ServerManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.junit.Before;
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
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = { ConnectionProfilesConstants.DB2_101_BQT,
		ConnectionProfilesConstants.DB2_81_BQT2, ConnectionProfilesConstants.DB2_97_BQT2,
		ConnectionProfilesConstants.ORACLE_10G_BQT2, ConnectionProfilesConstants.ORACLE_11G_BQT2,
		ConnectionProfilesConstants.ORACLE_12C_BQT, ConnectionProfilesConstants.SQL_SERVER_2008_BQT2,
		ConnectionProfilesConstants.SQL_SERVER_2012_BQT2, ConnectionProfilesConstants.DV6_DS1,
		ConnectionProfilesConstants.SQL_SERVER_2000_BQT2, ConnectionProfilesConstants.MYSQL_51_BQT2,
		ConnectionProfilesConstants.MYSQL_55_BQT2, ConnectionProfilesConstants.POSTGRESQL_84_BQT2,
		ConnectionProfilesConstants.POSTGRESQL_91_BQT2, ConnectionProfilesConstants.POSTGRESQL_92_DVQE,
		ConnectionProfilesConstants.SYBASE_15_BQT2, ConnectionProfilesConstants.INGRES_10_BQT2 })
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

	@Before
	public void openPerspective() {
		TeiidPerspective.getInstance();
	}

	// ============== generated jdbc tests ===========================

	@Test
	public void db2101Import() {

		String model = "db2101Model";
		importModel(model, ConnectionProfilesConstants.DB2_101_BQT, "BQT/TABLE/SMALLA,BQT/TABLE/SMALLB");
		checkImportedModel(model, "SMALLA", "SMALLB");
	}

	@Test
	public void db297Import() {
		// NPE at
		// org.teiid.designer.datatools.connection.ConnectionInfoHelper.getCommonProfileProperties(ConnectionInfoHelper.java:214)

		String model = "db297Model";
		importModel(model, ConnectionProfilesConstants.DB2_97_BQT2, "BQT2/TABLE/SMALLA,BQT2/TABLE/SMALLB");
		checkImportedModel(model, "SMALLA", "SMALLB");
	}

	@Test
	public void ingres10Import() {

		String model = "ingres10Model";
		importModel(model, ConnectionProfilesConstants.INGRES_10_BQT2, "bqt2/TABLE/smalla,bqt2/TABLE/smallb");
		checkImportedModel(model, "smalla", "smallb");
	}

	@Test
	@Ignore // server down for now
	public void oracle10gImport() {

		String model = "oracle10gModel";
		importModel(model, ConnectionProfilesConstants.ORACLE_10G_BQT2, "BQT2/TABLE/SMALLA,BQT2/TABLE/SMALLB");
		checkImportedModel(model, "SMALLA", "SMALLB");
	}

	@Test
	public void oracle11gImport() {

		String model = "oracle11gModel";
		importModel(model, ConnectionProfilesConstants.ORACLE_11G_BQT2, "BQT2/TABLE/SMALLA,BQT2/TABLE/SMALLB");
		checkImportedModel(model, "SMALLA", "SMALLB");
	}

	@Test
	public void oracle12cImport() {

		String model = "oracle12cModel";
		importModel(model, ConnectionProfilesConstants.ORACLE_12C_BQT, "DV/TABLE/SMALLA,DV/TABLE/SMALLB");
		checkImportedModel(model, "SMALLA", "SMALLB");
	}

	@Test
	public void sqlServer2008Import() {

		String model = "sqlServer2008Model";
		importModel(model, ConnectionProfilesConstants.SQL_SERVER_2008_BQT2,
				"bqt2/dbo/TABLE/SmallA,bqt2/dbo/TABLE/SmallB");
		checkImportedModel(model, "SmallA", "SmallB");
	}

	@Test
	public void sqlServer2012Import() {

		String model = "sqlServer2012Model";
		importModel(model, ConnectionProfilesConstants.SQL_SERVER_2012_BQT2,
				"bqt2/dbo/TABLE/SmallA,bqt2/dbo/TABLE/SmallB");
		checkImportedModel(model, "SmallA", "SmallB");
	}

	@Test // TEIIDDES-2458
	public void sybaseImport() {
		String model = "sybaseModel";
		importModel(model, ConnectionProfilesConstants.SYBASE_15_BQT2, "bqt2/TABLE/SmallA,bqt2/TABLE/SmallB");
		checkImportedModel(model, "SmallA", "SmallB");
	}

	@Test
	public void dv6Import() {

		String model = "dv6Model";
		importModel(model, ConnectionProfilesConstants.DV6_DS1, "PUBLIC/PUBLIC/TABLE/STATUS,PUBLIC/PUBLIC/TABLE/PARTS");
		checkImportedModel(model, "STATUS", "PARTS");
	}

	@Test
	@Ignore // db server is down
	public void mysql50Import() {

		String model = "mysql50Model";
		importModel(model, ConnectionProfilesConstants.MYSQL_50_BQT2, "bqt2/TABLE/smalla,bqt2/TABLE/smallb");
		checkImportedModel(model, "smalla", "smallb");
	}

	@Test
	public void mysql51Import() {
		String model = "mysql51Model";
		importModel(model, ConnectionProfilesConstants.MYSQL_51_BQT2, "bqt2/TABLE/smalla,bqt2/TABLE/smallb");
		checkImportedModel(model, "smalla", "smallb");
	}

	@Test
	public void mysql55Import() {

		String model = "mysql55Model";
		importModel(model, ConnectionProfilesConstants.MYSQL_55_BQT2, "bqt2/TABLE/smalla,bqt2/TABLE/smallb");
		checkImportedModel(model, "smalla", "smallb");
	}

	@Test
	public void postgresql84Import() {

		String model = "postgresql84Model";
		importModel(model, ConnectionProfilesConstants.POSTGRESQL_84_BQT2, "public/TABLE/smalla,public/TABLE/smallb");
		checkImportedModel(model, "smalla", "smallb");
	}

	@Test
	public void postgresql92Import() {

		String model = "postgresql92Model";
		importModel(model, ConnectionProfilesConstants.POSTGRESQL_92_DVQE, "public/TABLE/smalla,public/TABLE/smallb");
		checkImportedModel(model, "smalla", "smallb");
	}

	private void importModel(String modelName, String connectionProfile, String itemList) {
		Properties iProps = new Properties();
		iProps.setProperty("itemList", itemList);
		new ImportManager().importFromDatabase(MODEL_PROJECT, modelName, connectionProfile, iProps);
	}

	private void checkImportedModel(String model, String tableA, String tableB) {
		teiidBot.assertResource(MODEL_PROJECT, model + ".xmi", tableA);
		teiidBot.assertResource(MODEL_PROJECT, model + ".xmi", tableB);
		
		teiidBot.simulatePreview(teiidServer, MODEL_PROJECT, model, new String[]{tableA,tableB});

	}
	


}
