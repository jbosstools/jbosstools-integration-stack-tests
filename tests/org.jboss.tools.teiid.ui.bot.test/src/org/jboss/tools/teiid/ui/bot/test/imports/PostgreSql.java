package org.jboss.tools.teiid.ui.bot.test.imports;

import java.util.HashMap;
import java.util.Map;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.tools.teiid.reddeer.ImportHelper;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
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
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = {
		ConnectionProfileConstants.POSTGRESQL_84_BQT2,
		ConnectionProfileConstants.POSTGRESQL_92_DVQE})
public class PostgreSql {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;	
	
	public ImportHelper importHelper = null;

	private static final String PROJECT_NAME_JDBC = "jdbcImportTest";
	private static final String PROJECT_NAME_TEIID = "TeiidConnImporter";

	@Before
	public void before() {
		if (new ShellMenu("Project", "Build Automatically").isSelected()) {
			new ShellMenu("Project", "Build Automatically").select();
		}
		new ModelExplorer().createProject(PROJECT_NAME_JDBC);
		new TeiidDesignerPreferencePage().setTeiidConnectionImporterTimeout(240);
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
	public void postgresql84JDBCtest() {
		String model = "postgresql84Model";
		importHelper.importModelJDBC(PROJECT_NAME_JDBC, model, ConnectionProfileConstants.POSTGRESQL_84_BQT2, "public/TABLE/smalla,public/TABLE/smallb", false);
		importHelper.checkImportedTablesInModelJDBC(PROJECT_NAME_JDBC, model, "smalla", "smallb", teiidServer);
	}

	@Test
	public void postgresql92JDBCtest() {
		String model = "postgresql92Model";
		importHelper.importModelJDBC(PROJECT_NAME_JDBC, model, ConnectionProfileConstants.POSTGRESQL_92_DVQE, "public/TABLE/smalla,public/TABLE/smallb", false);
		importHelper.checkImportedTablesInModelJDBC(PROJECT_NAME_JDBC, model, "smalla", "smallb", teiidServer);
	}
	
	@Test
	public void postgresql84TeiidTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "%public%");
		importHelper.importModelTeiid(PROJECT_NAME_TEIID, ConnectionProfileConstants.POSTGRESQL_84_BQT2, "postgresql84Model", teiidImporterProperties, TimePeriod.VERY_LONG, teiidServer);
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, "postgresql84Model", "smalla", "smallb");
	}

	@Test
	public void postgresql92TeiidTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN,  "small%");
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "public");
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_CATALOG, "dvqe");
		importHelper.importModelTeiid(PROJECT_NAME_TEIID, ConnectionProfileConstants.POSTGRESQL_92_DVQE, "postgresql92Model", teiidImporterProperties, TimePeriod.VERY_LONG, teiidServer);
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, "postgresql92Model", "smalla", "smallb");
	}
}
