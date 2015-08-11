package org.jboss.tools.teiid.reddeer.wizard;

import java.util.Arrays;

import org.jboss.reddeer.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;

public class LdapImportWizard extends ImportWizardDialog {
	private static final String TABLE_NAME = "Table Name";

	private static final String SOURCE_MODEL_TABLE_ATTRIBUTES = "Source Model Table Attributes";

	private static final String SELECT_FOLDER = "Select a Folder";

	private static final String SOURCE_MODEL_NAME = "Name";

	private static final String SOURCE_MODEL_DEFINITION = "Source Model Definition";

	private static final String CONNECTION_PROFILE = "Connection Profile";
	
	private String connectionProfile;
	private String projectName;
	private String modelName;

	private String connectionUrl;
	private String principalDnSuffix;


	private String[] selectedEntries;
	private String[] selectedColumns;

	public LdapImportWizard() {
		super("Teiid Designer", "LDAP Service >> Source Model");
	}
	
	
	public void execute() {
		open();
		fillModelInfoPage();
		next();
		fillSelectTablesPage();
		new PushButton("Validate").click();
		next();
		fillSelectColumnsPage();
		new PushButton("Validate").click();
		finish();
		new WaitWhile(new IsInProgress(), TimePeriod.SHORT);
	}
	
	private void fillSelectColumnsPage() {
		for(String column : selectedColumns){
			String[] path = column.split("/");
			selectColumnEntry(path[0], path[1], null);
		}
		
	}


	private void fillModelInfoPage(){
		new DefaultShell("Create Relational Model from LDAP Service");
		
		// select connection profile
		new DefaultCombo(new DefaultGroup(CONNECTION_PROFILE), 0).setSelection(connectionProfile);
		
		// select model project
		new PushButton(new DefaultGroup(SOURCE_MODEL_DEFINITION),"...").click();
		new SelectTargetFolder().select(projectName);
		
		// set model name
		new LabeledText(new DefaultGroup(SOURCE_MODEL_DEFINITION),SOURCE_MODEL_NAME).setText(modelName);
	}
	
	private void fillSelectTablesPage(){
		for (String entry : selectedEntries){
			selectTableEntry(entry, null);
		}
	}
	
	public void selectColumnEntry(String tableName, String entryName, String entryAlias){
		new DefaultTreeItem(tableName).expand();
		
		TreeItem entryTreeItem = new DefaultTreeItem(tableName, entryName);
		entryTreeItem.select();
		entryTreeItem.setChecked(true);

		if (entryAlias != null){
			new LabeledText(new DefaultGroup(SOURCE_MODEL_TABLE_ATTRIBUTES),TABLE_NAME).setText(entryAlias);
		}


	}
	
	public void selectTableEntry(String entryName, String entryAlias){
		String treeRootName = getTreeRootName();
		new DefaultTreeItem(treeRootName).expand();
		
		String[] tablePath = entryName.split("/");
		String[] fullPath = new String[tablePath.length + 1];
		fullPath[0] = treeRootName;
		System.arraycopy(tablePath, 0, fullPath, 1, tablePath.length);
		
		TreeItem entryTreeItem = new DefaultTreeItem(fullPath);
		
		entryTreeItem.select();
		entryTreeItem.setChecked(true);

		if (entryAlias != null){
			new LabeledText(new DefaultGroup(SOURCE_MODEL_TABLE_ATTRIBUTES),TABLE_NAME).setText(entryAlias);
		}


	}
	
	private String getTreeRootName(){
		return connectionUrl + '/' + principalDnSuffix;
	}
	
	public void setConnectionProfile(String connectionProfile) {
		this.connectionProfile = connectionProfile;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	public void setConnectionUrl(String connectionUrl){
		this.connectionUrl = connectionUrl;
	}
	
	public void setSelectedEntries(String[] selectedEntries){
		this.selectedEntries = selectedEntries;
	}
	
	public void setPrincipalDnSuffix(String principalDnSuffix){
		this.principalDnSuffix = principalDnSuffix;
	}


	public void setSelectedColumns(String[] selectedColumns) {
		this.selectedColumns = selectedColumns;
	}

}
