package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.tools.teiid.reddeer.editor.SQLScrapbookEditor;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ImportMetadataManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorerView;
import org.jboss.tools.teiid.reddeer.view.SQLResult;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
/**
 * 
 * @author jstastny
 *
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerReqState.RUNNING)
public class XmlFileImportTest {

	private static final String PROJECT = "XmlFileImport";
	private static final String XML_FILE_PATH = "resources/flat/cd_catalog.xml";
	private static final String REMOTE_XML_URL = "https://raw.githubusercontent.com/mmakovy/import-files/master/cd_catalog.xml";
	
	private static final String LOCAL_XML_CP_NAME = "LOCAL_XML";
	private static final String REMOTE_XML_CP_NAME = "REMOTE_XML";
	
	private static final String VDB = "XML";
	private static final String XML_LOCAL_MODEL_PREFIX = "Local";
	private static final String XML_REMOTE_MODEL_PREFIX = "Remote";
	private static final String XML_VIEW_MODEL_SUFFIX = "View";
	private static final String XML_SOURCE_MODEL_SUFFIX = "Source";
	
	private static final String TESTLOCAL = "SELECT * FROM LocalView.LocalTable";
	private static final String TESTREMOTE = "SELECT * FROM RemoteView.RemoteTable";
	
	private static int i = 0;

	@BeforeClass
	public static void prepare() {
		new ModelExplorerManager().createProject(PROJECT);
	}
	
	
	@Test
	public void test(){
		localXmlModels();
		remoteXmlModels();
		createVdb();
		refreshServer();
		executeVdb();
	}
	
	public void localXmlModels(){
		new ConnectionProfileManager().createCPXml(LOCAL_XML_CP_NAME, XML_FILE_PATH);
		Properties props = new Properties();
		props.setProperty("local", "true");
		props.setProperty("rootPath", "/CATALOG/CD");
		props.setProperty("destination", PROJECT);
		props.setProperty("elements", "CATALOG/CD/TITLE,CATALOG/CD/ARTIST,CATALOG/CD/COUNTRY,CATALOG/CD/COMPANY,CATALOG/CD/PRICE,CATALOG/CD/YEAR");
		new ImportMetadataManager().importFromXML(PROJECT, XML_LOCAL_MODEL_PREFIX, LOCAL_XML_CP_NAME, props);
	}
	
	public void remoteXmlModels(){
		new ConnectionProfileManager().createCPXml(REMOTE_XML_CP_NAME, REMOTE_XML_URL);
		Properties props = new Properties();
		props.setProperty("local", "false");
		props.setProperty("rootPath", "/CATALOG/CD");
		props.setProperty("destination", PROJECT);
		props.setProperty("elements", "CATALOG/CD/TITLE,CATALOG/CD/ARTIST,CATALOG/CD/COUNTRY,CATALOG/CD/COMPANY,CATALOG/CD/PRICE,CATALOG/CD/YEAR");
		new ImportMetadataManager().importFromXML(PROJECT, XML_REMOTE_MODEL_PREFIX, REMOTE_XML_CP_NAME, props);
	}
	
	public void createVdb(){
		new VDBManager().createVDB(PROJECT, VDB);
		new VDBManager().addModelsToVDB(PROJECT, VDB, 
				new String[] {
				XML_LOCAL_MODEL_PREFIX+XML_VIEW_MODEL_SUFFIX,
				XML_REMOTE_MODEL_PREFIX+XML_VIEW_MODEL_SUFFIX
		});
	}
	
	public void executeVdb(){
		new ModelExplorerView().executeVDB(PROJECT, VDB + ".vdb");
		
		executeSQL(TESTLOCAL,VDB);
		executeSQL(TESTREMOTE,VDB);
	}
	
	public void refreshServer(){
		new ServersView().open();
		new ServersViewExt().refreshServer(new ServersView().getServers().get(0).getLabel().getName());
	}
	
	private void executeSQL(String sql, String vdb){
		
		SQLScrapbookEditor editor = new SQLScrapbookEditor("SQL Scrapbook0");
		editor.show();
		editor.setDatabase(vdb);

		editor.setText(sql);
		editor.executeAll();
		
		AbstractWait.sleep(TimePeriod.SHORT);

		SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(sql);
		
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());
		
	}
	
}
