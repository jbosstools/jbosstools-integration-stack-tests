package org.jboss.tools.drools.ui.bot.test.functional.drleditor;

import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.reddeer.editor.RuleEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UsePerspective;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeImplementationRestriction;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeImplementationType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@Runtime
@RunWith(RedDeerSuite.class)
public class DeclareCompletionTest extends DrlCompletionParent {

	@InjectRequirement
	private RuntimeRequirement droolsRequirement;
	
	@RequirementRestriction
	public static RequirementMatcher getRequirementMatcher() {
		return new RuntimeImplementationRestriction(RuntimeImplementationType.DROOLS, RuntimeImplementationType.BRMS);
	}

	@Test
	@Ignore("RHBRMS-1388")
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testFactTypeDeclare() {
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);

		selectFromContentAssist(editor, "declare");

		Assert.fail("Check if declare keyword is present.");
	}

	@Test
	@Ignore("RHBRMS-893")
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testFactTypeUsage() {
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);
		editor.writeText("declare Person\n\tname : String\n\tage : int\nend\n");

		editor.setPosition(10, 21);
		editor.writeText("\n        ");

		selectFromContentAssist(editor, "Person");
		
		selectFromContentAssist(editor, "name");
		editor.writeText("!= null, ");
		selectFromContentAssist(editor, "age");
		editor.writeText("> 18");

		assertCorrectText(editor, "Person( name != null, age > 18 )");
	}

	@Test
	@Ignore("RHBRMS-1888")
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testQueryDeclare() {
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);
		editor.writeText("import com.sample.domain.MyMessage\n\n");

		selectFromContentAssist(editor, "query");

		editor.setPosition(4, 6);
		editor.replaceText("testQuery", 12);
		assertCorrectText(editor, "query testQuery");

		editor.setPosition(5, 1);
		editor.replaceText("\n", 12); // delete "#consequences"
		editor.setPosition(5, 1);

		selectFromContentAssist(editor, "MyMessage");
		
		assertCorrectText(editor, "MyMessage( )");
	}

	@Test
	@Ignore("RHBRMS-2103")
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testQueryUsage() {
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);
		editor.writeText("import com.sample.domain.MyMessage\n\nquery testQuery\n\tMyMessage()\nend\n");

		editor.setPosition(11, 21);
		editor.writeText("\n        ");

		selectFromContentAssist(editor, "testQuery");

		assertCorrectText(editor, "testQuery( )");
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testFunctionDeclare() {
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);
		editor.writeText("import com.sample.domain.MyMessage\n\n");
		selectFromContentAssist(editor, "function");

		editor.setPosition(4, 9);
		editor.replaceText("String formatMessage(MyMessage msg)", 27);

		assertCorrectText(editor, "function String formatMessage(MyMessage msg) {");

		editor.setPosition(5, 1);
		editor.replaceText("return m", 19);

		selectFromContentAssist(editor, "msg : MyMessage");

		editor.writeText(".");

		selectFromContentAssist(editor, "getText() : String - MyMessage");

		editor.writeText(";");

		assertCorrectText(editor, "return msg.getText();");
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testFunctionUsage() {
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);
		editor.writeText("import com.sample.domain.MyMessage\n\nfunction String formatMessage(MyMessage msg) {\n");
		editor.writeText("    return msg.getText();\n}");

		editor.setPosition(10, 8);
		editor.replaceText("$msg: MyMessage()", 13);

		editor.setPosition(12, 8);
		editor.replaceText("", 23);

		selectFromContentAssist(editor, "formatMessage()");

		editor.writeText("$msg");

		assertCorrectText(editor, "formatMessage($msg);");
	}
}
