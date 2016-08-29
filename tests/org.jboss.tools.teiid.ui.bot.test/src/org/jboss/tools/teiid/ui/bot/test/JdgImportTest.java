package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.runtime.reddeer.requirement.ServerConnType;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.imports.TeiidConnectionImportWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionType = ServerConnType.REMOTE)
public class JdgImportTest {

	private static final String PROJECT_NAME = "JdgImportProject";

	@BeforeClass
	public static void createProject() {
		new ModelExplorer().createProject(PROJECT_NAME);
		new ModelExplorer().selectItem(PROJECT_NAME);

	}

	@Test
	public void jdgImport() {
		String modelName = "JdgModel";
	
		TeiidConnectionImportWizard.openWizard()
				.selectDataSource("infinispan-remote-cache-ds")
				.nextPage()
				.setTranslator("infinispan-cache-dsl")
				.nextPage()
				.setModelName(modelName)
				.setProject(PROJECT_NAME)
				.nextPageWithWait()
				.nextPageWithWait()
				.finish();
		
		new ModelExplorer().getProject(PROJECT_NAME).open();
		assertTrue(new ModelExplorer().containsItem(PROJECT_NAME,modelName + ".xmi", "SmallA"));
		assertTrue(new ModelExplorer().containsItem(PROJECT_NAME,modelName + ".xmi", "SmallA", "SmallAObject : object"));
		assertTrue(new ModelExplorer().containsItem(PROJECT_NAME,modelName + ".xmi", "SmallA", "intKey : int"));
		assertTrue(new ModelExplorer().containsItem(PROJECT_NAME,modelName + ".xmi", "SmallA", "PK_INTKEY"));
	}

}
