package org.jboss.tools.drools.ui.bot.test.functional.drleditor;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
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
 * TODO: window, entry-point
 */
@Runtime(type = RuntimeReqType.DROOLS)
@RunWith(RedDeerSuite.class)
public class DeclareCompletionTest extends DrlCompletionParent {

	@InjectRequirement
	private RuntimeRequirement droolsRequirement;

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testFactTypeDeclare() {
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);

		try {
			selectFromContentAssist(editor, "declare");
		} catch (AssertionError exception) {
			if (exception.getMessage().contains("Could not find 'declare' in content assist")) {
				Assert.fail("BZ1029044: No content assist for declarations");
			} else {
				throw exception;
			}
		}

		// TODO: not implemented in plugin, hard to tell what the line will look like
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testFactTypeUsage() {
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);
		editor.writeText("declare Person\n\tname : String\n\tage : int\nend\n");

		editor.setPosition(10, 21);
		editor.writeText("\n        ");

		try {
			selectFromContentAssist(editor, "Person");
		} catch (AssertionError exception) {
			if (exception.getMessage().contains("Could not find 'Person' in content assist")) {
				Assert.fail("BZ1029040: Content assist does not offer types declared in DRL file");
			} else {
				throw exception;
			}
		}
		selectFromContentAssist(editor, "name");
		editor.writeText("!= null, ");
		selectFromContentAssist(editor, "age");
		editor.writeText("> 18");

		assertCorrectText(editor, "Person( name != null, age > 18 )");
	}

	@Test
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

		try {
			selectFromContentAssist(editor, "MyMessage");
		} catch (AssertionError exception) {
			if (exception.getMessage().contains("Could not find 'MyMessage' in content assist")) {
				Assert.fail("BZ1031618: Wrong code completion for query");
			} else {
				throw exception;
			}
		}
		assertCorrectText(editor, "MyMessage( )");
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testQueryUsage() {
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);
		editor.writeText("import com.sample.domain.MyMessage\n\nquery testQuery\n\tMyMessage()\nend\n");

		editor.setPosition(11, 21);
		editor.writeText("\n        ");

		try {
			selectFromContentAssist(editor, "testQuery");
		} catch (AssertionError exception) {
			if (exception.getMessage().contains("Could not find 'testQuery' in content assist")) {
				Assert.fail("BZ1029054: Queries are not included in content assist");
			} else {
				throw exception;
			}
		}
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
