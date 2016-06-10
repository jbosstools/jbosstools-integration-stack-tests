package org.jboss.tools.teiid.reddeer.manager;

import java.util.Properties;

import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.wizard.DDLImportWizard;
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
	public void importWSDLToWSModel(String name, String project, String wsdl) {
		new WsdlWebImportWizard().importWsdl(name, project, wsdl);
	}
	
	/**
	 * Teiid Designer: DDL File >> Source or View Model
	 * @param wizardType - can be DDLImportWizard.CUSTOM_WIZARD or DDLImportWizard.TEIID_WIZARD
	 * @param importProps - can contains 'autoselectDialect' and 'modelType'(DDLImportWizard.Source_Type or DDLImportWizard.View_Type)
	 */
	@Deprecated //use DDLCustomImportWizard or DDLTeiidImportWizard
	public void importFromDDL(String projectName, String modelName, String ddlPath, String wizardType, Properties importProps) {
		if(!(wizardType.equals(DDLImportWizard.CUSTOM_WIZARD)||wizardType.equals(DDLImportWizard.TEIID_WIZARD))){
			throw new IllegalArgumentException("ddl wizard type is invalid");
		}
		DDLImportWizard importWizard = new DDLImportWizard(wizardType);
		String ddl = teiidBot.toAbsolutePath(ddlPath);// if already absolute, returned as is
		importWizard.setDdlPath(ddl);
		importWizard.setModelName(modelName);
		String modelType = null;
		if ((modelType = importProps.getProperty("modelType")) != null) {
			if(!(modelType.equals(DDLImportWizard.Source_Type)||modelType.equals(DDLImportWizard.View_Type))){
				throw new IllegalArgumentException("model type for ddl wizard is invalid");
			}
			importWizard.setModelType(modelType);
		}
		String loadedProperty = null;
		if ((loadedProperty = importProps.getProperty("autoselectDialect")) != null) {
			importWizard.setAutoselectDialect(Boolean.valueOf(loadedProperty));
		}
		importModel(projectName, importWizard);
		new WorkbenchShell();
	}

	/**
	 * File Source (XML) >> Source and View Model
	 * 
	 * @param projectName
	 * @param modelName
	 * @param cpName
	 * @param props
	 */
	public void importFromXML(String projectName, String modelName, String cpName, Properties props) {
		XMLImportWizard importWizard = new XMLImportWizard();
		String loadedProperty = null;
		importWizard.setName(modelName);

		if ((loadedProperty = props.getProperty("local")) != null) {
			importWizard.setLocal(Boolean.valueOf(loadedProperty));
		}

		importWizard.setProfileName(cpName);
		if ((loadedProperty = props.getProperty("rootPath")) != null) {
			importWizard.setRootPath(loadedProperty);
		}

		if ((loadedProperty = props.getProperty("elements")) != null) {
			String[] elements = loadedProperty.split(",");
			for (String element : elements) {
				importWizard.addElement(element.trim());
			}
		}
		importModel(projectName, importWizard);
	}

	/**
	 * WSDL File or URL >> Source and View Model (SOAP)
	 */
	public void importFromWSDLToSrcView(String projectName, String cpName, Properties props) {
		WsdlImportWizard importWizard = new WsdlImportWizard();
		importWizard.setProfile(cpName);
		String loadedProperty = null;
		if ((loadedProperty = props.getProperty("requestElements")) != null) {
			if (loadedProperty.contains(",")) {
				String[] elements = loadedProperty.split(",");
				for (String element : elements) {
					importWizard.addRequestElement(element.trim());
				}
			} else {
				importWizard.addRequestElement(loadedProperty);
			}

		}
		if ((loadedProperty = props.getProperty("responseElements")) != null) {
			if (loadedProperty.contains(",")) {
				String[] elements = loadedProperty.split(",");
				for (String element : elements) {
					importWizard.addResponseElement(element.trim());
				}
			} else {
				importWizard.addResponseElement(loadedProperty);
			}

		}
		importModel(projectName, importWizard);
	}

	/**
	 * WSDL File or URL >> Web Service Model
	 * 
	 * @param importProps
	 *            modelName, project, [wsdlName OR wsdlUrl, securityType (HTTPBasic), username, password],
	 *            [modelNameResponses], [generateVirtualXML]
	 * @param wsdlLocation
	 *            WsdlWebImportWizard.IMPORT_WSDL_FROM_WORKSPACE, IMPORT_WSDL_FROM_URL
	 */
	public void importFromWSDLToWebService(Properties importProps, String wsdlLocation) {
		WsdlWebImportWizard importWizard = new WsdlWebImportWizard();
		importWizard.importWsdl(importProps, wsdlLocation);
	}

	/**
	 * Designer Text File >> Source or View Models
	 * 
	 * @param projectName
	 * @param props
	 */
	public void importFromDesignerTextFile(String projectName, Properties props) {
		MetadataImportWizard importWizard = new MetadataImportWizard();
		String loadedProperty = null;
		if ((loadedProperty = props.getProperty("importType")) != null) {
			importWizard.setImportType(loadedProperty);
		}
		if ((loadedProperty = props.getProperty("source")) != null) {
			importWizard.setSource(loadedProperty);
		}
		if ((loadedProperty = props.getProperty("target")) != null) {
			importWizard.setTarget(loadedProperty);
		}
		importModel(projectName, importWizard);
	}

	public static void importModel(String projectName, TeiidImportWizard importWizard) {// import mgr
		ModelProject modelProject = new TeiidBot().modelExplorer().getModelProject(projectName);
		modelProject.importModel(importWizard);
	}

	public void importFromTeiidConnection(String projectName, String modelName, Properties importProps,
			Properties dataSourceProps) {
		importFromTeiidConnection(projectName, modelName, importProps, dataSourceProps, null);
	}

	/**
	 * Teiid Connection >> Source Model
	 */
	public void importFromTeiidConnection(String projectName, String modelName, Properties importProps,
			Properties dataSourceProps, Properties teiidImporterProperties) {
		TeiidConnectionImportWizard importWizard = new TeiidConnectionImportWizard();
		importWizard.setModelName(modelName);
		importWizard.setProjectName(projectName);
		importWizard.setTeiidImporterProperties(teiidImporterProperties);
		if (dataSourceProps != null) {
			importWizard.setDataSourceProperties(dataSourceProps);
		}
		String loadedProperty = null;

		if ((loadedProperty = importProps.getProperty("createNewDataSource")) != null) {
			importWizard.setCreateNewDataSource(Boolean.valueOf(loadedProperty));
		}
		if ((loadedProperty = importProps.getProperty("deleteDataSource")) != null) {
			importWizard.setDeleteDataSource(Boolean.valueOf(loadedProperty));
		}
		if ((loadedProperty = importProps.getProperty("editDataSource")) != null) {
			importWizard.setEditDataSource(Boolean.valueOf(loadedProperty));
		}
		if ((loadedProperty = importProps.getProperty("copyDataSource")) != null) {
			importWizard.setCopyDataSource(Boolean.valueOf(loadedProperty));
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
	 * 
	 * @param projectName
	 * @param props
	 *            local true: rootPath, schemas, destination<br />
	 *            local false: xmlSchemaURL, username, password, verifyHostname, addDependentSchemas
	 */
	public void importXMLSchema(String projectName, Properties props) {
		XMLSchemaImportWizard wizard = new XMLSchemaImportWizard();
		String loadedProperty = null;
		if ((loadedProperty = props.getProperty("local")) != null) {
			wizard.setLocal(Boolean.valueOf(loadedProperty));
		}
		if ((loadedProperty = props.getProperty("rootPath")) != null) {
			wizard.setRootPath(loadedProperty);// should be absolute path -- set in Properties!
		}
		if ((loadedProperty = props.getProperty("schemas")) != null) {
			if (loadedProperty.contains(",")) {
				String[] schemas = loadedProperty.split(",");
				wizard.setSchemas(schemas);
			} else {
				wizard.setSchemas(new String[] { loadedProperty });
			}

		}
		if ((loadedProperty = props.getProperty("destination")) != null) {
			wizard.setDestination(loadedProperty);
		}

		if ((loadedProperty = props.getProperty("xmlSchemaURL")) != null) {
			wizard.setXmlSchemaURL(loadedProperty);
		}
		if ((loadedProperty = props.getProperty("username")) != null) {
			wizard.setUsername(loadedProperty);
		}
		if ((loadedProperty = props.getProperty("password")) != null) {
			wizard.setPassword(loadedProperty);
		}
		if ((loadedProperty = props.getProperty("verifyHostname")) != null) {
			wizard.setVerifyHostname(Boolean.valueOf(loadedProperty));
		}
		if ((loadedProperty = props.getProperty("addDependentSchemas")) != null) {
			wizard.setAddDependentSchemas(Boolean.valueOf(loadedProperty));
		}

		importModel(projectName, wizard);
	}

}
