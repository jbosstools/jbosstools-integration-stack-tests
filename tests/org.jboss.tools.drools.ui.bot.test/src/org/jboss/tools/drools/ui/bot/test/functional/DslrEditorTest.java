package org.jboss.tools.drools.ui.bot.test.functional;


import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.matcher.RegexMatchers;
import org.jboss.reddeer.workbench.editor.TextEditor;
import org.jboss.tools.drools.reddeer.editor.ContentAssist;
import org.jboss.tools.drools.reddeer.editor.DslEditor;
import org.jboss.tools.drools.reddeer.editor.DslrEditor;
import org.jboss.tools.drools.reddeer.editor.RuleEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.reddeer.wizard.NewDslWizard;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizard;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultRuntime;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DslrEditorTest extends TestParent {

    @Before
    public void createDsl() {
        NewDslWizard wizard1 = new NewDslWizard();
        wizard1.open();
        wizard1.getFirstPage().setParentFolder(DEFAULT_RULES_PATH);
        wizard1.getFirstPage().setFileName(name.getMethodName());
        wizard1.finish();

        new DslEditor().close();

        PackageExplorer explorer = new PackageExplorer();
        explorer.open();
        explorer.getProject(DEFAULT_PROJECT_NAME).getProjectItem(RESOURCES_LOCATION, "rules", name.getMethodName() + ".dsl").select();
        DslEditorTest.openWith("Text Editor");

        TextEditor editor1 = new TextEditor();
        editor1.setText(getTemplateText("dslr/DslFile"));
        editor1.save();
        editor1.close();

        NewRuleResourceWizard wizard2 = new NewRuleResourceWizard();
        wizard2.open();
        wizard2.getFirstPage().setParentFolder(DEFAULT_RULES_PATH);
        wizard2.getFirstPage().setRulePackageName("com.redhat");
        wizard2.getFirstPage().setFileName(name.getMethodName());
        wizard2.getFirstPage().setUseDSL(true);
        wizard2.finish();

        DslrEditor dslr = new DslrEditor();
        RuleEditor editor2 = dslr.showRuleEditor();
        editor2.setText(String.format(getTemplateText("dslr/DslrTestFile"), name.getMethodName()));
        editor2.close(true);
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testTranslation() {
        DslrEditor editor = openTestDslr();

        String translation = editor.showDrlViewer().getText();

        Assert.assertEquals("Wrong text in translation!", getTemplateText("dslr/DslrTestTransformation"), translation);
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testExpanderCompletion() {
        RuleEditor editor = openTestDslr().showRuleEditor();
        editor.setPosition(2, 9);

        ContentAssist assist = editor.createContentAssist();
        List<String> items = assist.getItems();

        int dsls = 0;
        for (String item : items) {
            if (item.endsWith(".dsl")) {
                dsls++;
            }
        }

        Assert.assertEquals("No DSL files were found/proposed", 1, dsls);
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testKeywordCompletion() {
        DslrEditor master = openTestDslr();
        RuleEditor editor = master.showRuleEditor();
        editor.setPosition(18, 0);

        ContentAssist assist = editor.createContentAssist();
        List<String> items = assist.getItems();

        Assert.assertTrue("DSL for rule keyword was not proposed", items.contains("pravidlo"));

        assist.selectItem("pravidlo");
        Assert.assertEquals("Wrong text was inserted", "pravidlo \"new rule\"", editor.getTextOnCurrentLine());

        RuleEditor drl = master.showDrlViewer();
        drl.setPosition(16, 0);
        Assert.assertEquals("Wrong translation", "rule \"new rule\"", drl.getTextOnCurrentLine().trim());
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testConditionCompletion() {
        DslrEditor master = openTestDslr();
        RuleEditor editor = master.showRuleEditor();
        editor.setPosition(21, 8);

        ContentAssist assist = editor.createContentAssist();
        List<String> items = assist.getItems();

        Assert.assertEquals("Wrong number of items in the list", 1, items.size());
        assist.selectItem("There is an? {entity}");
        Assert.assertEquals("Wrong text inserted", "There is an? {entity}", editor.getTextOnCurrentLine().trim());

        editor.setPosition(21, 18);
        editor.replaceText(" Person", 11);

        Assert.assertEquals("Wrong text", "There is a Person", editor.getTextOnCurrentLine().trim());

        RuleEditor drl = master.showDrlViewer();
        drl.setPosition(19, 0);
        Assert.assertEquals("Wrong translation", "$person: Person()", drl.getTextOnCurrentLine().trim());
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testConstraintsCompletion() {
        DslrEditor master = openTestDslr();
        RuleEditor editor = master.showRuleEditor();
        editor.setPosition(21, 8);
        editor.writeText("There is a Person\n            ");

        ContentAssist assist = editor.createContentAssist();
        List<String> items = assist.getItems();

        Assert.assertEquals("Wrong number of items in the list", 3, items.size());
        assist.selectItem("- with an? {attr} greater than {amount}");
        Assert.assertEquals("Wrong text inserted", "- with an? {attr} greater than {amount}", editor.getTextOnCurrentLine().trim());

        editor.setPosition(22, 21);
        editor.replaceText(" age", 8);

        editor.setPosition(22, 39);
        editor.replaceText("18", 8);

        Assert.assertEquals("Wrong text", "- with an age greater than 18", editor.getTextOnCurrentLine().trim());

        RuleEditor drl = master.showDrlViewer();
        drl.setPosition(19, 0);
        Assert.assertEquals("Wrong translation", "$person: Person(age > 18)", drl.getTextOnCurrentLine().trim());
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testConsequenceCompletion() {
        DslrEditor master = openTestDslr();
        RuleEditor editor = master.showRuleEditor();
        editor.setPosition(23, 8);

        ContentAssist assist = editor.createContentAssist();
        List<String> items = assist.getItems();

        Assert.assertEquals("Wrong number of items in the list", 1, items.size());
        assist.selectItem("print \"{text}\"");

        Assert.assertEquals("Wrong text inserted", "print \"{text}\"", editor.getTextOnCurrentLine().trim());

        editor.setPosition(23, 15);
        editor.replaceText("Hello world!", 6);

        Assert.assertEquals("Wrong text", "print \"Hello world!\"", editor.getTextOnCurrentLine().trim());

        RuleEditor drl = master.showDrlViewer();
        drl.setPosition(21, 0);
        Assert.assertEquals("Wrong translation", "System.out.println(\"Hello world!\");", drl.getTextOnCurrentLine().trim());
    }

    private DslrEditor openTestDslr() {
        PackageExplorer explorer = new PackageExplorer();
        explorer.open();
        explorer.getProject(DEFAULT_PROJECT_NAME).getProjectItem(RESOURCES_LOCATION, "rules", name.getMethodName() + ".dslr").select();
        new ContextMenu(new RegexMatchers("Open.*").getMatchers()).select();

        // this has to be done to allow DRL Viewer to work properly
        DslrEditor editor = new DslrEditor();
        editor.showRuleEditor();

        return editor;
    }
}
