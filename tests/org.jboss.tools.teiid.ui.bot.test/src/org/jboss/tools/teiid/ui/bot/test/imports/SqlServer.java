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
		new ModelExplorer().deleteAllProjectsSafely();
	}	

	@Test
	public void sqlServer2008JDBCtest() {
		String model = "sqlServer2008Model";
		importHelper.importModelJDBC(PROJECT_NAME_JDBC, model, ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER,"partssupplier/dbo/TABLE/SHIP_VIA,partssupplier/dbo/TABLE/PARTS", false);
		new RelationalModelEditor(model + ".xmi").save();
		assertTrue(importHelper.checkNameInTableJDBC("\"AVERAGE TIME DELIVERY\"",6,2));
		importHelper.checkImportedTablesInModelJDBC(PROJECT_NAME_JDBC, model, "SHIP_VIA", "PARTS", teiidServer);
	}

	@Test
	public void sqlServer2012JDBCtest() {
		String model = "sqlServer2012Model";
		importHelper.importModelJDBC(PROJECT_NAME_JDBC, model, ConnectionProfileConstants.SQL_SERVER_2012_BQT2,
				"bqt2/dbo/TABLE/SmallA,bqt2/dbo/TABLE/SmallB", false);
		new RelationalModelEditor(model + ".xmi").save();
		importHelper.checkImportedTablesInModelJDBC(PROJECT_NAME_JDBC, model, "SmallA", "SmallB", teiidServer);
	}
	
	@Test
	public void sqlServer2014JDBCtest() {
		String model = "sqlServer2014Model";
		importHelper.importModelJDBC(PROJECT_NAME_JDBC, model, ConnectionProfileConstants.SQL_SERVER_2014_BQT2,
				"bqt2/dbo/TABLE/SMALLA,bqt2/dbo/TABLE/SMALLB", false);
		new RelationalModelEditor(model + ".xmi").save();
		importHelper.checkImportedTablesInModelJDBC(PROJECT_NAME_JDBC, model, "SMALLA", "SMALLB", teiidServer);
	}
	
	@Test		
	public void sqlServer2008TeiidTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "Small%");
		importHelper.importModelTeiid(PROJECT_NAME_TEIID, ConnectionProfileConstants.SQL_SERVER_2008_BQT2, "sqlServer2008Model", teiidImporterProperties, teiidServer);
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, "sqlServer2008Model", "SmallA", "SmallB");
	}

	@Test
	public void sqlServer2012TeiidTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "Small%");
		importHelper.importModelTeiid(PROJECT_NAME_TEIID, ConnectionProfileConstants.SQL_SERVER_2012_BQT2, "sqlServer2012Model", teiidImporterProperties, teiidServer);
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, "sqlServer2012Model", "SmallA", "SmallB");
	}
	
	@Test
	public void sqlServer2014TeiidTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		importHelper.importModelTeiid(PROJECT_NAME_TEIID, ConnectionProfileConstants.SQL_SERVER_2014_BQT2, "sqlServer2014Model", teiidImporterProperties, teiidServer);
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, "sqlServer2014Model", "SMALLA", "SMALLB");
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
	}
}
