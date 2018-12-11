package org.jboss.tools.teiid.ui.bot.test.reproducers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.eclipse.reddeer.common.platform.RunningPlatform;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.dialog.GenerateDynamicVdbDialog;
import org.jboss.tools.teiid.reddeer.editor.VdbEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles = {
    ConnectionProfileConstants.ORACLE_11G_PRODUCTS })
public class TeiidDes2888Test {
    private static final String PROJECT_NAME = "ProductsProject";
    private static final String SOURCE_MODEL = "ProductsSource.xmi";

    private ModelExplorer modelExplorer;

    @InjectRequirement
    private static TeiidServerRequirement teiidServer;

    @Before
    public void importProject() {
        modelExplorer = new ModelExplorer();
        modelExplorer.importProject(PROJECT_NAME);
        modelExplorer.refreshProject(PROJECT_NAME);
        modelExplorer.changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_PRODUCTS, PROJECT_NAME,
            SOURCE_MODEL);
        modelExplorer.createDataSource("Use Connection Profile Info", ConnectionProfileConstants.ORACLE_11G_PRODUCTS,
            PROJECT_NAME, SOURCE_MODEL);
    }

    @After
    public void after() {
        new ModelExplorer().deleteAllProjectsSafely();
    }

    @Test
    public void testDefaultValue() {
        String vdbName = "defaultValueVdb";
        String dynamicVdbName = vdbName + "-vdb.xml";
        VdbWizard.openVdbWizard()
                .setName(vdbName)
                .setLocation(PROJECT_NAME)
                .addModel(PROJECT_NAME, SOURCE_MODEL)
                .finish();
        VdbEditor vdbEditor = new VdbEditor(vdbName + ".vdb");
        vdbEditor.save();
        GenerateDynamicVdbDialog wizard = modelExplorer.generateDynamicVDB(PROJECT_NAME, vdbName);
        wizard.setName(vdbName).setFileName(dynamicVdbName).setLocation(PROJECT_NAME);
        String contents = wizard.getContents();
        wizard.finish();
        assertThat(contents, containsString("DEFAULT '(''Stock'')'"));
        
        /*TEIIDDES-3222*/
        if(RunningPlatform.isWindows()){
            new WaitUntil(new ShellIsActive("Error"), TimePeriod.DEFAULT, false);
            if(new ShellIsActive("Error").test()) {
                new OkButton().click();
            }
        }
        
        modelExplorer.deployVdb(PROJECT_NAME, dynamicVdbName);
        TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, vdbName);
        jdbchelper.isQuerySuccessful("SELECT * FROM PRODUCTDATA", true);
    }
}