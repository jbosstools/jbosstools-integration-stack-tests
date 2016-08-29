package org.jboss.tools.teiid.reddeer.wizard.imports;

import java.util.ArrayList;
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

	private static ImportJDBCDatabaseWizard INSTANCE;
	
	private static final String DIALOG_TITLE = "Import Database via JDBC";
	
	private ImportJDBCDatabaseWizard() {
		super("Teiid Designer", "JDBC Database >> Source Model");
		log.info("JDBC Database import wizard is opened");
	}

	public static ImportJDBCDatabaseWizard getInstance(){
		if(INSTANCE==null){
			INSTANCE=new ImportJDBCDatabaseWizard();
		}
		return INSTANCE;
	}
	
	public static ImportJDBCDatabaseWizard openWizard(){
		ImportJDBCDatabaseWizard wizard = getInstance();
		wizard.open();
		return wizard;
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
	public ImportJDBCDatabaseWizard importAsVDB(boolean check) {
		log.info("Import as VDB source model is : '" + check + "'");
		activate();
		CheckBox checkBox = new CheckBox("Import as VDB source model");
		if(check != checkBox.isChecked()){
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
	public ImportJDBCDatabaseWizard foreignKeys(boolean check, boolean IncludeIncompleteFK){
		log.info("Foreign keys are : '" + check + "'");
		activate();
		CheckBox checkBox = new CheckBox("Foreign Keys");
		if(check != checkBox.isChecked()){
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
	public ImportJDBCDatabaseWizard indexes(boolean check, boolean uniqueOnly, boolean ApprocimationsAllowed){
		log.info("Indexes are : '" + check + "'");
		activate();
		CheckBox checkBox = new CheckBox("Indexes");
		if(check != checkBox.isChecked()){
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
	
	public ImportJDBCDatabaseWizard procedures(boolean check){
		log.info("Procedures are : '" + check + "'");
		activate();
		CheckBox checkBox = new CheckBox("Procedures");
		if(check != checkBox.isChecked()){
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
		log.info("Check tables : '" + itemList + "'");
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
	public ImportJDBCDatabaseWizard makeTargetViewModel(boolean check) {
		log.info("Make target a view model is : '" + check + "'");
		activate();
		CheckBox checkBox = new CheckBox("Make target a view model");
		if(check != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	/**
	 * not working with teiid connection (VDB Source model)
	 */
	public ImportJDBCDatabaseWizard update(boolean check) {
		log.info("Update is : '" + check + "'");
		activate();
		CheckBox checkBox = new CheckBox("Update (if existing model selected)");
		if(check != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public ImportJDBCDatabaseWizard includeCostStatistics(boolean check) {
		log.info("Include cost statistics is : '" + check + "'");
		activate();
		CheckBox checkBox = new CheckBox("Include Cost Statistics (will increase time for large imports)");
		if(check != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public ImportJDBCDatabaseWizard autoCreateDataSource(boolean check) {
		log.info("Auto-Create Data Source is : '" + check + "'");
		activate();
		CheckBox checkBox = new CheckBox("Auto-create Data Source");
		if(check != checkBox.isChecked()){
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
	public ImportJDBCDatabaseWizard includeCatalog(boolean check) {
		log.info("Include catalog is : '" + check + "'");
		activate();
		CheckBox checkBox = new CheckBox("Include Catalog For Fully Qualified Names");
		if(check != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	/**
	 * not working with teiid connection (VDB Source model)
	 */
	public ImportJDBCDatabaseWizard useFullyNames(boolean check){
		log.info("Use Fully Qualified Names is : '" + check + "'");
		activate();
		new DefaultTabItem("Model Object Names (Tables, Procedures, Columns, etc...)").activate();
		CheckBox checkBox = new CheckBox("Use Fully Qualified Names  (Example: partssupplier.dbo.PARTS)");
		if(check != checkBox.isChecked()){
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
	
	public ImportJDBCDatabaseWizard nextPage(){
		log.info("Go to next wizard page");
		super.next();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		return this;
	}
	
	/**
	 * use nextPage()
	 */
	@Deprecated
	@Override
	public void next(){
		super.next();
	}
	
	@Override
	public void finish() {
		super.finish();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		new WaitWhile(new ShellWithTextIsAvailable(DIALOG_TITLE), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
}
