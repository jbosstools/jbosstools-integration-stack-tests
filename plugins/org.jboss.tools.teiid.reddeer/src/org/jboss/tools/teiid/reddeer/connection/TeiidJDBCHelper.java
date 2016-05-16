package org.jboss.tools.teiid.reddeer.connection;

import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
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

	public TeiidJDBCHelper(TeiidServerRequirement teiidServer, String vdbName) {
		this.pathToDriver = teiidServer.getTeiidDriverPath();
		this.username = teiidServer.getServerConfig().getServerBase().getProperty("teiidUser");
		this.password = teiidServer.getServerConfig().getServerBase().getProperty("teiidPassword");
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
		if (username == null)
			throw new IllegalArgumentException("username is null");
		if (password == null)
			throw new IllegalArgumentException("password is null");
		if (vdbName == null)
			throw new IllegalArgumentException("vdbName is null");

		registerDriver();
		log.info("Connecting to VDB " + vdbName + " with username " + username + " and password " + password);
		connection = DriverManager.getConnection("jdbc:teiid:" + vdbName + "@mm://localhost:31000", username, password);
	}

	/**
	 * Method executes query against existing database connection and expects
	 * result set
	 * 
	 * @param connection
	 *            existing database connection
	 * @param query
	 *            actual SQL query
	 * @return resultSet - result of the execution of query
	 */
	public ResultSet executeQueryWithResultSet(String query) throws SQLException {
		createConnection();
		if (connection == null) {
			throw new IllegalArgumentException("Connection is null (not established?)");
		}

		Statement st = connection.createStatement();
		log.info("Executing query " + query);
		ResultSet resultSet = st.executeQuery(query);
		return resultSet;

	}
	
	/**
	 * Executes query and retrieves XML output as String from ResultSet.
	 */
	public String executeQueryWithXmlStringResult(String query) throws SQLException {
		ResultSet rs = executeQueryWithResultSet(query);
		rs.next(); 			
        return rs.getString(1); 
	}

	/**
	 * Method executes queries against existing database connection 
	 * DDL queries don't return result set only just write in the console, how many lines was updated for each queries
	 * @param queries - list of queries
	 * @return list - list of result sets for each queries (except DDL queries)
	 * @throws SQLException
	 */
	public List<ResultSet> executeMultiQuery(String... queries) throws SQLException{
		createConnection();
		List<ResultSet> list = new ArrayList<ResultSet>();
		if (connection == null) {
			throw new IllegalArgumentException("Connection is null (not established?)");
		}
		ResultSet rs = null;
		for( int i = 0; i <= queries.length - 1; i++)
		{
			Statement st = connection.createStatement();
			log.info("Executing query " + queries[i]);
			if(queries[i].toLowerCase().contains("insert")||queries[i].toLowerCase().contains("delete")
					||queries[i].toLowerCase().contains("update")||queries[i].toLowerCase().contains("alter")){ //some DDL
				int numberUpdate = st.executeUpdate(queries[i]);
				log.info("DDL query change " + numberUpdate + " row/s");
			}else{
				rs = st.executeQuery(queries[i]);
				list.add(rs);
			}
		}
		return list;
	}
	
	/**
	 * Method executes query that has no result set against existing database connection
	 * 
	 * @param connection
	 *            existing database connection
	 * @param query
	 *            actual SQL query
	 * @return boolean - true if query has no result set, false otherwise
	 */
	public boolean executeQueryNoResultSet(String query) throws SQLException {
		createConnection();
		if (connection == null) {
			throw new IllegalArgumentException("Connection is null (not established?)");
		}

		Statement st = connection.createStatement();
		log.info("Executing query " + query);
		boolean hasResultSet = st.execute(query);
		return !hasResultSet;
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
	 * Public method for simple checking if query execution was successful
	 * 
	 * @param sql
	 *            SQL query
	 * @return true if query was successful, false otherwise
	 */
	public boolean isQuerySuccessful(String sql, boolean hasResultSet) {

		try {
			if (hasResultSet) {
				this.executeQueryWithResultSet(sql);
				return true;
			} else {
				return (this.executeQueryNoResultSet(sql));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}
	}

	/**
	 * Public method for retrieving number of results of supplied query
	 * 
	 * @param sql
	 *            SQL query
	 * @return number of results or -1 in case of failure
	 */
	public int getNumberOfResults(String sql) {
		try {
			ResultSet results = this.executeQueryWithResultSet(sql);
			int count = 0;
			while (results.next()) {
				++count;
			}
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			closeConnection();
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
