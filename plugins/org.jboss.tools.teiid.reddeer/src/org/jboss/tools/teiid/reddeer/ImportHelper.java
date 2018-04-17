package org.jboss.tools.teiid.reddeer;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tab.DefaultTabItem;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.TableEditor;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportJDBCDatabaseWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.TeiidConnectionImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;

public class ImportHelper {
	
	public void checkImportedModelTeiid(String projectName, String modelName, String... tables) {
		for (String table : tables) {
			String[] path = {projectName,modelName+".xmi",table};
			assertTrue(new ModelExplorer().containsItem(path));
		}
	}

	public void importModelTeiid(String projectName,String cpName, String modelName, Map<String,String> teiidImporterProperties,
			TeiidServerRequirement teiidServer) {
		importModelTeiid(projectName,cpName,modelName,teiidImporterProperties, null, teiidServer);
	}
	
	public void importModelTeiid(String projectName,String cpName, String modelName, Map<String,String> teiidImporterProperties,
			TimePeriod timePeriod, TeiidServerRequirement teiidServer) {
		new DefaultShell();
		new ServersViewExt().createDatasource(teiidServer.getName(), cpName);
		
		TeiidConnectionImportWizard wizard = TeiidConnectionImportWizard.openWizard();
		wizard.selectDataSource("java:/" + cpName)
				.nextPage();		

		if(cpName.equals("greenplum") ){
			Properties apacheProps = teiidServer.getServerConfig().getConnectionProfile(cpName).asProperties();
			wizard.setTranslator(apacheProps.getProperty("tran"));
		}
		
		for (Map.Entry<String,String> entry : teiidImporterProperties.entrySet())
		{
			wizard.setImportPropertie(entry.getKey(), entry.getValue());
		}		
		
		wizard.nextPage()
				.setProject(projectName)
				.setModelName(modelName);
		if(timePeriod!=null){
			wizard.nextPageWithWait(timePeriod);
		}else{
			wizard.nextPageWithWait();
		}
		wizard.nextPageWithWait()
				.finish();
	}
	
	public void importModelJDBC(String projectName,String modelName, String connectionProfile, String itemList, 
			boolean importProcedures, boolean updatable) {
		String[] splitList = null;
		if(itemList!=null){
			splitList = itemList.split(",");
		}
		ImportJDBCDatabaseWizard.openWizard()
				.setConnectionProfile(connectionProfile)
				.nextPage()
				.setTableTypes(false, true, false)
				.procedures(importProcedures)
				.nextPage();
		if(itemList!=null){
			ImportJDBCDatabaseWizard.getInstance()
					.setTables(splitList);
		}
		ImportJDBCDatabaseWizard.getInstance()
				.nextPage()
				.setFolder(projectName)
				.setModelName(modelName)
				.setUpdatable(updatable)
				.finish();
	}

	public void importModelJDBC(String projectName, String modelName, String connectionProfile, String itemList, boolean importProcedures) {
		importModelJDBC(projectName, modelName, connectionProfile, itemList, importProcedures, true);
	}

	public void checkImportedTablesInModelJDBC(String projectName, String model, String tableA, String tableB, TeiidServerRequirement teiidServer) {
		assertTrue(new ModelExplorer().containsItem(projectName,model + ".xmi", tableA));
		assertTrue(new ModelExplorer().containsItem(projectName,model + ".xmi", tableB));
		new ModelExplorer().simulateTablesPreview(teiidServer, projectName, model, new String[] { tableA, tableB });
		new ServersViewExt().undeployVdb(teiidServer.getName(), "Check_" + model);
	}
	
	public void checkImportedProcedureInModelJDBC(String projectName, String model, String procedure, TeiidServerRequirement teiidServer, String...parameters) {
		assertTrue(new ModelExplorer().containsItem(projectName,model + ".xmi", procedure));

		String vdb_name = "Check_" + model;	
		VdbWizard.openVdbWizard()
				.setLocation(projectName)
				.setName(vdb_name)
				.addModel(projectName, model + ".xmi")
				.finish();
		new ModelExplorer().deployVdb(projectName, vdb_name);
		
		TeiidJDBCHelper jdbcHelper = new TeiidJDBCHelper(teiidServer, vdb_name);
		ArrayList<String> parametersList = new ArrayList<String>(Arrays.asList(parameters));
		String params = "";
		for (int i = 0; i < parametersList.size(); i++) {
			params += "'" + parametersList.get(i) + "',";
		}
		params = params.substring(0, params.length() - 1);

		String previewSQL =  "exec \"" + model + "\".\"" + procedure + "\"(" + params + ")";
		assertTrue(jdbcHelper.isQuerySuccessful(previewSQL,false));
	}
	/**
	 * true if the text in the table (the intersection of the row and column) is equal to the argument
	 */
	public boolean checkNameInTableJDBC(String value,int row, int column){
		new DefaultCTabItem("Table Editor").activate();
		new DefaultTabItem("Columns").activate();
		List<TableItem> items = new DefaultTable().getItems();
		String nis = items.get(row).getText(column);
		return value.equals(nis);
	}
	
	public boolean checkUpdatableModelJDBC(String projectName, String model,boolean updatable){
		boolean result = false;
		String expectedValue = String.valueOf(updatable);
		new ModelExplorer().openModelEditor(projectName, model + ".xmi");
		RelationalModelEditor editor = new RelationalModelEditor(model + ".xmi");
		TableEditor tableEditor = editor.openTableEditor();
		for (int i=0;i<tableEditor.getRows().size();i++){
			if(expectedValue.equals(tableEditor.getCellText(i, 5))){
				result = true;
			}else{
				return false;
			}
		}
		return result;
	}
}
