package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.ui.perspectives.DebugPerspective;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.debug.Breakpoint;
import org.jboss.tools.switchyard.reddeer.debug.Breakpoint.TriggerOn;
import org.jboss.tools.switchyard.reddeer.debug.Breakpoint.TriggeringPhase;
import org.jboss.tools.switchyard.reddeer.debug.BreakpointsView;
import org.jboss.tools.switchyard.reddeer.debug.TerminateButton;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for SwitchYard Breakpoints
 * 
 * @author apodhrad
 * 
 */
@SwitchYard
@OpenPerspective(DebugPerspective.class)
@RunWith(RedDeerSuite.class)
public class BreakpointTest {

	// TODO Add test for checking whether checkboxes (triggering phases) are
	// enabled/disabled

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
		if (!breakpointsView.getBreakpoints().isEmpty()) {
			breakpointsView.removeAllBreakpoints();
		}
		TerminateButton terminateButton = new TerminateButton();
		if (terminateButton.isEnabled()) {
			terminateButton.click();
		}
	}

	@Test
	public void transformBreakpointTest() {
		// add transform breakpoint
		new SwitchYardProject("hello").openSwitchYardFile();
		new SwitchYardComponent("hello").getContextButton("Breakpoints", "Enable  Transform Breakpoint").click();

		// check the breakpoint in Breakpoints view
		List<Breakpoint> breakpoints = new BreakpointsView().getBreakpoints();
		assertEquals("No breakpoint found", 1, breakpoints.size());
		Breakpoint breakpoint = breakpoints.get(0);
		assertEquals("Transform Breakpoint: hello", breakpoint.getText());
		assertTrue(breakpoint.isChecked());
		assertBreakpoint(breakpoint, TriggeringPhase.IN, true);
		assertBreakpoint(breakpoint, TriggeringPhase.OUT, true);
		assertBreakpoint(breakpoint, TriggeringPhase.FAULT, true);
		List<TableItem> transformers = breakpoint.getTransformers();
		assertEquals("No transformer found", 1, transformers.size());
		assertEquals("String {java.lang}", transformers.get(0).getText(0));
		assertEquals("Person {com.example.switchyard.hello}", transformers.get(0).getText(1));
		breakpoint.checkTransformer("String {java.lang}", "Person {com.example.switchyard.hello}");

		// check the breakpoint in Properties
		new SwitchYardComponent("hello").getContextButton("Properties").click();
		new DefaultShell("Properties for ").setFocus();
		new DefaultTreeItem("Transform Breakpoint Properties").select();
		assertEquals("hello", new LabeledText("Project").getText());
		assertTrue(new CheckBox("Enabled").isChecked());
		assertTrue(new CheckBox("IN").isChecked());
		assertTrue(new CheckBox("OUT").isChecked());
		assertTrue(new CheckBox("FAULT").isChecked());
		assertTrue(new DefaultTable().getItem(0).isChecked());
		assertEquals("String {java.lang}", new DefaultTable().getItem(0).getText(0));
		assertEquals("Person {com.example.switchyard.hello}", new DefaultTable().getItem(0).getText(1));
		new CheckBox("IN").toggle(false);
		new CheckBox("OUT").toggle(false);
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for "));

		// check the breakpoint in Breakpoints view
		breakpoints = new BreakpointsView().getBreakpoints();
		assertEquals("No breakpoint found", 1, breakpoints.size());
		breakpoint = breakpoints.get(0);
		assertEquals("Transform Breakpoint: hello", breakpoint.getText());
		assertTrue(breakpoint.isChecked());
		assertBreakpoint(breakpoint, TriggeringPhase.IN, false);
		assertBreakpoint(breakpoint, TriggeringPhase.OUT, false);
		assertBreakpoint(breakpoint, TriggeringPhase.FAULT, true);
		breakpoint.check(TriggeringPhase.IN);
		breakpoint.setChecked(false);

		// check the breakpoint in Properties
		new SwitchYardComponent("hello").getContextButton("Properties").click();
		new DefaultShell("Properties for ").setFocus();
		new DefaultTreeItem("Transform Breakpoint Properties").select();
		assertEquals("hello", new LabeledText("Project").getText());
		assertFalse(new CheckBox("Enabled").isChecked());
		assertTrue(new CheckBox("IN").isChecked());
		assertFalse(new CheckBox("OUT").isChecked());
		assertTrue(new CheckBox("FAULT").isChecked());
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for "));

		// delete breakpoint
		new SwitchYardComponent("hello").getContextButton("Breakpoints", "Disable Transform Breakpoint").click();
		assertTrue(new BreakpointsView().getBreakpoints().isEmpty());
	}

	@Test
	public void validateBreakpointTest() {
		// add validate breakpoint
		new SwitchYardProject("hello").openSwitchYardFile();
		new SwitchYardComponent("hello").getContextButton("Breakpoints", "Enable  Validate Breakpoint").click();

		// check the breakpoint in Breakpoints view
		List<Breakpoint> breakpoints = new BreakpointsView().getBreakpoints();
		assertEquals("No breakpoint found", 1, breakpoints.size());
		Breakpoint breakpoint = breakpoints.get(0);
		assertEquals("Validate Breakpoint: hello", breakpoint.getText());
		assertTrue(breakpoint.isChecked());
		assertBreakpoint(breakpoint, TriggeringPhase.IN, true);
		assertBreakpoint(breakpoint, TriggeringPhase.OUT, true);
		assertBreakpoint(breakpoint, TriggeringPhase.FAULT, true);

		// check the breakpoint in Properties
		new SwitchYardComponent("hello").getContextButton("Properties").click();
		new DefaultShell("Properties for ").setFocus();
		new DefaultTreeItem("Validate Breakpoint Properties").select();
		assertEquals("hello", new LabeledText("Project").getText());
		assertTrue(new CheckBox("Enabled").isChecked());
		assertTrue(new CheckBox("IN").isChecked());
		assertTrue(new CheckBox("OUT").isChecked());
		assertTrue(new CheckBox("FAULT").isChecked());
		new CheckBox("IN").toggle(false);
		new CheckBox("OUT").toggle(false);
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for "));

		// check the breakpoint in Breakpoints view
		breakpoints = new BreakpointsView().getBreakpoints();
		assertEquals("No breakpoint found", 1, breakpoints.size());
		breakpoint = breakpoints.get(0);
		assertEquals("Validate Breakpoint: hello", breakpoint.getText());
		assertTrue(breakpoint.isChecked());
		assertBreakpoint(breakpoint, TriggeringPhase.IN, false);
		assertBreakpoint(breakpoint, TriggeringPhase.OUT, false);
		assertBreakpoint(breakpoint, TriggeringPhase.FAULT, true);
		breakpoint.check(TriggeringPhase.IN);
		breakpoint.setChecked(false);

		// check the breakpoint in Properties
		new SwitchYardComponent("hello").getContextButton("Properties").click();
		new DefaultShell("Properties for ").setFocus();
		new DefaultTreeItem("Validate Breakpoint Properties").select();
		assertEquals("hello", new LabeledText("Project").getText());
		assertFalse(new CheckBox("Enabled").isChecked());
		assertTrue(new CheckBox("IN").isChecked());
		assertFalse(new CheckBox("OUT").isChecked());
		assertTrue(new CheckBox("FAULT").isChecked());
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for "));

		// delete breakpoint
		new SwitchYardComponent("hello").getContextButton("Breakpoints", "Disable Validate Breakpoint").click();
		assertTrue(new BreakpointsView().getBreakpoints().isEmpty());
	}

	@Test
	public void consumerBreakpointTest() {
		// add validate breakpoint
		new SwitchYardProject("hello").openSwitchYardFile();
		new SwitchYardComponent("HelloService").getContextButton("Enable  Breakpoint").click();

		// check the breakpoint in Breakpoints view
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

		// check the breakpoint in Properties
		new SwitchYardComponent("HelloService").getContextButton("Properties").click();
		new DefaultShell("Properties for HelloService").setFocus();
		new DefaultTreeItem("Service Breakpoint Properties").select();
		assertEquals("HelloService", new LabeledText("Service Name").getText());
		assertEquals("urn:com.example.switchyard:hello:1.0", new LabeledText("Service Namespace").getText());
		assertEquals("hello", new LabeledText("Project").getText());
		assertTrue(new CheckBox("Enabled").isChecked());
		assertTrue(new CheckBox("IN").isChecked());
		assertTrue(new CheckBox("OUT").isChecked());
		assertTrue(new CheckBox("FAULT").isChecked());
		new CheckBox("IN").toggle(false);
		new CheckBox("OUT").toggle(false);
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for HelloService"));

		// check the breakpoint in Breakpoints view
		breakpoints = new BreakpointsView().getBreakpoints();
		assertEquals("No breakpoint found", 1, breakpoints.size());
		// wait for the change
		new WaitUntil(new WaitCondition() {

			@Override
			public boolean test() {
				Breakpoint b = new BreakpointsView().getBreakpoints().get(0);
				return b.getText().equals("SwitchYard CONSUMER: HelloService on [FAULT]");
			}

			@Override
			public String description() {
				return "";
			}
		});
		breakpoint = breakpoints.get(0);
		assertEquals("SwitchYard CONSUMER: HelloService on [FAULT]", breakpoint.getText());
		assertTrue(breakpoint.isChecked());
		assertBreakpoint(breakpoint, TriggeringPhase.IN, false);
		assertBreakpoint(breakpoint, TriggeringPhase.OUT, false);
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
		breakpoint.check(TriggeringPhase.IN);
		breakpoint.setChecked(false);

		// check the breakpoint in Properties
		new SwitchYardComponent("HelloService").getContextButton("Properties").click();
		new DefaultShell("Properties for HelloService").setFocus();
		new DefaultTreeItem("Service Breakpoint Properties").select();
		assertEquals("hello", new LabeledText("Project").getText());
		assertFalse(new CheckBox("Enabled").isChecked());
		assertTrue(new CheckBox("IN").isChecked());
		assertFalse(new CheckBox("OUT").isChecked());
		assertTrue(new CheckBox("FAULT").isChecked());
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for HelloService"));

		// delete breakpoint
		new BreakpointsView().removeAllBreakpoints();
		new SwitchYardComponent("HelloService").getContextButton("Properties").click();
		new DefaultShell("Properties for HelloService").setFocus();
		try {
			new DefaultTreeItem("Service Breakpoint Properties").select();
			fail("Item 'Service Breakpoint Properties' is still available");
		} catch (SWTLayerException|CoreLayerException ex) {
			// ok
		}
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for HelloService"));
	}

	@Test
	public void providerBreakpointTest() {
		// add validate breakpoint
		new SwitchYardProject("hello").openSwitchYardFile();
		new SwitchYardComponent("Hello").getContextButton("Enable  Breakpoint").click();

		// check the breakpoint in Breakpoints view
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

		// check the breakpoint in Properties
		new SwitchYardComponent("Hello").getContextButton("Properties").click();
		new DefaultShell("Properties for Hello").setFocus();
		new DefaultTreeItem("Service Breakpoint Properties").select();
		assertEquals("Hello", new LabeledText("Service Name").getText());
		assertEquals("urn:com.example.switchyard:hello:1.0", new LabeledText("Service Namespace").getText());
		assertEquals("hello", new LabeledText("Project").getText());
		assertTrue(new CheckBox("Enabled").isChecked());
		assertTrue(new CheckBox("IN").isChecked());
		assertTrue(new CheckBox("OUT").isChecked());
		assertTrue(new CheckBox("FAULT").isChecked());
		new CheckBox("IN").toggle(false);
		new CheckBox("OUT").toggle(false);
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for Hello"));

		// check the breakpoint in Breakpoints view
		breakpoints = new BreakpointsView().getBreakpoints();
		assertEquals("No breakpoint found", 1, breakpoints.size());
		breakpoint = breakpoints.get(0);
		// wait for the change
		new WaitUntil(new WaitCondition() {

			@Override
			public boolean test() {
				Breakpoint b = new BreakpointsView().getBreakpoints().get(0);
				return b.getText().equals("SwitchYard PROVIDER: Hello on [FAULT]");
			}

			@Override
			public String description() {
				return "";
			}
		});
		assertEquals("SwitchYard PROVIDER: Hello on [FAULT]", breakpoint.getText());
		assertTrue(breakpoint.isChecked());
		assertBreakpoint(breakpoint, TriggeringPhase.IN, false);
		assertBreakpoint(breakpoint, TriggeringPhase.OUT, false);
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
		breakpoint.check(TriggeringPhase.IN);
		breakpoint.setChecked(false);

		// check the breakpoint in Properties
		new SwitchYardComponent("Hello").getContextButton("Properties").click();
		new DefaultShell("Properties for Hello").setFocus();
		new DefaultTreeItem("Service Breakpoint Properties").select();
		assertEquals("hello", new LabeledText("Project").getText());
		assertFalse(new CheckBox("Enabled").isChecked());
		assertTrue(new CheckBox("IN").isChecked());
		assertFalse(new CheckBox("OUT").isChecked());
		assertTrue(new CheckBox("FAULT").isChecked());
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for Hello"));

		// delete breakpoint
		new BreakpointsView().removeAllBreakpoints();
		new SwitchYardComponent("Hello").getContextButton("Properties").click();
		new DefaultShell("Properties for Hello").setFocus();
		try {
			new DefaultTreeItem("Service Breakpoint Properties").select();
			fail("Item 'Service Breakpoint Properties' is still available");
		} catch (SWTLayerException|CoreLayerException ex) {
			// ok
		}
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for Hello"));
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
