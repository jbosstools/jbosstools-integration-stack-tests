package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.tools.fuse.reddeer.debug.Breakpoint;
import org.jboss.tools.fuse.reddeer.debug.BreakpointsView;
import org.jboss.tools.fuse.reddeer.debug.IsRunning;
import org.jboss.tools.fuse.reddeer.debug.IsSuspended;
import org.jboss.tools.fuse.reddeer.debug.ResumeButton;
import org.jboss.tools.fuse.reddeer.debug.StepOverButton;
import org.jboss.tools.fuse.reddeer.debug.TerminateButton;
import org.jboss.tools.fuse.reddeer.debug.VariablesView;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.reddeer.view.ErrorLogView;
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests Camel Routes Debugger
 * 
 * @author tsedmik
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class DebuggerTest extends DefaultTest {

	private static final String PROJECT_ARCHETYPE = "camel-archetype-spring";
	private static final String PROJECT_NAME = "camel-spring";
	private static final String CAMEL_CONTEXT = "camel-context.xml";
	private static final String CHOICE = "choice1";
	private static final String LOG = "log1";
	private static final String LOG2 = "log2";

	/**
	 * Prepares test environment
	 * 
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@BeforeClass
	public static void setupInitial() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject(PROJECT_NAME, PROJECT_ARCHETYPE);
		new CamelProject(PROJECT_NAME).openCamelContext(CAMEL_CONTEXT);
		CamelEditor.switchTab("Design");
		CamelEditor editor = new CamelEditor(CAMEL_CONTEXT);
		editor.setId("choice", CHOICE);
		editor.setId("log", LOG);
		editor.setId("log", LOG2);
		editor.save();
	}

	/**
	 * Cleans up test environment
	 */
	@After
	public void setupRemoveAllBreakpoints() {

		new BreakpointsView().removeAllBreakpoints();
	}

	/**
	 * <p>
	 * Test tries to add/remove/disable/enable breakpoints to the components in the Camel Editor.
	 * </p>
	 * <b>Steps</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>open camel-context.xml file</li>
	 * <li>set breakpoint to the choice component</li>
	 * <li>set breakpoint to the log component in the top branch</li>
	 * <li>open Breakpoints View and check if set breakpoints are present</li>
	 * <li>open camel-context.xml file</li>
	 * <li>disable the breakpoint on the choice component</li>
	 * <li>check if the breakpoint can be disabled again</li>
	 * <li>check if the breakpoint is disabled in the Breakpoints view</li>
	 * <li>enable the breakpoint on the choice component</li>
	 * <li>check if the breakpoint can be enabled again</li>
	 * <li>check if the breakpoint is enabled in the Breakpoints view</li>
	 * <li>delete the breakpoint in the CamelEditor</li>
	 * <li>check if the breakpoint can be deleted again</li>
	 * <li>check if the breakpoint is no longer available in the Breakpoints view</li>
	 * <li>remove the breakpoint on the log component via Breakpoints view</li>
	 * <li>check if the breakpoint is set in the Camel Editor</li>
	 * </ol>
	 */
	@Test
	public void testBreakpointManipulation() {

		CamelEditor editor = new CamelEditor(CAMEL_CONTEXT);

		// set some breakpoints
		editor.setBreakpoint(CHOICE);
		editor.setBreakpoint(LOG);

		// check Breakpoints View
		BreakpointsView view = new BreakpointsView();
		assertTrue(view.isBreakpointSet(CHOICE));
		assertTrue(view.isBreakpointSet(LOG));

		// do some operations (disable/enable/remove) and check
		Breakpoint choice = view.getBreakpoint(CHOICE);
		choice.disable();
		assertFalse(choice.isEnabled());
		assertFalse(editor.isBreakpointEnabled(CHOICE));
		editor.enableBreakpoint(CHOICE);
		assertTrue(editor.isBreakpointEnabled(CHOICE));
		view.open();
		assertTrue(choice.isEnabled());
		editor.deleteBreakpoint(CHOICE);
		assertFalse(editor.isBreakpointSet(CHOICE));
		view.open();
		assertTrue(view.getBreakpoint(CHOICE) == null);
		view.open();
		view.getBreakpoint(LOG).remove();
		assertFalse(editor.isBreakpointSet(LOG));
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	/**
	 * <p>
	 * Test tries debugging of the Camel route - suspending, resuming, step over, variables values.
	 * </p>
	 * <b>Steps</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>open camel-context.xml file</li>
	 * <li>set breakpoint to the choice component</li>
	 * <li>set breakpoint to the log component in the top branch</li>
	 * <li>debug the Camel Context without tests</li>
	 * <li>wait until process is suspended</li>
	 * <li>check if the Console View contains text Enabling Debugger</li>
	 * <li>check if the Variables View contains variable Endpoint with value choice1</li>
	 * <li>check if the Variables View contains variable Message with value <city>London</city></li>
	 * <li>resume debugging</li>
	 * <li>wait until process is suspended</li>
	 * <li>check if the Variables View contains variable Endpoint with value log1</li>
	 * <li>click on the Step over button</li>
	 * <li>wait until process is suspended</li>
	 * <li>check if the Console View contains text UK message</li>
	 * <li>check if the Variables View contains variable Endpoint with value to1</li>
	 * <li>remove all breakpoints</li>
	 * <li>check if the Console View contains text Removing breakpoint choice1</li>
	 * <li>check if the Console View contains text Removing breakpoint log1</li>
	 * <li>resume debugging</li>
	 * <li>terminate process via Terminate button in the Console View</li>
	 * <li>check if the Console View contains text Disabling debugger</li>
	 * </ol>
	 */
	@Test
	public void testDebugger() {

		new CamelProject(PROJECT_NAME).openCamelContext(CAMEL_CONTEXT);
		CamelEditor editor = new CamelEditor(CAMEL_CONTEXT);
		editor.setBreakpoint(CHOICE);
		editor.setBreakpoint(LOG);
		new CamelProject(PROJECT_NAME).debugCamelContextWithoutTests(CAMEL_CONTEXT);

		// should stop on the 'choice1' node
		new WaitUntil(new IsSuspended(), TimePeriod.NORMAL);
		assertTrue(new ConsoleHasText("Enabling debugger").test());
		VariablesView variables = new VariablesView();
		AbstractWait.sleep(TimePeriod.getCustom(5));
		assertEquals(CHOICE, variables.getValue("Endpoint"));

		// get body of message
		variables.close();
		AbstractWait.sleep(TimePeriod.SHORT);
		variables.open();
		new DefaultTreeItem(4).getItems().get(0).select();
		assertTrue(new DefaultStyledText().getText().contains("<city>Tampa</city>"));

		// resume and then should stop on the 'choice' node
		ResumeButton resume = new ResumeButton();
		assertTrue(resume.isEnabled());
		resume.select();
		new WaitUntil(new IsSuspended(), TimePeriod.NORMAL);
		AbstractWait.sleep(TimePeriod.getCustom(2));
		assertEquals(CHOICE, variables.getValue("Endpoint"));

		// resume and then should stop on the 'log1' node
		assertTrue(resume.isEnabled());
		resume.select();
		new WaitUntil(new IsSuspended(), TimePeriod.NORMAL);
		AbstractWait.sleep(TimePeriod.getCustom(2));
		assertEquals(LOG, variables.getValue("Endpoint"));

		// step over then should stop on the 'to1' endpoint
		assertTrue(resume.isEnabled());
		new StepOverButton().select();
		new WaitUntil(new IsSuspended(), TimePeriod.NORMAL);
		assertTrue(new ConsoleHasText("UK message").test());
		assertTrue(resume.isEnabled());
		AbstractWait.sleep(TimePeriod.getCustom(5));
		assertEquals("to1", variables.getValue("Endpoint"));

		// remove all breakpoints
		new BreakpointsView().removeAllBreakpoints();
		assertTrue(new ConsoleHasText("Removing breakpoint choice1").test());
		assertTrue(new ConsoleHasText("Removing breakpoint log1").test());
		resume.select();

		// all breakpoints should be processed
		new WaitUntil(new IsRunning(), TimePeriod.NORMAL);
		new TerminateButton().select();
		assertTrue(new ConsoleHasText("Disabling debugger").test());
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	/**
	 * <p>
	 * Test tries conditional debugging of the Camel route - suspending only when condition is fulfilled.
	 * </p>
	 * <b>Steps</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>open camel-context.xml file</li>
	 * <li>set conditional breakpoint to the choice component - simple with
	 * "${in.header.CamelFileName} == 'message1.xml'"</li>
	 * <li>set conditional breakpoint to the log component in the top branch - simple with
	 * "${in.header.CamelFileName} == 'message2.xml'"</li>
	 * <li>debug the Camel Context without tests</li>
	 * <li>wait until process is suspended</li>
	 * <li>check if the Variables View contains variable Endpoint with value choice1</li>
	 * <li>resume debugging</li>
	 * <li>check if the Console View contains text UK message</li>
	 * <li>check if the Console View contains text Other message</li>
	 * <li>check if the process is not suspended</li>
	 * <li>terminate process via Terminate button in the Console View</li>
	 * <li>check if the Console View contains text Disabling debugger</li>
	 * </ol>
	 */
	@Test
	public void testConditionalBreakpoints() {

		new CamelProject(PROJECT_NAME).openCamelContext(CAMEL_CONTEXT);
		CamelEditor editor = new CamelEditor(CAMEL_CONTEXT);
		editor.setConditionalBreakpoint(CHOICE, "simple", "${in.header.CamelFileName} == 'message1.xml'");
		editor.setConditionalBreakpoint(LOG, "simple", "${in.header.CamelFileName} == 'message2.xml'");
		ResumeButton resume = new ResumeButton();
		new CamelProject(PROJECT_NAME).debugCamelContextWithoutTests(CAMEL_CONTEXT);
		new WaitUntil(new IsSuspended(), TimePeriod.NORMAL);

		// should stop on 'choice1' node
		VariablesView variables = new VariablesView();
		assertEquals(CHOICE, variables.getValue("Endpoint"));
		assertTrue(resume.isEnabled());

		// all breakpoint should be processed
		resume.select();
		new WaitUntil(new IsRunning(), TimePeriod.NORMAL);
		assertTrue(new ConsoleHasText("UK message").test());
		assertTrue(new ConsoleHasText("Other message").test());
		new TerminateButton().select();
		assertTrue(new ConsoleHasText("Disabling debugger").test());
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}
}
