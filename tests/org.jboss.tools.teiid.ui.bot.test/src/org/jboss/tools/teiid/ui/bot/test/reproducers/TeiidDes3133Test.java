package org.jboss.tools.teiid.ui.bot.test.reproducers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;

import java.io.File;
import java.io.IOException;

import org.eclipse.reddeer.common.platform.RunningPlatform;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.condition.ConsoleHasText;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.common.reddeer.JiraClient;
import org.jboss.tools.teiid.reddeer.dialog.MaterializationDialog;
import org.jboss.tools.teiid.reddeer.editor.VdbEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase.Jdg7ConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerRequirementState.RUNNING)
public class TeiidDes3133Test {
    private static final String PROJECT_NAME = "ProductsProject";
    private static final String SOURCE_MODEL = "ProductsSource.xmi";
    private static final String VIEW_MODEL = "ProductsView.xmi";
    private static final String VIEW_TABLE = "PRODUCTDATA";
    private static final String JDG7_CP = "jdg7CP";
    private static final String MAT_SOURCE_MODEL = "ProductsSourceMat";
    private static final String VDB_NAME = "jdgVDB";

    private ModelExplorer modelExplorer;

    @InjectRequirement
    private static TeiidServerRequirement teiidServer;

    @Before
    public void importProject() {
        modelExplorer = new ModelExplorer();
        modelExplorer.importProject(PROJECT_NAME);
        modelExplorer.refreshProject(PROJECT_NAME);
        if (!new JiraClient().isIssueClosed("TEIIDDES-3185")) {
            addJdgTranslator();
        }
    }

    @After
    public void after() {
        modelExplorer.deleteAllProjectsSafely();
    }

    @Test
    public void overridingTranslatorTest() {
        Jdg7ConnectionProfileWizard.openWizard(JDG7_CP)
                .setJndiName("java:/test")
                .setRemoteServerList("localhost")
                .finish();
        modelExplorer.changeConnectionProfile(JDG7_CP, false, PROJECT_NAME, SOURCE_MODEL);
        modelExplorer.setMaterialization(PROJECT_NAME, VIEW_MODEL, VIEW_TABLE)
                .setSourceModelName(MAT_SOURCE_MODEL)
                .setVersionOfJdg(MaterializationDialog.TypeOfJdg.JDG_71)
                .setPrimaryCache("primaryCache")
                .setStagingCache("stagingCache")
                .finish();
        modelExplorer.saveSpecificModel(PROJECT_NAME, VIEW_MODEL);
        VdbWizard.openVdbWizard()
                .setName(VDB_NAME)
                .setLocation(PROJECT_NAME)
                .addModel(PROJECT_NAME, VIEW_MODEL)
                .finish();
        VdbEditor vdbEditor = new VdbEditor(VDB_NAME + ".vdb");
        vdbEditor.synchronizeAll();
        vdbEditor.save();
        assertThat(vdbEditor.getTranslatorName(SOURCE_MODEL), is("infinispan-hotrod"));
        assertThat(vdbEditor.getTranslatorName(MAT_SOURCE_MODEL + ".xmi"), isIn(vdbEditor.getTranslatorOverrides()));
        vdbEditor.synchronizeAll();
        vdbEditor.save();
    }

    /**
     * Add JDG 7 (infinispan-hotrod) translator to the server
     */
    private void addJdgTranslator() {
        String pathToServer = teiidServer.getServerConfig().getServer().getHome();
        String cmdAddJdgTranslator = "--connect --file=" + pathToServer + File.separator + "docs" + File.separator
                + "teiid" + File.separator + "datasources" + File.separator + "infinispan-hotrod-7.1" + File.separator
                + "add-infinispan-hotrod-translator.cli";
        String pathToCli = pathToServer + File.separator + "bin" + File.separator;
        if (RunningPlatform.isWindows()) {
            pathToCli += "jboss-cli.bat";
        } else {
            pathToCli += "jboss-cli.sh";
        }

        Runtime rt = Runtime.getRuntime();
        try {
            rt.exec(pathToCli + " " + cmdAddJdgTranslator);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new WaitUntil(new ConsoleHasText("TEIID50006 Teiid translator \"infinispan-hotrod\" added"), TimePeriod.LONG,
                false);
        if (!new ConsoleHasText("TEIID50006 Teiid translator \"infinispan-hotrod\" added").test()) {
            throw new IllegalStateException("Translator was not added");
        }
        new ServersViewExt().refreshServer(teiidServer.getName());
    }
}
