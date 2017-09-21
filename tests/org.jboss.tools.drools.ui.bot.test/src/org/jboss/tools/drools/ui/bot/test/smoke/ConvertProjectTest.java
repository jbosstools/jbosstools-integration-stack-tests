package org.jboss.tools.drools.ui.bot.test.smoke;

import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.jdt.ui.packageview.PackageExplorerPart;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.drools.ui.bot.test.util.ProjectTestParent;
import org.jboss.tools.drools.ui.bot.test.util.ProjectUtility;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UsePerspective;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Runtime
@RunWith(RedDeerSuite.class)
public class ConvertProjectTest extends ProjectTestParent {

	@InjectRequirement
	protected RuntimeRequirement droolsRuntime;

	@Test
	@UsePerspective(JavaPerspective.class)
	public void convertToDroolsProjectTest() {
		final String projectName = this.getMethodName();

		ProjectUtility.createJavaProject(projectName);
		PackageExplorerPart packageExplorer = new PackageExplorerPart();

		Assert.assertTrue("Project is not created.", packageExplorer.containsProject(projectName));
		Assert.assertFalse("Project already has Drools dependencies.",packageExplorer.getProject(projectName).containsResource(DROOLS_LIBRARY_NAME));

		packageExplorer.getProject(projectName).select();
		new ContextMenuItem(CONVERT_TO_DROOLS_PROJECT_PATH).select();
		new WaitWhile(new JobIsRunning());

		Assert.assertTrue("Project does not have Drools dependencies.", packageExplorer.getProject(projectName).containsResource(DROOLS_LIBRARY_NAME));
		assertNoErrorsInProblemsView();
	}
}
