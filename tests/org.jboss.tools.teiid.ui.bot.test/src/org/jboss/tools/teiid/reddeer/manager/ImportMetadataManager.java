package org.jboss.tools.teiid.reddeer.manager;

import java.util.ArrayList;
import java.util.Properties;

import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.wizard.CreateMetadataModel;
import org.jboss.tools.teiid.reddeer.wizard.DDLImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.FlatImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.MetadataImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.TeiidConnectionImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.TeiidImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.WsdlImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.WsdlWebImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.XMLImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.XMLSchemaImportWizard;
import org.jboss.tools.teiid.ui.bot.test.TeiidBot;

public class ImportMetadataManager {
	private static TeiidBot teiidBot = new TeiidBot();
	
	/**
	 * Teiid Designer: WSDL File or URL >> Web Service Model
	 */
	public void importWSDLToWSModel(String name, String project, String wsdl){
		new WsdlWebImportWizard().importWsdl(name, project, wsdl);
	}
	
	/**
	 * File Source (Flat) >> Source and View Model
	 * @param projectName
	 * @param modelName
	 * @param connectionProfile
	 * @param props
	 */
	public void importFromFlatFile(String projectName, String modelName, String connectionProfile, Properties props){
		FlatImportWizard importWizard = new FlatImportWizard();
		importWizard.setProfile(connectionProfile);
		
		String loadedProperty = null;
		/*if ((loadedProperty = props.getProperty("sourceModelName")) != null){
			importWizard.setSourceModelName(loadedProperty);
		}*/
		importWizard.setSourceModelName(modelName);
		
		if ((loadedProperty = props.getProperty("file")) != null){
			importWizard.setFile(loadedProperty);
		}
		if ((loadedProperty = props.getProperty("importMode")) != null){
			importWizard.setImportMode(loadedProperty);
		}
		if ((loadedProperty = props.getProperty("viewModelName")) != null){
			importWizard.setViewModelName(loadedProperty);
		} 
		if ((loadedProperty = props.getProperty("viewTableName")) != null){
			importWizard.setViewTableName(loadedProperty);
		}
		if ((loadedProperty = props.getProperty("headerLine")) != null){
			importWizard.setHeaderLine(Integer.parseInt(loadedProperty));
		}
		if ((loadedProperty = props.getProperty("dataLine")) != null){
			importWizard.setDataLine(Integer.parseInt(loadedProperty));
		}
		if ((loadedProperty = props.getProperty("editDelimiterCharacter")) != null){
			importWizard.setEditDelimiterCharacter(Boolean.parseBoolean(loadedProperty));
		}
		if ((loadedProperty = props.getProperty("editTexttableFunctionOptions")) != null){
			importWizard.setEditTexttableFunctionOptions(Boolean.parseBoolean(loadedProperty));
		}
		if ((loadedProperty = props.getProperty("delimiterCharacter")) != null){
			importWizard.setOtherDelimiter(loadedProperty);
		}
		if ((loadedProperty = props.getProperty("texttableFunctionOptions")) != null){
			String[] itemList = loadedProperty.split(",");
			ArrayList<String> options = new ArrayList<String>();
			for (String item : itemList){
				options.add(item.trim());
			}
			importWizard.setTexttableFunctionOptions(options);
		}
		importModel(projectName, importWizard);
	}
	
	public void importFromDDL(String projectName, String modelName, String ddlPath, Properties importProps){
		DDLImportWizard importWizard = new DDLImportWizard();
		String ddl = teiidBot.toAbsolutePath(ddlPath);//if already absolute, returned as is
		importWizard.setDdlPath(ddl);
		importWizard.setModelName(modelName);
		
		String loadedProperty = null;
		if ((loadedProperty = importProps.getProperty("autoselectDialect")) != null){
			importWizard.setAutoselectDialect(Boolean.getBoolean(loadedProperty));
		}
		importModel(projectName, importWizard);
	}
	
	/**
	 * File Source (XML) >> Source and View Model
	 * @param projectName
	 * @param modelName
	 * @param cpName
	 * @param props
	 */
	public void importFromXML(String projectName, String modelName, String cpName, Properties props){
		XMLImportWizard importWizard = new XMLImportWizard();
		String loadedProperty = null;
		importWizard.setName(modelName);
		
		if ((loadedProperty = props.getProperty("local")) != null){
			importWizard.setLocal(Boolean.getBoolean(loadedProperty));
		}
		
		importWizard.setProfileName(cpName);
		if ((loadedProperty = props.getProperty("rootPath")) != null){
			importWizard.setRootPath(loadedProperty);
		}
		
		if ((loadedProperty = props.getProperty("elements")) != null){
			String[] elements = loadedProperty.split(",");
			for (String element : elements){
				importWizard.addElement(element.trim());
			}
		}
		importModel(projectName, importWizard);
	}
	
	/**
	 * WSDL File or URL >> Source and View Model (SOAP)
	 */
	public void importFromWSDLToSrcView(String projectName, String cpName, Properties props){
		WsdlImportWizard importWizard = new WsdlImportWizard();
		importWizard.setProfile(cpName);
		String loadedProperty = null;
		if ((loadedProperty = props.getProperty("requestElements")) != null){
			String[] elements = loadedProperty.split(",");
			for (String element : elements){
				importWizard.addRequestElement(element.trim());
			}
		}
		if ((loadedProperty = props.getProperty("responseElements")) != null){
			String[] elements = loadedProperty.split(",");
			for (String element : elements){
				importWizard.addResponseElement(element.trim());
			}
		}
		importModel(projectName, importWizard);
	}
	
	/**
	 * Designer Text File >> Source or View Models
	 * @param projectName
	 * @param props
	 */
	public void importFromDesignerTextFile(String projectName, Properties props){
		MetadataImportWizard importWizard = new MetadataImportWizard();
		String loadedProperty = null;
		if ((loadedProperty = props.getProperty("importType")) != null){
			importWizard.setImportType(loadedProperty);
		}
		if ((loadedProperty = props.getProperty("source")) != null){
			importWizard.setSource(loadedProperty);
		}
		if ((loadedProperty = props.getProperty("target")) != null){
			importWizard.setTarget(loadedProperty);
		}
		importModel(projectName, importWizard);
	}
	
	public static void importModel(String projectName, TeiidImportWizard importWizard) {//import mgr
		ModelProject modelProject = new TeiidBot().modelExplorer().getModelProject(projectName);
		modelProject.importModel(importWizard);
	}
	
	/**
	 * Teiid Connection >> Source Model
	 */
	public void importFromTeiidConnection(String projectName, String modelName, Properties importProps, Properties dataSourceProps){
		TeiidConnectionImportWizard importWizard = new TeiidConnectionImportWizard();
		importWizard.setModelName(modelName);
		importWizard.setProjectName(projectName);
		if (dataSourceProps != null){
			importWizard.setDataSourceProperties(dataSourceProps);
		}
		String loadedProperty = null;

		if ((loadedProperty = importProps.getProperty("createNewDataSource")) != null){
			importWizard.setCreateNewDataSource(Boolean.getBoolean(loadedProperty));
		}
		if ((loadedProperty = importProps.getProperty("deleteDataSource")) != null){
			importWizard.setDeleteDataSource(Boolean.getBoolean(loadedProperty));
		}
		if ((loadedProperty = importProps.getProperty("editDataSource")) != null) {
			importWizard.setEditDataSource(Boolean.getBoolean(loadedProperty));
		}
		if ((loadedProperty = importProps.getProperty("copyDataSource")) != null) {
			importWizard.setCopyDataSource(Boolean.getBoolean(loadedProperty));
		}
		if ((loadedProperty = importProps.getProperty("copiedDataSourceName")) != null) {
			importWizard.setCopiedDataSourceName(loadedProperty);
		}
		if ((loadedProperty = importProps.getProperty("newDataSourceName")) != null) {
			importWizard.setNewDataSourceName(loadedProperty);
		}
		if ((loadedProperty = importProps.getProperty("driverName")) != null) {
			importWizard.setDriverName(loadedProperty);
		}
		if ((loadedProperty = importProps.getProperty("dataSourceName")) != null) {
			importWizard.setDataSourceName(loadedProperty);
		}
		if ((loadedProperty = importProps.getProperty("tablesToImport")) != null) {
			importWizard.setTablesToImport(loadedProperty.split(","));
		}
		if ((loadedProperty = importProps.getProperty("endAfterCreatingDS")) != null) {
			importWizard.setEndAfterCreatingDS(loadedProperty);
		}
		if ((loadedProperty = importProps.getProperty("translator")) != null) {
			importWizard.setTranslator(loadedProperty);
		}
		importWizard.execute();
	}
	
	/**
	 * New Teiid Metadata Model
	 */
	public void createNewMetadataModel(String projectName, String modelName, Properties props){
		//CreateMetadataModel
		CreateMetadataModel newModel = new CreateMetadataModel();
		String loadedProperty = null;
		
		newModel.setLocation(projectName);
		newModel.setName(modelName);
		if ((loadedProperty = props.getProperty("clazz")) != null){
			newModel.setClass(loadedProperty);
		}
		if ((loadedProperty = props.getProperty("type")) != null){
			newModel.setType(loadedProperty);
		}
		if ((loadedProperty = props.getProperty("modelBuilder")) != null){
			newModel.setModelBuilder(loadedProperty);
		}
		if ((loadedProperty = props.getProperty("pathToXmlSchema")) != null){
			String[] path = loadedProperty.split("/");
			newModel.setPathToXmlSchema(path);
		}
		if ((loadedProperty = props.getProperty("rootElement")) != null){
			newModel.setModelBuilder(loadedProperty);
		}
		if ((loadedProperty = props.getProperty("pathToExistingModel")) != null){
			String[] path = loadedProperty.split("/");
			newModel.setPathToExistingModel(path);
		}
		
		newModel.execute();
	}
	
	public void importXMLSchema(String projectName, Properties props){
		XMLSchemaImportWizard wizard = new XMLSchemaImportWizard();
		String loadedProperty = null;
		if ((loadedProperty = props.getProperty("local")) != null){
			wizard.setLocal(Boolean.getBoolean(loadedProperty));
		}
		if ((loadedProperty = props.getProperty("rootPath")) != null){
			wizard.setRootPath(loadedProperty);//should be absolute path -- set in Properties!
		}
		if ((loadedProperty = props.getProperty("schemas")) != null){
			String[] schemas = loadedProperty.split(",");
			wizard.setSchemas(schemas);
		}
		if ((loadedProperty = props.getProperty("destination")) != null){
			wizard.setDestination(loadedProperty);
		}
		importModel(projectName, wizard);
	}

}
