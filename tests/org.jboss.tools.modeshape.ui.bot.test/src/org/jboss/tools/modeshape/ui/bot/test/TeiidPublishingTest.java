package org.jboss.tools.modeshape.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.modeshape.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.modeshape.reddeer.util.ModeshapeWebdav;
import org.jboss.tools.modeshape.reddeer.util.TeiidDriver;
import org.jboss.tools.modeshape.reddeer.view.ModeshapeExplorer;
import org.jboss.tools.modeshape.reddeer.view.ModeshapeView;
import org.jboss.tools.modeshape.reddeer.wizard.ImportProjectWizard;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Bot test for publishing Teiid files into ModeShape repository.
 * 
 * @author apodhrad, lfabriko, tsedmik
 */
@CleanWorkspace
@OpenPerspective(TeiidPerspective.class)
@Server(type = ServerReqType.ANY, state = ServerReqState.RUNNING)
@RunWith(RedDeerSuite.class)
public class TeiidPublishingTest {

	@InjectRequirement
	private ServerRequirement server;

	public static final String SERVER_URL = "http://localhost:8080/modeshape-rest";
	public static final String PUBLISH_AREA = "files";
	public static final String WORKSPACE = "default";

	@Test
	public void publishingTest() throws Exception {

		String user = server.getConfig().getServerBase().getProperty("modeshapeUser");
		String password = server.getConfig().getServerBase().getProperty("modeshapePassword");

		new ModeshapeView().addServer(SERVER_URL, user, password);
		new ImportProjectWizard("resources/projects/ModeShapeGoodies.zip").execute();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		tryToClosePasswordDialog();
		new ModeshapeExplorer().getProject("ModeShapeGoodies").getProjectItem("RelModels", "Books_Oracle.xmi").open();
		tryToClosePasswordDialog();
		String repository = server.getConfig().getServerBase().getProperty("modeshape");
		new ModeshapeView().addPublishArea(SERVER_URL, repository, WORKSPACE, PUBLISH_AREA);

		System.out.println("DEBUG: publishing area " + PUBLISH_AREA + " added");

		new ModeshapeExplorer().publish("ModeShapeGoodies").finish();

		checkPublishedFile("BookDatatypes.xsd");
		checkPublishedFile("Books.xsd");
		checkPublishedFile("BooksDoc.xmi");
		checkPublishedFile("BooksMode.vdb");
		checkPublishedFile("Parts_Metadata.txt");
		checkPublishedFile("PartsData.csv");
		checkPublishedFile("RelModels/Books_Oracle.xmi");
		checkPublishedFile("RelModels/BooksInfo.xmi");
		System.out.println("DEBUG: files published (rest)");

		/* Test ModeShape VDB on Teiid server */
		String path = server.getConfig().getServerBase().getHome();
		if (repository.equals("dv") || repository.equals("sample")) {
			// MODESHAPE = dv
			String driverPath = TeiidDriver.getDriverPath(path);
			DriverManager.registerDriver(new TeiidDriver(path + driverPath));
		} else {
			// MODESHAPE = eds
			DriverManager.registerDriver(new TeiidDriver(path + "/client/teiid-client.jar"));
		}

		Connection conn = DriverManager.getConnection("jdbc:teiid:ModeShape@mm://localhost:31000", user, password);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM ModeShape.xmi_model");
		List<String> result = new ArrayList<String>();
		while (rs.next()) {
			result.add(rs.getString("jcr_name"));
		}
		conn.close();
		if (repository.equals("dv")) {
			assertTrue("Model 'Books_Oracle' isn't involved in ModeShape VDB", result.contains("Books_Oracle.xmi"));
			assertTrue("Model 'BooksInfo' isn't involved in ModeShape VDB", result.contains("BooksInfo.xmi"));
		} else {
			assertTrue("Model 'Books_Oracle' isn't involved in ModeShape VDB", result.contains("Books_Oracle"));
			assertTrue("Model 'BooksInfo' isn't involved in ModeShape VDB", result.contains("BooksInfo"));
		}
		System.out.println("DEBUG: files published (sql query)");
	}

	private static void tryToClosePasswordDialog() {

		try {
			// Sometimes the password is required (especially in new versions of teiid designer)
			new WaitUntil(new ShellWithTextIsAvailable("Missing Password Required"));
			new DefaultShell("Missing Password Required");
			new DefaultText().setText("mm");
			new PushButton("OK").click();
		} catch (Exception e) {
		}
	}

	private void checkPublishedFile(String path) throws IOException {

		String user = server.getConfig().getServerBase().getProperty("modeshapeUser");
		String password = server.getConfig().getServerBase().getProperty("modeshapePassword");
		String repository = server.getConfig().getServerBase().getProperty("modeshape");
		boolean result = new ModeshapeWebdav(SERVER_URL + "/v1", repository, WORKSPACE + "/items", PUBLISH_AREA)
				.isFileAvailable(path, user, password);
		assertTrue("File '" + path + "' isn't published", result);
	}
}
