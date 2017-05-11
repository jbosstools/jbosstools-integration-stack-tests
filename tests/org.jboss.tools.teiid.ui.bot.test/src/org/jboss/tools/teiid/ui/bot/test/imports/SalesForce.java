package org.jboss.tools.teiid.ui.bot.test.imports;

import java.util.Properties;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
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
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = { })
public class SalesForce {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;	
	
	public ImportHelper importHelper = null;

	private static final String PROJECT_NAME_TEIID = "TeiidConnImporter";

	@Before
	public void before() {
		if (new ShellMenu("Project", "Build Automatically").isSelected()) {
			new ShellMenu("Project", "Build Automatically").select();
		}
		
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
	public void salesforceTeiidTest() {
	    Assume.assumeFalse(System.getProperty("os.name").toLowerCase().startsWith("mac")); //this method works in Mac only local machine 
		String modelName = "sfImp";
		new ServersViewExt().deleteDatasource(teiidServer.getName(), "sfDS");
		
		Properties sfProps = teiidServer.getServerConfig().getConnectionProfile("salesforce").asProperties();

		TeiidConnectionImportWizard.openWizard()
				.createNewDataSource()
				.setName("sfDS")
				.setDriver("salesforce")
				.setImportPropertie(CreateDataSourceDialog.DATASOURCE_PROPERTY_PASSWORD, sfProps.getProperty("db.password"))
				.setImportPropertie(CreateDataSourceDialog.DATASOURCE_PROPERTY_USER_NAME, sfProps.getProperty("db.username"))
				.finish();
		TeiidConnectionImportWizard.getInstance()
				.selectDataSource("sfDS")
				.nextPage()
				.nextPage()
				.setModelName(modelName)
				.setProject(PROJECT_NAME_TEIID)
				.nextPageWithWait()
				.nextPageWithWait()
				.setTablesToImport("Objects to Create/Account","Objects to Create/Vote","Objects to Create/Profile")
				.finish();
		
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, modelName, "Account", "Vote", "Profile");
	}
	
}
