package org.jboss.tools.teiid.ui.bot.test;

import java.util.Properties;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.runtime.reddeer.requirement.ServerConnType;
import org.jboss.tools.teiid.reddeer.manager.ImportMetadataManager;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.TeiidConnectionImportWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionType=ServerConnType.REMOTE)
public class JdgImportTest {

	private static final String PROJECT_NAME = "JdgImportProject";
	private static TeiidBot teiidBot = new TeiidBot();
	
	@BeforeClass
	public static void createProject() {
		teiidBot.createModelProject(PROJECT_NAME);
		new ModelExplorer().getModelProject(PROJECT_NAME).open();

	}
	@Test
	public void jdgImport() {
		String modelName = "JdgModel";

		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, "infinispan-remote-cache");
		iProps.setProperty(TeiidConnectionImportWizard.TRANSLATOR, "infinispan-cache-dsl");
		
		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, modelName, iProps, null);

		teiidBot.assertResource(PROJECT_NAME, modelName + ".xmi", "SmallA");
		teiidBot.assertResource(PROJECT_NAME, modelName + ".xmi", "SmallA", "SmallAObject : object");
		teiidBot.assertResource(PROJECT_NAME, modelName + ".xmi", "SmallA", "intKey : biginteger");
		teiidBot.assertResource(PROJECT_NAME, modelName + ".xmi", "SmallA", "PK_INTKEY");
	}

}
