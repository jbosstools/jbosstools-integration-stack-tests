package org.jboss.tools.drools.ui.bot.test.functional;

import org.apache.log4j.Logger;
import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.jdt.ui.packageview.PackageExplorerPart;
import org.eclipse.reddeer.eclipse.ui.console.ConsoleView;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.eclipse.reddeer.eclipse.ui.perspectives.ResourcePerspective;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.drools.reddeer.editor.DrlEditor;
import org.jboss.tools.drools.reddeer.editor.RuleEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.ui.bot.test.util.ApplicationIsTerminated;
import org.jboss.tools.drools.ui.bot.test.util.OpenUtility;
import org.jboss.tools.drools.ui.bot.test.util.RunUtility;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UsePerspective;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeImplementationRestriction;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeImplementationType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@Runtime
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
	
	@RequirementRestriction
	public static RequirementMatcher getRequirementMatcher() {
		return new RuntimeImplementationRestriction(RuntimeImplementationType.DROOLS, RuntimeImplementationType.BRMS);
	}

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

		PackageExplorerPart explorer = new PackageExplorerPart();
		Assert.assertTrue("The original project was not created.", explorer.containsProject(oldName));
		explorer.getProject(oldName).select();

		new ContextMenuItem(new RegexMatcher("Refactor.*"), new RegexMatcher("Rename.*")).select();

		new DefaultShell("Rename Java Project");
		new LabeledText("New name:").setText(newName);
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning());

		AbstractWait.sleep(TimePeriod.SHORT);
		Assert.assertFalse("The original project is still present.", explorer.containsProject(oldName));
		Assert.assertTrue("The renamed project is not present.", explorer.containsProject(newName));
	}

	@Test
	@Ignore("RHBRMS-1795")
	@UsePerspective(JavaPerspective.class)
	@UseDefaultProject
	public void testSetBreakpoint() {
		new ResourcePerspective().open();
		new JavaPerspective().open();
		
		OpenUtility.openResource(DEFAULT_PROJECT_NAME, getResourcePath("Sample.drl"));

		RuleEditor editor = new DrlEditor().showRuleEditor();
		editor.setPosition(8, 0);

		new ShellMenuItem(new WorkbenchShell(), new RegexMatcher("Run"), new RegexMatcher("Toggle Breakpoint.*")).select();
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

		new ShellMenuItem(new WorkbenchShell(), new RegexMatcher("Run"), new RegexMatcher("Resume.*")).select();
		console.open();
		AbstractWait.sleep(TimePeriod.SHORT);
		consoleText = console.getConsoleText();
		Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.contains(OUTPUT_1));
		Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.contains(OUTPUT_2));
		Assert.assertFalse("Unexpected error in console\n" + consoleText, consoleText.toLowerCase().contains(ERROR_1));
		Assert.assertFalse("Unexpected error in console\n" + consoleText, consoleText.toLowerCase().contains(ERROR_2));
	}
}
