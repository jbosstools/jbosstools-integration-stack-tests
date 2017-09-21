package org.jboss.tools.drools.ui.bot.test.functional;

import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.jdt.ui.packageview.PackageExplorerPart;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.jboss.tools.drools.ui.bot.test.util.ProjectTestParent;
import org.jboss.tools.drools.ui.bot.test.util.ProjectUtility;
import org.jboss.tools.drools.ui.bot.test.util.RunUtility;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UsePerspective;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public class MavenProjectTest extends ProjectTestParent {

	@Test
	@UsePerspective(JavaPerspective.class)
	public void testMavenDecisionTableClass() {
		final String projectName = this.getMethodName();
		final String className = "DecisionTableTest.java"; 

		ProjectUtility.createMavenProjectWithDecisionTableSample(projectName);
		updateMavenProject(projectName);
		String consoleText = RunUtility.runTest(projectName, className);

		assertConsoleText(consoleText, HELLO_GOODBYE_WORLD_REGEX);
	}

	@Test
	@UsePerspective(JavaPerspective.class)
	public void testMavenDroolsClass() {
		final String projectName = this.getMethodName();
		final String className = "DroolsTest.java"; 

		ProjectUtility.createMavenProjectWithRuleSample(projectName);
		updateMavenProject(projectName);
		String consoleText = RunUtility.runTest(projectName, className);

		assertConsoleText(consoleText, HELLO_GOODBYE_WORLD_REGEX);
	}
	
	@Test
	@UsePerspective(JavaPerspective.class)
	public void testMavenProcessClass() {
		final String projectName = this.getMethodName();
		final String className = "ProcessTest.java"; 

		ProjectUtility.createMavenProjectWithProcessSample(projectName);
		updateMavenProject(projectName);
		String consoleText = RunUtility.runTest(projectName, className);

		assertConsoleText(consoleText, HELLO_WORLD_REGEX);
	}
	
	@SuppressWarnings("unchecked")
	private void updateMavenProject(String projectName) {
		PackageExplorerPart packageExplorer = new PackageExplorerPart();
		packageExplorer.getProject(projectName).select();
		new ContextMenuItem(new RegexMatcher("Maven.*"), new RegexMatcher("Update Project.*")).select();
		new PushButton("OK").click();
		WaitWhile.sleep(TimePeriod.LONG);
	}
}
