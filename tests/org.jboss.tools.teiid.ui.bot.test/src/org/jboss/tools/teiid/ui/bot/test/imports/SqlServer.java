package org.jboss.tools.teiid.ui.bot.test.imports;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.teiid.reddeer.ImportHelper;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.preference.TeiidDesignerPreferencePage;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.imports.TeiidConnectionImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles = {
		ConnectionProfileConstants.SQL_SERVER_2008_BOOKS,
		ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER,
		ConnectionProfileConstants.SQL_SERVER_2008_BQT2,
		ConnectionProfileConstants.SQL_SERVER_2012_BQT2,
		ConnectionProfileConstants.SQL_SERVER_2014_BQT2})
public class SqlServer {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	public ImportHelper importHelper = null;

	private static final String SOURCE_VDB_NAME = "teiid";
	private static final String SQL_SERVER_EXISTING_MODEL_NAME = "sqlserver";

	private static final String PROJECT_NAME_JDBC = "jdbcImportTest";
	private static final String PROJECT_NAME_TEIID = "TeiidConnImporter";

    private static final String MODEL_NAME_SQL_2008_PARTS = "sql2008parts";
    private static final String MODEL_NAME_SQL_2008_BQT2 = "sql2008bqt2";
    private static final String MODEL_NAME_SQL_2012 = "sql2012";
    private static final String MODEL_NAME_SQL_2014 = "sql2014";

	@Before
	public void before() {
		if (new ShellMenuItem(new WorkbenchShell(), "Project", "Build Automatically").isSelected()) {
			new ShellMenuItem(new WorkbenchShell(), "Project", "Build Automatically").select();
		}
		new ModelExplorer().createProject(PROJECT_NAME_JDBC);
		WorkbenchPreferenceDialog preferences = new WorkbenchPreferenceDialog();
		preferences.open();
		new TeiidDesignerPreferencePage(preferences).setTeiidConnectionImporterTimeout(240);
		new ModelExplorer().importProject(PROJECT_NAME_TEIID);
		new ModelExplorer().selectItem(PROJECT_NAME_TEIID);
		new ServersViewExt().refreshServer(teiidServer.getName());
		importHelper = new ImportHelper();
	}

    @After
    public void after(){
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER + "_DS");
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/Check_" + MODEL_NAME_SQL_2008_PARTS);

        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.SQL_SERVER_2008_BQT2);
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.SQL_SERVER_2008_BQT2 + "_DS");

        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.SQL_SERVER_2012_BQT2);
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.SQL_SERVER_2012_BQT2 + "_DS");
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/Check_" + MODEL_NAME_SQL_2012);

        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.SQL_SERVER_2014_BQT2);
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.SQL_SERVER_2014_BQT2 + "_DS");
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/Check_" + MODEL_NAME_SQL_2014);

        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + SQL_SERVER_EXISTING_MODEL_NAME);
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + SOURCE_VDB_NAME);

        new ModelExplorer().deleteAllProjectsSafely();
    }

	@Test
	public void sqlServer2008JDBCtest() {
		importHelper.importModelJDBC(PROJECT_NAME_JDBC, MODEL_NAME_SQL_2008_PARTS, ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER,"partssupplier/dbo/TABLE/SHIP_VIA,partssupplier/dbo/TABLE/PARTS", false);
		new RelationalModelEditor(MODEL_NAME_SQL_2008_PARTS + ".xmi").save();
		assertTrue(importHelper.checkNameInTableJDBC("\"AVERAGE TIME DELIVERY\"",6,2));
		importHelper.checkImportedTablesInModelJDBC(PROJECT_NAME_JDBC, MODEL_NAME_SQL_2008_PARTS, "SHIP_VIA", "PARTS", teiidServer);
	}

	@Test
	public void sqlServer2012JDBCtest() {
		importHelper.importModelJDBC(PROJECT_NAME_JDBC, MODEL_NAME_SQL_2012, ConnectionProfileConstants.SQL_SERVER_2012_BQT2,
				"bqt2/dbo/TABLE/SmallA,bqt2/dbo/TABLE/SmallB", false);
		new RelationalModelEditor(MODEL_NAME_SQL_2012 + ".xmi").save();
		importHelper.checkImportedTablesInModelJDBC(PROJECT_NAME_JDBC, MODEL_NAME_SQL_2012, "SmallA", "SmallB", teiidServer);
	}

	@Test
	public void sqlServer2014JDBCtest() {
		importHelper.importModelJDBC(PROJECT_NAME_JDBC, MODEL_NAME_SQL_2014, ConnectionProfileConstants.SQL_SERVER_2014_BQT2,
				"bqt2/dbo/TABLE/SMALLA,bqt2/dbo/TABLE/SMALLB", false);
		new RelationalModelEditor(MODEL_NAME_SQL_2014 + ".xmi").save();
		importHelper.checkImportedTablesInModelJDBC(PROJECT_NAME_JDBC, MODEL_NAME_SQL_2014, "SMALLA", "SMALLB", teiidServer);
	}

	@Test
	public void sqlServer2008TeiidTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "Small%");
		importHelper.importModelTeiid(PROJECT_NAME_TEIID, ConnectionProfileConstants.SQL_SERVER_2008_BQT2, MODEL_NAME_SQL_2008_BQT2, teiidImporterProperties, teiidServer);
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, MODEL_NAME_SQL_2008_BQT2, "SmallA", "SmallB");
	}

	@Test
	public void sqlServer2012TeiidTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "Small%");
		importHelper.importModelTeiid(PROJECT_NAME_TEIID, ConnectionProfileConstants.SQL_SERVER_2012_BQT2, MODEL_NAME_SQL_2012, teiidImporterProperties, teiidServer);
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, MODEL_NAME_SQL_2012, "SmallA", "SmallB");
	}

	@Test
	public void sqlServer2014TeiidTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		importHelper.importModelTeiid(PROJECT_NAME_TEIID, ConnectionProfileConstants.SQL_SERVER_2014_BQT2, MODEL_NAME_SQL_2014, teiidImporterProperties, teiidServer);
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, MODEL_NAME_SQL_2014, "SMALLA", "SMALLB");
	}

	@Test
	public void teiidTest() {
		new ModelExplorer().changeConnectionProfile(ConnectionProfileConstants.SQL_SERVER_2008_BOOKS,
				PROJECT_NAME_TEIID, SQL_SERVER_EXISTING_MODEL_NAME);
	
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME_TEIID)
				.setName(SOURCE_VDB_NAME)
				.addModel(PROJECT_NAME_TEIID, SQL_SERVER_EXISTING_MODEL_NAME + ".xmi")
				.finish();

		new ModelExplorer().deployVdb(PROJECT_NAME_TEIID, SOURCE_VDB_NAME);

		String modelName = SOURCE_VDB_NAME + "Imp";

		TeiidConnectionImportWizard.openWizard()
				.selectDataSource(SOURCE_VDB_NAME)
				.nextPage()
				.setImportPropertie(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "%sqlserver%")
				.nextPage()
				.setModelName(modelName)
				.setProject(PROJECT_NAME_TEIID)
				.nextPageWithWait()
				.nextPageWithWait()
				.finish();

		assertTrue(new ModelExplorer().containsItem(PROJECT_NAME_TEIID,modelName + ".xmi", "AUTHORS"));

		new ServersViewExt().undeployVdb(teiidServer.getName(), SOURCE_VDB_NAME);
	}
}
