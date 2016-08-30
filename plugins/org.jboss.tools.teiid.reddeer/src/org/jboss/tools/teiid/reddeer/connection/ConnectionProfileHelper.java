package org.jboss.tools.teiid.reddeer.connection;

import java.sql.Connection;
import java.util.Properties;

import org.eclipse.datatools.connectivity.ConnectionProfileException;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.IManagedConnection;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.datatools.connectivity.drivers.DriverInstance;
import org.eclipse.datatools.connectivity.drivers.DriverManager;
import org.jboss.tools.teiid.reddeer.requirement.ConnectionProfileConfig;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.database.DatabaseConnectionProfile;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.database.LdapConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.database.SalesforceConnectionProfileWizard;

public class ConnectionProfileHelper {

	private ProfileManager profileManager = ProfileManager.getInstance();

	/**
	 * Creates a connection profile. Will try to create it using eclipse API if possible.
	 * 
	 * @param cp
	 *            Connection profile to create
	 */
	public void createConnectionProfile(ConnectionProfileConfig cp) {
		createConnectionProfile(cp, false, false);
	}

	/**
	 * Creates a connection profile. Will try to create it using eclipse API if possible, unless forceWizard is true
	 * 
	 * @param cp
	 *            Connection profile to create
	 * @param forceWizard
	 *            Whether to use a wizard or try to create the profile using eclipse API. Passing true will ensure the
	 *            profile is always created using the wizard, even when it already exists
	 * @param connectAfter 
	 * 			  Whether eclipse can connect after created CP
	 */
	public void createConnectionProfile(ConnectionProfileConfig cp, boolean forceWizard, boolean connectAfter) {
		String vendor = cp.getVendor();
		String cpName = cp.getName();
//		Properties cpProps = cp.asProperties();

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
		
		DatabaseConnectionProfile wizard = null;
		switch (vendor) {
        case "DB2 for Linux, UNIX, and Windows":  
        case "Oracle":
        case "SQL Server":
        	wizard = DatabaseConnectionProfile.openWizard(vendor,cpName);
        	wizard.createNewDriver()
        			.selectTemplate(cp.getTemplate(), cp.getVersion())
        			.setName(cpName + "_driver")
        			.addDriver(cp.getJdbcPath())
        			.finish();
        	wizard.setDatabase(cp.getDbName())
        			.setHostname(cp.getHostname())
					.setPort(cp.getPort())
					.setUsername(cp.getUsername())
					.setPassword(cp.getPassword())
					.savePassword(true)
					.testConnection()
					.connectAfter(connectAfter)
					.finish();
        	break;
        case "PostgreSQL":  
 //     case "Ingres":
        case "MySQL": 
        	wizard = DatabaseConnectionProfile.openWizard(vendor,cpName);
        	wizard.createNewDriver()
					.selectTemplate(cp.getTemplate(), cp.getVersion())
					.setName(cpName + "_driver")
					.addDriver(cp.getJdbcPath())
					.finish();
        	wizard.setDatabase(cp.getDbName())
					.setHostname(cp.getHostname())
					.setUsername(cp.getUsername())
					.setPassword(cp.getPassword())
					.savePassword(true)
					.testConnection()
					.connectAfter(connectAfter)
					.finish();        	
        	break;
        case "Sybase ASE": 
        	wizard = DatabaseConnectionProfile.openWizard(vendor,cpName);
        	wizard.createNewDriver()
					.selectTemplate(cp.getTemplate(), cp.getVersion())
					.setName(cpName + "_driver")
					.addDriver(cp.getJdbcPath())
					.setDriverClass(cp.getJdbcClass())
					.setDatabaseName(cp.getDbName())
					.setConnectionUrl(cp.getHostname())
					.finish();
        	wizard.setDatabase(cp.getDbName())
					.setHostname(cp.getHostname())
					.setUsername(cp.getUsername())
					.setPassword(cp.getPassword())
					.savePassword(true)
					.testConnection()
					.connectAfter(connectAfter)
					.finish();      
        	break;
        case "HSQLDB":  
        	wizard = DatabaseConnectionProfile.openWizard(vendor,cpName);
        	wizard.createNewDriver()
					.selectTemplate(cp.getTemplate(), cp.getVersion())
					.setName(cpName + "_driver")
					.addDriver(cp.getJdbcPath())
					.finish();
        	wizard.setDatabase(cp.getDbName())
					.setUsername(cp.getUsername())
					.setPassword(cp.getPassword())
					.savePassword(true)
					.testConnection()
					.connectAfter(connectAfter)
					.finish(); 
        	break;
        case "Generic JDBC": 
        	wizard = DatabaseConnectionProfile.openWizard(vendor,cpName);
        	wizard.createNewDriver()
					.selectTemplate(cp.getTemplate(), cp.getVersion())
					.setName(cpName + "_driver")
					.addDriver(cp.getJdbcPath())
					.setDriverClassGeneric(cp.getJdbcClass())
					.finish();
        	wizard.setDatabase(cp.getDbName())
					.setHostname(cp.getHostname())
					.setUsername(cp.getUsername())
					.setPassword(cp.getPassword())
					.savePassword(true)
					.testConnection()
					.connectAfter(connectAfter)
					.finish(); 
        	break;
        case "SalesForce":
        	SalesforceConnectionProfileWizard.openWizard(cpName)
        			.setUsername(cp.getUsername())
        			.setPassword(cp.getPassword())
        			.testConnection()
        			.connectAfter(connectAfter)
        			.finish();
        	break;
        case "LDAP":  
        	LdapConnectionProfileWizard.openWizard(cpName)
        			.setHostname(cp.getHostname())
        			.setPort(cp.getPort())
        			.nextPage()
        			.setUsername(cp.getUsername())
        			.setPassword(cp.getPassword())
        			.testConnection()
        			.connectAfter(connectAfter)
        			.finish();
        	break;
        case "Ingres": //when TEIIDDES-2905 be done, delete this and uncomment Ingres below the PostgeSQL case
			createConnectionProfileDirectly(cp);
        	break;

        default: 
        	new AssertionError("Wizard for CP which has vendor name '"+ vendor +"' not yet implemented.");
            break;
		}
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
	
	//TODO delete after TEIIDDES-2905 will be done
	private IConnectionProfile createConnectionProfileDirectly(ConnectionProfileConfig cp){

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

			try{
				profile = profileManager.createProfile(cp.getName(), cp.getDescription(), cpProps.getProperty("providerID"),
						props);
				profileManager.addProfile(profile, true);
			}catch(ConnectionProfileException ex){
				ex.printStackTrace();
			}

			return profile;
		}
}
