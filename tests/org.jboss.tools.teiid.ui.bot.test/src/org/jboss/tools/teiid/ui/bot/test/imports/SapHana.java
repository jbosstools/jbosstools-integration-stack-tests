package org.jboss.tools.teiid.ui.bot.test.imports;

import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.teiid.reddeer.ImportHelper;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.VdbEditor;
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
		ConnectionProfileConstants.SAP_HANA})
public class SapHana {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;	
	
	public ImportHelper importHelper = null;

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

	/**
	 * Note: start database before this test! (AWS)
	 */
	@Test
	public void sapHanaJDBCtest() {
		String model = "sapHanaModel";
		importHelper.importModelJDBC(PROJECT_NAME_JDBC, model, ConnectionProfileConstants.SAP_HANA, "BQT1/TABLE/SMALLA,BQT1/TABLE/SMALLB", false);
		
		new RelationalModelEditor(model + ".xmi").save();
		
		// TODO temp till hana translator is not set automatically (updated checkImportedTablesInModel method)
		assertTrue(new ModelExplorer().containsItem(PROJECT_NAME_JDBC,model + ".xmi", "SMALLA"));
		assertTrue(new ModelExplorer().containsItem(PROJECT_NAME_JDBC,model + ".xmi", "SMALLB"));
		
		String vdb_name = "Check_" + model;
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME_JDBC)
				.setName(vdb_name)
				.addModel(PROJECT_NAME_JDBC, model)
				.finish();
		
		VdbEditor.getInstance(vdb_name + ".vdb").setModelTranslator(model + ".xmi", model, "hana");
		VdbEditor.getInstance(vdb_name + ".vdb").save();
		
		new ModelExplorer().deployVdb(PROJECT_NAME_JDBC, vdb_name);

		TeiidJDBCHelper jdbcHelper = new TeiidJDBCHelper(teiidServer, vdb_name);
		String[] tables = new String[] { "SMALLA", "SMALLB" };
		for (int i = 0; i < tables.length; i++) {
			String previewSQL = "select * from \"" + model + "\".\"" + tables[i] + "\"";
			assertTrue(jdbcHelper.isQuerySuccessful(previewSQL,true));
		}		
		//checkImportedTablesInModel(model, "SMALLA", "SMALLB");
	}
	
	@Test
	public void sapHanaTeiidTest() {
		String modelName = "sapHanaModel";
		
		new ServersViewExt().createDatasource(teiidServer.getName(), ConnectionProfileConstants.SAP_HANA);
		
		TeiidConnectionImportWizard.openWizard()
				.selectDataSource(ConnectionProfileConstants.SAP_HANA)
				.nextPage()
				.setTranslator("hana")
				.setImportPropertie(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "BQT1")
				.setImportPropertie(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%")
				.nextPage()
				.setModelName(modelName)
				.setProject(PROJECT_NAME_TEIID)
				.nextPageWithWait()
				.nextPageWithWait()
				.finish();
		
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, modelName, "SMALLA", "SMALLB");
	}
}
