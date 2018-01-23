package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.imports.TeiidConnectionImportWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerRequirementState.RUNNING)
public class JdgImportTest {

    @InjectRequirement
    private static TeiidServerRequirement teiidServer;

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
                .selectDataSource("java:/infinispanRemoteDSL")
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
		assertTrue(new ModelExplorer().containsItem(PROJECT_NAME,modelName + ".xmi", "SmallA", "objectValue : varbinary"));
		assertTrue(new ModelExplorer().containsItem(PROJECT_NAME,modelName + ".xmi", "SmallA", "intKey : int"));
		assertTrue(new ModelExplorer().containsItem(PROJECT_NAME,modelName + ".xmi", "SmallA", "PK_INTKEY"));
	}

}
