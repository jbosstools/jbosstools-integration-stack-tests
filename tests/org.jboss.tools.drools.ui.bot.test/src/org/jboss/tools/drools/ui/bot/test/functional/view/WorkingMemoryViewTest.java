package org.jboss.tools.drools.ui.bot.test.functional.view;

import java.util.List;
import java.util.Map;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.StyledText;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.drools.reddeer.debug.DebugView;
import org.jboss.tools.drools.reddeer.editor.DrlEditor;
import org.jboss.tools.drools.reddeer.editor.RuleEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.reddeer.view.WorkingMemoryView;
import org.jboss.tools.drools.ui.bot.test.util.OpenUtility;
import org.jboss.tools.drools.ui.bot.test.util.RunUtility;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UsePerspective;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeReqType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Runtime(type = RuntimeReqType.DROOLS)
@RunWith(RedDeerSuite.class)
public class WorkingMemoryViewTest extends ViewTestParent {

	@InjectRequirement
	private RuntimeRequirement droolsRequirement;

	public WorkingMemoryViewTest() {
		super(WorkingMemoryView.class);
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testSampleFile() {
		OpenUtility.openResource(DEFAULT_PROJECT_NAME, getResourcePath("Sample.drl"));
		RuleEditor editor = new DrlEditor().showRuleEditor();
		editor.setBreakpoint(8);
		editor.setBreakpoint(18);

		RunUtility.debugAsDroolsApplication(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");
		WorkingMemoryView view = new WorkingMemoryView();
		view.open();

		List<String> objects = view.getObjects();
		Assert.assertEquals("Unexpected number of objects in WM", 1, objects.size());

		Map<String, String> attribs = view.getObjectAttributes(objects.get(0));
		Assert.assertTrue("Unable to find attribute 'message'", attribs.containsKey("message"));
		Assert.assertEquals("Wrong value of 'message' attribute", "\"Hello World\"", attribs.get("message"));

		Assert.assertTrue("Unable to find attribute 'status'", attribs.containsKey("status"));
		Assert.assertEquals("Wrong value of 'status' attribute", "0", attribs.get("status"));

		new DebugView().selectItem(new RegexMatcher("DroolsTest.*"), new RegexMatcher("com\\.sample\\.DroolsTest.*"));
		new ShellMenu(new RegexMatcher("Run"), new RegexMatcher("Resume.*")).select();
		AbstractWait.sleep(TimePeriod.SHORT);

		objects = view.getObjects();
		Assert.assertEquals("Unexpected number of objects in WM", 1, objects.size());

		attribs = view.getObjectAttributes(objects.get(0));
		Assert.assertTrue("Unable to find attribute 'message'", attribs.containsKey("message"));
		Assert.assertEquals("Wrong value of 'message' attribute", "\"Goodbye cruel world\"", attribs.get("message"));

		Assert.assertTrue("Unable to find attribute 'status'", attribs.containsKey("status"));
		Assert.assertEquals("Wrong value of 'status' attribute", "1", attribs.get("status"));
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testMultipleFacts() {
		OpenUtility.openResource(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");
		TextEditor txtEditor = new TextEditor();
		StyledText text = new DefaultStyledText();
		text.insertText(17, 0, "\n            kSession.insert(\"testString\");\n");
		text.insertText(18, 0, "            kSession.insert(new Object());\n");
		text.insertText(19, 0, "            kSession.insert(java.util.Arrays.asList(1, 2, 3));\n");
		txtEditor.save();

		OpenUtility.openResource(DEFAULT_PROJECT_NAME, getResourcePath("Sample.drl"));
		RuleEditor editor = new DrlEditor().showRuleEditor();
		editor.setBreakpoint(8);

		RunUtility.debugAsDroolsApplication(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");
		WorkingMemoryView view = new WorkingMemoryView();
		view.open();

		List<String> objects = view.getObjects();
		Assert.assertEquals("Unexpected number of objects in WM", 4, objects.size());
		Assert.assertTrue("Expected object not found 'testString'", objects.contains("\"testString\""));
		Assert.assertTrue("Expected object not found 'Object'", objects.contains("Object"));
		Assert.assertTrue("Expected object not found 'Arrays$ArrayList<E>'", objects.contains("Arrays$ArrayList<E>"));
		Assert.assertTrue("Expected object not found 'DroolsTest$Message'", objects.contains("DroolsTest$Message"));
	}
}
