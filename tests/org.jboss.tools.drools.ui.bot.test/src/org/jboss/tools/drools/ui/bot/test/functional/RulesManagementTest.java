package org.jboss.tools.drools.ui.bot.test.functional;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.eclipse.ui.perspectives.ResourcePerspective;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed.Jira;
import org.jboss.tools.drools.reddeer.editor.DrlEditor;
import org.jboss.tools.drools.reddeer.editor.RuleEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.ui.bot.test.util.ApplicationIsTerminated;
import org.jboss.tools.drools.ui.bot.test.util.OpenUtility;
import org.jboss.tools.drools.ui.bot.test.util.RunUtility;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UsePerspective;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeReqType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Runtime(type = RuntimeReqType.DROOLS)
@RunWith(RedDeerSuite.class)
public class RulesManagementTest extends TestParent {
	private static final Logger LOGGER = Logger.getLogger(RulesManagementTest.class);
	private static final String DEBUG_1 = "KieModule was added:";
	private static final String DEBUG_2 = "Found kmodule:";
	private static final String DEBUG_3 = "SLF4J:";
	private static final String OUTPUT_1 = "Hello World";
	private static final String OUTPUT_2 = "Goodbye cruel world";
	private static final String ERROR_1 = "error";
	private static final String ERROR_2 = "failure";

	@InjectRequirement
	private RuntimeRequirement droolsRequirement;

	@Test
	@UsePerspective(JavaPerspective.class)
	@UseDefaultProject
	public void testRunRulesFromContextMenu() {
		ConsoleView console = new ConsoleView();
		console.open();

		RunUtility.runAsJavaApplication(true, DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");
		new WaitUntil(new ApplicationIsTerminated());
		AbstractWait.sleep(TimePeriod.SHORT); // this is quite annoying - the text is updated AFTER the application is terminated

		console.open();
		String consoleText = console.getConsoleText();
		Assert.assertNotNull("Console text was empty.", consoleText);
		LOGGER.debug(consoleText);
		Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.contains(OUTPUT_1));
		Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.contains(OUTPUT_2));
		Assert.assertFalse("Unexpected error in console\n" + consoleText, consoleText.toLowerCase().contains(ERROR_1));
		Assert.assertFalse("Unexpected error in console\n" + consoleText, consoleText.toLowerCase().contains(ERROR_2));
	}

	@Test
	@UsePerspective(JavaPerspective.class)
	@UseDefaultProject
	public void testRunRulesFromToolbar() {
		ConsoleView console = new ConsoleView();
		console.open();

		RunUtility.runAsJavaApplication(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");
		new WaitUntil(new ApplicationIsTerminated());
		AbstractWait.sleep(TimePeriod.SHORT); // this is quite annoying - the text is updated AFTER the application is terminated

		console.open();
		String consoleText = console.getConsoleText();
		Assert.assertNotNull("Console text was empty.", consoleText);
		LOGGER.debug(consoleText);
		Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.contains(OUTPUT_1));
		Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.contains(OUTPUT_2));
		Assert.assertFalse("Unexpected error in console\n" + consoleText, consoleText.toLowerCase().contains(ERROR_1));
		Assert.assertFalse("Unexpected error in console\n" + consoleText, consoleText.toLowerCase().contains(ERROR_2));
	}

	@Test
	@UsePerspective(JavaPerspective.class)
	@UseDefaultProject
	public void testRenameProject() {
		final String oldName = DEFAULT_PROJECT_NAME;
		final String newName = "renamed" + oldName;

		PackageExplorer explorer = new PackageExplorer();
		Assert.assertTrue("The original project was not created.", explorer.containsProject(oldName));
		explorer.getProject(oldName).select();

		new ContextMenu(new RegexMatcher("Refactor.*"), new RegexMatcher("Rename.*")).select();

		new DefaultShell("Rename Java Project");
		new LabeledText("New name:").setText(newName);
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning());

		AbstractWait.sleep(TimePeriod.SHORT);
		Assert.assertFalse("The original project is still present.", explorer.containsProject(oldName));
		Assert.assertTrue("The renamed project is not present.", explorer.containsProject(newName));
	}

	@Test
	@Jira("RHBRMS-1795")
	@RunIf(conditionClass = IssueIsClosed.class)
	@UsePerspective(JavaPerspective.class)
	@UseDefaultProject
	public void testSetBreakpoint() {
		OpenUtility.openResource(DEFAULT_PROJECT_NAME, getResourcePath("Sample.drl"));

		RuleEditor editor = new DrlEditor().showRuleEditor();
		editor.setPosition(8, 0);

		new ResourcePerspective().open();
		new JavaPerspective().open();

		new ShellMenu(new RegexMatcher("Run"), new RegexMatcher("Toggle Breakpoint.*")).select();
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testDebugRule() {
		OpenUtility.openResource(DEFAULT_PROJECT_NAME, getResourcePath("Sample.drl"));

		new DrlEditor().showRuleEditor().setBreakpoint(8);

		RunUtility.debugAsDroolsApplication(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");

		ConsoleView console = new ConsoleView();
		console.open();
		String consoleText = console.getConsoleText();
		Assert.assertNotNull("Console text was empty.", consoleText);
		LOGGER.debug(consoleText);

		if (!consoleText.contains(DEBUG_3)) {
			Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.contains(DEBUG_1));
			Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.contains(DEBUG_2));
		}

		// wait a moment before Debug perspective is fully loaded
		AbstractWait.sleep(TimePeriod.SHORT);

		new ShellMenu(new RegexMatcher("Run"), new RegexMatcher("Resume.*")).select();
		console.open();
		AbstractWait.sleep(TimePeriod.SHORT);
		consoleText = console.getConsoleText();
		Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.contains(OUTPUT_1));
		Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.contains(OUTPUT_2));
		Assert.assertFalse("Unexpected error in console\n" + consoleText, consoleText.toLowerCase().contains(ERROR_1));
		Assert.assertFalse("Unexpected error in console\n" + consoleText, consoleText.toLowerCase().contains(ERROR_2));
	}
}
