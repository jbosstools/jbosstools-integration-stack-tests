package org.jboss.tools.teiid.ui.bot.test.imports;

import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.tools.teiid.reddeer.ImportHelper;
import org.jboss.tools.teiid.reddeer.connection.SimpleHttpClient;
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
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = {})
public class Modeshape {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;	
	
	public ImportHelper importHelper = null;

	private static final String MODESHAPE_CP_NAME = "ModeShapeDS";
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
	public void modeshapeTeiidTest() throws IOException {
    	// initialize modeshape
		String resp = new SimpleHttpClient("http://localhost:8080/modeshape-rest/dv/")
				.setBasicAuth(teiidServer.getServerConfig().getServerBase().getProperty("modeshapeUser"),
						teiidServer.getServerConfig().getServerBase().getProperty("modeshapePassword"))
				.get();

		assertFalse("initializing modeshape failed", resp.isEmpty());
		
		String modelName = "ModeshapeModel";
		
		TeiidConnectionImportWizard.openWizard()
				.selectDataSource(MODESHAPE_CP_NAME)
				.nextPage()
				.setImportPropertie(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "mix:title")
				.setImportPropertie(TeiidConnectionImportWizard.IMPORT_PROPERTY_USE_FULL_SCHEMA_NAME, "false")
				.nextPage()
				.setModelName(modelName)
				.setProject(PROJECT_NAME_TEIID)
				.nextPageWithWait()
				.nextPageWithWait()
				.finish();
		
		importHelper.checkImportedModelTeiid(modelName, "mix:title");
	}
}
