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
		new ModelExplorer().getModelProject(PROJECT_NAME).open();

	}

	@Test
	public void jdgImport() {
		String modelName = "JdgModel";
		
		TeiidConnectionImportWizard importWizard = new TeiidConnectionImportWizard();
		importWizard.setModelName(modelName);
		importWizard.setProjectName(PROJECT_NAME);
		importWizard.setDataSourceName("infinispan-remote-cache-ds");
		importWizard.setTranslator("infinispan-cache-dsl");
		importWizard.execute();

		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", "SmallA"));
		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", "SmallA", "SmallAObject : object"));
		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", "SmallA", "intKey : int"));
		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", "SmallA", "PK_INTKEY"));
	}

}
