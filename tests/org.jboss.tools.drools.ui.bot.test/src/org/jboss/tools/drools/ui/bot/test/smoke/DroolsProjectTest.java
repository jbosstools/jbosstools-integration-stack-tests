package org.jboss.tools.drools.ui.bot.test.smoke;

import java.io.UnsupportedEncodingException;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.drools.ui.bot.test.util.ProjectTestParent;
import org.jboss.tools.drools.ui.bot.test.util.ProjectUtility;
import org.jboss.tools.drools.ui.bot.test.util.RunUtility;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UsePerspective;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeReqType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;

@Runtime(type = RuntimeReqType.DROOLS)
@RunWith(RedDeerSuite.class)
public class DroolsProjectTest extends ProjectTestParent {

	@InjectRequirement
	protected RuntimeRequirement droolsRuntime;

	@Test
	@UsePerspective(JavaPerspective.class)
	public void testRuntimeDecisionTableClass() throws UnsupportedEncodingException {
		final String projectName = this.getMethodName();
		final String className = "DecisionTableTest.java"; 

		assertNoErrorsInProblemsView();
		ProjectUtility.createDroolsProjectWithDecisionTableSample(projectName);

		String consoleText = RunUtility.runTest(projectName, className);
		assertConsoleText(consoleText, HELLO_GOODBYE_WORLD_REGEX);

		assertProjectAndDelete(projectName);
	}
	
	@Test
	@UsePerspective(JavaPerspective.class)
	public void testRuntimeDroolsClass() throws UnsupportedEncodingException {
		final String projectName = this.getMethodName();
		final String className = "DroolsTest.java"; 

		assertNoErrorsInProblemsView();
		ProjectUtility.createDroolsProjectWithRuleSample(projectName);

		String consoleText = RunUtility.runTest(projectName, className);
		assertConsoleText(consoleText, HELLO_GOODBYE_WORLD_REGEX);

		assertProjectAndDelete(projectName);
	}

	@Test
	@UsePerspective(JavaPerspective.class)
	public void testRuntimeProcessClass() throws UnsupportedEncodingException {
		boolean droolsRuntimeIsUsed = getTestName().contains("drools-runtime-config.xml");
		Assume.assumeFalse("Test does not work with drools runtime - DROOLS-1211", droolsRuntimeIsUsed);

		final String projectName = this.getMethodName();
		final String className = "ProcessTest.java"; 

		assertNoErrorsInProblemsView();
		ProjectUtility.createDroolsProjectWithProcessSample(projectName);

		String consoleText = RunUtility.runTest(projectName, className);
		assertConsoleText(consoleText, HELLO_WORLD_REGEX);

		assertProjectAndDelete(projectName);
	}
	
	private void assertProjectAndDelete(String projectName) throws UnsupportedEncodingException {
		PackageExplorer packageExplorer = new PackageExplorer();

		Assert.assertTrue("Project is not created.", packageExplorer.containsProject(projectName));
		Assert.assertTrue("Project does not have Drools dependencies.", packageExplorer.getProject(projectName).containsItem(DROOLS_LIBRARY_NAME));
		Assert.assertTrue("Wrong drools runtime is used.", findDroolsCoreJar(projectName).contains(getRuntimeLocation(droolsRuntime.getConfig().getRuntimeFamily().getHome())));
		assertNoErrorsInProblemsView();

		packageExplorer.getProject(projectName).delete(true);

		Assert.assertFalse("Project is not deleted.", packageExplorer.containsProject(projectName));
	}

	private String findDroolsCoreJar(String projectName) {
		new PackageExplorer().open();
		TreeItem droolsLibrary = new DefaultTreeItem(projectName, DROOLS_LIBRARY_NAME);
		for (TreeItem droolsJar : droolsLibrary.getItems()) {
			if (droolsJar.getText().startsWith(DROOLS_CORE_JAR_PREFIX)) {
				return droolsJar.getText();
			}
		}

		throw new RuntimeException(DROOLS_CORE_JAR_PREFIX + " jar is not found in " + DROOLS_LIBRARY_NAME + ".");
	}
}
