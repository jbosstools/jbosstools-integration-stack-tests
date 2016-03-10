package org.jboss.tools.teiid.reddeer.manager;

import java.util.Properties;

import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.teiid.reddeer.wizard.ImportGeneralItemWizard;
import org.jboss.tools.teiid.reddeer.wizard.ImportJDBCDatabaseWizard;
import org.jboss.tools.teiid.reddeer.wizard.ImportProjectWizard;
import org.jboss.tools.teiid.reddeer.wizard.LdapImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.SalesforceImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.TeiidImportWizard;
import org.jboss.tools.teiid.ui.bot.test.TeiidBot;

public class ImportManager {

	/**
	 * Import file from General: File System
	 */
	@Deprecated
	public void importFromFileSystem(String path, String importFolder) {
		new ImportFileWizard().importFile(path, importFolder);
	}

	/**
	 * Import metadata from Salesforce account
	 * 
	 * @param propsFile
	 */
	/*
	 * public void importFromSalesForce(String projectName, String modelName, String connectionProfile, String
	 * propsFile){ Properties props = new TeiidBot().getProperties(propsFile); this.importFromSalesForce(projectName,
	 * modelName, connectionProfile, props); }
	 */

	public void importFromSalesForce(String projectName, String modelName, String connectionProfile, Properties props) {
		SalesforceImportWizard sfWizard = new SalesforceImportWizard();
		sfWizard.setConnectionProfile(connectionProfile);
		sfWizard.setModelName(modelName);
		sfWizard.setProjectName(projectName);

		String objects = null;
		if ((objects = props.getProperty("deselectedObjects")) != null) {
			sfWizard.setDeselectedObjects(objects.split(","));
		} else if ((objects = props.getProperty("selectedObjects")) != null) {
			sfWizard.setSelectedObjects(objects.split(","));
		}

		sfWizard.execute();
	}

	/**
	 * Import metadata from Oracle, HSQLDB, SQL Server
	 * 
	 * @param connectionProfile
	 * @param propsFile
	 */
	/*
	 * public void importFromDatabase(String projectName, String modelName, String connectionProfile, String propsFile){
	 * Properties props = new TeiidBot().getProperties(propsFile); importFromDatabase(projectName, modelName,
	 * connectionProfile, props); }
	 */

	/**
	 * Import metadata from Oracle, HSQLDB, SQL Server
	 * 
	 * @param connectionProfile
	 * @param propsFile
	 *            itemList (e.g. PUBLIC/PUBLIC/TABLE/PARTS,...)
	 */
	public void importFromDatabase(String projectName, String modelName, String connectionProfile, Properties props, boolean importProcedures) {
		ImportJDBCDatabaseWizard wizard = new ImportJDBCDatabaseWizard();
		wizard.setConnectionProfile(connectionProfile);
		wizard.setProjectName(projectName);
		wizard.setModelName(modelName);
		wizard.setImportProcedures(importProcedures);

		if (props.getProperty("itemList") != null) {
			String loadedProperty = props.getProperty("itemList");
			if (loadedProperty.contains(",")) {
				String[] itemList = loadedProperty.split(",");
				for (String item : itemList) {
					wizard.addItem(item.trim());
				}
			} else {
				wizard.addItem(loadedProperty);
			}
		}
		wizard.execute();// projectName is set on some page of wizard
	}

	// TODO: refactor the parameters
	public void importFromLdap(String projectName, String modelName, String connectionProfile, String connectionUrl,
			String principalDnSuffix, Properties props) {

		LdapImportWizard wizard = new LdapImportWizard();
		wizard.setConnectionProfile(connectionProfile);
		wizard.setModelName(modelName);
		wizard.setProjectName(projectName);
		wizard.setConnectionUrl(connectionUrl);
		wizard.setPrincipalDnSuffix(principalDnSuffix);
		String objects;
		if ((objects = props.getProperty("selectedEntries")) != null) {
			wizard.setSelectedEntries(objects.split(","));
		}
		if ((objects = props.getProperty("selectedColumns")) != null) {
			wizard.setSelectedColumns(objects.split(","));
		}
		wizard.execute();
	}

	/*
	 * public void importFromFlatFile(String projectName, String modelName, String connectionProfile, String propsFile){
	 * Properties props = new TeiidBot().getProperties(propsFile); importFromFlatFile(projectName, modelName,
	 * connectionProfile, props); }
	 */

	public static void importModel(String projectName, TeiidImportWizard importWizard) {// import mgr
		ModelProject modelProject = new TeiidBot().modelExplorer().getModelProject(projectName);
		modelProject.importModel(importWizard);
	}

	/*
	 * public void importFromDDL(String projectName, String modelName, String cpName, String propsFile){ Properties
	 * props = teiidBot.getProperties(propsFile); importFromDDL(projectName, modelName, cpName, props); }
	 */

	/*
	 * public void importFromXML(String projectName, String modelName, String cpName, String propsFile){ Properties
	 * props = teiidBot.getProperties(propsFile); importFromXML(projectName, modelName, cpName, props); }
	 */

	/**
	 * WSDL File or URL >> Source and View Model (SOAP)
	 */
	/*
	 * public void importFromWSDLToSrcView(String projectName, String modelName, String cpName, String propsFile){
	 * Properties props = teiidBot.getProperties(propsFile); importFromWSDLToSrcView(projectName, modelName, cpName,
	 * props); }
	 */

	public void importProject(String location) {// TODO use generalItem instead
		new ImportProjectWizard(location).execute();
	}

	/**
	 * 
	 * @param generalItemType
	 *            ImportGeneralItemWizard.Type
	 * @param itemProps
	 */
	public void importGeneralItem(String generalItemType, Properties itemProps) {
		new ImportGeneralItemWizard(generalItemType, itemProps).execute();
	}

}
