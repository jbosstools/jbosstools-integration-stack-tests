package org.jboss.tools.teiid.ui.bot.test;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfilesConstants;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.ServerManager;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.GuidesView;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = { ConnectionProfilesConstants.SALESFORCE })
public class SalesforceImportTest {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	public static final String MODEL_PROJECT = "jdbcImportTest";

	private static TeiidBot teiidBot = new TeiidBot();

	@BeforeClass
	public static void before() {

		teiidBot.uncheckBuildAutomatically();
		new ModelExplorerManager().createProject(MODEL_PROJECT);
		new ServerManager().getServersViewExt().refreshServer(teiidServer.getName());
	}

	@Test
	public void salesforceTest() {

		String model = "SFModel";

		String importProps = teiidBot.toAbsolutePath("resources/importWizard/sf.properties");
		new ImportManager().importFromSalesForce(MODEL_PROJECT, model, ConnectionProfilesConstants.SALESFORCE,
				teiidBot.getProperties(importProps));

		teiidBot.assertResource(MODEL_PROJECT, model + ".xmi", "salesforce", "AccountFeed");
		teiidBot.assertFailResource(MODEL_PROJECT, model + ".xmi", "salesforce", "Account");
		teiidBot.assertFailResource(MODEL_PROJECT, model + ".xmi", "salesforce", "Apex Class");

		// old TD(prior to 9.0.4) needs table name "salesforce.Case_". From TD 9.0.4 there is no schema "salesforce"
		// when importing from SF.
		teiidBot.simulatePreview(teiidServer, MODEL_PROJECT, model, new String[] { "Case_" });

	}
}
