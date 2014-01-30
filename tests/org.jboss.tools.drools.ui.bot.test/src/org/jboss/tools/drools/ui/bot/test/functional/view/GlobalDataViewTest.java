package org.jboss.tools.drools.ui.bot.test.functional.view;

import java.util.List;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.reddeer.editor.DrlEditor;
import org.jboss.tools.drools.reddeer.editor.EnhancedTextEditor;
import org.jboss.tools.drools.reddeer.editor.RuleEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.reddeer.view.GlobalDataView;
import org.jboss.tools.drools.ui.bot.test.annotation.Drools6Runtime;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.jboss.tools.drools.ui.bot.test.util.OpenUtility;
import org.jboss.tools.drools.ui.bot.test.util.RunUtility;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public class GlobalDataViewTest extends ViewTestParent {

    public GlobalDataViewTest() {
        super(GlobalDataView.class);
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @Drools6Runtime @UseDefaultProject
    public void testNoGlobalsDefined() {
        OpenUtility.openResource(DEFAULT_PROJECT_NAME, getResourcePath("Sample.drl"));

        RuleEditor editor = new DrlEditor().showRuleEditor();
        editor.setBreakpoint(8);

        RunUtility.debugAsDroolsApplication(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");

        GlobalDataView globals = new GlobalDataView();
        globals.open();

        Assert.assertEquals("Undefined globals found", 0, globals.getGlobalsList().size());
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @Drools6Runtime @UseDefaultProject
    public void testUninitiatedGlobal() {
        OpenUtility.openResource(DEFAULT_PROJECT_NAME, getResourcePath("Sample.drl"));

        RuleEditor editor = new DrlEditor().showRuleEditor();
        editor.setPosition(3, 0);
        editor.writeText("\nglobal String stringVar\n");
        editor.save();
        editor.setBreakpoint(10);

        RunUtility.debugAsDroolsApplication(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");

        GlobalDataView globals = new GlobalDataView();
        globals.open();

        List<String> names = globals.getGlobalsList();
        Assert.assertEquals("Wrong number of globals found", 1, names.size());
        Assert.assertEquals("Wrong global name encountered", "stringVar", names.get(0));
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @Drools6Runtime @UseDefaultProject
    public void testMultipleGlobals() {
        OpenUtility.openResource(DEFAULT_PROJECT_NAME, getResourcePath("Sample.drl"));

        RuleEditor editor = new DrlEditor().showRuleEditor();

        editor.setPosition(3, 0);
        editor.writeText("import java.util.List\n\n");
        editor.writeText("global String stringVar\n");
        editor.writeText("global Object objectVar\n");
        editor.writeText("global List listVar\n");
        editor.save();
        editor.setBreakpoint(13);

        OpenUtility.openResource(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");
        EnhancedTextEditor txtEditor = new EnhancedTextEditor();
        txtEditor.setPosition(17, 0);
        txtEditor.writeText("\n            kSession.setGlobal(\"stringVar\", \"testStringValue\");\n");
        txtEditor.writeText("\n            kSession.setGlobal(\"objectVar\", new Object());\n");
        txtEditor.writeText("\n            kSession.setGlobal(\"listVar\", java.util.Arrays.asList(1, 2, 3));\n");
        txtEditor.save();

        RunUtility.debugAsDroolsApplication(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");

        GlobalDataView globals = new GlobalDataView();
        globals.open();

        List<String> names = globals.getGlobalsList();
        Assert.assertEquals("Wrong number of globals found", 3, names.size());
        Assert.assertTrue("Global 'stringVar' not found", names.contains("stringVar"));
        Assert.assertTrue("Global 'objectVar' not found", names.contains("objectVar"));
        Assert.assertTrue("Global 'listVar' not found", names.contains("listVar"));
    }
}
