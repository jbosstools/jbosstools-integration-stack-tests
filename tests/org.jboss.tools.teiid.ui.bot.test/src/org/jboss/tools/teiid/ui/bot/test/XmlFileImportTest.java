package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileHelper;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.VdbWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.XMLImportWizard;
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
		new ConnectionProfileHelper().createCpXml(LOCAL_CP_NAME, "resources/flat/cd_catalog.xml");

		XMLImportWizard importWizard = new XMLImportWizard();
		importWizard.setName(LOCAL_MODEL_PREFIX);
		importWizard.setLocal(true);
		importWizard.setRootPath("/CATALOG/CD");
		importWizard.addElement("CATALOG/CD/TITLE");
		importWizard.addElement("CATALOG/CD/ARTIST");
		importWizard.addElement("CATALOG/CD/COUNTRY");
		importWizard.addElement("CATALOG/CD/COMPANY");
		importWizard.addElement("CATALOG/CD/PRICE");
		importWizard.addElement("CATALOG/CD/YEAR");
		importWizard.setJndiName("XmlLocalSource");
		importWizard.execute();
		
		// Import from remote XML file
		new ConnectionProfileHelper().createCpXml(REMOTE_CP_NAME, "https://raw.githubusercontent.com/mmakovy/import-files/master/cd_catalog.xml");

		importWizard = new XMLImportWizard();
		importWizard.setName(REMOTE_MODEL_PREFIX);
		importWizard.setLocal(false);
		importWizard.setRootPath("/CATALOG/CD");
		importWizard.addElement("CATALOG/CD/TITLE");
		importWizard.addElement("CATALOG/CD/ARTIST");
		importWizard.addElement("CATALOG/CD/COUNTRY");
		importWizard.addElement("CATALOG/CD/COMPANY");
		importWizard.addElement("CATALOG/CD/PRICE");
		importWizard.addElement("CATALOG/CD/YEAR");
		importWizard.setJndiName("XmlRemoteSource");
		importWizard.execute();
		
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
