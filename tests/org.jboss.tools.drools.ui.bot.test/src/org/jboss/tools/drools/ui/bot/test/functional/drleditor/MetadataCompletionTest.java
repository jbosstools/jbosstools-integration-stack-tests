package org.jboss.tools.drools.ui.bot.test.functional.drleditor;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassCreationWizard;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.execution.annotation.RunIf;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed.Jira;
import org.jboss.tools.drools.reddeer.editor.ContentAssist;
import org.jboss.tools.drools.reddeer.editor.DrlEditor;
import org.jboss.tools.drools.reddeer.editor.RuleEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UsePerspective;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeImplementationRestriction;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeImplementationType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.Test;
import org.junit.runner.RunWith;

@Runtime
@RunWith(RedDeerSuite.class)
public class MetadataCompletionTest extends DrlCompletionParent {

	@InjectRequirement
	private RuntimeRequirement droolsRequirement;
	
	@RequirementRestriction
	public static RequirementMatcher getRequirementMatcher() {
		return new RuntimeImplementationRestriction(RuntimeImplementationType.DROOLS);
	}

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
		NewClassCreationWizard diag = new NewClassCreationWizard();
		diag.open();
		new NewClassWizardPage(diag).setName("TestDomainClass");
		new NewClassWizardPage(diag).setPackage("com.sample.domain");
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
	@Jira("RHBRMS-642")
	@RunIf(conditionClass = IssueIsClosed.class)
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testGlobalCodeCompletion() {
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);
		editor.writeText("import java.util.List\n\n");

		selectFromContentAssist(editor, "global");
		
		selectFromContentAssist(editor, "List");
		
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
		final String jdkAddObjectLine = "add(Object e) : boolean - List";
		final String openjdkAddObjectLine = "add(Object arg0) : boolean - List";
		
		RuleEditor editor = master.showRuleEditor();
		editor.setPosition(2, 0);
		editor.writeText("import java.util.List\n\nglobal List list");

		editor.setPosition(10, 31);
		editor.writeText("\n        ");
		selectFromContentAssist(editor, "list : List");

		editor.writeText(".");
		
		// open JDK workaround
		ContentAssist contentAssist = editor.createContentAssist();
		WaitUntil.sleep(TimePeriod.SHORT);
		
		if (contentAssist.getItems().contains(jdkAddObjectLine)) {
			contentAssist.close();
			selectFromContentAssist(editor, jdkAddObjectLine);
		} else {
			contentAssist.close();
			selectFromContentAssist(editor, openjdkAddObjectLine);
		}
		editor.writeText("$s");

		editor.setPosition(11, 20);
		editor.writeText(";");

		assertCorrectText(editor, "list.add($s);");
	}
}
