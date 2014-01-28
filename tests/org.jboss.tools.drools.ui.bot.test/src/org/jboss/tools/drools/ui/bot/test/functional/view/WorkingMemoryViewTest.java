package org.jboss.tools.drools.ui.bot.test.functional.view;

import java.util.List;
import java.util.Map;

import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.matcher.RegexMatchers;
import org.jboss.tools.drools.reddeer.debug.DebugView;
import org.jboss.tools.drools.reddeer.editor.DrlEditor;
import org.jboss.tools.drools.reddeer.editor.EnhancedTextEditor;
import org.jboss.tools.drools.reddeer.editor.RuleEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.reddeer.view.WorkingMemoryView;
import org.jboss.tools.drools.ui.bot.test.annotation.Drools6Runtime;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.jboss.tools.drools.ui.bot.test.util.OpenUtility;
import org.jboss.tools.drools.ui.bot.test.util.RunUtility;
import org.junit.Assert;
import org.junit.Test;

public class WorkingMemoryViewTest extends ViewTestParent {

    public WorkingMemoryViewTest() {
        super(WorkingMemoryView.class);
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @Drools6Runtime @ UseDefaultProject
    public void testSampleFile() {
        OpenUtility.openResource(DEFAULT_PROJECT_NAME, RESOURCES_LOCATION, "rules", "Sample.drl");
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

        new DebugView().selectItem(new RegexMatchers("DroolsTest.*", "com\\.sample\\.DroolsTest.*").getMatchers());
        new ShellMenu(new RegexMatchers("Run", "Resume.*").getMatchers()).select();
        waitASecond();

        objects = view.getObjects();
        Assert.assertEquals("Unexpected number of objects in WM", 1, objects.size());

        attribs = view.getObjectAttributes(objects.get(0));
        Assert.assertTrue("Unable to find attribute 'message'", attribs.containsKey("message"));
        Assert.assertEquals("Wrong value of 'message' attribute", "\"Goodbye cruel world\"", attribs.get("message"));

        Assert.assertTrue("Unable to find attribute 'status'", attribs.containsKey("status"));
        Assert.assertEquals("Wrong value of 'status' attribute", "1", attribs.get("status"));
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @Drools6Runtime @ UseDefaultProject
    public void testMultipleFacts() {
        OpenUtility.openResource(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");
        EnhancedTextEditor txt = new EnhancedTextEditor();
        txt.setPosition(17, 0);
        txt.writeText("\n            kSession.insert(\"testString\");\n");
        txt.writeText("            kSession.insert(new Object());\n");
        txt.writeText("            kSession.insert(java.util.Arrays.asList(1, 2, 3));\n");
        txt.close(true);

        OpenUtility.openResource(DEFAULT_PROJECT_NAME, RESOURCES_LOCATION, "rules", "Sample.drl");
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
