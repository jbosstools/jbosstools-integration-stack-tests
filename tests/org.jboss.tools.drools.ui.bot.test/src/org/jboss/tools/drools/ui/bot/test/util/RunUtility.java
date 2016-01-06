package org.jboss.tools.drools.ui.bot.test.util;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;

public final class RunUtility {
	private static final Logger LOGGER = Logger.getLogger(RunUtility.class);

	private RunUtility() {
	}

	public static void runAsJavaApplication(String projectName, String... path) {
		runAsJavaApplication(false, projectName, path);
	}

	public static void runAsJavaApplication(boolean useContextMenu, String projectName, String... path) {
		selectProject(projectName, path);

		if (useContextMenu) {
			new ContextMenu(new WithTextMatcher("Run As"), new RegexMatcher(".*Java Application.*")).select();
		} else {
			new ShellMenu(new WithTextMatcher("Run"), new WithTextMatcher("Run As"),
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
			new ContextMenu(new WithTextMatcher("Debug As"), new RegexMatcher(".*Drools Application.*")).select();
		} else {
			new ShellMenu(new WithTextMatcher("Run"), new WithTextMatcher("Debug As"),
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

	private static void selectProject(String projectName, String... path) {
		PackageExplorer explorer = new PackageExplorer();
		explorer.getProject(projectName).getProjectItem(path).select();
	}

	private static void waitAfterStarting() {
		try {
			new DefaultShell("Progress Information");
			new WaitWhile(new ShellWithTextIsActive("Progress Information"), TimePeriod.VERY_LONG);
		} catch (Exception ex) {
			LOGGER.debug("'Progress Information' shell was not shown.");
		}
	}
}
