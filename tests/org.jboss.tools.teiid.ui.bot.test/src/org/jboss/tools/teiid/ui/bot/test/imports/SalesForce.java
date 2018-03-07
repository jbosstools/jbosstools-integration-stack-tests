package org.jboss.tools.teiid.ui.bot.test.imports;

import java.util.Properties;

import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.teiid.reddeer.ImportHelper;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.dialog.CreateDataSourceDialog;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.preference.TeiidDesignerPreferencePage;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.imports.TeiidConnectionImportWizard;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles = {
	ConnectionProfileConstants.SALESFORCE })
public class SalesForce {
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
	public void salesforceTeiidTest() {
	    Assume.assumeFalse(System.getProperty("os.name").toLowerCase().startsWith("mac")); //this method works in Mac only local machine 
		String modelName = "salesForceTeiid";
		String dataSourceName = "salesForceDS";

		Properties sfProps = teiidServer.getServerConfig().getConnectionProfile(ConnectionProfileConstants.SALESFORCE).asProperties();

		TeiidConnectionImportWizard.openWizard()
				.createNewDataSource()
				.setName(dataSourceName)
				.setDriver("salesforce-34")
				.setImportPropertie(CreateDataSourceDialog.DATASOURCE_PROPERTY_PASSWORD, sfProps.getProperty("db.password"))
				.setImportPropertie(CreateDataSourceDialog.DATASOURCE_PROPERTY_USER_NAME, sfProps.getProperty("db.username"))
				.finish();
		TeiidConnectionImportWizard.getInstance()
				.selectDataSource("java:/" + dataSourceName)
				.nextPage()
				.setTranslator("salesforce-34")
				.nextPage()
				.setModelName(modelName)
				.setProject(PROJECT_NAME_TEIID)
				.nextPageWithWait()
				.nextPageWithWait()
				.setTablesToImport("Objects to Create/Account","Objects to Create/Vote","Objects to Create/Profile")
				.finish();

		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, modelName, "Account", "Vote", "Profile");
		new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + dataSourceName);
	}

}
