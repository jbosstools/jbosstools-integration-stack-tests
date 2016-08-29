package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.common.reddeer.JiraClient;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase.FlatLocalConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase.FlatRemoteConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.FlatImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Flat file Import test
 * 
 * @author felias
 *
 */
@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING)
public class FlatFileTest {

	private static final String PROJECT_NAME = "Flat_file_import";
	private final String LOCAL_FILE_PATH = "resources/flat";
	private final String LOCAL_FILE_NAME = "productdata_data.csv";
	private static final String LOCAL_PROFILE_NAME = "localFlatProfile";
	private static final String LOCAL_SOURCE_MODEL = "localSourceModel";
	private static final String LOCAL_VIEW_MODEL = "localViewModel";
	private static final String LOCAL_VIEW_TABLE = "localViewTable";
	private static final String LOCAL_VDB = "LocalFileVDB";
	
	private static final String REMOTE_PROFILE_NAME = "remoteFlatProfile";
	private static final String REMOTE_URL = "https://raw.githubusercontent.com/mmakovy/import-files/master/productdata_data.csv";
	private static final String REMOTE_SOURCE_MODEL = "remoteSourceModel";
	private static final String REMOTE_VIEW_MODEL = "remoteViewModel";
	private static final String REMOTE_VIEW_TABLE = "remoteViewTable";
	private static final String REMOTE_VDB = "RemoteFileVDB";
	private static final String JNDI_NAME_LOCAL = "LocalSource";
	private static final String JNDI_NAME_REMOTE = "RemoteSource";

	private final String EMPTY_SPACE_PATH = "resources/flat/folder empty space";

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	@BeforeClass
	public static void beforeClass() {
		new ModelExplorer().createProject(PROJECT_NAME);
	}
	@Before
	public void before(){
		new TeiidPerspective().open();
	}

	/**
	 * Creates a VDB from a file from a local computer
	 */
	@Test
	public void localFileTest() {
		FlatLocalConnectionProfileWizard.openWizard(LOCAL_PROFILE_NAME)
				.setFile(LOCAL_FILE_PATH)
				.testConnection()
				.finish();
		
		FlatImportWizard.openWizard()
				.selectLocalFileImportMode()
				.nextPage()
				.selectProfile(LOCAL_PROFILE_NAME)
				.selectFile(LOCAL_FILE_NAME)
				.setSourceModel(LOCAL_SOURCE_MODEL)
				.setProject(PROJECT_NAME)
				.nextPage()
				.setJndiName(JNDI_NAME_LOCAL)
				.nextPage()
				.nextPage()
				.nextPage()
				.setViewModel(LOCAL_VIEW_MODEL)
				.setViewTable(LOCAL_VIEW_TABLE)
				.finish();
		
		// Deploying the VDB
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(LOCAL_VDB)
				.addModel(PROJECT_NAME,LOCAL_VIEW_MODEL + ".xmi")
				.finish();

		new ModelExplorer().deployVdb(PROJECT_NAME, LOCAL_VDB);

		new WaitWhile(new ShellWithTextIsActive("Progress Information"));

		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, LOCAL_VDB);
		assertEquals(122, jdbchelper.getNumberOfResults("SELECT * FROM "+LOCAL_VIEW_TABLE));
	}
	
	/*
	 * Creates a VDB using remote file
	 */
	@Test
	public void remoteFileTest() {
		FlatRemoteConnectionProfileWizard.openWizard(REMOTE_PROFILE_NAME)
				.setUrl(REMOTE_URL)
				.testConnection()
				.finish();

		FlatImportWizard.openWizard()
				.selectRemoteUrlImportMode()
				.nextPage()
				.selectProfile(REMOTE_PROFILE_NAME)
				.setSourceModel(REMOTE_SOURCE_MODEL)
				.setProject(PROJECT_NAME)
				.nextPage()
				.setJndiName(JNDI_NAME_REMOTE)
				.nextPage()
				.nextPage()
				.nextPage()
				.setViewModel(REMOTE_VIEW_MODEL)
				.setViewTable(REMOTE_VIEW_TABLE)
				.finish();

		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(REMOTE_VDB)
				.addModel(PROJECT_NAME,REMOTE_VIEW_MODEL + ".xmi")
				.finish();

		new ModelExplorer().deployVdb(PROJECT_NAME, REMOTE_VDB);


		new WaitWhile(new ShellWithTextIsActive("Progress Information"));

		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, REMOTE_VDB);
		assertEquals(122, jdbchelper.getNumberOfResults("SELECT * FROM "+REMOTE_VIEW_TABLE));
	}
	
	@Test
	public void testEmptySpace(){
		FlatLocalConnectionProfileWizard.openWizard("emptySpace")
				.setFile(EMPTY_SPACE_PATH);
		
		assertEquals(" The folder path cannot contain spaces.", new DefaultText(2).getText());
		assertFalse(new PushButton("Next >").isEnabled());

		FlatLocalConnectionProfileWizard.getInstance()
				.testConnection();
		
		if(new JiraClient().isIssueClosed("TEIIDDES-2493")){
			assertFalse(new PushButton("Next >").isEnabled());
			assertEquals(" The folder path cannot contain spaces.", new DefaultText(2).getText());
		}
		
		FlatLocalConnectionProfileWizard.getInstance().cancel();
	}

}