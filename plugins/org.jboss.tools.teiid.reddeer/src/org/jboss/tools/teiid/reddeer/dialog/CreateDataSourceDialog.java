package org.jboss.tools.teiid.reddeer.dialog;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.jface.viewers.CellEditor;
import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

public class CreateDataSourceDialog extends AbstractDialog {
	
	private static final Logger log = Logger.getLogger(CreateDataSourceDialog.class);

	
	public static final String DATASOURCE_PROPERTY_USER_NAME = "* User Name";
	public static final String DATASOURCE_PROPERTY_USER_NAME_OPTIONAL = "User Name";
	public static final String DATASOURCE_PROPERTY_USER_NAME_OPTIONAL_SMALL = "user-name";
	
	public static final String DATASOURCE_PROPERTY_PASSWORD = "* Password";
	public static final String DATASOURCE_PROPERTY_PASSWORD_OPTIONAL = "Password";
	public static final String DATASOURCE_PROPERTY_PASSWORD_OPTIONAL_SMALL = "password";
	
	public static final String DATASOURCE_PROPERTY_INSTANCE_NAME = "* Instance Name";
	public static final String DATASOURCE_PROPERTY_ZOO_KEEPER = "* Zoo Keeper Server List";
	public static final String DATASOURCE_PROPERTY_PARENT_DIR = "* Parent Directory";
	public static final String DATASOURCE_PROPERTY_URL = "* URL, End Point";
	public static final String DATASOURCE_PROPERTY_ADDRESS = "* Address";
	public static final String DATASOURCE_PROPERTY_KEYSPACE = "* Keyspace";
	public static final String DATASOURCE_PROPERTY_CORE_INDEX_NAME = "* Core Index Name";
	public static final String DATASOURCE_PROPERTY_URL_SOLR = "* URL of the Solr server";
	public static final String DATASOURCE_PROPERTY_URL_SERVER_LIST = "* URL/Server List";
	public static final String DATASOURCE_DATABASE = "Database";
	
	public CreateDataSourceDialog() {
		super("Create DataSource");
		log.info("Generate dynamic vdb dialog is opened");

	}

	public CreateDataSourceDialog setName(String name){
		log.info("Set driver to: '"+ name +"'");
		activate();
		new LabeledText("Name:").setText(name);
		return this;
	}
	
	public CreateDataSourceDialog setDriver(String driver){
		log.info("Set driver to: '"+ driver +"'");
		activate();
		Table table = new DefaultTable(0);
		table.getItem(driver).select();
		return this;
	}
	
	/**
	 * @param propertyName - use one of TeiidConnectionImportWizard.DATASOURCE_PROPERTY_
	 */
	public CreateDataSourceDialog setImportPropertie(String propertyName, String value){
		log.info("Set property " + propertyName + "'s value to: '"+value+"'");
		activate();
		try{
			Table propertiesTable = new DefaultTable(new DefaultGroup("Data Source Properties"), 0);
			TableItem item = null;
			item = propertiesTable.getItem(propertyName);
			item.click(1);
			new DefaultText(new CellEditor(item),0).setText(value);
			activate();
		}catch(Exception ex){
			log.warn("Property is not exist. It will be added.");
		}
		new PushButton("Apply").click();
		return this;
	}
	
	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new PushButton("OK").click();
		new WaitWhile(new ShellIsAvailable("Progress Information"), TimePeriod.DEFAULT);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
}
