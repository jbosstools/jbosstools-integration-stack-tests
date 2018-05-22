package org.jboss.tools.teiid.reddeer.wizard.imports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.NextButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tab.DefaultTabItem;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;

/**
 * Imports JDBC Database to Teiid project.
 * 
 * @author Lucia Jelinkova
 */
public class ImportJDBCDatabaseWizard extends TeiidImportWizard {
	
	private static final String DIALOG_TITLE = "Import Database via JDBC";
	
	private ImportJDBCDatabaseWizard() {
		super(DIALOG_TITLE, "JDBC Database >> Source Model");
		log.info("JDBC Database import wizard is opened");
	}

	public static ImportJDBCDatabaseWizard getInstance(){
		return new ImportJDBCDatabaseWizard();
	}
	
	public static ImportJDBCDatabaseWizard openWizard(){
		ImportJDBCDatabaseWizard wizard = new ImportJDBCDatabaseWizard();
		wizard.open();
		return wizard;
	}
	
	public ImportJDBCDatabaseWizard nextPage(){
		log.info("Go to next wizard page");
		new NextButton().click();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		return this;
	}
	
	@Override
	public void finish() {
		super.finish();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		new WaitWhile(new ShellIsAvailable(DIALOG_TITLE), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	public ImportJDBCDatabaseWizard activateWizard() {
        new DefaultShell(DIALOG_TITLE).setFocus();
		return this;
	}
	
	public ImportJDBCDatabaseWizard setConnectionProfile(String connectionProfile) {
		log.info("Set connection profile: '" + connectionProfile + "'");
		activateWizard();
		new DefaultCombo(0).setSelection(connectionProfile);
		return this;
	}
	
	public ImportJDBCDatabaseWizard setJDBCMetadataProcessor(String metadataProcessor) {
		log.info("Set JDBC Metadata Processor: '" + metadataProcessor + "'");
		activateWizard();
		new DefaultCombo(1).setSelection(metadataProcessor);
		return this;
	}
	
	public ImportJDBCDatabaseWizard setCPpassword(String password) {
		log.info("Set connection profile password: '" + password + "'");
		activateWizard();
		new LabeledText("Password:").setText(password);
		return this;
	}
	
	/**
	 * Only with teiid connection (VDB Source model)
	 */
	public ImportJDBCDatabaseWizard importAsVDB(boolean checked) {
		log.info("Import as VDB source model is : '" + checked + "'");
		activateWizard();
		CheckBox checkBox = new CheckBox("Import as VDB source model");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	/**
	 * Only with teiid connection (VDB Source model)
	 */
	public ImportJDBCDatabaseWizard setVDBversion(int version) {
		log.info("Set VDB version to : '" + version + "'");
		activateWizard();
		new LabeledText("VDB Version").setText(Integer.toString(version));
		return this;
	}
	
	/**
	 * @param IncludeIncompleteFK -sub check box
	 */
	public ImportJDBCDatabaseWizard foreignKeys(boolean checked, boolean IncludeIncompleteFK){
		log.info("Foreign keys are : '" + checked + "'");
		activateWizard();
		CheckBox checkBox = new CheckBox("Foreign Keys");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		if(checkBox.isChecked()){
			CheckBox subCheckBox = new CheckBox("Include Incomplete FKs");
			if(IncludeIncompleteFK != subCheckBox.isChecked()){
				subCheckBox.click();
			}
		}
		return this;
	}
	
	/**
	 * @param uniqueOnly - sub check box
	 * @param ApprocimationsAllowed - sub check box
	 */
	public ImportJDBCDatabaseWizard indexes(boolean checked, boolean uniqueOnly, boolean ApprocimationsAllowed){
		log.info("Indexes are : '" + checked + "'");
		activateWizard();
		CheckBox checkBox = new CheckBox("Indexes");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		if(checkBox.isChecked()){
			CheckBox subCheckBox = new CheckBox("Unique Only");
			if(uniqueOnly != subCheckBox.isChecked()){
				subCheckBox.click();
			}
			subCheckBox = new CheckBox("Approximations Allowed");
			if(ApprocimationsAllowed != subCheckBox.isChecked()){
				subCheckBox.click();
			}
		}
		return this;
	}
	
	public ImportJDBCDatabaseWizard procedures(boolean checked){
		log.info("Procedures are : '" + checked + "'");
		activateWizard();
		CheckBox checkBox = new CheckBox("Procedures");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public ImportJDBCDatabaseWizard setTableTypes(boolean systemTable, boolean table, boolean view){
		return setTableTypes(systemTable,table,view,false,false);
	}
	
	/**
	 * Only with teiid connection (VDB Source model)
	 */
	public ImportJDBCDatabaseWizard setTableTypes(boolean systemTable, boolean table, boolean view, boolean xmlstagingtable, boolean document){
		log.info("Table types are: System table is: '" + systemTable + "'; Table is: '" + table + "'; View is: '"+view+"'; "
				+ "XmlStagingTable is: '"+xmlstagingtable+"'; Document is: '"+document+"'");
		activateWizard();
		new PushButton("Deselect All").click();
		
		List<String> list = new ArrayList<String>();

		if(systemTable){
			list.add("SYSTEM TABLE");
		}
		if(table){
			list.add("TABLE");
		}
		if(view){
			list.add("VIEW");
		}
		if(xmlstagingtable){
			list.add("XMLSTAGINGTABLE");
		}
		if(document){
			list.add("DOCUMENT");
		}
		
		String[] items = new String[list.size()];
		for( int j = 0; j < list.size(); j++ ){
			items[j]=list.get(j);
		}
		
		new DefaultTable(new DefaultGroup("Table Types"),0).select(items);
		return this;
	}
	
	/**
	 * @param itemList each of item have full path (for example: db/subDB/table)
	 */
	public ImportJDBCDatabaseWizard setTables(String... itemList){
		log.info("Check tables : '" + Arrays.toString(itemList) + "'");
		activateWizard();

		if ((itemList != null) && (itemList.length!=0)) {
			new PushButton("Deselect All").click();
		}
		for (String item : itemList) {
			String[] itemArray = item.split("/");
			DefaultTreeItem treeItem = new DefaultTreeItem(itemArray);
			treeItem.setChecked(true);
			
		}
		return this;
	}
	
	public ImportJDBCDatabaseWizard setFolder(String folder) {
		log.info("Set folder to: '" + folder + "'");
		activateWizard();
		new LabeledText("Into Folder:").setText(folder);
		return this;
	}
	
	/**
	 * not working with teiid connection (VDB Source model)
	 */
	public ImportJDBCDatabaseWizard setModelName(String modelName) {
		log.info("Set model name to: '" + modelName + "'");
		activateWizard();
		new LabeledText("Model Name:").setText(modelName);
		return this;
	}
	/**
	 * not working with teiid connection (VDB Source model)
	 */
	public ImportJDBCDatabaseWizard makeTargetViewModel(boolean checked) {
		log.info("Make target a view model is : '" + checked + "'");
		activateWizard();
		CheckBox checkBox = new CheckBox("Make target a view model");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	/**
	 * not working with teiid connection (VDB Source model)
	 */
	public ImportJDBCDatabaseWizard update(boolean checked) {
		log.info("Update is : '" + checked + "'");
		activateWizard();
		CheckBox checkBox = new CheckBox("Update (if existing model selected)");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public ImportJDBCDatabaseWizard includeCostStatistics(boolean checked) {
		log.info("Include cost statistics is : '" + checked + "'");
		activateWizard();
		CheckBox checkBox = new CheckBox("Include Cost Statistics (will increase time for large imports)");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public ImportJDBCDatabaseWizard autoCreateDataSource(boolean checked) {
		log.info("Auto-Create Data Source is : '" + checked + "'");
		activateWizard();
		CheckBox checkBox = new CheckBox("Auto-create Data Source");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public ImportJDBCDatabaseWizard setJndiName(String JndiName) {
		log.info("Set JNDI name to: '" + JndiName + "'");
		activateWizard();
		new LabeledText("JNDI Name").setText(JndiName);
		return this;
	}
	
	/**
	 * not working with teiid connection (VDB Source model)
	 */
	public ImportJDBCDatabaseWizard includeCatalog(boolean checked) {
		log.info("Include catalog is : '" + checked + "'");
		activateWizard();
		CheckBox checkBox = new CheckBox("Include Catalog For Fully Qualified Names");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	/**
	 * not working with teiid connection (VDB Source model)
	 */
	public ImportJDBCDatabaseWizard useFullyNames(boolean checked){
		log.info("Use Fully Qualified Names is : '" + checked + "'");
		activateWizard();
		new DefaultTabItem("Model Object Options").activate();
		CheckBox checkBox = new CheckBox("Use Fully Qualified Names  (Example: partssupplier.dbo.PARTS)");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		new DefaultTabItem("Model Definition").activate();
		return this;
	}
	
	public ImportJDBCDatabaseWizard setUpdatable(boolean checked){
		log.info("Updatable is : '" + checked + "'");
		activateWizard();
		new DefaultTabItem("Model Object Options").activate();
		CheckBox checkBox = new CheckBox("Set all as updatable");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		new DefaultTabItem("Model Definition").activate();
		return this;
	}
	
	/**
	 * not working with teiid connection (VDB Source model)
	 * @param lowerCase - false -> upper case
	 */
	public ImportJDBCDatabaseWizard changeCase(boolean lowerCase){
		log.info("Change case to lower : '" + lowerCase + "'");
		activateWizard();
		new DefaultTabItem("Model Object Options").activate();
		new CheckBox("Change Case For All Characters").click();
		if(lowerCase){
			new RadioButton(new DefaultGroup("Case Options"),"Make All Lower Case  (Example: SUPPLIERS > suppliers)").click();
		}else{
			new RadioButton(new DefaultGroup("Case Options"),"Make All Upper Case  (Example: Suppliers > SUPPLIERS)").click();
		}
		new DefaultTabItem("Model Definition").activate();
		return this;
	}
}
