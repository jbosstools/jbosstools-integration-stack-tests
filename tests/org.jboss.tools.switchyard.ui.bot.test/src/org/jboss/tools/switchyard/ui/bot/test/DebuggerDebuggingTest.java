package org.jboss.tools.switchyard.ui.bot.test;

import static org.jboss.tools.switchyard.reddeer.project.ProjectItemExt.Configuration.JUNIT_TEST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.eclipse.ui.perspectives.DebugPerspective;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.condition.IsSuspended;
import org.jboss.tools.switchyard.reddeer.condition.IsTerminated;
import org.jboss.tools.switchyard.reddeer.debug.Breakpoint;
import org.jboss.tools.switchyard.reddeer.debug.Breakpoint.TriggerOn;
import org.jboss.tools.switchyard.reddeer.debug.Breakpoint.TriggeringPhase;
import org.jboss.tools.switchyard.reddeer.debug.BreakpointsView;
import org.jboss.tools.switchyard.reddeer.debug.DebugView;
import org.jboss.tools.switchyard.reddeer.debug.ResumeButton;
import org.jboss.tools.switchyard.reddeer.debug.TerminateButton;
import org.jboss.tools.switchyard.reddeer.debug.VariablesView;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for SwitchYard Debugger
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@OpenPerspective(DebugPerspective.class)
@RunWith(RedDeerSuite.class)
public class DebuggerDebuggingTest {

	public static final String PACKAGE = "com.example.switchyard.hello";
	public static final String HELLO_TEST = "HelloTest.java";
	public static final String HELLO_SERVICE_TEST = "HelloServiceTest.java";

	private static final String RESOURCES = Activator.getDefault().getResourcesPath();

	@BeforeClass
	public static void maximize() {
		new WorkbenchShell().maximize();
	}

	@BeforeClass
	public static void importTestProject() {
		new ExternalProjectImportWizardDialog().open();
		WizardProjectsImportPage importPage = new WizardProjectsImportPage();
		importPage.setRootDirectory(new File(RESOURCES, "projects/hello").getAbsolutePath());
		importPage.copyProjectsIntoWorkspace(true);
		// This may take several minutes
		new PushButton("Finish").click();
		TimePeriod timeout = TimePeriod.getCustom(20 * 60 * 1000);
		new WaitWhile(new ShellWithTextIsActive("Import"), timeout);
		new WaitWhile(new JobIsRunning(), timeout);
		// Finally update the project
		new SwitchYardProject("hello").update();
	}

	@Before
	@After
	public void removeAllBreakpoints() {
		BreakpointsView breakpointsView = new BreakpointsView();
		if (!breakpointsView.isEmpty()) {
			breakpointsView.removeAllBreakpoints();
		}
		TerminateButton terminateButton = new TerminateButton();
		if (terminateButton.isEnabled()) {
			terminateButton.click();
		}
	}

	// TODO: add test for SwitchYard Context view
	// TODO: add tests for each TriggerOn

	// TODO Add test which without any transformer checked

	@Test
	public void transformDebugTest() {
		// add transform breakpoint
		new SwitchYardProject("hello").openSwitchYardFile();
		new SwitchYardComponent("hello").getContextButton("Breakpoints", "Enable  Transform Breakpoint").click();

		// check the breakpoint
		List<Breakpoint> breakpoints = new BreakpointsView().getBreakpoints();
		assertEquals("No breakpoint found", 1, breakpoints.size());
		Breakpoint breakpoint = breakpoints.get(0);
		assertEquals("Transform Breakpoint: hello", breakpoint.getText());
		breakpoint.uncheckTransformer("String {java.lang}", "Person {com.example.switchyard.hello}");

		// run as junit test
		IsSuspended isSuspended = new IsSuspended();
		new SwitchYardProject("hello").getTestClass(PACKAGE, HELLO_SERVICE_TEST).debugAs(JUNIT_TEST);
		new WaitUntil(isSuspended, TimePeriod.LONG);
		assertDebugView("TransformSequence.apply(Message, TransformerRegistry)");
		new ResumeButton().click();
		new WaitUntil(isSuspended, TimePeriod.LONG);
		assertDebugView("TransformSequence.apply(Message, TransformerRegistry)");
		new ResumeButton().click();
		new WaitUntil(isSuspended, TimePeriod.LONG);
		assertDebugView("TransformSequence.apply(Message, TransformerRegistry)");
		new ResumeButton().click();
		new WaitUntil(new IsTerminated(), TimePeriod.LONG);
	}

	// TODO Add test which without any validator checked

	@Test
	public void validateDebugTest() {
		// add transform breakpoint
		new SwitchYardProject("hello").openSwitchYardFile();
		new SwitchYardComponent("hello").getContextButton("Breakpoints", "Enable  Validate Breakpoint").click();

		// check the breakpoint
		List<Breakpoint> breakpoints = new BreakpointsView().getBreakpoints();
		assertEquals("No breakpoint found", 1, breakpoints.size());
		assertEquals("Validate Breakpoint: hello", breakpoints.get(0).getText());

		// run as junit test
		IsSuspended isSuspended = new IsSuspended();
		new SwitchYardProject("hello").getTestClass(PACKAGE, HELLO_SERVICE_TEST).debugAs(JUNIT_TEST);
		new WaitUntil(isSuspended, TimePeriod.LONG);
		assertDebugView("ValidateHandler.handleMessage(Exchange)");
		new ResumeButton().click();
		new WaitUntil(isSuspended, TimePeriod.LONG);
		assertDebugView("ValidateHandler.handleMessage(Exchange)");
		new ResumeButton().click();
		new WaitUntil(isSuspended, TimePeriod.LONG);
		assertDebugView("ValidateHandler.handleMessage(Exchange)");
		new ResumeButton().click();
		new WaitUntil(isSuspended, TimePeriod.LONG);
		assertDebugView("ValidateHandler.handleMessage(Exchange)");
		new ResumeButton().click();
		new WaitUntil(new IsTerminated(), TimePeriod.LONG);
	}

	@Test
	public void consumerDebugTest() {
		new SwitchYardProject("hello").openSwitchYardFile();
		new Service("HelloService").getContextButton("Enable  Breakpoint").click();

		List<Breakpoint> breakpoints = new BreakpointsView().getBreakpoints();
		assertEquals("No breakpoint found", 1, breakpoints.size());
		Breakpoint breakpoint = breakpoints.get(0);

		assertEquals("SwitchYard CONSUMER: HelloService on [IN, OUT, FAULT]", breakpoint.getText());
		assertTrue(breakpoint.isChecked());
		assertBreakpoint(breakpoint, TriggeringPhase.IN, true);
		assertBreakpoint(breakpoint, TriggeringPhase.OUT, true);
		assertBreakpoint(breakpoint, TriggeringPhase.FAULT, true);
		assertBreakpoint(breakpoint, TriggerOn.ENTRY, true);
		assertBreakpoint(breakpoint, TriggerOn.RETURN, true);
		assertBreakpoint(breakpoint, TriggerOn.FAULT, true);
		assertBreakpoint(breakpoint, TriggerOn.TRANSACTION, false);
		assertBreakpoint(breakpoint, TriggerOn.SECURITY, false);
		assertBreakpoint(breakpoint, TriggerOn.POLICY, false);
		assertBreakpoint(breakpoint, TriggerOn.TARGET_INVOCATION, false);
		assertBreakpoint(breakpoint, TriggerOn.VALIDATION, false);
		assertBreakpoint(breakpoint, TriggerOn.TRANSFORMATION, false);

		IsSuspended isSuspended = new IsSuspended();
		new SwitchYardProject("hello").getTestClass(PACKAGE, HELLO_SERVICE_TEST).debugAs(JUNIT_TEST);
		new WaitUntil(isSuspended, TimePeriod.LONG);
		assertDebugView("InterceptProcessor.process(Exchange)");
		System.out.println(new VariablesView().getValue("ex"));
		assertEquals("Exchange[Message: Johnny Cash]", new VariablesView().getValue("ex"));
		new ResumeButton().click();
		new WaitUntil(isSuspended, TimePeriod.LONG);
		assertDebugView("ConsumerCallbackProcessor.process(Exchange)");
		assertEquals("Exchange[Message: Hello Johnny Cash]", new VariablesView().getValue("ex"));
		new ResumeButton().click();
		new WaitUntil(new IsTerminated(), TimePeriod.LONG);
	}

	@Test
	public void consumerInDebugTest() {
		new SwitchYardProject("hello").openSwitchYardFile();
		new Service("HelloService").getContextButton("Enable  Breakpoint").click();

		List<Breakpoint> breakpoints = new BreakpointsView().getBreakpoints();
		assertEquals("No breakpoint found", 1, breakpoints.size());
		Breakpoint breakpoint = breakpoints.get(0);

		assertEquals("SwitchYard CONSUMER: HelloService on [IN, OUT, FAULT]", breakpoint.getText());
		assertTrue(breakpoint.isChecked());
		assertBreakpoint(breakpoint, TriggeringPhase.IN, true);
		assertBreakpoint(breakpoint, TriggeringPhase.OUT, true);
		assertBreakpoint(breakpoint, TriggeringPhase.FAULT, true);
		assertBreakpoint(breakpoint, TriggerOn.ENTRY, true);
		assertBreakpoint(breakpoint, TriggerOn.RETURN, true);
		assertBreakpoint(breakpoint, TriggerOn.FAULT, true);
		assertBreakpoint(breakpoint, TriggerOn.TRANSACTION, false);
		assertBreakpoint(breakpoint, TriggerOn.SECURITY, false);
		assertBreakpoint(breakpoint, TriggerOn.POLICY, false);
		assertBreakpoint(breakpoint, TriggerOn.TARGET_INVOCATION, false);
		assertBreakpoint(breakpoint, TriggerOn.VALIDATION, false);
		assertBreakpoint(breakpoint, TriggerOn.TRANSFORMATION, false);
		// trigger only on IN phase
		breakpoint.uncheck(TriggeringPhase.OUT);
		// wait for changing the breakpoint description
		AbstractWait.sleep(TimePeriod.SHORT);
		assertEquals("SwitchYard CONSUMER: HelloService on [IN, FAULT]", breakpoint.getText());

		IsSuspended isSuspended = new IsSuspended();
		new SwitchYardProject("hello").getTestClass(PACKAGE, HELLO_SERVICE_TEST).debugAs(JUNIT_TEST);
		new WaitUntil(isSuspended, TimePeriod.LONG);
		assertDebugView("InterceptProcessor.process(Exchange)");
		assertEquals("Exchange[Message: Johnny Cash]", new VariablesView().getValue("ex"));
		new ResumeButton().click();
		new WaitUntil(new IsTerminated(), TimePeriod.LONG);
	}

	@Test
	public void consumerOutDebugTest() {
		new SwitchYardProject("hello").openSwitchYardFile();
		new Service("HelloService").getContextButton("Enable  Breakpoint").click();

		List<Breakpoint> breakpoints = new BreakpointsView().getBreakpoints();
		assertEquals("No breakpoint found", 1, breakpoints.size());
		Breakpoint breakpoint = breakpoints.get(0);

		assertEquals("SwitchYard CONSUMER: HelloService on [IN, OUT, FAULT]", breakpoint.getText());
		assertTrue(breakpoint.isChecked());
		assertBreakpoint(breakpoint, TriggeringPhase.IN, true);
		assertBreakpoint(breakpoint, TriggeringPhase.OUT, true);
		assertBreakpoint(breakpoint, TriggeringPhase.FAULT, true);
		assertBreakpoint(breakpoint, TriggerOn.ENTRY, true);
		assertBreakpoint(breakpoint, TriggerOn.RETURN, true);
		assertBreakpoint(breakpoint, TriggerOn.FAULT, true);
		assertBreakpoint(breakpoint, TriggerOn.TRANSACTION, false);
		assertBreakpoint(breakpoint, TriggerOn.SECURITY, false);
		assertBreakpoint(breakpoint, TriggerOn.POLICY, false);
		assertBreakpoint(breakpoint, TriggerOn.TARGET_INVOCATION, false);
		assertBreakpoint(breakpoint, TriggerOn.VALIDATION, false);
		assertBreakpoint(breakpoint, TriggerOn.TRANSFORMATION, false);
		// trigger only on OUT phase
		breakpoint.uncheck(TriggeringPhase.IN);
		// wait for changing the breakpoint description
		AbstractWait.sleep(TimePeriod.SHORT);
		assertEquals("SwitchYard CONSUMER: HelloService on [OUT, FAULT]", breakpoint.getText());

		IsSuspended isSuspended = new IsSuspended();
		new SwitchYardProject("hello").getTestClass(PACKAGE, HELLO_SERVICE_TEST).debugAs(JUNIT_TEST);
		new WaitUntil(isSuspended, TimePeriod.LONG);
		assertDebugView("ConsumerCallbackProcessor.process(Exchange)");
		assertEquals("Exchange[Message: Hello Johnny Cash]", new VariablesView().getValue("ex"));
		new ResumeButton().click();
		new WaitUntil(new IsTerminated(), TimePeriod.LONG);
	}

	@Test
	public void providerDebugTest() {
		new SwitchYardProject("hello").openSwitchYardFile();
		new Service("Hello").getContextButton("Enable  Breakpoint").click();

		List<Breakpoint> breakpoints = new BreakpointsView().getBreakpoints();
		assertEquals("No breakpoint found", 1, breakpoints.size());
		Breakpoint breakpoint = breakpoints.get(0);

		assertEquals("SwitchYard PROVIDER: Hello on [IN, OUT, FAULT]", breakpoint.getText());
		assertTrue(breakpoint.isChecked());
		assertBreakpoint(breakpoint, TriggeringPhase.IN, true);
		assertBreakpoint(breakpoint, TriggeringPhase.OUT, true);
		assertBreakpoint(breakpoint, TriggeringPhase.FAULT, true);
		assertBreakpoint(breakpoint, TriggerOn.ENTRY, true);
		assertBreakpoint(breakpoint, TriggerOn.RETURN, true);
		assertBreakpoint(breakpoint, TriggerOn.FAULT, true);
		assertBreakpoint(breakpoint, TriggerOn.TRANSACTION, false);
		assertBreakpoint(breakpoint, TriggerOn.SECURITY, false);
		assertBreakpoint(breakpoint, TriggerOn.POLICY, false);
		assertBreakpoint(breakpoint, TriggerOn.TARGET_INVOCATION, true);
		assertBreakpoint(breakpoint, TriggerOn.VALIDATION, false);
		assertBreakpoint(breakpoint, TriggerOn.TRANSFORMATION, false);

		IsSuspended isSuspended = new IsSuspended();
		new SwitchYardProject("hello").getTestClass(PACKAGE, HELLO_TEST).debugAs(JUNIT_TEST);
		new WaitUntil(isSuspended, TimePeriod.LONG);
		new TerminateButton().click();
	}

	private static void assertBreakpoint(Breakpoint breakpoint, TriggeringPhase triggeringPhase, boolean checked) {
		if (checked) {
			assertTrue("Breakpoint '" + breakpoint.getText() + "' has unchecked triggering phase '"
					+ triggeringPhase.getLabel() + "'", breakpoint.isChecked(triggeringPhase));
		} else {
			assertFalse("Breakpoint '" + breakpoint.getText() + "' has checked triggering phase '"
					+ triggeringPhase.getLabel() + "'", breakpoint.isChecked(triggeringPhase));
		}
	}

	private static void assertBreakpoint(Breakpoint breakpoint, TriggerOn triggerOn, boolean checked) {
		if (checked) {
			assertTrue("Breakpoint '" + breakpoint.getText() + "' has unchecked trigger '" + triggerOn.getLabel() + "'",
					breakpoint.isChecked(triggerOn));
		} else {
			assertFalse("Breakpoint '" + breakpoint.getText() + "' has checked trigger '" + triggerOn.getLabel() + "'",
					breakpoint.isChecked(triggerOn));
		}
	}
	
	private static void assertDebugView(String expectedText) {
		String actualText = new DebugView().getSelectedText();
		if (actualText == null) {
			fail("Cannot get any text from a debug view");
		}
		actualText = actualText.split(" line:")[0];
		assertEquals(expectedText, actualText);
	}
}
