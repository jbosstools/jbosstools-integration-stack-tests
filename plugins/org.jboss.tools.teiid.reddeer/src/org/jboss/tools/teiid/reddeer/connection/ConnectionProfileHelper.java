package org.jboss.tools.teiid.reddeer.connection;

import java.io.File;
import java.sql.Connection;
import java.util.Properties;

import org.eclipse.datatools.connectivity.ConnectionProfileException;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.IManagedConnection;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.datatools.connectivity.drivers.DriverInstance;
import org.eclipse.datatools.connectivity.drivers.DriverManager;
import org.jboss.reddeer.eclipse.datatools.ui.FlatFileProfile;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileSelectPage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.requirement.ConnectionProfileConfig;
import org.jboss.tools.teiid.reddeer.wizard.TeiidConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.WsdlProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.ConnectionProfileXmlLocalPage;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.ConnectionProfileXmlPage;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.ConnectionProfileXmlUrlPage;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.RestProfileWizard;

public class ConnectionProfileHelper {

	private ProfileManager profileManager = ProfileManager.getInstance();

	/**
	 * Creates a connection profile. Will try to create it using eclipse API if possible.
	 * 
	 * @param cp
	 *            Connection profile to create
	 */
	public void createConnectionProfile(ConnectionProfileConfig cp) {
		createConnectionProfile(cp, false);
	}

	/**
	 * Creates a connection profile. Will try to create it using eclipse API if possible, unless forceWizard is true
	 * 
	 * @param cp
	 *            Connection profile to create
	 * @param forceWizard
	 *            Whether to use a wizard or try to create the profile using eclipse API. Passing true will ensure the
	 *            profile is always created using the wizard, even when it already exists
	 */
	public void createConnectionProfile(ConnectionProfileConfig cp, boolean forceWizard) {
		String vendor = cp.getVendor();
		String cpName = cp.getName();
		Properties cpProps = cp.asProperties();

		if (connectionProfileExists(cpName)) {
			if (forceWizard) {
				// we want to explicitly create the profile using a wizard,
				// delete the existing one first
				deleteProfile(cpName);
			} else {
				// already exists and we don't need to recreate it using the wizard
				return;
			}
		}

		if (cpProps.containsKey("providerID") && !forceWizard) {
			try {
				createConnectionProfileDirectly(cp);
				return;
			} catch (Exception e) {
				// ok, will create the profile the usual way
				e.printStackTrace();
			}
		}

		cpProps.put(TeiidConnectionProfileWizard.KEY_CONNECT_AFTER_COMPLETING, "false");
		if ("LDAP".equals(vendor)) {
			new TeiidConnectionProfileWizard().createLdapConnectionProfile(cpName, cpProps);
		} else if ("SalesForce".equals(vendor)) {
			new TeiidConnectionProfileWizard().createSalesforceConnectionProfile(cpName, cpProps);
		} else {
			new TeiidConnectionProfileWizard().createDatabaseProfile(cpName, cpProps);
		}
	}

	private IConnectionProfile createConnectionProfileDirectly(ConnectionProfileConfig cp)
			throws ConnectionProfileException {

		IConnectionProfile profile = null;

		profile = profileManager.getProfileByName(cp.getName());
		if (profile != null) {
			return profile;
		}

		Properties cpProps = cp.asProperties();

		Properties props = new Properties();
		props.put("org.eclipse.datatools.connectivity.db.savePWD", "true");
		props.put("org.eclipse.datatools.connectivity.db.username", cp.getUsername());
		props.put("org.eclipse.datatools.connectivity.db.password", cp.getPassword());
		props.put("org.eclipse.datatools.connectivity.db.URL", cpProps.getProperty("url"));
		props.put("org.eclipse.datatools.connectivity.db.vendor", cp.getVendor());

		DriverInstance di = DriverManager.getInstance().createNewDriverInstance(cpProps.getProperty("defnType"),
				cp.getName() + "Driver", cp.getJdbcPath());

		props.put("org.eclipse.datatools.connectivity.db.driverClass",
				di.getProperty("org.eclipse.datatools.connectivity.db.driverClass"));
		props.put("org.eclipse.datatools.connectivity.driverDefinitionID", di.getId());
		props.put("jarList", cp.getJdbcPath());

		profile = profileManager.createProfile(cp.getName(), cp.getDescription(), cpProps.getProperty("providerID"),
				props);

		profileManager.addProfile(profile, true);

		return profile;
	}

	public void deleteProfile(String cpName) {

		IConnectionProfile profile = profileManager.getProfileByName(cpName);
		DriverManager.getInstance().removeDriverInstance(
				profile.getBaseProperties().getProperty("org.eclipse.datatools.connectivity.driverDefinitionID"));
		try {
			profileManager.deleteProfile(profile);
		} catch (ConnectionProfileException e) {
			e.printStackTrace();
		}
	}

	public boolean connectionProfileExists(String cpName) {
		return profileManager.getProfileByName(cpName) != null;
	}

	public Connection getConnectionForProfile(IConnectionProfile profile) {
		// https://wiki.eclipse.org/DTP_FAQ
		IManagedConnection managedConnection = ((IConnectionProfile) profile)
				.getManagedConnection("java.sql.Connection");
		if (managedConnection != null) {
			return (Connection) managedConnection.getConnection().getRawConnection();
		}
		return null;
	}

	public void createCpXml(String name, String path) {// cp
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
		xmlPage.setPath(isRemote ? path : new File(path).getAbsolutePath());

		wizard.finish();
	}
	
	public void createCpRest(String name, Properties props) {

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
	
	public void createCpWsdl(String cpName, Properties props) {
		String wsdl;

		if (props.getProperty("wsdl").contains("http")) {
			wsdl = props.getProperty("wsdl");
		} else {
			wsdl = "file:" + new File(props.getProperty("wsdl")).getAbsolutePath();
		}

		WsdlProfileWizard profileWizard = new WsdlProfileWizard();
		profileWizard.setName(cpName);
		profileWizard.setWsdl(wsdl);
		profileWizard.setEndPoint(props.getProperty("endPoint"));// !!!PROBLEM - reddeer
		profileWizard.execute();
	}
	
	public FlatFileProfile createCpFlatFile(String connectionProfileName, String folder) {
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
}
