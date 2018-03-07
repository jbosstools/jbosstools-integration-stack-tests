package org.jboss.tools.teiid.ui.bot.test.imports;

import static org.junit.Assert.assertEquals;

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
		ConnectionProfileConstants.DB2_101_BQT,
		ConnectionProfileConstants.DB2_97_BQT2})
public class Db2 {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	public ImportHelper importHelper = null;

	private static final String PROJECT_NAME_JDBC = "jdbcImportTest";
	private static final String PROJECT_NAME_TEIID = "TeiidConnImporter";

	private static final String modelNameDB2_101 = "db2_101";
	private static final String modelNameDB2_97 = "db2_97";

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
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.DB2_101_BQT);
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.DB2_101_BQT + "_DS");
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/Check_" + modelNameDB2_101);

        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.DB2_97_BQT2);
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.DB2_97_BQT2 + "_DS");
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/Check_" + modelNameDB2_97);
        new ModelExplorer().deleteAllProjectsSafely();
    }

	@Test
	public void db2101JDBCtest() {
		importHelper.importModelJDBC(PROJECT_NAME_JDBC, modelNameDB2_101, ConnectionProfileConstants.DB2_101_BQT, "BQT/TABLE/SMALLA,BQT/TABLE/SMALLB", false);
		new RelationalModelEditor(modelNameDB2_101 + ".xmi").save();
		importHelper.checkImportedTablesInModelJDBC(PROJECT_NAME_JDBC, modelNameDB2_101, "SMALLA", "SMALLB", teiidServer);
		checkDatatypes(modelNameDB2_101);
	}

	@Test
	public void db297JDBCtest() {
		importHelper.importModelJDBC(PROJECT_NAME_JDBC, modelNameDB2_97, ConnectionProfileConstants.DB2_97_BQT2, "BQT2/TABLE/SMALLA,BQT2/TABLE/SMALLB", false);
		new RelationalModelEditor(modelNameDB2_97 + ".xmi").save();
		importHelper.checkImportedTablesInModelJDBC(PROJECT_NAME_JDBC, modelNameDB2_97, "SMALLA", "SMALLB", teiidServer);
		checkDatatypes(modelNameDB2_97);
	}

	@Test
	public void db2101TeiidTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "%BQT%");
		importHelper.importModelTeiid(PROJECT_NAME_TEIID, ConnectionProfileConstants.DB2_101_BQT, modelNameDB2_101, teiidImporterProperties,teiidServer);
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, modelNameDB2_101, "SMALLA", "SMALLB");
		checkDatatypes(modelNameDB2_101);
	}

	@Test
	public void db297TeiidTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		importHelper.importModelTeiid(PROJECT_NAME_TEIID,ConnectionProfileConstants.DB2_97_BQT2, modelNameDB2_97, teiidImporterProperties,teiidServer);
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, modelNameDB2_97, "SMALLA", "SMALLB");
		checkDatatypes(modelNameDB2_97);
	}

	private void checkDatatypes(String modelName){
		org.jboss.tools.teiid.reddeer.editor.TableEditor editor = new RelationalModelEditor(modelName + ".xmi").openTableEditor();
		editor.openTab("Columns");
		assertEquals("date", editor.getCellText(1, "DATEVALUE", "Datatype"));
		assertEquals("decimal", editor.getCellText(1, "BIGDECIMALVALUE", "Datatype"));
		assertEquals("time", editor.getCellText(1, "TIMEVALUE", "Datatype"));
		assertEquals("decimal", editor.getCellText(1, "SHORTVALUE", "Datatype"));
		assertEquals("string", editor.getCellText(1, "OBJECTVALUE", "Datatype"));
		assertEquals("string", editor.getCellText(1, "STRINGNUM", "Datatype"));
		assertEquals("decimal", editor.getCellText(1, "FLOATNUM", "Datatype"));
		assertEquals("decimal", editor.getCellText(1, "BIGINTEGERVALUE", "Datatype"));
		assertEquals("double", editor.getCellText(1, "DOUBLENUM", "Datatype"));
		assertEquals("decimal", editor.getCellText(1, "INTKEY", "Datatype"));
		assertEquals("decimal", editor.getCellText(1, "LONGNUM", "Datatype"));
		assertEquals("timestamp : xs:string", editor.getCellText(1, "TIMESTAMPVALUE", "Datatype"));
		assertEquals("decimal", editor.getCellText(1, "BYTENUM", "Datatype"));
		assertEquals("string", editor.getCellText(1, "STRINGKEY", "Datatype"));
		assertEquals("char : xs:string", editor.getCellText(1, "CHARVALUE", "Datatype"));
		assertEquals("decimal", editor.getCellText(1, "INTNUM", "Datatype"));
		assertEquals("decimal", editor.getCellText(1, "BOOLEANVALUE", "Datatype"));
	}
}
