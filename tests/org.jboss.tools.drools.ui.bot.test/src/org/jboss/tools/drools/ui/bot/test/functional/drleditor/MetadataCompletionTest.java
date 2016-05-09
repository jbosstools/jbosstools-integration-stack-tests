package org.jboss.tools.drools.ui.bot.test.functional.drleditor;

import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.drools.reddeer.editor.DrlEditor;
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

@Runtime(type = RuntimeReqType.DROOLS)
@RunWith(RedDeerSuite.class)
public class MetadataCompletionTest extends DrlCompletionParent {

	@InjectRequirement
	private RuntimeRequirement droolsRequirement;

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testImportCodeCompletion() {
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);

		selectFromContentAssist(editor, "import");

		editor.writeText("my");
		selectFromContentAssist(editor, "MyMessage - com.sample.domain");

		assertCorrectText(editor, "import com.sample.domain.MyMessage");
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testStarImportUsage() {
		NewJavaClassWizardDialog diag = new NewJavaClassWizardDialog();
		diag.open();
		new NewJavaClassWizardPage().setName("TestDomainClass");
		new NewJavaClassWizardPage().setPackage("com.sample.domain");
		new LabeledText("Source folder:").setText(DEFAULT_PROJECT_NAME + "/src/main/java");
		diag.finish();

		new TextEditor().close();

		master = new DrlEditor();
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);
		editor.writeText("import com.sample.domain.*");

		editor.setPosition(6, 21);
		editor.writeText("\n        ");

		selectFromContentAssist(editor, "TestDomainClass");
		assertCorrectText(editor, "TestDomainClass(  )");

		editor.setPosition(7, 27);
		editor.writeText("\n        ");

		selectFromContentAssist(editor, "MyMessage");
		assertCorrectText(editor, "MyMessage(  )");
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testImportFunctionUsage() {
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);
		editor.writeText("import static java.lang.String.format");

		editor.setPosition(8, 31);
		editor.writeText("\n        ");

		selectFromContentAssist(editor, "format()");
		editor.writeText("\"formatted text '%s'\", $s");

		assertCorrectText(editor, "format(\"formatted text '%s'\", $s);");
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testGlobalCodeCompletion() {
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);
		editor.writeText("import java.util.List\n\n");

		selectFromContentAssist(editor, "global");
		
		try {
			selectFromContentAssist(editor, "List");
		} catch (AssertionError exception) {
			if (exception.getMessage().contains("Could not find 'List' in content assist")) {
				Assert.fail("BZ1029429: No code completion after 'global'");
			} else {
				throw exception;
			}
		}
		
		editor.writeText("list");

		assertCorrectText(editor, "global List list");
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testGlobalUsageCondition() {
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);
		editor.writeText("import java.util.List\n\nglobal List list");

		editor.setPosition(8, 20);
		editor.writeText("this memberOf ");
		selectFromContentAssist(editor, "list");

		assertCorrectText(editor, "$s : String(this memberOf list)");
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testGlobalUsageConsequence() {
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);
		editor.writeText("import java.util.List\n\nglobal List list");

		editor.setPosition(10, 31);
		editor.writeText("\n        ");
		selectFromContentAssist(editor, "list : List");

		editor.writeText(".");
		selectFromContentAssist(editor, "add(Object e) : boolean - List");
		editor.writeText("$s");

		editor.setPosition(11, 20);
		editor.writeText(";");

		assertCorrectText(editor, "list.add($s);");
	}
}
