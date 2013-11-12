package org.jboss.tools.drools.ui.bot.test.functional.drleditor;

import java.util.List;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.reddeer.editor.ContentAssist;
import org.jboss.tools.drools.reddeer.editor.RuleEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultRuntime;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/*
 * TODO: accumulate, from, and/or, exists, forall, entry-points, windows
 * TODO: named consequences, inline cast, grouped accessors
 */
@RunWith(RedDeerSuite.class)
public class ConditionsCompletionTest extends DrlCompletionParent {
    @Test
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testFactTypeCompletion() {
        RuleEditor editor = master.showRuleEditor();
        editor.setPosition(2, 0);

        editor.writeText("import com.sample.domain.Message\n\n");

        selectFromContentAssist(editor, "rule");
        assertCorrectText(editor, "rule \"new rule\"");

        editor.setPosition(6, 2);
        selectFromContentAssist(editor, "Message");
        assertCorrectText(editor, "Message(  )");
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testConstraintsCompletion() {
        RuleEditor editor = master.showRuleEditor();
        editor.setPosition(2, 0);
        editor.writeText("import com.sample.domain.Message\n\nrule newRule\n\twhen\n\t\tMessage( )\n\tthen\nend\n");

        editor.setPosition(6, 11);

        ContentAssist assist = editor.createContentAssist();
        List<String> items = assist.getItems();
        Assert.assertTrue("Text field is not available", items.contains("text"));
        Assert.assertTrue("Parameter field is not available", items.contains("parameter"));
        Assert.assertTrue("Parameterized field is not available", items.contains("parameterized"));

        assist.selectItem("text");
        assertCorrectText(editor, "Message( text )");

        // finish the constraint
        editor.writeText("!= null, ");

        assist = editor.createContentAssist();
        items = assist.getItems();
        Assert.assertTrue("Text field is not available as second constraint", items.contains("text"));
        Assert.assertTrue("Parameter field is not available as second constraint", items.contains("parameter"));
        Assert.assertTrue("Parameterized field is not available as second constraint", items.contains("parameterized"));
        assist.close();

        editor.writeText("$var: ");
        assist = editor.createContentAssist();
        items = assist.getItems();
        Assert.assertTrue("Text field is not available for variable assignment", items.contains("text"));
        Assert.assertTrue("Parameter field is not available for variable assignment", items.contains("parameter"));
        Assert.assertTrue("Parameterized field is not available for variable assignment", items.contains("parameterized"));

    }
}
