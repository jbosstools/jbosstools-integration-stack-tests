package org.jboss.tools.teiid.ui.bot.test.imports;

import java.util.Properties;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.teiid.reddeer.ImportHelper;
import org.jboss.tools.teiid.reddeer.dialog.CreateDataSourceDialog;
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
@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles = {})
public class MongoDB {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	public ImportHelper importHelper = null;

	private static final String PROJECT_NAME_TEIID = "TeiidConnImporter";

	@Before
	public void before() {
		if (new ShellMenuItem(new WorkbenchShell(), "Project", "Build Automatically").isSelected()) {
			new ShellMenuItem(new WorkbenchShell(), "Project", "Build Automatically").select();
		}
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
	public void mongoDBteiidTest() {
		String modelName = "mongoDB";
		String dataSourceName = "mongodbDS";

		Properties apacheProps = teiidServer.getServerConfig().getConnectionProfile("mongo_db").asProperties();

		TeiidConnectionImportWizard.openWizard()
				.createNewDataSource()
				.setName(dataSourceName)
				.setDriver("mongodb")
				.setImportPropertie(CreateDataSourceDialog.DATASOURCE_PROPERTY_USER_NAME_OPTIONAL, apacheProps.getProperty("db.username"))
				.setImportPropertie(CreateDataSourceDialog.DATASOURCE_PROPERTY_PASSWORD_OPTIONAL, apacheProps.getProperty("db.password"))
				.setImportPropertie(CreateDataSourceDialog.DATASOURCE_DATABASE, apacheProps.getProperty("db.name"))
				.setImportPropertie(CreateDataSourceDialog.DATASOURCE_PROPERTY_URL_SERVER_LIST, apacheProps.getProperty("url"))
				.finish();
		AbstractWait.sleep(TimePeriod.getCustom(5));
		TeiidConnectionImportWizard.getInstance()
				.selectDataSource("java:/" + dataSourceName)
				.nextPage()
				.setTranslator("mongodb")
				.nextPage()
				.setModelName(modelName)
				.setProject(PROJECT_NAME_TEIID)
				.nextPageWithWait()
				.nextPageWithWait()
				.setTablesToImport("Objects to Create/smalla","Objects to Create/smallb")
				.finish();

		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, modelName, "smalla", "smallb");
		new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + dataSourceName);
	}
}
