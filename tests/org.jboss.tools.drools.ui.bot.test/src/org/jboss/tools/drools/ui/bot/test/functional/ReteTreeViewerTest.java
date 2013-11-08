package org.jboss.tools.drools.ui.bot.test.functional;

import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.workbench.editor.TextEditor;
import org.jboss.tools.drools.reddeer.editor.DrlEditor;
import org.jboss.tools.drools.reddeer.editor.RuleEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizard;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultRuntime;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.jboss.tools.drools.ui.bot.test.util.SmokeTest;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public class ReteTreeViewerTest extends TestParent {
    public static final String MESSAGE_TEXT = getTemplateText("MessageClass");
    public static final String RULE_RESOURCE_TEXT = getTemplateText("DummyRuleFile");

    @Before
    public void setUpDomainAndRule() {
        // create domain class
        NewJavaClassWizardDialog diag = new NewJavaClassWizardDialog();
        diag.open();
        diag.getFirstPage().setName("Message");
        diag.getFirstPage().setPackage("com.sample.domain");
        diag.finish();

        TextEditor txtEditor = new TextEditor();
        txtEditor.setText(MESSAGE_TEXT);
        txtEditor.close(true);

        // create RuleResource
        NewRuleResourceWizard wiz = new NewRuleResourceWizard();
        wiz.open();
        wiz.getFirstPage().setParentFolder(DEFAULT_PROJECT_NAME + "/" + RESOURCES_LOCATION);
        wiz.getFirstPage().setFileName(name.getMethodName());
        wiz.getFirstPage().setRulePackageName("com.sample");
        wiz.finish();

        RuleEditor drlEditor = new DrlEditor().showRuleEditor();
        drlEditor.setText(RULE_RESOURCE_TEXT);
        drlEditor.save();
    }

    @Test @Category(SmokeTest.class)
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testTimer() {
        DrlEditor master = new DrlEditor();
        RuleEditor editor = master.showRuleEditor();
        editor.setPosition(2, 0);
        editor.writeText("import com.sample.domain.Message");

        editor.setPosition(9, 3);
        editor.writeText("\n\n");
        editor.writeText("rule testTimer\n");
        editor.writeText("    timer(int: 1s 0)\n");
        editor.writeText("    when\n");
        editor.writeText("        $msg: Message( )\n");
        editor.writeText("    then\n");
        editor.writeText("        retract($msg);\n");
        editor.writeText("end\n");

        editor.save();

        master.showReteTree();
        Assert.assertTrue("Rete Tree is not opened! Check log for errors.", master.isReteTreeOpened());
    }

    @Test @Category(SmokeTest.class)
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testOrConstraint() {
        DrlEditor master = new DrlEditor();
        RuleEditor editor = master.showRuleEditor();
        editor.setPosition(2, 0);
        editor.writeText("import com.sample.domain.Message");

        editor.setPosition(9, 3);
        editor.writeText("\n\n");
        editor.writeText("rule testOrConstraint\n");
        editor.writeText("    when\n");
        editor.writeText("        $msg: Message( text != null || parameter != null )\n");
        editor.writeText("    then\n");
        editor.writeText("        System.out.println($msg);\n");
        editor.writeText("end\n");

        editor.save();

        master.showReteTree();
        Assert.assertTrue("Rete Tree is not opened! Check log for errors.", master.isReteTreeOpened());
    }

    @Test @Category(SmokeTest.class)
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testNamedConsequeces() {
        DrlEditor master = new DrlEditor();
        RuleEditor editor = master.showRuleEditor();
        editor.setPosition(2, 0);
        editor.writeText("import com.sample.domain.Message");

        editor.setPosition(9, 3);
        editor.writeText("\n\n");
        editor.writeText("rule testNamedConsequeces\n");
        editor.writeText("    when\n");
        editor.writeText("        $msg: Message( )\n");
        editor.writeText("        do[everyMessage]");
        editor.writeText("        Message( this == $msg, text != null )\n");
        editor.writeText("    then\n");
        editor.writeText("        System.out.println($msg);\n");
        editor.writeText("    then[everyMessage]\n");
        editor.writeText("        System.out.println(\"There is a message!\");\n");
        editor.writeText("end\n");

        editor.save();

        master.showReteTree();
        Assert.assertTrue("Rete Tree is not opened! Check log for errors.", master.isReteTreeOpened());
    }
}
