package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.matcher.WithMnemonicTextMatcher;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersViewException;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.CancelButton;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsKilled;
import org.eclipse.reddeer.workbench.handler.EditorHandler;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.hamcrest.Matcher;
import org.jboss.tools.common.reddeer.JiraClient;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.condition.IsPreviewInProgress;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.ResourceFileHelper;
import org.jboss.tools.teiid.reddeer.connection.SimpleHttpClient;
import org.jboss.tools.teiid.reddeer.dialog.CreateWarDialog;
import org.jboss.tools.teiid.reddeer.dialog.TableDialog;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.GuidesView;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.SQLResult;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase.FlatLocalConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase.XmlLocalConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase.XmlRemoteConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.FlatImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportJDBCDatabaseWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.WsdlImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.XMLImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.NewProcedureWizard;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
/**
 * @author mkralik
 */

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles={
	ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER,
	ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER,
    ConnectionProfileConstants.SOAP
})

public class GuidesTest {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	private static final String CONNECTION_PROFILE= ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER;
	private static final GuidesView guides = new GuidesView();
	private static final ResourceFileHelper fileHelper = new ResourceFileHelper();

	@Before
	public void openPerspective() {
		TeiidPerspective.activate();
		EditorHandler.getInstance().closeAll(false);
		new ModelExplorer().deleteAllProjects(false);

        guides.setDefaultTeiidInstance(teiidServer.getName());
        ServersView2 view = new ServersView2();
        view.open();
        try {
            view.getServer(teiidServer.getName()).start();
        } catch (ServersViewException ex) {
            // server is running
        }
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

        new RelationalModelEditor(model_JDBC_name + ".xmi").save();
		guides.previewDataViaActionSet(actionSet,project_JDBC_name, model_JDBC_name+".xmi","PARTS"); 
		assertTrue(testLastPreview());
		
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
		
		guides.createProjectViaGuides(actionSet, project_SOAP_name);

		guides.chooseAction(actionSet, "Create Web ");
        DefaultShell cp = new DefaultShell("New Connection Profile");
        new CancelButton(cp).click();

		guides.chooseAction(actionSet, "Generate "); 
		new DefaultShell("Create Relational Model from Web Service");
        wsdlImportWizard(ConnectionProfileConstants.SOAP, model_SOAP_name, view_SOAP_name);
		
		guides.previewDataViaActionSet(actionSet, project_SOAP_name, view_SOAP_name+".xmi","FullCountryInfoAllCountries");
		assertTrue(testLastPreview());

		guides.defineVDB(actionSet, project_SOAP_name, vdb_SOAP_name, view_SOAP_name);

		assertTrue(guides.editVDB(actionSet, project_SOAP_name, vdb_SOAP_name));

		List<Integer> results = guides.executeVDB(actionSet, teiidServer, vdb_SOAP_name,test_SQL,test_SQL2);
		assertEquals(Integer.valueOf(1),results.get(0));
		assertEquals(Integer.valueOf(3),results.get(1));
	}

    @Test
	public void Rest() throws IOException{
 		String actionSet = "Create a REST WAR";
		String project_REST_name = "REST_WAR";
		String model_REST_name = "REST_WarSource";
		String view_REST_name = "REST_WarView";
		String table_REST_name = "viewSupplier";
		String procedure_REST_name = "getSupplierByName";
		String query_view = "SELECT * FROM REST_WARSource.SUPPLIER";
		String query_procedure = fileHelper.getSql("GuidesTest/restProcedure");
		String uriRest = "supplier/{nameIN}";
		String vdb_REST_name = "Rest_WarVDB";
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
		new RelationalModelEditor(view_REST_name+".xmi").save();

		guides.previewDataViaActionSetWithParam(actionSet, param, project_REST_name, view_REST_name+".xmi",procedure_REST_name); 
		assertTrue(testLastPreview());

		guides.defineVDB(actionSet, project_REST_name,vdb_REST_name, view_REST_name);
		
		guides.chooseAction(actionSet, "Deploy VDB");
		new CheckBox("Create Data Source for this VDB").click();
		new PushButton("OK").click();
		new WaitWhile(new IsInProgress(), TimePeriod.DEFAULT);

		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.deployVdb(project_REST_name, vdb_REST_name);
		
		CreateWarDialog dialog = modelExplorer.generateWar(false, project_REST_name, vdb_REST_name);
		dialog.setVdbJndiName(vdb_REST_name)
			.setWarFileLocation(modelExplorer.getProjectPath(project_REST_name));
		dialog.finish();
		
		guides.chooseAction(actionSet, "Deploy WAR file "); 
		new DefaultShell("Deploy WAR Instructions");
		new PushButton("OK").click();
		
		modelExplorer.deployWar(teiidServer, project_REST_name,vdb_REST_name);
		String url = "http://localhost:8080/"+vdb_REST_name+"/REST_WarView/supplier/Park";
		
        assertEquals(result, new SimpleHttpClient().get(url));
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
		String fileName = "supplier.csv";
		
		guides.createProjectViaGuides(actionSet, project_Flat_name);
		
		guides.chooseAction(actionSet, "Create Teiid flat ");
		
		new DefaultShell("New Connection Profile");
		new LabeledText("Name:").setText(flatProfile);
		new PushButton("Next >").click();
		FlatLocalConnectionProfileWizard.getInstance()
				.setFile("resources/guides")
				.finish();

		guides.chooseAction(actionSet, "Create source model from ");
		createFlatLocalSource(flatProfile,fileName,project_Flat_name,model_Flat_name, view_Flat_name,view_Flat_table);
		
		guides.previewDataViaActionSet(actionSet,project_Flat_name, view_Flat_name+".xmi",view_Flat_table);
		assertTrue(testLastPreview());

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
		new LabeledText("Name:").setText(xmlLocalprofile);
		new PushButton("Next >").click();
		XmlLocalConnectionProfileWizard.getInstance()
				.setFile("resources/guides/supplier.xml")
				.finish();

		guides.chooseAction(actionSet, "Create source model from XML file source");
		new DefaultShell("Import From XML File Source");	
		XMLImportWizard.getInstance()
			   .setImportMode(XMLImportWizard.LOCAL)
			   .nextPage()
			   .setDataFileSource(xmlLocalprofile)
			   .setSourceModelName(xmlLocalName + "Source")
			   .nextPage()
			   .setJndiName(xmlLocalName + "Source")
			   .nextPage()
			   .setRootPath("/SUPPLIERS/SUPPLIER")
			   .addElement("SUPPLIERS/SUPPLIER/SUPPLIER_ID")
			   .addElement("SUPPLIERS/SUPPLIER/SUPPLIER_NAME")
			   .addElement("SUPPLIERS/SUPPLIER/SUPPLIER_STATUS")
			   .addElement("SUPPLIERS/SUPPLIER/SUPPLIER_CITY")
			   .addElement("SUPPLIERS/SUPPLIER/SUPPLIER_STATE")
			   .nextPage()
			   .setViewModelName(xmlLocalName + "View")
			   .setViewTableName(xmlLocalName + "Table")
			   .finish();
		
		guides.previewDataViaActionSet(actionSet,xmlLocalprofile, xmlLocalName+"View.xmi",xmlLocalName+"Table");
		assertTrue(testLastPreview());

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
		new LabeledText("Name:").setText(xmlRemoteprofile);
		new PushButton("Next >").click();
		XmlRemoteConnectionProfileWizard.getInstance()
				.setUrl("https://raw.githubusercontent.com/mmakovy/import-files/master/cd_catalog.xml")
				.testConnection()
				.finish();
		
		guides.chooseAction(actionSet, "Create source model from remote XML");
		new DefaultShell("Import From XML File Source");
		XMLImportWizard.getInstance()
			   .setImportMode(XMLImportWizard.REMOTE)
			   .nextPage()
			   .setDataFileSource(xmlRemoteprofile)
			   .setSourceModelName(xmlRemoteName + "Source")
			   .nextPage()
			   .setJndiName(xmlRemoteName + "Source")
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
			   .setViewModelName(xmlRemoteName + "View")
			   .setViewTableName(xmlRemoteName + "Table")
			   .finish();
		
		
		guides.previewDataViaActionSet(actionSet,xmlRemoteprofile, xmlRemoteName+"View.xmi",xmlRemoteName+"Table");
		assertTrue(testLastPreview());

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
		
		guides.createDataSource(dataSource, ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER);
		
		guides.createSourceModelFromTeiid(modelName, dataSource,null,"dbo");
		
		TeiidPerspective.activate();
		
		new WaitUntil(new JobIsKilled("Refreshing server adapter list"), TimePeriod.LONG, false); //windows 10

		guides.chooseAction(actionSet, "Set Connection Profile");
		new DefaultShell("Set Connection Profile");
        new DefaultTreeItem("Teiid Importer Connections", "TeiidImportCP_java:/" + dataSource).select();
		new PushButton("OK").click();
		
		new WaitWhile(new IsPreviewInProgress(), TimePeriod.DEFAULT);
		new WaitWhile(new IsInProgress(), TimePeriod.DEFAULT);
		new ShellMenuItem(new WorkbenchShell(), "File", "Save All").select();

		guides.previewDataViaActionSet(actionSet, projectName, modelName+".xmi","STATUS"); 	
		assertTrue(testLastPreview());

		guides.defineVDB(actionSet, projectName, vdbName, modelName);
				
		List<Integer> results = guides.executeVDB(actionSet, teiidServer, vdbName,test_SQL);
		assertEquals(Integer.valueOf(3),results.get(0));	
	}

	@Test
	public void teiid(){
        String serverName = "testServer";
        try {
            EditorHandler.getInstance().closeAll(false);

            guides.newServer(serverName);

            assertTrue(guides.editServer(serverName));

            guides.setDefaultTeiidInstance(serverName);

            guides.startAndRefreshServer(serverName, teiidServer.getName());
        } finally {
            cleanAfter(serverName);
        }
    }
	
    private void createJDBCSource(String modelName,String projectName,String cp_name){
    	ImportJDBCDatabaseWizard.getInstance()
				.setConnectionProfile(cp_name)
				.nextPage()
				.setTableTypes(false, true, false)
				.nextPage()
				.nextPage()
				.setFolder(projectName)
				.setModelName(modelName)
				.finish();
    }
    private void createFlatLocalSource(String profile, String fileName,String projectName,String modelName, String viewModelName, String viewTableName){
    	FlatImportWizard.getInstance()
				.selectLocalFileImportMode()
				.nextPage()
				.selectProfile(profile)
				.selectFile(fileName)
				.setSourceModel(modelName)
				.setProject(projectName)
				.nextPage()
				.setJndiName(modelName)
				.nextPage()
				.nextPage()
				.nextPage()
				.setViewModel(viewModelName)
				.setViewTable(viewTableName)
				.finish();
    }
    private void wsdlImportWizard(String soapProfile, String model_SOAP_name, String view_SOAP_name){
    	WsdlImportWizard.getInstance()
            .setConnectionProfile(soapProfile).selectOperations("FullCountryInfo", "FullCountryInfoAllCountries")
            .nextPage().setSourceModelName(model_SOAP_name).setViewModelName(view_SOAP_name).nextPage()
            .setJndiName("java:/" + model_SOAP_name).nextPage().nextPage()
            .addRequestElement("FullCountryInfo/sequence/arg0")
            .addResponseElement("FullCountryInfo", "FullCountryInfoResponse/sequence/return/sequence/capitalCity")
            .addResponseElement("FullCountryInfo", "FullCountryInfoResponse/sequence/return/sequence/continentCode")
            .addResponseElement("FullCountryInfo", "FullCountryInfoResponse/sequence/return/sequence/currencyIsoCode")
            .addResponseElement("FullCountryInfo", "FullCountryInfoResponse/sequence/return/sequence/isoCode")
            .addResponseElement("FullCountryInfo", "FullCountryInfoResponse/sequence/return/sequence/name")
            .addResponseElement("FullCountryInfo", "FullCountryInfoResponse/sequence/return/sequence/phoneCode")

            .addResponseElement("FullCountryInfoAllCountries",
                "FullCountryInfoAllCountriesResponse/sequence/return/sequence/capitalCity")
            .addResponseElement("FullCountryInfoAllCountries",
                "FullCountryInfoAllCountriesResponse/sequence/return/sequence/continentCode")
            .addResponseElement("FullCountryInfoAllCountries",
                "FullCountryInfoAllCountriesResponse/sequence/return/sequence/currencyIsoCode")
            .addResponseElement("FullCountryInfoAllCountries",
                "FullCountryInfoAllCountriesResponse/sequence/return/sequence/isoCode")
            .addResponseElement("FullCountryInfoAllCountries",
                "FullCountryInfoAllCountriesResponse/sequence/return/sequence/name")
            .addResponseElement("FullCountryInfoAllCountries",
                "FullCountryInfoAllCountriesResponse/sequence/return/sequence/phoneCode")
            .finish();
    }
    private void defineViewTable(String projectName, String viewName, String query, String tableName){
		Matcher<String> matcher = new WithMnemonicTextMatcher("New...");
    	new PushButton(0, matcher).click();	
		MetadataModelWizard.getInstance()
				.setLocation(projectName)
				.setModelName(viewName)
				.selectModelClass(MetadataModelWizard.ModelClass.RELATIONAL)
				.selectModelType(MetadataModelWizard.ModelType.VIEW)
				.finish();
		new DefaultShell("Define View Table");
		//3.2
        new PushButton(1, matcher).click();
        new RadioButton("Option 1: Build with new table wizard").click();
        new OkButton().click();
		new TableDialog(true)
				.setName(tableName)
				.setTransformationSql(query)
				.finish();	
    }
    private void defineProcedure(String procedureName, String query, String uri){
    	new DefaultShell("Define View Procedure");
    	Matcher<String> matcher = new WithMnemonicTextMatcher("New...");
    	new PushButton(1, matcher).click();
    	NewProcedureWizard.createViewProcedure()
    			.setName(procedureName)
    			.setTransformationSql(query)
				.addParameter("nameIN", "string", "40", "IN")
				.finish();
		
		new DefaultShell("Define View Procedure");
		new DefaultCombo().setSelection("GET");
		new LabeledText("URI").setText(uri);
		new PushButton("OK").click();
    }
    /**
     * Restore default server after teiid test
     */
    private void cleanAfter(String serverName){
        ServersViewExt viewEx = new ServersViewExt();
        viewEx.stopServer(serverName);
        viewEx.setDefaultTeiidInstance(teiidServer.getName());
        viewEx.startServer(teiidServer.getName());
        viewEx.refreshServer(teiidServer.getName());
    }
    
    private boolean testLastPreview(){
    	if(new ShellIsAvailable("Internal Error").test()){ //problem with preview data on the Linux (if SWT_GTK3=0 is set)
    		new PushButton("No").click();
    	}
    	DatabaseDevelopmentPerspective.getInstance().getSqlResultsView();
		List<TreeItem> items = new DefaultTree().getItems();
		String status = items.get(items.size() - 1).getCell(0);
		TeiidPerspective.activate();
		return SQLResult.STATUS_SUCCEEDED.equals(status);
    }
}
