package org.jboss.tools.drools.ui.bot.test.functional;

import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
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
		PackageExplorer packageExplorer = new PackageExplorer();
		packageExplorer.getProject(projectName).select();
		new ContextMenu(new RegexMatcher("Maven.*"), new RegexMatcher("Update Project.*")).select();
		new PushButton("OK").click();
		WaitWhile.sleep(TimePeriod.LONG);
	}
}
