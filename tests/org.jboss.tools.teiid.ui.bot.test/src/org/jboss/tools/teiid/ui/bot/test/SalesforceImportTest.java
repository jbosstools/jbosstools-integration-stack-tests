package org.jboss.tools.teiid.ui.bot.test;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ServerManager;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = { ConnectionProfileConstants.SALESFORCE })
public class SalesforceImportTest {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	public static final String MODEL_PROJECT = "jdbcImportTest";

	private static TeiidBot teiidBot = new TeiidBot();

	@BeforeClass
	public static void before() {

		teiidBot.uncheckBuildAutomatically();
		new ModelExplorer().createProject(MODEL_PROJECT);
		new ServerManager().getServersViewExt().refreshServer(teiidServer.getName());
	}

	@Test
	public void salesforceTest() {

		String model = "SFModel";

		String importProps = teiidBot.toAbsolutePath("resources/importWizard/sf.properties");
		new ImportManager().importFromSalesForce(MODEL_PROJECT, model, ConnectionProfileConstants.SALESFORCE,
				teiidBot.getProperties(importProps));

		teiidBot.assertResource(MODEL_PROJECT, model + ".xmi", "AccountFeed");
		teiidBot.assertFailResource(MODEL_PROJECT, model + ".xmi", "Account");
		teiidBot.assertFailResource(MODEL_PROJECT, model + ".xmi", "Apex Class");

		// old TD(prior to 9.0.4) needs table name "salesforce.Case_". From TD 9.0.4 there is no schema "salesforce"
		// when importing from SF.
		teiidBot.simulateTablesPreview(teiidServer, MODEL_PROJECT, model, new String[] { "Case_" });

	}
}
