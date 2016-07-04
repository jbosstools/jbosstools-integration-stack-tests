package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Properties;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ImportMetadataManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.VdbWizard;
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
		new ConnectionProfileManager().createCPXml(LOCAL_CP_NAME, "resources/flat/cd_catalog.xml");
		Properties props = new Properties();
		props.setProperty("local", "true");
		props.setProperty("rootPath", "/CATALOG/CD");
		props.setProperty("destination", PROJECT_NAME);
		props.setProperty("elements",
				"CATALOG/CD/TITLE,CATALOG/CD/ARTIST,CATALOG/CD/COUNTRY,CATALOG/CD/COMPANY,CATALOG/CD/PRICE,CATALOG/CD/YEAR");
		props.setProperty("JNDI Name", "XmlLocalSource");
		new ImportMetadataManager().importFromXML(PROJECT_NAME, LOCAL_MODEL_PREFIX, LOCAL_CP_NAME, props);		
		
		// Import from remote XML file
		new ConnectionProfileManager().createCPXml(REMOTE_CP_NAME, "https://raw.githubusercontent.com/mmakovy/import-files/master/cd_catalog.xml");
		props = new Properties();
		props.setProperty("local", "false");
		props.setProperty("rootPath", "/CATALOG/CD");
		props.setProperty("destination", PROJECT_NAME);
		props.setProperty("elements",
				"CATALOG/CD/TITLE,CATALOG/CD/ARTIST,CATALOG/CD/COUNTRY,CATALOG/CD/COMPANY,CATALOG/CD/PRICE,CATALOG/CD/YEAR");
		props.setProperty("JNDI Name", "XmlRemoteSource");
		new ImportMetadataManager().importFromXML(PROJECT_NAME, REMOTE_MODEL_PREFIX, REMOTE_CP_NAME, props);
		
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
