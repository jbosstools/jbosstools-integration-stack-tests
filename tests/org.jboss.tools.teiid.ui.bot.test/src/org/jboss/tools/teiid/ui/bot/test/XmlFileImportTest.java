package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.JiraClient;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase.XmlLocalConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase.XmlRemoteConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.XMLImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author jstastny, skaleta
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerReqState.RUNNING)
public class XmlFileImportTest {
	private static final String PROJECT_NAME = "XmlFileImport";
	private static final String LOCAL_CP_NAME = "LOCAL_XML";
	private static final String REMOTE_CP_NAME = "REMOTE_XML";
	private static final String LOCAL_MODEL_PREFIX = "Local";
	private static final String REMOTE_MODEL_PREFIX = "Remote";
	private static final String VDB_NAME = "XmlFileImportVdb";

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	@BeforeClass
	public static void importProject() throws IOException {
		new WorkbenchShell().maximize();
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.createProject(PROJECT_NAME);
	}

	@Test
	public void test() {
		// Import from local XML file
		XmlLocalConnectionProfileWizard.openWizard(LOCAL_CP_NAME)
				.setFile("resources/flat/cd_catalog.xml")
				.finish();

		XMLImportWizard.openWizard()
				.setImportMode(XMLImportWizard.LOCAL)
				.nextPage()
				.setDataFileSource(LOCAL_CP_NAME)
				.setSourceModelName(LOCAL_MODEL_PREFIX + "Source")
				.nextPage()
				.setJndiName(LOCAL_MODEL_PREFIX + "Source")
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
				.setViewModelName(LOCAL_MODEL_PREFIX + "View")
				.setViewTableName(LOCAL_MODEL_PREFIX + "Table")
				.finish();
		
		// Import from remote XML file
		XmlRemoteConnectionProfileWizard.openWizard(REMOTE_CP_NAME)
				.setUrl("https://raw.githubusercontent.com/mmakovy/import-files/master/cd_catalog.xml")
				.testConnection()
				.finish();
		
		XMLImportWizard.openWizard()
				.setImportMode(XMLImportWizard.REMOTE)
				.nextPage()
				.setDataFileSource(REMOTE_CP_NAME)
				.setSourceModelName(REMOTE_MODEL_PREFIX + "Source")
				.nextPage()
				.setJndiName(REMOTE_MODEL_PREFIX + "Source")
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
					.setViewModelName(REMOTE_MODEL_PREFIX + "View")
					.setViewTableName(REMOTE_MODEL_PREFIX + "Table")
					.finish();
		
		// Create VDB and test it.
		new ModelExplorer().getProject(PROJECT_NAME).refresh();
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(VDB_NAME)
				.addModel(PROJECT_NAME, LOCAL_MODEL_PREFIX + "View.xmi")
				.addModel(PROJECT_NAME, REMOTE_MODEL_PREFIX + "View.xmi")
				.finish();
		
		new ModelExplorer().deployVdb(PROJECT_NAME, VDB_NAME);
		
		TeiidJDBCHelper jdbcHelper = new TeiidJDBCHelper(teiidServer, VDB_NAME);
		assertEquals(26, jdbcHelper.getNumberOfResults("SELECT * FROM LocalView.LocalTable"));
		assertEquals(26, jdbcHelper.getNumberOfResults("SELECT * FROM RemoteView.RemoteTable"));
	}
}
