package org.jboss.tools.drools.ui.bot.test.functional.drleditor;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.reddeer.editor.RuleEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultRuntime;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.junit.Test;
import org.junit.runner.RunWith;

/*
 * TODO: function, window, entry-point
 */
@RunWith(RedDeerSuite.class)
public class DeclareCompletionTest extends DrlCompletionParent {
    @Test
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testFactTypeDeclare() {
        RuleEditor editor = master.showRuleEditor();
        editor.setPosition(2, 0);

        selectFromContentAssist(editor, "declare");

        // TODO: not implemented in plugin, hard to tell what the line will look like
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testFactTypeUsage() {
        RuleEditor editor = master.showRuleEditor();
        editor.setPosition(2, 0);
        editor.writeText("declare Person\n\tname : String\n\tage : int\nend\n");

        editor.setPosition(9, 21);
        editor.writeText("\n        ");

        selectFromContentAssist(editor, "Person");
        selectFromContentAssist(editor, "name");
        editor.writeText("!= null, ");
        selectFromContentAssist(editor, "age");
        editor.writeText("> 18");

        assertCorrectText(editor, "Person( name != null, age > 18 )");
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testQueryDeclare() {
        RuleEditor editor = master.showRuleEditor();
        editor.setPosition(2, 0);
        editor.writeText("import com.sample.domain.Message\n\n");

        selectFromContentAssist(editor, "query");

        editor.setPosition(4, 6);
        editor.replaceText("testQuery", 12);
        assertCorrectText(editor, "query testQuery");

        editor.setPosition(5, 1);
        editor.replaceText("", 12); // delete "#consequences"

        selectFromContentAssist(editor, "Message");
        assertCorrectText(editor, "Message( )");
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testQueryUsage() {
        RuleEditor editor = master.showRuleEditor();
        editor.setPosition(2, 0);
        editor.writeText("import com.sample.domain.Message\n\nquery testQuery\n\tMessage()\nend\n");

        editor.setPosition(10, 21);
        editor.writeText("\n        ");

        selectFromContentAssist(editor, "testQuery");
        assertCorrectText(editor, "testQuery( )");
    }

}
