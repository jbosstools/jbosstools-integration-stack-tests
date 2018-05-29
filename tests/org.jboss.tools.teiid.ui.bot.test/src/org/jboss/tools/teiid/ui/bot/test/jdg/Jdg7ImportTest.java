package org.jboss.tools.teiid.ui.bot.test.jdg;

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

/**
 * Work with DV-6.4.0-REMOTE
 */
@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerRequirementState.RUNNING)
public class Jdg7ImportTest {

    @InjectRequirement
    private static TeiidServerRequirement teiidServer;

    private static final String PROJECT_NAME = "JdgImportProject";
    private static ModelExplorer explorer;

    @BeforeClass
    public static void createProject() {
        explorer = new ModelExplorer();
        explorer.createProject(PROJECT_NAME);
        explorer.selectItem(PROJECT_NAME);
    }

    @Test
    public void jdgImport() {
        String modelName = "s_smallA";

        TeiidConnectionImportWizard wizard = TeiidConnectionImportWizard.openWizard()
                .selectDataSource("java:/jdg71HotrodDS")
                .nextPage()
                .setTranslator("infinispan-hotrod")
                .setImportPropertie("Protobuf Name", "/org/jboss/qe/jdg/remote/protobuf/SmallA.proto")
                .nextPage()
                .setModelName(modelName)
                .setProject(PROJECT_NAME)
                .nextPageWithWait();
        String createdDDL = wizard.getDdlContent();
        wizard.nextPageWithWait()
                .finish();

        explorer.getProject(PROJECT_NAME).open();
        assertTrue(explorer.containsItem(PROJECT_NAME, modelName + ".xmi", "SmallA"));
        assertTrue(explorer.containsItem(PROJECT_NAME, modelName + ".xmi", "SmallA", "objectValue : varbinary"));
        assertTrue(explorer.containsItem(PROJECT_NAME, modelName + ".xmi", "SmallA", "intKey : int"));
        assertTrue(explorer.containsItem(PROJECT_NAME, modelName + ".xmi", "SmallA", "booleanValue : boolean"));
        assertTrue(explorer.containsItem(PROJECT_NAME, modelName + ".xmi", "SmallA", "bigDecimalValue : string"));
        assertTrue(explorer.containsItem(PROJECT_NAME, modelName + ".xmi", "SmallA", "timeStampValue : long"));
        assertTrue("Reproducer TEIIDDES-3169 - DDL doesn't contan NATIVE tag", createdDDL.contains("NATIVE"));
    }
}
