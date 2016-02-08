package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.*;
import java.util.List;
import java.util.Properties;
import org.hamcrest.Matcher;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.Procedure;
import org.jboss.tools.teiid.reddeer.Table;
import org.jboss.tools.teiid.reddeer.WAR;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.condition.IsPreviewInProgress;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfilesConstants;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ImportMetadataManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.matcher.WithMnemonicTextMatcher;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.util.SimpleHttpClient;
import org.jboss.tools.teiid.reddeer.view.GuidesView;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.FlatImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.ImportGeneralItemWizard;
import org.jboss.tools.teiid.reddeer.wizard.ImportJDBCDatabaseWizard;
import org.jboss.tools.teiid.reddeer.wizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.WsdlImportWizard;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
/**
 * @author mkralik
 */

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles={
		ConnectionProfilesConstants.ORACLE_11G_PARTS_SUPPLIER,
		ConnectionProfilesConstants.SQL_SERVER_2008_PARTS_SUPPLIER,
})

public class GuidesTest {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	private static final String CONNECTION_PROFILE= ConnectionProfilesConstants.SQL_SERVER_2008_PARTS_SUPPLIER;
	
	private static final GuidesView guides = new GuidesView();
	
	@Before
	public void openPerspective() {
		TeiidPerspective.getInstance();
		EditorHandler.getInstance().closeAll(false);
		new ModelExplorer().deleteAllProjects(false);
	}	
	@Test
	public  void JDBC_Source(){
		String actionSet = "Model JDBC Source";
		String project_JDBC_name = "JDBC_Source";
		String model_JDBC_name = "SQLtestSource";
		String vdb_JDBC_name = "JDBC_VDB";
		String test_SQL = "SELECT * FROM JDBC_VDB.SQLtestSource.STATUS";

		
		guides.createProjectViaGuides(actionSet, project_JDBC_name);
		
		guides.chooseAction(actionSet, "Create JDBC "); 
		new DefaultShell("New Connection Profile");
		new PushButton("Cancel").click();
		
		guides.chooseAction(actionSet, "Create source ");
		createJDBCSource(model_JDBC_name,project_JDBC_name,CONNECTION_PROFILE);
		
		guides.previewDataViaActionSet(actionSet,project_JDBC_name, model_JDBC_name+".xmi","PARTS"); 
		
		guides.defineVDB(actionSet, project_JDBC_name,vdb_JDBC_name, model_JDBC_name);
		
		assertTrue(guides.editVDB(actionSet, project_JDBC_name, vdb_JDBC_name));
		
		List<Integer> results = guides.executeVDB(actionSet, teiidServer, vdb_JDBC_name,test_SQL);
		assertEquals(Integer.valueOf(3),results.get(0));
	}
	@Test
	public void Soap(){	
		String actionSet = "Consume SOAP Web Service";
		String project_SOAP_name = "SOAP_Web";
		String model_SOAP_name = "soapSourceModel";
		String view_SOAP_name = "soapViewModel";
		String vdb_SOAP_name = "SOAP_VDB";
		String test_SQL = "exec FullCountryInfo('US')";
		String test_SQL2 = "exec FullCountryInfoAllCountries()";
		String soapProfile = "SOAP_WS";
		Properties wsdlCP = new Properties();
		wsdlCP.setProperty("wsdl", "http://ws-dvirt.rhcloud.com/dv-test-ws/soap?wsdl");
		wsdlCP.setProperty("endPoint", "Countries");
		
		guides.createProjectViaGuides(actionSet, project_SOAP_name);

		guides.chooseAction(actionSet, "Create Web ");
		new DefaultShell("New Connection Profile");
		new PushButton("Cancel").click();
		new ConnectionProfileManager().createCPWSDL(soapProfile, wsdlCP);

		guides.chooseAction(actionSet, "Generate "); 
		new DefaultShell("Create Relational Model from Web Service");
		new PushButton("Cancel").click();		
		wsdlImportWizard(soapProfile, model_SOAP_name, view_SOAP_name);
		
		guides.previewDataViaActionSet(actionSet, project_SOAP_name, view_SOAP_name+".xmi","FullCountryInfoAllCountries");

		guides.defineVDB(actionSet, project_SOAP_name, vdb_SOAP_name, view_SOAP_name);

		assertTrue(guides.editVDB(actionSet, project_SOAP_name, vdb_SOAP_name));

		List<Integer> results = guides.executeVDB(actionSet, teiidServer, vdb_SOAP_name,test_SQL,test_SQL2);
		assertEquals(Integer.valueOf(1),results.get(0));
		assertEquals(Integer.valueOf(3),results.get(1));
	}
    @Test
	public void Rest(){
 		String actionSet = "Create a REST WAR";
		String project_REST_name = "REST_WAR";
		String model_REST_name = "REST_WarSource";
		String view_REST_name = "REST_WarView";
		String table_REST_name = "viewSupplier";
		String procedure_REST_name = "getSupplierByName";
		String query_view = "SELECT * FROM REST_WARSource.SUPPLIER";
		String query_procedure = "BEGIN SELECT XMLELEMENT(NAME Supplier, XMLAGG(XMLELEMENT(NAME Supplier, "
				+ "XMLFOREST(REST_WarView.viewSupplier.SUPPLIER_ID, REST_WarView.viewSupplier.SUPPLIER_NAME, "
				+ "REST_WarView.viewSupplier.SUPPLIER_STATUS, REST_WarView.viewSupplier.SUPPLIER_CITY, REST_WarView.viewSupplier.SUPPLIER_STATE)))) "
				+ "AS result FROM REST_WarView.viewSupplier WHERE REST_WarView.viewSupplier.SUPPLIER_NAME = REST_WarView.getSupplierByName.nameIN; END";
		String uriRest = "supplier/{nameIN}";
		String vdb_REST_name = "Rest_WarVDB";
		String vdbJndiName = "Rest_WarVDB";
		String result = "<Supplier><Supplier><SUPPLIER_ID>S111</SUPPLIER_ID><SUPPLIER_NAME>Park</SUPPLIER_NAME>"
				+ "<SUPPLIER_STATUS>10</SUPPLIER_STATUS><SUPPLIER_CITY>Chicago</SUPPLIER_CITY><SUPPLIER_STATE>IL</SUPPLIER_STATE></Supplier></Supplier>";
		
		guides.createProjectViaGuides(actionSet, project_REST_name);

		guides.chooseAction(actionSet, "Define Source ");
		new PushButton("OK").click();
		createJDBCSource(model_REST_name,project_REST_name,CONNECTION_PROFILE);
		
		guides.chooseAction(actionSet, "Define relational view table");
		defineViewTable(project_REST_name, view_REST_name, query_view, table_REST_name);
		new DefaultShell("Define View Table");
		new PushButton("OK").click();

		guides.chooseAction(actionSet, "Define relational view procedure");
		defineProcedure(procedure_REST_name, query_procedure, uriRest);

		String param = "Park";
		new ModelEditor(view_REST_name + ".xmi").save();
		guides.previewDataViaActionSetWithParam(actionSet, param, project_REST_name, view_REST_name+".xmi",procedure_REST_name); 

		guides.defineVDB(actionSet, project_REST_name,vdb_REST_name, view_REST_name);
		
		guides.chooseAction(actionSet, "Deploy VDB");
		new CheckBox("Create Data Source for this VDB").click();
		new PushButton("OK").click();
		new WaitWhile(new IsInProgress(), TimePeriod.NORMAL);
		String[] pathToVDB = new String[] { project_REST_name, vdb_REST_name };
		new VDBManager().createVDBDataSource(pathToVDB, vdbJndiName , false);
		
		String path = new TeiidBot().toAbsolutePath("target");
		WAR war = guides.createWAR(actionSet, vdb_REST_name, WAR.NONE_SECURITY, vdbJndiName, path, pathToVDB);

		guides.chooseAction(actionSet, "Deploy WAR file "); 
		new DefaultShell("Deploy WAR Instructions");
		new PushButton("OK").click();
		
		Properties itemProps = new Properties();
		itemProps.setProperty("dirName", new TeiidBot().toAbsolutePath("target"));
		itemProps.setProperty("file", vdb_REST_name + ".war");
		itemProps.setProperty("intoFolder", project_REST_name);
		new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, itemProps);

		war.deploy(teiidServer.getName());
		String url = "http://localhost:8080/"+vdb_REST_name+"/REST_WarView/supplier/Park";
		
		assertEquals(result, new SimpleHttpClient(url).get());
    }
    @Test
	public void Flat(){
		String actionSet = "Model Flat File Source";
		String project_Flat_name = "ModelFlat";
		String model_Flat_name = "flatSourceModel";
		String view_Flat_name = "flatViewModel";
		String view_Flat_table = "viewTable";
		String vdb_Flat_name = "ModelFlatVDB";
		String test_SQL = "SELECT * FROM "+view_Flat_name+"."+view_Flat_table;		
		String flatProfile = "FlatDataSource";
		
		guides.createProjectViaGuides(actionSet, project_Flat_name);
		
		guides.chooseAction(actionSet, "Create Teiid flat ");
		new DefaultShell("New Connection Profile");
		new PushButton("Cancel").click();
		new ConnectionProfileManager().createCPFlatFile(flatProfile, "resources/guides");

		guides.chooseAction(actionSet, "Create source model from ");
		createFlatLocalSource(flatProfile,project_Flat_name,model_Flat_name, view_Flat_name,view_Flat_table);
		
		guides.previewDataViaActionSet(actionSet,project_Flat_name, view_Flat_name+".xmi",view_Flat_table);
		
		guides.defineVDB(actionSet, project_Flat_name,vdb_Flat_name, view_Flat_name);
		
		assertTrue(guides.editVDB(actionSet, project_Flat_name, vdb_Flat_name));
		
		List<Integer> results = guides.executeVDB(actionSet, teiidServer, vdb_Flat_name,test_SQL);
		assertEquals(Integer.valueOf(16),results.get(0));
	}
    @Test
	public void localXML(){
		String actionSet = "Model Local XML File Source";	
		String xmlLocalprofile = "LocalXML";
		String xmlLocalName = "localXML";
		String vdb_localXML_name = "LocalXMLvdb";
		String test_SQL = "SELECT * FROM "+xmlLocalName+"View."+xmlLocalName+"Table";

		guides.createProjectViaGuides(actionSet, "LocalXML");

		guides.chooseAction(actionSet, "Create Teiid local ");
		new DefaultShell("New Connection Profile");
		new PushButton("Cancel").click();
		new ConnectionProfileManager().createCPXml(xmlLocalprofile, "resources/guides/supplier.xml");

		guides.chooseAction(actionSet, "Create source model from XML file source");
		new DefaultShell("Import From XML File Source");
		new PushButton("Cancel").click();
		Properties props = new Properties();
		props.setProperty("local", "true");
		props.setProperty("rootPath", "/SUPPLIERS/SUPPLIER");
		props.setProperty("destination", xmlLocalprofile);
		props.setProperty("elements", "SUPPLIERS/SUPPLIER/SUPPLIER_ID,SUPPLIERS/SUPPLIER/SUPPLIER_NAME,SUPPLIERS/SUPPLIER/SUPPLIER_STATUS,SUPPLIERS/SUPPLIER/SUPPLIER_CITY,SUPPLIERS/SUPPLIER/SUPPLIER_STATE");
		new ImportMetadataManager().importFromXML(xmlLocalprofile , xmlLocalName, xmlLocalprofile, props);
		
		guides.previewDataViaActionSet(actionSet,xmlLocalprofile, xmlLocalName+"View.xmi",xmlLocalName+"Table");
		
		guides.defineVDB(actionSet, xmlLocalprofile,vdb_localXML_name, xmlLocalName+"View"); 
		
		assertTrue(guides.editVDB(actionSet, xmlLocalprofile, vdb_localXML_name));

		List<Integer> results = guides.executeVDB(actionSet, teiidServer, vdb_localXML_name,test_SQL);
		assertEquals(Integer.valueOf(16),results.get(0));
	}
	@Test
	public void remoteXML(){
		String actionSet = "Model Remote XML File Source";	
		String xmlRemoteName = "remoteXML";
		String vdb_remoteXML_name = "remoteXMLvdb";
		String test_SQL = "SELECT * FROM "+xmlRemoteName+"View."+xmlRemoteName+"Table";
		String xmlRemoteprofile = "RemoteXML";
		
		guides.createProjectViaGuides(actionSet, "RemoteXML");
		
		guides.chooseAction(actionSet, "Create Teiid Remote ");
		new DefaultShell("New Connection Profile");
		new PushButton("Cancel").click();
		new ConnectionProfileManager().createCPXml(xmlRemoteprofile, "https://raw.githubusercontent.com/mmakovy/import-files/master/cd_catalog.xml");
		
		guides.chooseAction(actionSet, "Create source model from remote XML");
		new DefaultShell("Import From XML File Source");
		new PushButton("Cancel").click();
		Properties props = new Properties();
		props.setProperty("local", "false");
		props.setProperty("rootPath", "/CATALOG/CD");
		props.setProperty("destination", xmlRemoteprofile);
		props.setProperty("elements", "CATALOG/CD/TITLE,CATALOG/CD/ARTIST,CATALOG/CD/COUNTRY,CATALOG/CD/COMPANY,CATALOG/CD/PRICE,CATALOG/CD/YEAR");
		new ImportMetadataManager().importFromXML(xmlRemoteprofile , xmlRemoteName, xmlRemoteprofile, props);
		
		guides.previewDataViaActionSet(actionSet,xmlRemoteprofile, xmlRemoteName+"View.xmi",xmlRemoteName+"Table");
		
		guides.defineVDB(actionSet, xmlRemoteprofile,vdb_remoteXML_name, xmlRemoteName+"View"); 
		
		assertTrue(guides.editVDB(actionSet, xmlRemoteprofile, vdb_remoteXML_name));

		List<Integer> results = guides.executeVDB(actionSet, teiidServer, vdb_remoteXML_name,test_SQL);
		assertEquals(Integer.valueOf(26),results.get(0));
	}
	@Test
	public void teiidDataSource(){
		String actionSet = "Model Teiid Data Source";
		String projectName = "TeiidDataSource";
		String dataSource = "TeiidDataSource";
		String modelName = "TeiidModel";
		String vdbName = "TeiidDataVDB";
		String test_SQL = "SELECT * FROM STATUS";

		new WaitWhile(new IsPreviewInProgress(), TimePeriod.LONG);

		
		guides.createProjectViaGuides(actionSet, projectName);
		
		guides.createDataSource(dataSource, ConnectionProfilesConstants.SQL_SERVER_2008_PARTS_SUPPLIER);
		
		guides.createSourceModelFromTeiid(modelName, dataSource,null,"dbo");
		
		guides.chooseAction(actionSet, "Set Connection Profile");
		new DefaultShell("Set Connection Profile");
		new DefaultTreeItem("Teiid Importer Connections", "TeiidImportCP_" + dataSource).select();
		new PushButton("OK").click();
		new TeiidBot().saveAll();
		
		new WaitWhile(new IsPreviewInProgress(), TimePeriod.NORMAL);
		new WaitWhile(new IsInProgress(), TimePeriod.NORMAL);
		guides.previewDataViaActionSet(actionSet, projectName, modelName+".xmi","STATUS"); 	
		guides.defineVDB(actionSet, projectName, vdbName, modelName);
				
		List<Integer> results = guides.executeVDB(actionSet, teiidServer, vdbName,test_SQL);
		assertEquals(Integer.valueOf(3),results.get(0));
		
	}
	@Test
	public void teiid(){
		EditorHandler.getInstance().closeAll(false);

		String serverName = "testServer";
		guides.newServer(serverName);
		
		assertTrue(guides.editServer(serverName));
		
		guides.setDefaultTeiidInstance(serverName);
		
		guides.startAndRefreshServer(serverName);

		cleanAfter();
    }
	
    private void createJDBCSource(String modelName,String projectName,String cp_name){
    	ImportJDBCDatabaseWizard importJDBC = new ImportJDBCDatabaseWizard();
		importJDBC.setConnectionProfile(cp_name);
		importJDBC.setProjectName(projectName);
		importJDBC.setModelName(modelName);
		importJDBC.fill();
		importJDBC.finish();
    }
    private void createFlatLocalSource(String profile,String projectName,String modelName, String viewModelName, String viewTableName){
    	FlatImportWizard importWizard = new FlatImportWizard();
		importWizard.selectLocalFileImportMode();
		importWizard.next();
		importWizard.selectProfile(profile);
		new DefaultTable().getItem(0).setChecked(true); // importWizard.selectFile(fileName+"     <<<<"); unstable
		new DefaultShell("Import From Flat File Source");
		importWizard.setSourceModel(modelName);
		importWizard.setProject(projectName);
		importWizard.next();
		importWizard.next();
		importWizard.next();
		importWizard.setViewModel(viewModelName);
		importWizard.setViewTable(viewTableName);
		importWizard.finish();
    }
    private void wsdlImportWizard(String soapProfile, String model_SOAP_name, String view_SOAP_name){
    	WsdlImportWizard wsdlWizard = new WsdlImportWizard();
		wsdlWizard.setProfile(soapProfile);
		wsdlWizard.setSourceModelName(model_SOAP_name);
		wsdlWizard.setViewModelName(view_SOAP_name);
		
		wsdlWizard.addOperation("FullCountryInfo");
		wsdlWizard.addOperation("FullCountryInfoAllCountries");
		wsdlWizard.addRequestElement("FullCountryInfo/sequence/arg0");
		
		wsdlWizard.addResponseElement("FullCountryInfoResponse/sequence/return/sequence/capitalCity");
		wsdlWizard.addResponseElement("FullCountryInfoResponse/sequence/return/sequence/continentCode");
		wsdlWizard.addResponseElement("FullCountryInfoResponse/sequence/return/sequence/currencyIsoCode");
		wsdlWizard.addResponseElement("FullCountryInfoResponse/sequence/return/sequence/isoCode");
		wsdlWizard.addResponseElement("FullCountryInfoResponse/sequence/return/sequence/name");
		wsdlWizard.addResponseElement("FullCountryInfoResponse/sequence/return/sequence/phoneCode");
		
		wsdlWizard.addResponseElement("FullCountryInfoAllCountriesResponse/sequence/return/sequence/capitalCity");
		wsdlWizard.addResponseElement("FullCountryInfoAllCountriesResponse/sequence/return/sequence/continentCode");
		wsdlWizard.addResponseElement("FullCountryInfoAllCountriesResponse/sequence/return/sequence/currencyIsoCode");
		wsdlWizard.addResponseElement("FullCountryInfoAllCountriesResponse/sequence/return/sequence/isoCode");
		wsdlWizard.addResponseElement("FullCountryInfoAllCountriesResponse/sequence/return/sequence/name");
		wsdlWizard.addResponseElement("FullCountryInfoAllCountriesResponse/sequence/return/sequence/phoneCode");
		wsdlWizard.execute();
    }
    private void defineViewTable(String projectName, String viewName, String query, String tableName){
		Matcher<String> matcher = new WithMnemonicTextMatcher("New...");
    	new PushButton(0, matcher).click();	
		MetadataModelWizard wizard = new MetadataModelWizard();
		wizard
			.setLocation(projectName)
			.setModelName(viewName)
			.selectModelClass(ModelClass.RELATIONAL)
			.selectModelType(ModelType.VIEW)
			.finish();
		new DefaultShell("Define View Table");
		//3.2
		new PushButton(1, matcher).click();	
		Properties props = new Properties();
		props.setProperty("sql", query);
		new Table().create(Table.Type.VIEW, tableName, props);
    }
    private void defineProcedure(String procedureName, String query, String uri){
		Matcher<String> matcher = new WithMnemonicTextMatcher("New...");
    	new PushButton(1, matcher).click();
		Properties props = new Properties();
		props.setProperty("sql",query);
		props.setProperty("type", Procedure.Type.RELVIEW_PROCEDURE);
		props.setProperty("params", "nameIN");
		new Procedure().create(procedureName, props);
		new DefaultShell("Define View Procedure");
		new DefaultCombo().setSelection("GET");
		new LabeledText("URI").setText(uri);
		new PushButton("OK").click();
    }
    /**
     * Restore default server after teiid test
     */
    private void cleanAfter(){
    	ServersView view = new ServersView();
		view.open(); 
		List <Server> servers = view.getServers();
		servers.get(1).stop();
		guides.chooseAction("Teiid", "Set the Default ");
		new DefaultCombo().setSelection(0);
		new PushButton("OK").click();
		new DefaultShell("Default Server Changed");
		new PushButton("OK").click();
		servers.get(0).start();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		guides.chooseAction("Teiid", "Refresh ");
		new DefaultCombo().setSelection(0);
		new PushButton("OK").click();
		new DefaultShell("Notification");
		new PushButton("OK").click();
    }
}
