package org.jboss.tools.drools.ui.bot.test.smoke;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.drools.ui.bot.test.util.ProjectTestParent;
import org.jboss.tools.drools.ui.bot.test.util.ProjectUtility;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UsePerspective;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeReqType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Runtime(type = RuntimeReqType.DROOLS)
@RunWith(RedDeerSuite.class)
public class ConvertProjectTest extends ProjectTestParent {

	@InjectRequirement
	protected RuntimeRequirement droolsRuntime;

	@Test
	@UsePerspective(JavaPerspective.class)
	public void convertToDroolsProjectTest() {
		final String projectName = this.getMethodName();

		ProjectUtility.createJavaProject(projectName);
		PackageExplorer packageExplorer = new PackageExplorer();

		Assert.assertTrue("Project is not created.", packageExplorer.containsProject(projectName));
		Assert.assertFalse("Project already has Drools dependencies.",packageExplorer.getProject(projectName).containsItem(DROOLS_LIBRARY_NAME));

		packageExplorer.getProject(projectName).select();
		new ContextMenu(CONVERT_TO_DROOLS_PROJECT_PATH).select();
		new WaitWhile(new JobIsRunning());

		Assert.assertTrue("Project does not have Drools dependencies.", packageExplorer.getProject(projectName).containsItem(DROOLS_LIBRARY_NAME));
		assertNoErrorsInProblemsView();
	}
}
