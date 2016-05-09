package org.jboss.tools.drools.ui.bot.test.functional.drleditor;

import java.util.List;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.reddeer.editor.ContentAssist;
import org.jboss.tools.drools.reddeer.editor.RuleEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UsePerspective;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeReqType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/*
 * TODO: accumulate, from, and/or, exists, forall, entry-points, windows
 * TODO: named consequences, inline cast, grouped accessors
 */
@Runtime(type = RuntimeReqType.DROOLS)
@RunWith(RedDeerSuite.class)
public class ConditionsCompletionTest extends DrlCompletionParent {

	@InjectRequirement
	private RuntimeRequirement droolsRequirement;

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testFactTypeCompletion() {
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);

		editor.writeText("import com.sample.domain.MyMessage\n\n");

		selectFromContentAssist(editor, "rule");
		assertCorrectText(editor, "rule \"new rule\"");

		editor.setPosition(6, 2);
		selectFromContentAssist(editor, "MyMessage");
		assertCorrectText(editor, "MyMessage(  )");
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testConstraintsCompletion() {
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);
		editor.writeText("import com.sample.domain.MyMessage\n\nrule newRule\n\twhen\n\t\tMyMessage( )\n\tthen\nend\n");

		editor.setPosition(6, 13);

		ContentAssist assist = editor.createContentAssist();
		List<String> items = assist.getItems();
		Assert.assertTrue("Text field is not available", items.contains("text"));
		Assert.assertTrue("Parameter field is not available", items.contains("parameter"));
		Assert.assertTrue("Parameterized field is not available", items.contains("parameterized"));

		assist.selectItem("text");
		assertCorrectText(editor, "MyMessage( text )");

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
		Assert.assertTrue("Parameterized field is not available for variable assignment",
				items.contains("parameterized"));
		assist.selectItem("text");
	}
}
