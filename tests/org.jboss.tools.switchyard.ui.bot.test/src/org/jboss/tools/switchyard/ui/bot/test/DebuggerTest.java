package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.jboss.reddeer.eclipse.ui.perspectives.DebugPerspective;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.component.Service;
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
import org.jboss.tools.switchyard.reddeer.project.ProjectItemExt.Configuration;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
public class DebuggerTest {

	private static final String RESOURCES = Activator.getDefault().getResourcesPath();

	@BeforeClass
	public static void maximize() {
		new WorkbenchShell().maximize();
	}

	@BeforeClass
	public static void importTestProject() {
		new ExternalProjectImportWizardDialog().open();
		WizardProjectsImportPage importPage = new WizardProjectsImportPage(null, 0);
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

	// TODO This test causes different unexpected problems such as Breakpoint
	// Error, disabling resume button after immediate after suspend or causes
	// that all variable names are blank
	@Ignore
	@Test
	public void transformDebugTest() {
		// add transform breakpoint
		new SwitchYardProject("hello").openSwitchYardFile();
		new Component("hello").contextButton("Breakpoints", "Enable  Transform Breakpoint").click();

		// check the breakpoint
		List<Breakpoint> breakpoints = new BreakpointsView().getBreakpoints();
		assertEquals("No breakpoint found", 1, breakpoints.size());
		Breakpoint breakpoint = breakpoints.get(0);
		assertEquals("Transform Breakpoint: hello", breakpoint.getText());
		breakpoint.checkTransformer("String {java.lang}", "Person {com.example.switchyard.hello}");

		// run as junit test
		new SwitchYardProject("hello").getTestClass("com.example.switchyard.hello", "HelloTest.java").debugAs(
				Configuration.JUNIT_TEST);
		new WaitUntil(new IsSuspended(), TimePeriod.LONG);
		assertEquals("TransformSequence.apply(Message, TransformerRegistry) line: 103",
				new DebugView().getSelectedText());
		new ResumeButton().click();
		new WaitUntil(new IsSuspended(), TimePeriod.LONG);
		assertEquals("TransformSequence.apply(Message, TransformerRegistry) line: 103",
				new DebugView().getSelectedText());
		new ResumeButton().click();
		new WaitUntil(new IsSuspended(), TimePeriod.LONG);
		assertEquals("TransformSequence.apply(Message, TransformerRegistry) line: 103",
				new DebugView().getSelectedText());
		new ResumeButton().click();
		new WaitUntil(new IsTerminated(), TimePeriod.LONG);
	}

	// TODO Add test which without any validator checked

	@Test
	public void validateDebugTest() {
		// add transform breakpoint
		new SwitchYardProject("hello").openSwitchYardFile();
		new Component("hello").contextButton("Breakpoints", "Enable  Validate Breakpoint").click();

		// check the breakpoint
		List<Breakpoint> breakpoints = new BreakpointsView().getBreakpoints();
		assertEquals("No breakpoint found", 1, breakpoints.size());
		assertEquals("Validate Breakpoint: hello", breakpoints.get(0).getText());

		// run as junit test
		new SwitchYardProject("hello").getTestClass("com.example.switchyard.hello", "HelloTest.java").debugAs(
				Configuration.JUNIT_TEST);
		new WaitUntil(new IsSuspended(), TimePeriod.LONG);
		assertEquals("ValidateHandler.handleMessage(Exchange) line: 66", new DebugView().getSelectedText());
		new ResumeButton().click();
		new WaitUntil(new IsSuspended(), TimePeriod.LONG);
		assertEquals("ValidateHandler.handleMessage(Exchange) line: 66", new DebugView().getSelectedText());
		new ResumeButton().click();
		new WaitUntil(new IsSuspended(), TimePeriod.LONG);
		assertEquals("ValidateHandler.handleMessage(Exchange) line: 66", new DebugView().getSelectedText());
		new ResumeButton().click();
		new WaitUntil(new IsSuspended(), TimePeriod.LONG);
		assertEquals("ValidateHandler.handleMessage(Exchange) line: 66", new DebugView().getSelectedText());
		new ResumeButton().click();
		new WaitUntil(new IsTerminated(), TimePeriod.LONG);
	}

	@Test
	public void consumerDebugTest() {
		new SwitchYardProject("hello").openSwitchYardFile();
		new Service("HelloService").contextButton("Enable  Breakpoint").click();

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

		new SwitchYardProject("hello").getTestClass("com.example.switchyard.hello", "HelloTest.java").debugAs(
				Configuration.JUNIT_TEST);
		new WaitUntil(new IsSuspended(), TimePeriod.LONG);
		assertEquals("InterceptProcessor.process(Exchange) line: 61", new DebugView().getSelectedText());
		System.out.println(new VariablesView().getValue("ex"));
		assertEquals("Exchange[Message: Johnny Cash]", new VariablesView().getValue("ex"));
		new ResumeButton().click();
		new WaitUntil(new IsSuspended(), TimePeriod.LONG);
		assertEquals("ConsumerCallbackProcessor.process(Exchange) line: 30", new DebugView().getSelectedText());
		assertEquals("Exchange[Message: Hello Johnny Cash]", new VariablesView().getValue("ex"));
		new ResumeButton().click();
		new WaitUntil(new IsTerminated(), TimePeriod.LONG);
	}

	@Test
	public void consumerInDebugTest() {
		new SwitchYardProject("hello").openSwitchYardFile();
		new Service("HelloService").contextButton("Enable  Breakpoint").click();

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

		new SwitchYardProject("hello").getTestClass("com.example.switchyard.hello", "HelloTest.java").debugAs(
				Configuration.JUNIT_TEST);
		new WaitUntil(new IsSuspended(), TimePeriod.LONG);
		assertEquals("InterceptProcessor.process(Exchange) line: 61", new DebugView().getSelectedText());
		assertEquals("Exchange[Message: Johnny Cash]", new VariablesView().getValue("ex"));
		new ResumeButton().click();
		new WaitUntil(new IsTerminated(), TimePeriod.LONG);
	}

	@Test
	public void consumerOutDebugTest() {
		new SwitchYardProject("hello").openSwitchYardFile();
		new Service("HelloService").contextButton("Enable  Breakpoint").click();

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

		new SwitchYardProject("hello").getTestClass("com.example.switchyard.hello", "HelloTest.java").debugAs(
				Configuration.JUNIT_TEST);
		new WaitUntil(new IsSuspended(), TimePeriod.LONG);
		assertEquals("ConsumerCallbackProcessor.process(Exchange) line: 30", new DebugView().getSelectedText());
		assertEquals("Exchange[Message: Hello Johnny Cash]", new VariablesView().getValue("ex"));
		new ResumeButton().click();
		new WaitUntil(new IsTerminated(), TimePeriod.LONG);
	}

	@Test
	public void providerDebugTest() {
		new SwitchYardProject("hello").openSwitchYardFile();
		new Service("Hello").contextButton("Enable  Breakpoint").click();

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

		new SwitchYardProject("hello").getTestClass("com.example.switchyard.hello", "HelloTest.java").debugAs(
				Configuration.JUNIT_TEST);
		new WaitUntil(new IsSuspended(), TimePeriod.LONG);
		new TerminateButton().click();
	}

	private static void assertBreakpoint(Breakpoint breakpoint, TriggeringPhase triggeringPhase, boolean checked) {
		if (checked) {
			assertTrue(
					"Breakpoint '" + breakpoint.getText() + "' has unchecked triggering phase '"
							+ triggeringPhase.getLabel() + "'", breakpoint.isChecked(triggeringPhase));
		} else {
			assertFalse(
					"Breakpoint '" + breakpoint.getText() + "' has checked triggering phase '"
							+ triggeringPhase.getLabel() + "'", breakpoint.isChecked(triggeringPhase));
		}
	}

	private static void assertBreakpoint(Breakpoint breakpoint, TriggerOn triggerOn, boolean checked) {
		if (checked) {
			assertTrue(
					"Breakpoint '" + breakpoint.getText() + "' has unchecked trigger '" + triggerOn.getLabel() + "'",
					breakpoint.isChecked(triggerOn));
		} else {
			assertFalse("Breakpoint '" + breakpoint.getText() + "' has checked trigger '" + triggerOn.getLabel() + "'",
					breakpoint.isChecked(triggerOn));
		}
	}
}
