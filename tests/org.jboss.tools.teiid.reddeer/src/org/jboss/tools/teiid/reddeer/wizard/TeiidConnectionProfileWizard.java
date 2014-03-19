package org.jboss.tools.teiid.reddeer.wizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.datatools.ui.DatabaseProfile;
import org.jboss.reddeer.eclipse.datatools.ui.DriverDefinition;
import org.jboss.reddeer.eclipse.datatools.ui.DriverTemplate;
import org.jboss.reddeer.eclipse.datatools.ui.FlatFileProfile;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileDatabasePage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileFlatFilePage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileSelectPage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileWizard;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.extensions.DriverDefinitionExt;
import org.jboss.tools.teiid.reddeer.preference.DriverDefinitionPreferencePageExt;

/**
 * Extends reddeer class 
 * @author apodhrad, lfabriko
 *
 */
public class TeiidConnectionProfileWizard extends ConnectionProfileWizard {// TODO should be renamed to ConnectionProfileExt 

	public TeiidConnectionProfileWizard() {
		super();//db.vendor
		wizardMap.put("HSQLDB", new ConnectionProfileHsqlPage(this, 2));
		wizardMap.put("XML File URL Source", new ConnectionProfileXmlUrlPage(this, 2));
		wizardMap.put("XML Local File Source", new ConnectionProfileXmlLocalPage(this, 2));
		wizardMap.put("SalesForce", new ConnectionProfileSalesForcePage(this,2));
		wizardMap.put("Generic JDBC", new GenericProfilePage(this, 2));
		wizardMap.put("Sybase ASA", new GenericProfilePage(this, 2));//sybasepage for sybase asa driver, but this driver is nok; should be other driver and generic page
		wizardMap.put("DB2 for Linux, UNIX, and Windows", new ConnectionProfileDB2Page(this, 2));
	}
	
	/**
	 * Only for: SalesForce
	 * @param profileName
	 * @param props
	 * @return 
	 */
	public DatabaseProfile createSalesforceConnectionProfile(String profileName, String props){
		DatabaseProfile dbProfile = this.prepareDatabaseProfile(profileName, this.getProperties(props));
		open();
		ConnectionProfileSelectPage selectPage = getFirstPage();
		selectPage.setConnectionProfile(dbProfile.getVendor());
		selectPage.setName(dbProfile.getName());

		next();
		
		ConnectionProfileDatabasePage dbPage = (ConnectionProfileDatabasePage) getSecondPage();
		dbPage.setUsername(dbProfile.getUsername());
		dbPage.setPassword(dbProfile.getPassword());

		new PushButton("Test Connection").click(); 

		new PushButton("OK").click();
		
		finish();
		return dbProfile;
	}
	
	/**
	 * Only for: Oracle, HSQLDB, SQL Server, XML, SalesForce
	 * @param name of connection profile (e.g. My Oracle profile)
	 * @param props
	 */
	public void createDatabaseProfileWithoutCreatingDriver(String name, String props) {
		DatabaseProfile dbProfile = this.prepareDatabaseProfile(name, this.getProperties(props));
		
		open();

		createDatabaseProfile(dbProfile);
	}
	
	public DatabaseProfile prepareDatabaseProfile(String name, Properties props){
		DatabaseProfile dbProfile = new DatabaseProfile();
		dbProfile.setName(name);
		
		String loadedProperty = null;
		if ((loadedProperty = props.getProperty("db.name")) != null){
			dbProfile.setDatabase(loadedProperty);
		}
		
		if ((loadedProperty = props.getProperty("db.hostname")) != null){
			dbProfile.setHostname(loadedProperty);
		}
		
		if ((loadedProperty = props.getProperty("db.username")) != null){
			dbProfile.setUsername(loadedProperty);
		}
		
		if ((loadedProperty = props.getProperty("db.password")) != null){
			dbProfile.setPassword(loadedProperty);
		}
		
		if ((loadedProperty = props.getProperty("db.vendor")) != null){
			dbProfile.setVendor(loadedProperty);
		}
		
		if ((loadedProperty = props.getProperty("db.port")) != null){
			dbProfile.setVendor(loadedProperty);
		}
		return dbProfile;
	}

	public Properties getProperties(String fileName){
		Properties props = new Properties();
		try {
			props.load(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}
	
	@Override
	public void createFlatFileProfile(FlatFileProfile flatProfile) {
		ConnectionProfileSelectPage selectPage = getFirstPage();
		selectPage.setConnectionProfile("Flat File Data Source");
		selectPage.setName(flatProfile.getName());

		next();

		ConnectionProfileFlatFilePage flatPage = (ConnectionProfileFlatFilePage) getSecondPage();
		new SWTWorkbenchBot().text().setText(new File(flatProfile.getFolder()).getAbsolutePath());//should be absolute path!
		//switch off validation of home folder
		new CheckBox("Validate home folder").click();
		flatPage.setCharset(flatProfile.getCharset());
		flatPage.setStyle(flatProfile.getStyle());

		finish();
	}
	
	/**
	 * 
	 * @param name of connection profile (e.g. My oracle profile)
	 * @param props
	 */
	public void createDatabaseProfile(String name, Properties props) {//cp ext
		DriverTemplate drvTemp = new DriverTemplate(props.getProperty("db.template"),
				props.getProperty("db.version"));

		//DriverDefinition driverDefinition = new DriverDefinition();
		DriverDefinitionExt driverDefinition = new DriverDefinitionExt();
		
		driverDefinition.setDriverName(name + " Driver");
		driverDefinition.setDriverTemplate(drvTemp);
		
		driverDefinition = loadDriverDefinition(driverDefinition, props);
		
		/*String driverPath = new File(props.getProperty("db.jdbc_path")).getAbsolutePath();
		driverDefinition.setDriverLibrary(driverPath);
		//in case of Generic JDBC: also db.jdbc_class
		String driverClass;
		if ((driverClass = props.getProperty("db.jdbc_class")) != null){
			driverDefinition.setDriverClass(driverClass);
		}
		String vendorTemplate = props.getProperty("db.vendor_template");
		if (vendorTemplate != null){
			driverDefinition.setVendorTemplate(vendorTemplate);
		}
		String loadedProperty;
		if ((loadedProperty = props.getProperty("db.conn_url")) != null){
			driverDefinition.setConnectionUrl(loadedProperty);
		}*/

		DriverDefinitionPreferencePageExt prefPage = new DriverDefinitionPreferencePageExt();
		prefPage.open();
		prefPage.addDriverDefinition(driverDefinition);
		prefPage.ok();

		DatabaseProfile dbProfile = new DatabaseProfile();
		dbProfile.setDriverDefinition(driverDefinition);
		dbProfile.setName(name);
		dbProfile.setDatabase(props.getProperty("db.name"));
		dbProfile.setHostname(props.getProperty("db.hostname"));
		dbProfile.setUsername(props.getProperty("db.username"));
		dbProfile.setPassword(props.getProperty("db.password"));
		dbProfile.setVendor(props.getProperty("db.vendor"));
		dbProfile.setPort(props.getProperty("db.port"));

		TeiidConnectionProfileWizard wizard = new TeiidConnectionProfileWizard();
		wizard.open();
		wizard.createDatabaseProfile(dbProfile);
	}

	public DriverDefinitionExt loadDriverDefinition(
			DriverDefinitionExt driverDefinition, Properties props) {
		String driverPath = new File(props.getProperty("db.jdbc_path")).getAbsolutePath();
		driverDefinition.setDriverLibrary(driverPath);
		
		String loadedProperty;
		if ((loadedProperty = props.getProperty("db.jdbc_class")) != null){
			driverDefinition.setDriverClass(loadedProperty);
		}
	
		if ((loadedProperty = props.getProperty("db.vendor_template")) != null){
			driverDefinition.setVendorTemplate(loadedProperty);
		}
		
		if ((loadedProperty = props.getProperty("db.conn_url")) != null){
			driverDefinition.setConnectionUrl(loadedProperty);
		}
		if ((loadedProperty = props.getProperty("db.name")) != null){
			driverDefinition.setDatabaseName(loadedProperty);
		}
		return driverDefinition;
	}
	
	@Override
	public void open(){
		try {
			super.open();
		} catch (Exception ex){
			new DefaultTreeItem("Connection Profiles").collapse();
			new DefaultTreeItem("Connection Profiles", "Connection Profile").expand();
			new DefaultTreeItem("Connection Profiles", "Connection Profile").select();
			next();
		}
	}
	
	}
