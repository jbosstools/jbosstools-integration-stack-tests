package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.imports.SalesforceImportWizard;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles = { ConnectionProfileConstants.SALESFORCE })
public class SalesforceImportTest {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	public static final String MODEL_PROJECT = "jdbcImportTest";
    public static final String MODEL_NAME = "SFModel";
	@BeforeClass
	public static void before() {
		if (new ShellMenuItem(new WorkbenchShell(), "Project", "Build Automatically").isSelected()) {
			new ShellMenuItem(new WorkbenchShell(), "Project", "Build Automatically").select();
		}
		new ModelExplorer().createProject(MODEL_PROJECT);
		new ServersViewExt().refreshServer(teiidServer.getName());
	}

    @After
    public void after(){
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + MODEL_NAME);
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/Check_" + MODEL_NAME);

        new ModelExplorer().deleteAllProjectsSafely();
    }

	@Test
	public void salesforceTest() {

		SalesforceImportWizard.openWizard()
				.setConnectionProfile(ConnectionProfileConstants.SALESFORCE)
				.nextPage()
				.deselectObjects("Account","Apex Class")
				.nextPage()
				.setModelName(MODEL_NAME)
				.setProject(MODEL_PROJECT)
				.finish();

		assertTrue(new ModelExplorer().containsItem(MODEL_PROJECT, MODEL_NAME + ".xmi", "AccountFeed"));
		assertFalse(new ModelExplorer().containsItem(MODEL_PROJECT, MODEL_NAME + ".xmi", "Account"));
		assertFalse(new ModelExplorer().containsItem(MODEL_PROJECT, MODEL_NAME + ".xmi", "Apex Class"));

		// old TD(prior to 9.0.4) needs table name "salesforce.Case_". From TD 9.0.4 there is no schema "salesforce"
		// when importing from SF.
		new ModelExplorer().simulateTablesPreview(teiidServer, MODEL_PROJECT, MODEL_NAME, new String[] { "Case_" });

        new ServersViewExt().undeployVdb(teiidServer.getName(), "Check_" + MODEL_NAME);
	}
}
