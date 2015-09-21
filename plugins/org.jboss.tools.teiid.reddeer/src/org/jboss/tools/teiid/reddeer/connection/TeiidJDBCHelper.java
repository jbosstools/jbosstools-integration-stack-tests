package org.jboss.tools.teiid.reddeer.connection;

import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.tools.teiid.reddeer.util.TeiidDriver;

/**
 * Helper for testing deployed VDBs using JDBC connection
 * 
 * @author: mmakovy
 */
public class TeiidJDBCHelper {

	private String pathToDriver;
	private Connection connection;
	private String username;
	private String password;
	private String vdbName;
	
	private static final String TEIID_DRIVER_CLASS = "org.teiid.jdbc.TeiidDriver";
	private static Logger log = Logger.getLogger(TeiidJDBCHelper.class);
	

	public TeiidJDBCHelper(String pathToDriver, String vdbName, String username, String password) {
		this.pathToDriver = pathToDriver;
		this.username = username;
		this.password = password;
		this.vdbName = vdbName;
	}

	/**
	 * Method creates JDBC conenction to teiid VDB
	 * 
	 * @param vdbName
	 *            name of desired VDB
	 * @param username
	 *            username for connection
	 * @param password
	 *            password for connection
	 */
	public void createConnection() throws SQLException {
		if (username == null) throw new IllegalArgumentException("username is null");
		if (password == null) throw new IllegalArgumentException("password is null");
		if (vdbName == null) throw new IllegalArgumentException("vdbName is null");
		
		registerDriver();
		log.info("Connecting to VDB " + vdbName + " with username " + username + " and password " + password);
		connection = DriverManager.getConnection("jdbc:teiid:" + vdbName + "@mm://localhost:31000", username, password);
	}

	/**
	 * Method executes query against existing database connection
	 * 
	 * @param connection
	 *            existing database connection
	 * @param query
	 *            actual SQL query
	 * @return resultSet - result of the execution of query
	 */
	public ResultSet executeQuery(String query) throws SQLException {
		createConnection();
		if (connection == null) {
			throw new IllegalArgumentException("Connection is null (not established?)");
		}
		
		Statement st = connection.createStatement();
		log.info("Executing query " + query);
		ResultSet resultSet = st.executeQuery(query);
		return resultSet;

	}

	public void closeConnection() {
		log.info("Closing connection to VDB");
		if (connection == null) {
			throw new IllegalStateException("Connection is null");
		} else {
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (Exception ex) {
				log.error("Error while closing the connection");
				ex.printStackTrace();
			}
		}

	}

	/**
	 * Private method for registering Teiid Driver in DriverManager
	 * 
	 */
	private void registerDriver() throws SQLException {
		if (pathToDriver == null)
			throw new IllegalArgumentException("path to driver is null");

		try {
			URL url = new URL("jar:file:" + pathToDriver + "!/");
			URLClassLoader ucl = new URLClassLoader(new URL[] { url });
			Driver driver = (Driver) Class.forName(TEIID_DRIVER_CLASS, true, ucl).newInstance();
			DriverManager.registerDriver(new TeiidDriver(driver));

		} catch (Exception ex) {
			log.error("Exception during registration of Teiid Driver");
			ex.printStackTrace();
		}

	}

}
