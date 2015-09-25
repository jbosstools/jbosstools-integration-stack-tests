package org.jboss.tools.esb.ui.bot.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.esb.reddeer.wizard.ProjectExamples;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests creation of ESB Project Examples (Help --> Project Examples...)
 * 
 * @author tsedmik
 */
@CleanWorkspace
@Server(type = {ServerReqType.AS, ServerReqType.EAP}, state = ServerReqState.PRESENT)
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class ProjectExamplesTest {

	@InjectRequirement
	ServerRequirement serverReq;

	@AfterClass
	public static void deleteAllProjects() {
		new ProjectExplorer().deleteAllProjects();
	}

	@Test
	public void testESBProjectExamples() {

		List<String[]> projects;
		ProjectExamples wizard = new ProjectExamples();
		StringBuilder result = new StringBuilder();
		
		// get all available projects
		wizard.open();
		projects = wizard.getESBProjects("5.0");
		wizard.cancel();
		assertTrue(projects.size() > 0);

		// try to create every available projects
		for (String[] project : projects) {

			// create projects
			wizard = new ProjectExamples();
			wizard.open();
			wizard.selectProject(project);
			wizard.finish();

			// check projects
			ProblemsView problemsView = new ProblemsView();
			problemsView.open();
			if (problemsView.getProblems(ProblemType.ERROR).size() > 0) {
				result.append(Arrays.toString(project));
			}

			// delete projects
			new ProjectExplorer().deleteAllProjects();
		}

		if (result.length() > 0) {
			fail("The following projects have some problems: " + result.toString());
		}
	}
}
