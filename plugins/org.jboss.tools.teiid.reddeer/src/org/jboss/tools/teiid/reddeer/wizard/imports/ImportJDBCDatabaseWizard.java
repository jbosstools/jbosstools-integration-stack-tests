package org.jboss.tools.teiid.reddeer.wizard.imports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.reddeer.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;

/**
 * Imports JDBC Database to Teiid project.
 * 
 * @author Lucia Jelinkova
 */
public class ImportJDBCDatabaseWizard extends ImportWizardDialog {
	
	private static final String DIALOG_TITLE = "Import Database via JDBC";
	
	private ImportJDBCDatabaseWizard() {
		super("Teiid Designer", "JDBC Database >> Source Model");
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
	
	/**
	 * use nextPage()
	 */
	@Deprecated
	@Override
	public void next(){
		super.next();
	}
	
	public ImportJDBCDatabaseWizard nextPage(){
		log.info("Go to next wizard page");
		super.next();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		return this;
	}
	
	@Override
	public void finish() {
		super.finish();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		new WaitWhile(new ShellWithTextIsAvailable(DIALOG_TITLE), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	public ImportJDBCDatabaseWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public ImportJDBCDatabaseWizard setConnectionProfile(String connectionProfile) {
		log.info("Set connection profile: '" + connectionProfile + "'");
		activate();
		new DefaultCombo(0).setSelection(connectionProfile);
		return this;
	}
	
	public ImportJDBCDatabaseWizard setJDBCMetadataProcessor(String metadataProcessor) {
		log.info("Set JDBC Metadata Processor: '" + metadataProcessor + "'");
		activate();
		new DefaultCombo(1).setSelection(metadataProcessor);
		return this;
	}
	
	public ImportJDBCDatabaseWizard setCPpassword(String password) {
		log.info("Set connection profile password: '" + password + "'");
		activate();
		new LabeledText("Password:").setText(password);
		return this;
	}
	
	/**
	 * Only with teiid connection (VDB Source model)
	 */
	public ImportJDBCDatabaseWizard importAsVDB(boolean checked) {
		log.info("Import as VDB source model is : '" + checked + "'");
		activate();
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
		activate();
		new LabeledText("VDB Version").setText(Integer.toString(version));
		return this;
	}
	
	/**
	 * @param IncludeIncompleteFK -sub check box
	 */
	public ImportJDBCDatabaseWizard foreignKeys(boolean checked, boolean IncludeIncompleteFK){
		log.info("Foreign keys are : '" + checked + "'");
		activate();
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
		activate();
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
		activate();
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
		activate();
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
		activate();

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
		activate();
		new LabeledText("Into Folder:").setText(folder);
		return this;
	}
	
	/**
	 * not working with teiid connection (VDB Source model)
	 */
	public ImportJDBCDatabaseWizard setModelName(String modelName) {
		log.info("Set model name to: '" + modelName + "'");
		activate();
		new LabeledText("Model Name:").setText(modelName);
		return this;
	}
	/**
	 * not working with teiid connection (VDB Source model)
	 */
	public ImportJDBCDatabaseWizard makeTargetViewModel(boolean checked) {
		log.info("Make target a view model is : '" + checked + "'");
		activate();
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
		activate();
		CheckBox checkBox = new CheckBox("Update (if existing model selected)");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public ImportJDBCDatabaseWizard includeCostStatistics(boolean checked) {
		log.info("Include cost statistics is : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Include Cost Statistics (will increase time for large imports)");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public ImportJDBCDatabaseWizard autoCreateDataSource(boolean checked) {
		log.info("Auto-Create Data Source is : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Auto-create Data Source");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public ImportJDBCDatabaseWizard setJndiName(String JndiName) {
		log.info("Set JNDI name to: '" + JndiName + "'");
		activate();
		new LabeledText("JNDI Name").setText(JndiName);
		return this;
	}
	
	/**
	 * not working with teiid connection (VDB Source model)
	 */
	public ImportJDBCDatabaseWizard includeCatalog(boolean checked) {
		log.info("Include catalog is : '" + checked + "'");
		activate();
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
		activate();
		new DefaultTabItem("Model Object Names (Tables, Procedures, Columns, etc...)").activate();
		CheckBox checkBox = new CheckBox("Use Fully Qualified Names  (Example: partssupplier.dbo.PARTS)");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		new DefaultTabItem("Relational Model Definition").activate();
		return this;
	}
	
	/**
	 * not working with teiid connection (VDB Source model)
	 * @param lowerCase - false -> upper case
	 */
	public ImportJDBCDatabaseWizard changeCase(boolean lowerCase){
		log.info("Change case to lower : '" + lowerCase + "'");
		activate();
		new DefaultTabItem("Model Object Names (Tables, Procedures, Columns, etc...)").activate();
		new CheckBox("Change Case For All Characters").click();
		if(lowerCase){
			new RadioButton(new DefaultGroup("Case Options"),"Make All Lower Case  (Example: SUPPLIERS > suppliers)").click();
		}else{
			new RadioButton(new DefaultGroup("Case Options"),"Make All Upper Case  (Example: Suppliers > SUPPLIERS)").click();
		}
		new DefaultTabItem("Relational Model Definition").activate();
		return this;
	}
}
