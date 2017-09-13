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
	public void db2101JDBCtest() {
		String model = "db2101Model";
		importHelper.importModelJDBC(PROJECT_NAME_JDBC, model, ConnectionProfileConstants.DB2_101_BQT, "BQT/TABLE/SMALLA,BQT/TABLE/SMALLB", false);
		new RelationalModelEditor(model + ".xmi").save();
		importHelper.checkImportedTablesInModelJDBC(PROJECT_NAME_JDBC, model, "SMALLA", "SMALLB", teiidServer);
		checkDatatypes(model);		
	}
	
	@Test
	public void db297JDBCtest() {
		String model = "db297Model";
		importHelper.importModelJDBC(PROJECT_NAME_JDBC, model, ConnectionProfileConstants.DB2_97_BQT2, "BQT2/TABLE/SMALLA,BQT2/TABLE/SMALLB", false);
		new RelationalModelEditor(model + ".xmi").save();
		importHelper.checkImportedTablesInModelJDBC(PROJECT_NAME_JDBC, model, "SMALLA", "SMALLB", teiidServer);
		checkDatatypes(model);
	}
	
	@Test
	public void db2101TeiidTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "%BQT%");
		importHelper.importModelTeiid(PROJECT_NAME_TEIID, ConnectionProfileConstants.DB2_101_BQT, "db2101Model", teiidImporterProperties,teiidServer);
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, "db2101Model", "SMALLA", "SMALLB");
		checkDatatypes("db2101Model");		
	}
	
	@Test
	public void db297TeiidTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		importHelper.importModelTeiid(PROJECT_NAME_TEIID,ConnectionProfileConstants.DB2_97_BQT2, "db297Model", teiidImporterProperties,teiidServer);
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, "db297Model", "SMALLA", "SMALLB");
		checkDatatypes("db297Model");
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
