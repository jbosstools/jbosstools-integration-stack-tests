package org.jboss.tools.teiid.ui.bot.test.imports;

import java.util.HashMap;
import java.util.Map;

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
	ConnectionProfileConstants.MYSQL_51_BQT2,
	ConnectionProfileConstants.MYSQL_55_BQT2})
public class MySql {
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
	public void mysql51JDBCtest() {
		String model = "mysql51Model";
		importHelper.importModelJDBC(PROJECT_NAME_JDBC, model, ConnectionProfileConstants.MYSQL_51_BQT2, "bqt2/TABLE/smalla,bqt2/TABLE/smallb", false);
		importHelper.checkImportedTablesInModelJDBC(PROJECT_NAME_JDBC, model, "smalla", "smallb",teiidServer);
	}

	@Test
	public void mysql55JDBCtest() {
		String model = "mysql55Model";
		importHelper.importModelJDBC(PROJECT_NAME_JDBC, model, ConnectionProfileConstants.MYSQL_55_BQT2, "bqt2/TABLE/smalla,bqt2/TABLE/smallb", false);
		importHelper.checkImportedTablesInModelJDBC(PROJECT_NAME_JDBC, model, "smalla", "smallb", teiidServer);
	}
	
	@Test
	public void mysql51TeiidTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		importHelper.importModelTeiid(PROJECT_NAME_TEIID, ConnectionProfileConstants.MYSQL_51_BQT2, "mysql51Model", teiidImporterProperties, teiidServer);
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, "mysql51Model", "smalla", "smallb");
	}

	@Test
	public void mysql55TeiidTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		importHelper.importModelTeiid(PROJECT_NAME_TEIID, ConnectionProfileConstants.MYSQL_55_BQT2, "mysql55Model", teiidImporterProperties, teiidServer);
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, "mysql55Model", "smalla", "smallb");
	}
}
