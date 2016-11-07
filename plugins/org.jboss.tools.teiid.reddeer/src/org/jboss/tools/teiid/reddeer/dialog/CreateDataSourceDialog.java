package org.jboss.tools.teiid.reddeer.dialog;

import java.awt.event.KeyEvent;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.jface.viewers.CellEditor;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

public class CreateDataSourceDialog extends AbstractDialog {
	
	private static final Logger log = Logger.getLogger(CreateDataSourceDialog.class);

	public static final String DATASOURCE_PROPERTY_PARENT_DIR = "* Parent Directory";
	public static final String DATASOURCE_PROPERTY_USER_NAME = "* User Name";
	public static final String DATASOURCE_PROPERTY_PASSWORD = "* Password";
	public static final String DATASOURCE_PROPERTY_URL = "* URL, End Point";
	
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
		new WaitWhile(new ShellWithTextIsAvailable("Progress Information"), TimePeriod.NORMAL);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
}
