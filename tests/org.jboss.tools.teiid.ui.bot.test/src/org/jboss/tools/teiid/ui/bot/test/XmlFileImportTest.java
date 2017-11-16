package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.YesButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.JiraClient;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase.XmlLocalConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase.XmlRemoteConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.XMLImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author jstastny, skaleta
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerRequirementState.RUNNING)
public class XmlFileImportTest {
	private static final String PROJECT_NAME = "XmlFileImport";

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	@Before
	public void importProject() {
		new ModelExplorer().createProject(PROJECT_NAME);
	}
	
	@After
	public void cleanUp(){
		new ModelExplorer().deleteAllProjectsSafely();
	}

	@Test
	public void testImportLocalXml() {		
		XmlLocalConnectionProfileWizard.openWizard("LOCAL_XML")
				.setFile("resources/flat/cd_catalog.xml")
				.finish();

        String sourceModelName = "LocalXmlSourceModel";
        String viewModelName = "LocalXmlModel";
		String vdbName = "LocalXmlFileImportVdb";

		XMLImportWizard.openWizard()
				.setImportMode(XMLImportWizard.LOCAL)
				.nextPage()
				.setDataFileSource("LOCAL_XML")
            .setSourceModelName(sourceModelName)
				.nextPage()
				.setJndiName("LocalSource")
				.nextPage()
				.setRootPath("/CATALOG/CD")
				.addElement("CATALOG/CD/TITLE")
				.addElement("CATALOG/CD/ARTIST")
				.addElement("CATALOG/CD/COUNTRY")
				.addElement("CATALOG/CD/COMPANY")
				.addElement("CATALOG/CD/PRICE");
		if(new JiraClient().isIssueClosed("TEIIDDES-2858")){
			XMLImportWizard.getInstance()
					.addElement("CATALOG/CD/YEAR");
		}
		XMLImportWizard.getInstance()
				.nextPage()
            .setViewModelName(viewModelName)
				.setViewTableName("Table")
				.finish();
        workaround(sourceModelName);

		new ModelExplorer().refreshProject(PROJECT_NAME);
		VdbWizard.openVdbWizard()
            .setLocation(PROJECT_NAME).setName(vdbName)
            .addModel(PROJECT_NAME, viewModelName)
            .finish();
		new ModelExplorer().deployVdb(PROJECT_NAME, vdbName);
		
		TeiidJDBCHelper jdbcHelper = new TeiidJDBCHelper(teiidServer, vdbName);
        assertEquals(26, jdbcHelper.getNumberOfResults("SELECT * FROM " + viewModelName + ".Table"));
	}
	
	@Test
    public void testImportRemoteXml() {
		XmlRemoteConnectionProfileWizard.openWizard("REMOTE_XML")
				.setUrl("https://raw.githubusercontent.com/mmakovy/import-files/master/cd_catalog.xml")
				.testConnection()
				.finish();
		
		String modelName = "RemoteXmlModel";
		String vdbName = "RemoteXmlFileImportVdb";
		
		XMLImportWizard.openWizard()
				.setImportMode(XMLImportWizard.REMOTE)
				.nextPage()
				.setDataFileSource("REMOTE_XML")
				.setSourceModelName("RemoteSource")
				.nextPage()
				.setJndiName("RemoteSource")
				.nextPage()
				.setRootPath("/CATALOG/CD")
				.addElement("CATALOG/CD/TITLE")
				.addElement("CATALOG/CD/ARTIST")
				.addElement("CATALOG/CD/COUNTRY")
				.addElement("CATALOG/CD/COMPANY")
				.addElement("CATALOG/CD/PRICE");
		if(new JiraClient().isIssueClosed("TEIIDDES-2858")){
			XMLImportWizard.getInstance()
					.addElement("CATALOG/CD/YEAR");
			}
			XMLImportWizard.getInstance()
					.nextPage()
					.setViewModelName(modelName)
					.setViewTableName("Table")
					.finish();
			
			new ModelExplorer().refreshProject(PROJECT_NAME);
			VdbWizard.openVdbWizard()
					.setLocation(PROJECT_NAME)
					.setName(vdbName)
					.addModel(PROJECT_NAME, modelName)
					.finish();
			new ModelExplorer().deployVdb(PROJECT_NAME, vdbName);
			
			TeiidJDBCHelper jdbcHelper = new TeiidJDBCHelper(teiidServer, vdbName);
			assertEquals(26, jdbcHelper.getNumberOfResults("SELECT * FROM RemoteXmlModel.Table"));
	}

    /**
     * TEIIDDES-3107 Workaround for issue with number of parameters
     */
    public void workaround(String modelName) {
        if (!new JiraClient().isIssueClosed("TEIIDDES-3107")) {
            ModelExplorer explorer = new ModelExplorer();
            explorer.selectItem(PROJECT_NAME, modelName + ".xmi", "getTextFiles", "Result", "lastModified : timestamp");
            new ContextMenuItem("Delete").select();
            new WaitUntil(new ShellIsAvailable("Dependent Models Detected"));
            new YesButton().click();
            new WaitWhile(new IsInProgress(), false);
            explorer.selectItem(PROJECT_NAME, modelName + ".xmi", "getTextFiles", "Result", "created : timestamp");
            new ContextMenuItem("Delete").select();
            new WaitUntil(new ShellIsAvailable("Dependent Models Detected"));
            new YesButton().click();
            new WaitWhile(new IsInProgress(), false);
            explorer.selectItem(PROJECT_NAME, modelName + ".xmi", "getTextFiles", "Result", "size : int");
            new ContextMenuItem("Delete").select();
            new WaitUntil(new ShellIsAvailable("Dependent Models Detected"));
            new YesButton().click();
            new WaitWhile(new IsInProgress(), false);
            new ShellMenuItem(new WorkbenchShell(), "File", "Save All").select();
        }
    }
}
