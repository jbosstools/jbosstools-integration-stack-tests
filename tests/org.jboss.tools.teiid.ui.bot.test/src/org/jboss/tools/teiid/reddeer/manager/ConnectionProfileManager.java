package org.jboss.tools.teiid.reddeer.manager;

import java.util.Properties;

import org.jboss.reddeer.eclipse.datatools.ui.DatabaseProfile;
import org.jboss.reddeer.eclipse.datatools.ui.FlatFileProfile;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileSelectPage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.ConnectionProfileXmlLocalPage;
import org.jboss.tools.teiid.reddeer.wizard.ConnectionProfileXmlPage;
import org.jboss.tools.teiid.reddeer.wizard.ConnectionProfileXmlUrlPage;
import org.jboss.tools.teiid.reddeer.wizard.RestProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.TeiidConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.WsdlProfileWizard;
import org.jboss.tools.teiid.ui.bot.test.TeiidBot;

public class ConnectionProfileManager {
	// TODO String propsFileName --> Properties props (only in memory) -> or both

	// no private attributes which would have to be set prior to usage!!
	// only simply usable methods, or constants
	// include descriptive help!!!
	// robust methods, split class if too large
	// move differentiation to lower level - wizard steps etc.

	public static String FLAT_FILE_DATA_SOURCE = "Flat File Data Source";
	/**
	 * Firstly, you should create new FlatFileProfileExt and set properties to it Create connection profile to a flat
	 * file data source
	 * 
	 */
	// presun do conn profile mgr

	/**
	 * Create SalesForce connection profile
	 */
	public void createCPSalesForce(String connectionProfileName, Properties properties) {
		new TeiidConnectionProfileWizard().createSalesforceConnectionProfile(connectionProfileName, properties);
	}

	/**
	 * Create SalesForce connection profile
	 */
	public void createCPSalesForce(String connectionProfileName, String propertiesFileName) {
		new TeiidConnectionProfileWizard().createSalesforceConnectionProfile(connectionProfileName, propertiesFileName);
	}

	/**
	 * Create LDAP connection profile
	 */
	public void createCPLdap(String connectionProfileName, Properties properties) {
		new TeiidConnectionProfileWizard().createLdapConnectionProfile(connectionProfileName, properties);
	}

	/**
	 * Create connection profile and define driver, only for: Oracle, HSQLDB, SQL Server
	 */
	public void createCPWithoutDriverDefinition(String connectionProfileName, String propertiesFileName) {
		new TeiidConnectionProfileWizard().createDatabaseProfileWithoutCreatingDriver(connectionProfileName,
				propertiesFileName);
	}

	/**
	 * Create connection profile, only for: Oracle, HSQLDB, SQL Server
	 */
	public void createCPWithDriverDefinition(String connectionProfileName, String propertiesFileName) {
		createCPWithDriverDefinition(connectionProfileName, new TeiidBot().getProperties(propertiesFileName));
	}

	/**
	 * Create connection profile, only for: Oracle, HSQLDB, SQL Server
	 */
	public void createCPWithDriverDefinition(String connectionProfileName, Properties properties) {
		// new TeiidBot().createDatabaseProfile(connectionProfileName, propertiesFileName);
		new TeiidConnectionProfileWizard().createDatabaseProfile(connectionProfileName, properties);
	}

	public FlatFileProfile createCPFlatFile(String connectionProfileName, String folder) {
		FlatFileProfile flatProfile = new FlatFileProfile();
		flatProfile.setName(connectionProfileName);
		flatProfile.setFolder(folder);
		flatProfile.setCharset("UTF-8");
		flatProfile.setStyle("CSV");// too less properties to create a file --> Properties!

		ConnectionProfileWizard connWizard = new TeiidConnectionProfileWizard();
		connWizard.open();
		connWizard.createFlatFileProfile(flatProfile);
		return flatProfile;
	}

	public DatabaseProfile prepareDatabaseProfile(String name, Properties props) {
		DatabaseProfile dbProfile = new DatabaseProfile();
		dbProfile.setName(name);

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
			dbProfile.setVendor(loadedProperty);
		}
		return dbProfile;
	}

	/**
	 * Create XML profile
	 * 
	 * @param name
	 * @param path
	 *            for URL source, in form "http..."
	 */
	public void createCPXml(String name, String path) {// cp
		boolean isRemote = path.startsWith("http");
		String xmlProfile = "XML Local File Source";
		if (isRemote) {
			xmlProfile = "XML File URL Source";
		}

		TeiidConnectionProfileWizard wizard = new TeiidConnectionProfileWizard();
		wizard.open();

		ConnectionProfileSelectPage selectPage = new ConnectionProfileSelectPage();
		selectPage.setConnectionProfile(xmlProfile);
		selectPage.setName(name);

		wizard.next();

		ConnectionProfileXmlPage xmlPage = isRemote ? new ConnectionProfileXmlUrlPage()
				: new ConnectionProfileXmlLocalPage();
		xmlPage.setPath(isRemote ? path : new TeiidBot().toAbsolutePath(path));

		wizard.finish();
	}

	/**
	 * Create WSDL connection profile (Web Services Data Source (SOAP))
	 * 
	 * @param cpName
	 * @param props
	 */
	public void createCPWSDL(String cpName, Properties props) {
		String wsdl;

		if (props.getProperty("wsdl").contains("http")) {
			wsdl = props.getProperty("wsdl");
		} else {
			wsdl = "file:" + new TeiidBot().toAbsolutePath(props.getProperty("wsdl"));
		}

		// Create wsdl profile
		WsdlProfileWizard profileWizard = new WsdlProfileWizard();
		profileWizard.setName(cpName);
		profileWizard.setWsdl(wsdl);
		profileWizard.setEndPoint(props.getProperty("endPoint"));// !!!PROBLEM - reddeer
		profileWizard.execute();

	}

	public void createCPREST(String name, Properties props) {

		String connectionUrl = props.getProperty("connectionUrl");
		String type = props.getProperty("type");

		RestProfileWizard profileWizard = new RestProfileWizard();
		profileWizard.setConnectionUrl(connectionUrl);
		profileWizard.setName(name);
		profileWizard.setType(type);
		profileWizard.setAuthType(props.getProperty("authType"));
		profileWizard.setUsername(props.getProperty("username"));
		profileWizard.setPassword(props.getProperty("password"));
		
		profileWizard.execute();

	}
}
