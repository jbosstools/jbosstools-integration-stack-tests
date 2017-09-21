package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.eclipse.ui.perspectives.DebugPerspective;
import org.eclipse.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.eclipse.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.exception.SWTLayerException;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.debug.Breakpoint;
import org.jboss.tools.switchyard.reddeer.debug.Breakpoint.TriggerOn;
import org.jboss.tools.switchyard.reddeer.debug.Breakpoint.TriggeringPhase;
import org.jboss.tools.switchyard.reddeer.debug.BreakpointsView;
import org.jboss.tools.switchyard.reddeer.debug.TerminateButton;
import org.jboss.tools.switchyard.reddeer.preference.CompositePropertiesDialog;
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
public class DebuggerBreakpointTest {

	// TODO Add test for checking whether checkboxes (triggering phases) are
	// enabled/disabled

	private static final String RESOURCES = Activator.getDefault().getResourcesPath();

	@BeforeClass
	public static void maximize() {
		new WorkbenchShell().maximize();
	}

	@BeforeClass
	public static void importTestProject() {
		ExternalProjectImportWizardDialog wizard = new ExternalProjectImportWizardDialog();
		wizard.open();
		WizardProjectsImportPage importPage = new WizardProjectsImportPage(wizard);
		importPage.setRootDirectory(new File(RESOURCES, "projects/hello").getAbsolutePath());
		importPage.copyProjectsIntoWorkspace(true);
		// This may take several minutes
		new PushButton("Finish").click();
		TimePeriod timeout = TimePeriod.getCustom(20 * 60 * 1000);
		new WaitWhile(new ShellIsActive("Import"), timeout);
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
		CompositePropertiesDialog dialog = new SwitchYardComponent("hello").showProperties(false);
		dialog.select("Transform Breakpoint Properties");
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
		dialog.ok();
		new WaitWhile(new ShellIsAvailable("Properties for "));

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
		dialog = new SwitchYardComponent("hello").showProperties(false);
		dialog.select("Transform Breakpoint Properties");
		assertEquals("hello", new LabeledText("Project").getText());
		assertFalse(new CheckBox("Enabled").isChecked());
		assertTrue(new CheckBox("IN").isChecked());
		assertFalse(new CheckBox("OUT").isChecked());
		assertTrue(new CheckBox("FAULT").isChecked());
		dialog.ok();
		new WaitWhile(new ShellIsAvailable("Properties for "));

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
		CompositePropertiesDialog dialog = new SwitchYardComponent("hello").showProperties(false);
		dialog.select("Validate Breakpoint Properties");
		assertEquals("hello", new LabeledText("Project").getText());
		assertTrue(new CheckBox("Enabled").isChecked());
		assertTrue(new CheckBox("IN").isChecked());
		assertTrue(new CheckBox("OUT").isChecked());
		assertTrue(new CheckBox("FAULT").isChecked());
		new CheckBox("IN").toggle(false);
		new CheckBox("OUT").toggle(false);
		dialog.ok();
		new WaitWhile(new ShellIsAvailable("Properties for "));

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
		dialog = new SwitchYardComponent("hello").showProperties(false);
		dialog.select("Validate Breakpoint Properties");
		assertEquals("hello", new LabeledText("Project").getText());
		assertFalse(new CheckBox("Enabled").isChecked());
		assertTrue(new CheckBox("IN").isChecked());
		assertFalse(new CheckBox("OUT").isChecked());
		assertTrue(new CheckBox("FAULT").isChecked());
		dialog.ok();
		new WaitWhile(new ShellIsAvailable("Properties for "));

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
		CompositePropertiesDialog dialog = new SwitchYardComponent("HelloService").showProperties();
		dialog.select("Service Breakpoint Properties");
		assertEquals("HelloService", new LabeledText("Service Name").getText());
		assertEquals("urn:com.example.switchyard:hello:1.0", new LabeledText("Service Namespace").getText());
		assertEquals("hello", new LabeledText("Project").getText());
		assertTrue(new CheckBox("Enabled").isChecked());
		assertTrue(new CheckBox("IN").isChecked());
		assertTrue(new CheckBox("OUT").isChecked());
		assertTrue(new CheckBox("FAULT").isChecked());
		new CheckBox("IN").toggle(false);
		new CheckBox("OUT").toggle(false);
		dialog.ok();
		new WaitWhile(new ShellIsAvailable("Properties for HelloService"));

		// check the breakpoint in Breakpoints view
		breakpoints = new BreakpointsView().getBreakpoints();
		assertEquals("No breakpoint found", 1, breakpoints.size());
		// wait for the change
		new WaitUntil(new AbstractWaitCondition() {

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
		dialog = new SwitchYardComponent("HelloService").showProperties();
		dialog.select("Service Breakpoint Properties");
		assertEquals("hello", new LabeledText("Project").getText());
		assertFalse(new CheckBox("Enabled").isChecked());
		assertTrue(new CheckBox("IN").isChecked());
		assertFalse(new CheckBox("OUT").isChecked());
		assertTrue(new CheckBox("FAULT").isChecked());
		dialog.ok();
		new WaitWhile(new ShellIsAvailable("Properties for HelloService"));

		// delete breakpoint
		new BreakpointsView().removeAllBreakpoints();
		dialog = new SwitchYardComponent("HelloService").showProperties();
		try {
			dialog.select("Service Breakpoint Properties");
			fail("Item 'Service Breakpoint Properties' is still available");
		} catch (SWTLayerException | CoreLayerException ex) {
			// ok
		}
		dialog.ok();
		new WaitWhile(new ShellIsAvailable("Properties for HelloService"));
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
		CompositePropertiesDialog dialog = new SwitchYardComponent("Hello").showProperties();
		dialog.select("Service Breakpoint Properties");
		assertEquals("Hello", new LabeledText("Service Name").getText());
		assertEquals("urn:com.example.switchyard:hello:1.0", new LabeledText("Service Namespace").getText());
		assertEquals("hello", new LabeledText("Project").getText());
		assertTrue(new CheckBox("Enabled").isChecked());
		assertTrue(new CheckBox("IN").isChecked());
		assertTrue(new CheckBox("OUT").isChecked());
		assertTrue(new CheckBox("FAULT").isChecked());
		new CheckBox("IN").toggle(false);
		new CheckBox("OUT").toggle(false);
		dialog.ok();
		new WaitWhile(new ShellIsAvailable("Properties for Hello"));

		// check the breakpoint in Breakpoints view
		breakpoints = new BreakpointsView().getBreakpoints();
		assertEquals("No breakpoint found", 1, breakpoints.size());
		breakpoint = breakpoints.get(0);
		// wait for the change
		new WaitUntil(new AbstractWaitCondition() {

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
		dialog = new SwitchYardComponent("Hello").showProperties();
		dialog.select("Service Breakpoint Properties");
		assertEquals("hello", new LabeledText("Project").getText());
		assertFalse(new CheckBox("Enabled").isChecked());
		assertTrue(new CheckBox("IN").isChecked());
		assertFalse(new CheckBox("OUT").isChecked());
		assertTrue(new CheckBox("FAULT").isChecked());
		dialog.ok();
		new WaitWhile(new ShellIsAvailable("Properties for Hello"));

		// delete breakpoint
		new BreakpointsView().removeAllBreakpoints();
		dialog = new SwitchYardComponent("Hello").showProperties();
		new DefaultShell("Properties for Hello").setFocus();
		try {
			dialog.select("Service Breakpoint Properties");
			fail("Item 'Service Breakpoint Properties' is still available");
		} catch (SWTLayerException | CoreLayerException ex) {
			// ok
		}
		dialog.ok();
		new WaitWhile(new ShellIsAvailable("Properties for Hello"));
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

}
