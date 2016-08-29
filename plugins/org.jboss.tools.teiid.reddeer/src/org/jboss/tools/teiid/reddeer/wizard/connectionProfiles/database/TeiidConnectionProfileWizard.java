package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.jboss.reddeer.eclipse.datatools.ui.DatabaseProfile;
import org.jboss.reddeer.eclipse.datatools.ui.DriverDefinition;
import org.jboss.reddeer.eclipse.datatools.ui.DriverTemplate;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileDatabasePage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileGenericPage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileSelectPage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileWizard;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.tools.teiid.reddeer.extensions.DriverDefinitionExt;
import org.jboss.tools.teiid.reddeer.preference.DriverDefinitionPreferencePageExt;

/**
 * Extends reddeer class
 * 
 * @author apodhrad, lfabriko
 *
 */
public class TeiidConnectionProfileWizard extends ConnectionProfileWizard {

	public static final String KEY_CONNECT_AFTER_COMPLETING = "connectAfterCompleting";

	public TeiidConnectionProfileWizard() {
		super();
		wizardMap.put("DB2 for Linux, UNIX, and Windows", new ConnectionProfileDB2Page());
		wizardMap.put("HSQLDB", new ConnectionProfileHsqlPage());
		wizardMap.put("Sybase ASE", new ConnectionProfileGenericPage());
		wizardMap.put("Ingres", new ConnectionProfileGenericPage());
	}

	/**
	 * Only for: SalesForce
	 * 
	 * @param profileName
	 * @param props
	 * @return
	 */
	public DatabaseProfile createSalesforceConnectionProfile(String profileName, Properties props) {
		DatabaseProfile dbProfile = this.prepareDatabaseProfile(profileName, props);
		open();
		ConnectionProfileSelectPage selectPage = new ConnectionProfileSelectPage();
		selectPage.setConnectionProfile(dbProfile.getVendor());
		selectPage.setName(dbProfile.getName());

		next();

		ConnectionProfileDatabasePage dbPage = new ConnectionProfileSalesForcePage();
		dbPage.setUsername(dbProfile.getUsername());
		dbPage.setPassword(dbProfile.getPassword());

		new PushButton("Test Connection").click();

		new DefaultShell("Success");

		new PushButton("OK").click();

		finish();
		return dbProfile;
	}

	/**
	 * Only for: LDAP
	 * 
	 * @param profileName
	 * @param props
	 * @return
	 */
	public DatabaseProfile createLdapConnectionProfile(String profileName, Properties cpProperties) {
		DatabaseProfile dbProfile = this.prepareDatabaseProfile(profileName, cpProperties);
		open();
		ConnectionProfileSelectPage selectPage = new ConnectionProfileSelectPage();
		selectPage.setConnectionProfile(dbProfile.getVendor());
		selectPage.setName(dbProfile.getName());

		next();

		ConnectionProfileLdapPage dbPage = new ConnectionProfileLdapPage();
		dbPage.setHostname(dbProfile.getHostname());
		dbPage.setPort(dbProfile.getPort());

		next();

		dbPage.setUsername(dbProfile.getUsername());
		dbPage.setPassword(dbProfile.getPassword());

		new PushButton("Test Connection").click();

		new WaitUntil(new ShellWithTextIsAvailable("Success"));
		new DefaultShell("Success");
		new PushButton("OK").click();

		new DefaultShell("New connection profile");
		finish();
		return dbProfile;
	}

	/**
	 * Only for: SalesForce
	 * 
	 * @param profileName
	 * @param props
	 * @return
	 */
	public DatabaseProfile createSalesforceConnectionProfile(String profileName, String props) {
		return this.createSalesforceConnectionProfile(profileName, props);
	}

	/**
	 * Only for: Oracle, HSQLDB, SQL Server, XML, SalesForce
	 * 
	 * @param name
	 *            of connection profile (e.g. My Oracle profile)
	 * @param props
	 */
	public void createDatabaseProfileWithoutCreatingDriver(String name, String props) {
		DatabaseProfile dbProfile = this.prepareDatabaseProfile(name, this.getProperties(props));

		open();

		createDatabaseProfile(dbProfile);
	}

	public DatabaseProfile prepareDatabaseProfile(String name, Properties props) {
		// TODO add some boolean param to createDatabaseProfile -> add driver
		DriverTemplate drvTemp = new DriverTemplate(props.getProperty("db.template"), props.getProperty("db.version"));

		// DriverDefinition driverDefinition = new DriverDefinition();
		DriverDefinitionExt driverDefinition = new DriverDefinitionExt();

		driverDefinition.setDriverName(props.getProperty("driverName"));
		driverDefinition.setDriverTemplate(drvTemp);

		driverDefinition = loadDriverDefinition(driverDefinition, props);

		// do not create a new driver definition

		DatabaseProfile dbProfile = new DatabaseProfile();
		dbProfile.setName(name);
		dbProfile.setDriverDefinition(driverDefinition);

		String loadedProperty = null;
		if ((loadedProperty = props.getProperty("db.name")) != null) {
			dbProfile.setDatabase(loadedProperty);
		}

		if ((loadedProperty = props.getProperty("db.hostname")) != null) {
			dbProfile.setHostname(loadedProperty);
		}

		if ((loadedProperty = props.getProperty("db.username")) != null) {
			dbProfile.setUsername(loadedProperty);
		}

		if ((loadedProperty = props.getProperty("db.password")) != null) {
			dbProfile.setPassword(loadedProperty);
		}

		if ((loadedProperty = props.getProperty("db.vendor")) != null) {
			dbProfile.setVendor(loadedProperty);
		}

		if ((loadedProperty = props.getProperty("db.port")) != null) {
			dbProfile.setPort(loadedProperty);
		}
		return dbProfile;
	}

	public Properties getProperties(String fileName) {
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
	
	/**
	 * 
	 * @param name
	 *            of connection profile (e.g. My oracle profile)
	 * @param props
	 */
	public void createDatabaseProfile(String name, Properties props) {// cp ext
		DriverTemplate drvTemp = new DriverTemplate(props.getProperty("db.template"), props.getProperty("db.version"));

		// DriverDefinition driverDefinition = new DriverDefinition();
		DriverDefinitionExt driverDefinition = new DriverDefinitionExt();

		driverDefinition.setDriverName(name + " Driver");
		driverDefinition.setDriverTemplate(drvTemp);

		driverDefinition = loadDriverDefinition(driverDefinition, props);

		/*
		 * String driverPath = new File(props.getProperty("db.jdbc_path")).getAbsolutePath();
		 * driverDefinition.setDriverLibrary(driverPath); //in case of Generic JDBC: also db.jdbc_class String
		 * driverClass; if ((driverClass = props.getProperty("db.jdbc_class")) != null){
		 * driverDefinition.setDriverClass(driverClass); } String vendorTemplate =
		 * props.getProperty("db.vendor_template"); if (vendorTemplate != null){
		 * driverDefinition.setVendorTemplate(vendorTemplate); } String loadedProperty; if ((loadedProperty =
		 * props.getProperty("db.conn_url")) != null){ driverDefinition.setConnectionUrl(loadedProperty); }
		 */

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

		open();
		createDatabaseProfile(dbProfile, false,
				Boolean.valueOf(props.getProperty(KEY_CONNECT_AFTER_COMPLETING, "true")));
	}

	public void createDatabaseProfile(DatabaseProfile dbProfile, boolean test) {
		createDatabaseProfile(dbProfile, test, true);
	}

	public void createDatabaseProfile(DatabaseProfile dbProfile, boolean test, boolean connect) {
		ConnectionProfileSelectPage selectPage = new ConnectionProfileSelectPage();
		selectPage.setConnectionProfile(dbProfile.getVendor());
		selectPage.setName(dbProfile.getName());

		next();

		ConnectionProfileDatabasePage dbPage = wizardMap.get(dbProfile.getVendor());
		DriverDefinition drvDef = dbProfile.getDriverDefinition();
		dbPage.setDriver(drvDef.getDriverName());
		dbPage.setDatabase(dbProfile.getDatabase());
		dbPage.setHostname(dbProfile.getHostname());
		dbPage.setPort(dbProfile.getPort());
		dbPage.setUsername(dbProfile.getUsername());
		dbPage.setPassword(dbProfile.getPassword());

		if (test) {
			String success = "Success";
			new PushButton("Test Connection").click();
			new WaitUntil(new ShellWithTextIsActive(success), TimePeriod.NORMAL, false);
			String text = new DefaultShell().getText();
			new OkButton().click();
			if (!text.equals(success)) {
				throw new EclipseLayerException("Connection ping failed!");
			}
		}

		new CheckBox("Connect when the wizard completes").toggle(connect);

		finish();
	}

	public DriverDefinitionExt loadDriverDefinition(DriverDefinitionExt driverDefinition, Properties props) {

		String loadedProperty;
		if ((loadedProperty = props.getProperty("db.jdbc_path")) != null) {
			String driverPath = new File(props.getProperty("db.jdbc_path")).getAbsolutePath();
			driverDefinition.setDriverLibrary(driverPath);
		}

		if ((loadedProperty = props.getProperty("db.jdbc_class")) != null) {
			driverDefinition.setDriverClass(loadedProperty);
		}

		if ((loadedProperty = props.getProperty("db.vendor_template")) != null) {
			driverDefinition.setVendorTemplate(loadedProperty);
		}

		if ((loadedProperty = props.getProperty("db.conn_url")) != null) {
			driverDefinition.setConnectionUrl(loadedProperty);
		}
		if ((loadedProperty = props.getProperty("db.name")) != null) {
			driverDefinition.setDatabaseName(loadedProperty);
		}
		return driverDefinition;
	}

	@Override
	public void open() {
		try {
			super.open();
		} catch (Exception ex) {
			new DefaultTreeItem("Connection Profiles").collapse();
			new DefaultTreeItem("Connection Profiles", "Connection Profile").expand();
			new DefaultTreeItem("Connection Profiles", "Connection Profile").select();
			next();
		}
	}

}
