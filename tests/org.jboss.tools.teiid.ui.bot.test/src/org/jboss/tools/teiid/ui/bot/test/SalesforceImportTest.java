package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.imports.SalesforceImportWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = { ConnectionProfileConstants.SALESFORCE })
public class SalesforceImportTest {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	public static final String MODEL_PROJECT = "jdbcImportTest";

	@BeforeClass
	public static void before() {
		if (new ShellMenu("Project", "Build Automatically").isSelected()) {
			new ShellMenu("Project", "Build Automatically").select();
		}
		new ModelExplorer().createProject(MODEL_PROJECT);
		new ServersViewExt().refreshServer(teiidServer.getName());
	}

	@Test
	public void salesforceTest() {
		String model = "SFModel";

		SalesforceImportWizard.openWizard()
				.setConnectionProfile(ConnectionProfileConstants.SALESFORCE)
				.nextPage()
				.deselectObjects("Account","Apex Class")
				.nextPage()
				.setModelName(model)
				.setProject(MODEL_PROJECT)
				.finish();

		assertTrue(new ModelExplorer().getProject(MODEL_PROJECT).containsItem(model + ".xmi", "AccountFeed"));
		assertFalse(new ModelExplorer().getProject(MODEL_PROJECT).containsItem(model + ".xmi", "Account"));
		assertFalse(new ModelExplorer().getProject(MODEL_PROJECT).containsItem(model + ".xmi", "Apex Class"));

		// old TD(prior to 9.0.4) needs table name "salesforce.Case_". From TD 9.0.4 there is no schema "salesforce"
		// when importing from SF.
		new ModelExplorer().simulateTablesPreview(teiidServer, MODEL_PROJECT, model, new String[] { "Case_" });

	}
}
