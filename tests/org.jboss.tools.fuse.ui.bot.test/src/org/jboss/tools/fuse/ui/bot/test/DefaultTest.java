package org.jboss.tools.fuse.ui.bot.test;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.handler.ShellHandler;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.matcher.WithTooltipTextMatcher;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.fuse.reddeer.preference.ConsolePreferencePage;
import org.jboss.tools.fuse.reddeer.preference.FuseToolingEditorPreferencePage;
import org.jboss.tools.fuse.reddeer.server.ServerManipulator;
import org.jboss.tools.fuse.reddeer.view.ErrorLogView;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Prepares environment for tests
 * 
 * @author tsedmik
 */
public class DefaultTest {

	private static Logger log = Logger.getLogger(DefaultTest.class);

	@BeforeClass
	public static void defaultClassSetup() {

		log.info("Maximizing workbench shell.");
		new WorkbenchShell().maximize();

		log.info("Set 'ID values will be used for labels if existing'");
		FuseToolingEditorPreferencePage preferences = new FuseToolingEditorPreferencePage();
		preferences.open();
		preferences.setShowIDinEditor(true);
		preferences.ok();

		log.info("Disable showing Console view after standard output changes");
		ConsolePreferencePage consolePref = new ConsolePreferencePage();
		consolePref.open();
		consolePref.toggleShowConsoleErrorWrite(false);
		consolePref.toggleShowConsoleStandardWrite(false);
		consolePref.ok();
	}

	@Before
	public void defaultSetup() {

		new WorkbenchShell();

		log.info("Deleting Error Log.");
		new ErrorLogView().deleteLog();
	}

	@After
	public void defaultClean() {

		new WorkbenchShell();

		log.info("Closing all non workbench shells.");
		ShellHandler.getInstance().closeAllNonWorbenchShells();

		log.info("Try to terminate a console.");
		ConsoleView console = new ConsoleView();
		console.open();
		try {
			console.terminateConsole();
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		} catch (SWTLayerException ex) {
			log.warn("Cannot terminate a console. Perhaps there is no active console.");
		}

		log.info("Save editor");
		try {
			new DefaultToolItem(new WorkbenchShell(), 0, new WithTooltipTextMatcher(new RegexMatcher("Save All.*"))).click();
		} catch (Exception e) {
			log.info("Nothing to save");
		}
	}

	@AfterClass
	public static void defaultFinalClean() {

		new WorkbenchShell();
		
		log.info("Deleting all projects");
		new ProjectExplorer().deleteAllProjects();

		log.info("Stopping and deleting configured servers");
		ServerManipulator.deleteAllServers();
		ServerManipulator.deleteAllServerRuntimes();
	}
}