package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.impl.button.CancelButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ConnectionView;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase.FlatLocalConnectionProfileWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles = {
    ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER,
    ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER, })
public class ConnectionViewTest {
    private static final String PROJECT_NAME = "PreviewProject";
    private static final String NAME_ORACLE_MODEL = "partsSourceOracle";
	private static final String NAME_SQL_MODEL = "partsSourceSQLServer";
    private static final String[] PATH_TO_CP = {"ODA", "Flat File", "myCp"};
    private static final String NAME_OWN_CP = "myCp";
    private static final String[] PATH_TO_UPDATED_CP = {"ODA", "Flat File", "myCpUpdated"};
    private static final String NAME_OWN__UPDATED_CP = "myCpUpdated";

    @InjectRequirement
    private static TeiidServerRequirement teiidServer;

    private ModelExplorer modelExplorer = new ModelExplorer();;

    private ConnectionView connectionView = ConnectionView.getInstance();

    @Before
    public void createProject() {
        modelExplorer.importProject(PROJECT_NAME);
        modelExplorer.changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER, PROJECT_NAME,
            NAME_ORACLE_MODEL);
        modelExplorer.changeConnectionProfile(ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER, PROJECT_NAME, NAME_SQL_MODEL);
        modelExplorer.createDataSource("Use Connection Profile Info",
                ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER, PROJECT_NAME, NAME_ORACLE_MODEL);
        modelExplorer.createDataSource("Use Connection Profile Info",
                ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER, PROJECT_NAME, NAME_SQL_MODEL);
    	createOwnConectionProfile();
    }

    @After
    public void cleanUp(){
        modelExplorer.deleteAllProjectsSafely();
    }
    
    @Test
    public void getCPTest() {
    	assertTrue(connectionView.existConnectionProfile(PATH_TO_CP));
    }
    
    @Test
    public void createCPTest() {
        connectionView.createConnectionProfile();
        assertTrue(new ShellIsActive("New Connection Profile").test());
        new CancelButton(new DefaultShell("New Connection Profile")).click();
    }
    
    @Test
    public void updateCPTest() {
        assertTrue(connectionView.existConnectionProfile(PATH_TO_CP));
        connectionView.editConnectionProfile(PATH_TO_CP);
        assertTrue(new ShellIsActive("Properties for " + NAME_OWN_CP).test());
        new DefaultShell("Properties for " + NAME_OWN_CP);
        new LabeledText("Name:").setText(NAME_OWN__UPDATED_CP);
        new PushButton("Apply and Close").click();
        
        assertTrue(connectionView.existConnectionProfile(PATH_TO_UPDATED_CP));
    }

    @Test
    public void deleteCPTest() {
        connectionView.deleteConnectionProfile(PATH_TO_CP);
        assertFalse(connectionView.existConnectionProfile(PATH_TO_CP));
    }

    @Test
    public void getDataSourceTest() {
        assertTrue(connectionView.existDataSource(NAME_ORACLE_MODEL));
    }

    @Test
    public void createDataSourceTest() {
        connectionView.createDataSource();
        assertTrue(new ShellIsActive("Create DataSource").test());
        new CancelButton(new DefaultShell("Create DataSource")).click();
    }

    @Test
    public void updateDataSourceTest() {
        assertTrue(connectionView.existDataSource(NAME_ORACLE_MODEL));
        connectionView.editDataSource(NAME_ORACLE_MODEL);
        assertTrue(new ShellIsActive("Edit DataSource").test());
        new CancelButton(new DefaultShell("Edit DataSource")).click();
    }

    @Test
    public void deleteDataSourceTest() {
        assertTrue(connectionView.existDataSource(NAME_SQL_MODEL));
        connectionView.deleteDataSource(NAME_SQL_MODEL);
        assertFalse(connectionView.existDataSource(NAME_SQL_MODEL));
    }

    @Test(expected = Error.class)
    public void changeTeiid() {
        try {
            connectionView.changeDefaultServer("whatEver");
        }catch(Error ex) {
            assertTrue(ex.getMessage().equals("Only 1 server is configured"));
        }
    }

    private void createOwnConectionProfile() {
        connectionView.refreshView();
        if(!connectionView.existConnectionProfile(PATH_TO_CP)){
            FlatLocalConnectionProfileWizard.openWizard(NAME_OWN_CP)
            .setFile("resources/flat")
            .testConnection()
            .finish();
    	}
    }
}
