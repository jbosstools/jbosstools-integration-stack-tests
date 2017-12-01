package org.jboss.tools.drools.ui.bot.test.util;

import org.apache.log4j.Logger;
import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.eclipse.jdt.ui.packageview.PackageExplorerPart;
import org.eclipse.reddeer.eclipse.ui.console.ConsoleView;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;

public final class RunUtility {
	private static final Logger LOGGER = Logger.getLogger(RunUtility.class);

	public static void runAsJavaApplication(String projectName, String... path) {
		runAsJavaApplication(false, projectName, path);
	}

	public static void runAsJavaApplication(boolean useContextMenu, String projectName, String... path) {
		selectProject(projectName, path);

		if (useContextMenu) {
			new ContextMenuItem(new WithTextMatcher("Run As"), new RegexMatcher(".*Java Application.*")).select();
		} else {
			new ShellMenuItem(new WorkbenchShell(), new WithTextMatcher("Run"), new WithTextMatcher("Run As"),
					new RegexMatcher(".*Java Application.*")).select();
		}

		waitAfterStarting();
	}

	public static void debugAsDroolsApplication(String projectName, String... path) {
		debugAsDroolsApplication(false, projectName, path);
	}

	public static void debugAsDroolsApplication(boolean useContextMenu, String projectName, String... path) {
		selectProject(projectName, path);

		if (useContextMenu) {
			new ContextMenuItem(new WithTextMatcher("Debug As"), new RegexMatcher(".*Drools Application.*")).select();
		} else {
			new ShellMenuItem(new WorkbenchShell(), new WithTextMatcher("Run"), new WithTextMatcher("Debug As"),
					new RegexMatcher(".*Drools Application.*")).select();
		}

		waitAfterStarting();

		try {
			new DefaultShell("Confirm Perspective Switch");
			new PushButton("Yes").click();
		} catch (Exception ex) {
			LOGGER.debug("Confirm Perspective Switch dialog not shown.");
		}
	}

	public static String runTest(String projectName, String className) {
		final String testDirectory = "src/main/java";
		final String testPackage = "com.sample";

		ConsoleView consoleView = new ConsoleView();
		consoleView.open();

		runAsJavaApplication(projectName, testDirectory, testPackage, className);
		new WaitUntil(new ApplicationIsTerminated(), TimePeriod.LONG);

		consoleView.open();

		String consoleText = consoleView.getConsoleText();
		LOGGER.debug(consoleText);

		return consoleText;
	}

	private static void selectProject(String projectName, String... path) {
		PackageExplorerPart explorer = new PackageExplorerPart();
		explorer.getProject(projectName).getProjectItem(path).select();
	}

	private static void waitAfterStarting() {
		try {
			new DefaultShell("Progress Information");
			new WaitWhile(new ShellIsActive("Progress Information"), TimePeriod.VERY_LONG);
		} catch (Exception ex) {
			LOGGER.debug("'Progress Information' shell was not shown.");
		}
	}
}
