package org.jboss.tools.bpmn2.ui.bot.test.testcase.dialog;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.ui.markers.matcher.MarkerDescriptionMatcher;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.dialog.JBPMProjectWizard;
import org.jboss.tools.bpmn2.reddeer.dialog.JBPMProjectWizard.ProjectType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeImplementationRestriction;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeImplementationType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Verify functionality of the project wizard.
 */
@Runtime
@RunWith(RedDeerSuite.class)
public class JBPMProjectWizardTest {
	
	@InjectRequirement
	protected RuntimeRequirement jbpmRuntime;
	
	@RequirementRestriction
	public static RequirementMatcher getRequirementMatcher() {
		return new RuntimeImplementationRestriction(RuntimeImplementationType.JBPM);
	}

	private static ProjectExplorer explorerView;
	private static JBPMProjectWizard wizardView;
	private static ProblemsView problemsView;
	
	@BeforeClass
	public static void init() {
		explorerView = new ProjectExplorer();
		wizardView = new JBPMProjectWizard();
		problemsView = new ProblemsView();
	}

	@After
	public void deleteAllProjects() {
		try {
			problemsView.open();
			problemsView.getProblems(ProblemType.ERROR, new MarkerDescriptionMatcher("")).isEmpty();	
		} finally {
		
			for (Project p : explorerView.getProjects()) {
				p.delete(true);
			}
		}
	}
	
	@Test()
	public void emptyProjectTest() throws Exception {
		boolean withRuntime = false;
		wizardView.execute("TestProject", ProjectType.EMPTY, withRuntime);

		Project p = explorerView.getProject("TestProject");
		// the node list will contain one empty node!
		assertTrue(p.containsResource("src/main/resources", "com.sample"));
		assertTrue(p.containsResource("src/main/resources", "META-INF", "kmodule.xml"));
	}
	
	@Test
	public void emptyProjectWithRuntimeTest() throws Exception {
		boolean withRuntime = true;
		wizardView.execute("TestProjectWithRuntime", ProjectType.EMPTY, withRuntime);

		Project p = explorerView.getProject("TestProjectWithRuntime");
		// the node list will contain one empty node!
		assertTrue(p.containsResource("src/main/resources", "com.sample"));
		assertTrue(p.containsResource("src/main/resources", "META-INF", "kmodule.xml"));
	}

	@Test
	public void projectWithHelloWorldTest() throws Exception {
		wizardView.execute("TestProject", false);

		Project p = explorerView.getProject("TestProject");
		assertFalse(p.containsResource("src/main/java", "com.sample", "ProcessTest.java"));
		assertTrue(p.containsResource("src/main/resources", "META-INF", "kmodule.xml"));
	}

	@Test
	public void projectWithTestedHelloWorldTest() {
		wizardView.execute("TestProject", true);

		Project p = explorerView.getProject("TestProject");
		assertTrue(p.containsResource("src/main/resources", "com.sample", "sample.bpmn"));
		assertTrue(p.containsResource("src/main/java", "com.sample", "ProcessTest.java"));
		assertTrue(p.containsResource("src/main/resources", "META-INF", "kmodule.xml"));
	}
	
	/*
	@Test
	public void projectWithExampleFromInternetTest() {
		wizardView.executeForHumanResourcesExample();

		assertTrue(explorerView.getProject("human-resources").containsResource("src/main/resources"));
		assertTrue(explorerView.getProject("human-resources-tests").containsResource("src/main/resources"));
	}
	*/

	@Test()
	public void newProjectFormValidationTest() throws Exception {
		
			wizardView.open();
			new PushButton(ProjectType.EMPTY.getButtonIndex()).click();
			wizardView.next();	
			new LabeledText("Project name:").setText("");
			new RadioButton("Maven").click();
			new LabeledText("Artifact ID:").setText("");
		try{
			wizardView.finish();
			Assert.fail("Project with an empty name was created!");
		} catch (WaitTimeoutExpiredException ex) {
		
		} finally {
			wizardView.cancel();
		}
		
		assertTrue(explorerView.getProjects().isEmpty());
	}

}
