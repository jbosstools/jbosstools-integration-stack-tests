package org.jboss.tools.teiid.ui.bot.test.imports;

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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles = {
		ConnectionProfileConstants.POSTGRESQL_84_BQT2,
		ConnectionProfileConstants.POSTGRESQL_92_DVQE})
public class PostgreSql {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	public ImportHelper importHelper = null;

	private static final String PROJECT_NAME_JDBC = "jdbcImportTest";
	private static final String PROJECT_NAME_TEIID = "TeiidConnImporter";

    private static final String MODEL_NAME_POSTGRESQL_84 = "postgresql84";
    private static final String MODEL_NAME_POSTGRESQL_92 = "postgresql92";

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
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.POSTGRESQL_84_BQT2);
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.POSTGRESQL_84_BQT2 + "_DS");
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/Check_" + MODEL_NAME_POSTGRESQL_84);

        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.POSTGRESQL_92_DVQE);
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.POSTGRESQL_92_DVQE + "_DS");
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/Check_" + MODEL_NAME_POSTGRESQL_92);

        new ModelExplorer().deleteAllProjectsSafely();
    }

	@Test
	public void postgresql84JDBCtest() {
		importHelper.importModelJDBC(PROJECT_NAME_JDBC, MODEL_NAME_POSTGRESQL_84, ConnectionProfileConstants.POSTGRESQL_84_BQT2, "public/TABLE/smalla,public/TABLE/smallb", false);
		new RelationalModelEditor(MODEL_NAME_POSTGRESQL_84 + ".xmi").save();
		importHelper.checkImportedTablesInModelJDBC(PROJECT_NAME_JDBC, MODEL_NAME_POSTGRESQL_84, "smalla", "smallb", teiidServer);
	}

	@Test
	public void postgresql92JDBCtest() {
		importHelper.importModelJDBC(PROJECT_NAME_JDBC, MODEL_NAME_POSTGRESQL_92, ConnectionProfileConstants.POSTGRESQL_92_DVQE, "public/TABLE/smalla,public/TABLE/smallb", false);
		new RelationalModelEditor(MODEL_NAME_POSTGRESQL_92 + ".xmi").save();
		importHelper.checkImportedTablesInModelJDBC(PROJECT_NAME_JDBC, MODEL_NAME_POSTGRESQL_92, "smalla", "smallb", teiidServer);
	}

	@Test
	public void postgresql84TeiidTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "%public%");
		importHelper.importModelTeiid(PROJECT_NAME_TEIID, ConnectionProfileConstants.POSTGRESQL_84_BQT2, MODEL_NAME_POSTGRESQL_84, teiidImporterProperties, teiidServer);
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, MODEL_NAME_POSTGRESQL_84, "smalla", "smallb");
	}

	@Test
	public void postgresql92TeiidTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN,  "small%");
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "public");
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_CATALOG, "dvqe");
		importHelper.importModelTeiid(PROJECT_NAME_TEIID, ConnectionProfileConstants.POSTGRESQL_92_DVQE, MODEL_NAME_POSTGRESQL_92, teiidImporterProperties, teiidServer);
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, MODEL_NAME_POSTGRESQL_92, "smalla", "smallb");
	}
}
