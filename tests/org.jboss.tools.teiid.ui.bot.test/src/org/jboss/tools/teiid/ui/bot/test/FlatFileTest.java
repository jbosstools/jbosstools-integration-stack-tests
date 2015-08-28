package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.editor.SQLScrapbookEditor;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.SQLResult;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Flat file Import test
 * 
 * @author felias
 *
 */
@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING)
public class FlatFileTest {
	private static Logger logger = new Logger(FlatFileTest.class);

	private final String VDB_FILELOCAL = "LocalFileVDB";
	private final String VDB_FILEREMOTE = "RemoteFileVDB";
	private static final String MODEL_PROJECT = "Flat_file_import";
	private final String LOCAL_FILE_PATH = "resources/flat/productdata_data.csv";
	private final String REMOTE_FILE_PATH = "https://raw.githubusercontent.com/mmakovy/import-files/master/productdata_data.csv";
	private final String EXPECTED_RESULT_PATH = "resources/query_results/flat_file_importer.csv";

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	@BeforeClass
	public static void before() {
		// Creates a new project named Flat_file_import
		new ShellMenu("File", "New", "Teiid Model Project").select();
		new DefaultShell("New Model Project");
		new LabeledText("Project name:").setText(MODEL_PROJECT);
		new PushButton("Next >").click();
		new PushButton("Finish").click();
	}

	/**
	 * Creates a VDB from a file from a local computer
	 */

	@Test
	public void localFileTest() {
		new TeiidPerspective().open();
		TeiidBot teiidBot = new TeiidBot();
		teiidBot.toAbsolutePath("resources/flat/productdata_data.csv");

		// Run wizard for file import
		new ShellMenu("File", "Import...").select();
		new DefaultShell("Import");
		new DefaultTreeItem("Teiid Designer", "File Source (Flat) >> Source and View Model").select();
		new PushButton("Next >").click();
		new RadioButton(new DefaultGroup("Select Flat File Import Mode"), "Flat file on local file system").click();
		new PushButton("Next >").click();
		new PushButton(new DefaultGroup("Source Model Definition"), "...").click();
		new DefaultTreeItem(MODEL_PROJECT).select();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(MODEL_PROJECT).select();
		new PushButton("OK").click();

		// Creates new Data File source
		new DefaultShell("Import From Flat File Source");
		new PushButton(new DefaultGroup("Data File Source"), "New...").click();
		new DefaultShell("New Connection Profile");
		new LabeledText("Name:").setText("Local_flat");
		try {
			new PushButton("Next >").click();
		} catch (Exception e) {
			logger.warn("Timeout when opening Data File Importer. Waiting for next 10 seconds.");
		}
		new WaitUntil(new ShellWithTextIsActive("New Flat File Data Source Profile"), TimePeriod.NORMAL);
		new DefaultShell("New Flat File Data Source Profile");
		new RadioButton("Enter file URI:").click();
		new DefaultText(1).setText(teiidBot.toAbsolutePath(LOCAL_FILE_PATH));
		new PushButton("Test Connection").click();
		new DefaultShell("Success");
		new PushButton("OK").click();
		new DefaultShell("New Flat File Data Source Profile");
		new PushButton("Next >").click();
		new PushButton("Finish").click();

		// File import wizard continues
		new PushButton("Next >").click();
		new RadioButton(new DefaultGroup("Select Column Format"), "Character delimited").click();
		new PushButton("Next >").click();
		new PushButton("Next >").click();
		new LabeledText(new DefaultGroup("View Model Definition"), "Name:").setText("LocalView");
		new LabeledText(new DefaultGroup("View Model Definition"), "New view table name:").setText("local_products");
		new DefaultShell("Import From Flat File Source");
		new PushButton(new DefaultGroup("View Model Definition"), "...").click();
		new DefaultTreeItem(MODEL_PROJECT).select();
		new PushButton("OK").click();
		new PushButton("Finish").click();
		AbstractWait.sleep(TimePeriod.NORMAL); // waiting for progress bar to show
		new WaitWhile(new ShellWithTextIsActive("Progress Information")); // waiting for progress bar to dissepear
		new WaitWhile(new JobIsRunning());

		//Deploying the VDB
		new VDBManager().createVDB(MODEL_PROJECT, VDB_FILELOCAL);
		new VDBManager().addModelsToVDB(MODEL_PROJECT, VDB_FILELOCAL, new String[] {"SourceModel.xmi", "LocalView.xmi"});
		new VDBManager().executeVDB(false, MODEL_PROJECT, VDB_FILELOCAL);

		new WaitWhile(new ShellWithTextIsActive("Progress Information"));

		SQLScrapbookEditor editor = getCorrectScrapbook(VDB_FILELOCAL);
		if (editor == null) {
			throw new RuntimeException("Can't find the database: " + VDB_FILELOCAL);
		}

		editor.setText("select * from local_products");
		editor.executeAll();

		SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation("select * from local_products");
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());
		assertTrue("Unexpected Query Results", result.compareCSVQueryResults(new File(teiidBot.toAbsolutePath(EXPECTED_RESULT_PATH))));

	}

	/*
	 * Creates a VDB using remote file
	 */
	@Test
	public void remoteFileTest() {
		new TeiidPerspective().open();
		TeiidBot teiidBot = new TeiidBot();
		new ShellMenu("File", "Import...").select();
		new DefaultShell("Import");
		new DefaultTreeItem("Teiid Designer", "File Source (Flat) >> Source and View Model").select();
		new PushButton("Next >").click();
		new RadioButton(new DefaultGroup("Select Flat File Import Mode"), "Flat file via remote URL").click();
		new PushButton("Next >").click();
		new PushButton(new DefaultGroup("Source Model Definition"), "...").click();
		new DefaultTreeItem(MODEL_PROJECT).select();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(MODEL_PROJECT).select();
		new PushButton("OK").click();

		// Creates new Data File source
		new DefaultShell("Import From Flat File Source");
		new PushButton(new DefaultGroup("Data File Source"), "New...").click();
		new DefaultShell("New Connection Profile");
		new LabeledText("Name:").setText("Remote_flat");
		try {
			new PushButton("Next >").click();
		} catch (Exception e) {
			logger.warn("Timeout when opening Data File Importer. Waiting for next 10 seconds.");
		}
		new WaitUntil(new ShellWithTextIsActive("New connection profile"), TimePeriod.NORMAL);
		new DefaultShell("New connection profile");
		new LabeledText("Connection URL").setText(REMOTE_FILE_PATH);
		new PushButton("Test Connection").click();
		new DefaultShell("Success");
		new PushButton("OK").click();
		new DefaultShell("New connection profile");
		new PushButton("Next >").click();
		new PushButton("Finish").click();

		// File import wizard continues
		new DefaultShell("Import From Flat File Source");
		new LabeledText("Name:").setText("SourceRemoteFileModel");
		new PushButton("Next >").click();
		new RadioButton(new DefaultGroup("Select Column Format"), "Character delimited").click();
		new PushButton("Next >").click();
		new PushButton("Next >").click();
		new PushButton(new DefaultGroup("View Model Definition"), "...").click();
		new DefaultTreeItem(MODEL_PROJECT).select();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(MODEL_PROJECT).select();
		new PushButton("OK").click();

		new LabeledText(new DefaultGroup("View Model Definition"), "Name:").setText("RemoteView");
		new LabeledText(new DefaultGroup("View Model Definition"), "New view table name:").setText("Products");
		new PushButton("Finish").click();
		AbstractWait.sleep(TimePeriod.NORMAL); // waiting for progress bar to show
		new WaitWhile(new ShellWithTextIsActive("Progress Information")); // waiting for progress bar to dissepear
		new WaitWhile(new JobIsRunning());

		//Deploying the VDB
		new VDBManager().createVDB(MODEL_PROJECT, VDB_FILEREMOTE);
		new VDBManager().addModelsToVDB(MODEL_PROJECT, VDB_FILEREMOTE, new String[] {"SourceRemoteFileModel.xmi", "RemoteView.xmi"});
		new VDBManager().executeVDB(false, MODEL_PROJECT, VDB_FILEREMOTE);
		
		new WaitWhile(new ShellWithTextIsActive("Progress Information"));

		SQLScrapbookEditor editor = getCorrectScrapbook(VDB_FILEREMOTE);
		if (editor == null) {
			throw new RuntimeException("Can't find the database: " + VDB_FILEREMOTE);
		}
		editor.setText("select * from products");
		editor.executeAll();

		SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation("select * from products");
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());
		assertTrue("Unexpected Query Results", result.compareCSVQueryResults(new File(teiidBot.toAbsolutePath(EXPECTED_RESULT_PATH))));
	}


	private SQLScrapbookEditor getCorrectScrapbook(String database) {

		SQLScrapbookEditor editor = null;
		for (int i = 0; i < 10; i++) {
			try {
				new WorkbenchShell();
				editor = new SQLScrapbookEditor("SQL Scrapbook" + i);
				editor.show();
				try {
					editor.setDatabase(database);
				} catch (Exception e) {
					continue;
				}
				break;
			} catch (Exception e) {
				return null;
			}
		}
		return editor;
	}

}