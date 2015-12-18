package org.jboss.tools.bpel.ui.bot.test;

import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.tools.bpel.reddeer.activity.Sequence;
import org.jboss.tools.bpel.reddeer.condition.NoErrorExists;
import org.jboss.tools.bpel.reddeer.editor.BpelEditor;
import org.jboss.tools.bpel.reddeer.perspective.BPELPerspective;
import org.jboss.tools.bpel.reddeer.wizard.ImportProjectWizard;
import org.jboss.tools.bpel.ui.bot.test.util.ResourceHelper;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author apodhrad
 *
 */
@CleanWorkspace
@OpenPerspective(BPELPerspective.class)
@RunWith(RedDeerSuite.class)
public class AssignActivityTest {

	@BeforeClass
	public static void maximizeEclipse() throws Exception {
		new WorkbenchShell().maximize();
	}

	@After
	public void saveAndClose() {
		new BpelEditor().saveAndClose();
	}

	@Test
	public void testAssignment() throws Exception {
		new BPELProject("AssignerProject").openBpelProcess("AssignTestProcess");

		// variables
		String[] simpleIn = new String[] { "simpleIn : simpleRequestMessage", "payload : string" };
		String[] simpleOut = new String[] { "simpleOut : simpleResponseMessage", "payload : string" };
		String[] complexOut = new String[] { "complexOut : complexResponseMessage",
				"complexResponse : complexResponse", "result : string" };
		String[] moderateOut = new String[] { "moderateOut : moderateResponseMessage",
				"moderateResponse : complexResponseType", "result : string" };

		Sequence main = new Sequence("main");
		main.addReceive("receiveSimple").pickOperation("simple").checkCreateInstance();
		main.addAssign("assignSimpleToSimple").addVarToVar(simpleIn, simpleOut);
		main.addAssign("assignSimpleToComplex").addVarToVar(simpleIn, complexOut);
		main.addAssign("assignSimpleToModerate").addVarToVar(simpleIn, moderateOut);
		main.addAssign("assignExpToExp").addExpToExp("$simpleIn.payload",
				"$moderateOut.moderateResponse/result");
		main.addAssign("assignFixToExp").addFixToExp("Fixed Expression", "$simpleOut.payload");
		main.addReply("replySimple").pickOperation("simple");

		new WaitUntil(new NoErrorExists(), TimePeriod.LONG);
	}

	@Test
	public void testAssignment2() {
		new BPELProject("DiscriminantProcess").openBpelProcess("Discriminant");

		// variables
		String[] discriminantA = new String[] { "DiscriminantRequest : DiscriminantRequestMessage",
				"parameters : DiscriminantRequest", "a : decimal" };
		String[] discriminantB = new String[] { "DiscriminantRequest : DiscriminantRequestMessage",
				"parameters : DiscriminantRequest", "b : decimal" };
		String[] discriminantResult = new String[] {
				"DiscriminantResponse : DiscriminantResponseMessage",
				"parameters : DiscriminantResponse", "result : int" };
		String[] mathA = new String[] { "MathRequest1 : MathRequestMessage",
				"parameters : MathRequest", "a : decimal" };
		String[] mathB = new String[] { "MathRequest1 : MathRequestMessage",
				"parameters : MathRequest", "b : decimal" };
		String[] mathOperator = new String[] { "MathRequest1 : MathRequestMessage",
				"parameters : MathRequest", "operator : string" };
		String[] mathResult = new String[] { "MathResponse1 : MathResponseMessage",
				"parameters : MathResponse", "result : decimal" };

		Sequence main = new Sequence("Main");
		main.addReceive("receive").pickOperation("calculateDiscriminant").checkCreateInstance();
		main.addAssign("assignRequest1").addVarToVar(discriminantA, mathA);
		main.addAssign("assignRequest2").addVarToVar(discriminantB, mathB);
		main.addAssign("assignRequest3").addFixToVar("*", mathOperator);
		main.addValidate("validateInput").addVariable("DiscriminantRequest");
		main.addInvoke("invokePartner").pickOperation("calculate");
		main.addAssign("assignRequest4").addVarToVar(mathResult, discriminantResult);
		main.addReply("reply").pickOperation("calculateDiscriminant");

		new WaitUntil(new NoErrorExists(), TimePeriod.LONG);
	}

	private class BPELProject {

		private String projectName;

		public BPELProject(String projectName) {
			this.projectName = projectName;
			String projectLocation = ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID,
					"resources/projects/" + projectName + ".zip");
			new ImportProjectWizard(projectLocation).execute();
		}

		public void open(String fileName) {

			ProjectExplorer projectExplorer = new ProjectExplorer();
			projectExplorer.open();
			Project project = projectExplorer.getProject(projectName);
			project.getProjectItem("bpelContent", fileName).open();
		}

		public void openBpelProcess(String bpelProcess) {
			open(bpelProcess + ".bpel");
		}
	}

}