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
 * TODO: modify, insert, retract
 */
@Runtime(type = RuntimeReqType.DROOLS)
@RunWith(RedDeerSuite.class)
public class ConsequencesCompletionTest extends DrlCompletionParent {

	@InjectRequirement
	private RuntimeRequirement droolsRequirement;

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testConsequencesCompletion() {
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);
		editor.writeText(
				"import com.sample.domain.MyMessage\n\nrule newRule\n\twhen\n\t\t$msg: MyMessage()\n\tthen\n\t\t\nend\n");
		editor.setPosition(8, 2);

		ContentAssist assist = editor.createContentAssist();
		List<String> items = assist.getItems();
		Assert.assertTrue("Variable kcontext is unavailable", items.contains("kcontext : RuleContext"));
		Assert.assertTrue("Variable $msg is unavailable", items.contains("$msg : MyMessage"));

		assist.selectItem("kcontext : RuleContext");
		editor.writeText(".");
		assist = editor.createContentAssist();
		items = assist.getItems();
		// 3*wait + notify + notifyAll + toString + hashCode + equals + getClass
		Assert.assertTrue("kcontext methods are not available", items.size() > 9);
		Assert.assertTrue("kcontext is missing method getRule()", items.contains("getRule() : Rule - RuleContext"));
		assist.selectItem("getRule() : Rule - RuleContext");
		assertCorrectText(editor, "kcontext.getRule()");

		editor.writeText(";\n\t\t");
		assist = editor.createContentAssist();
		items = assist.getItems();
		Assert.assertTrue("Variable kcontext is unavailable", items.contains("kcontext : RuleContext"));
		Assert.assertTrue("Variable $msg is unavailable", items.contains("$msg : MyMessage"));
		assist.selectItem("$msg : MyMessage");
		editor.writeText(".");
		assist = editor.createContentAssist();
		items = assist.getItems();
		Assert.assertEquals("Some MyMessage methods are missing", 15, items.size());
		Assert.assertTrue("Method setParameter() is unavailable",
				items.contains("setParameter(Object parameter) : void - MyMessage"));
		assist.selectItem("setParameter(Object parameter) : void - MyMessage");

		editor.writeText("MyMessage.");
		assist = editor.createContentAssist();
		items = assist.getItems();
		Assert.assertEquals("Some Message methods are missing", 3, items.size());
		Assert.assertTrue("Constant NO_PARAMETER is missing", items.contains("NO_PARAMETER : Object - MyMessage"));
		assist.selectItem("NO_PARAMETER : Object - MyMessage");
		assertCorrectText(editor, "$msg.setParameter(MyMessage.NO_PARAMETER);");
	}
}
