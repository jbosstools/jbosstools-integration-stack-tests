package org.jboss.tools.drools.ui.bot.test.functional;

import java.util.List;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.drools.reddeer.editor.ContentAssist;
import org.jboss.tools.drools.reddeer.editor.DslEditor;
import org.jboss.tools.drools.reddeer.editor.DslrEditor;
import org.jboss.tools.drools.reddeer.editor.RuleEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.reddeer.wizard.NewDslWizard;
import org.jboss.tools.drools.reddeer.wizard.NewDslWizardPage;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizard;
import org.jboss.tools.drools.ui.bot.test.util.OpenUtility;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UsePerspective;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeReqType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@Runtime(type = RuntimeReqType.DROOLS)
@RunWith(RedDeerSuite.class)
public class DslrEditorTest extends TestParent {

	@InjectRequirement
	private RuntimeRequirement droolsRequirement;

	@Before
	public void createDsl() {
		{ // create *.dsl file
			NewDslWizard wizard = new NewDslWizard();
			wizard.open();
			NewDslWizardPage page = new NewDslWizardPage();
			page.setParentFolder(getRulesLocation());
			page.setFileName(getTestName());
			wizard.finish();

			// close dsl editor
			new DslEditor().close();

			// open in text editor
			OpenUtility.openResourceWith("Text Editor", DEFAULT_PROJECT_NAME, getResourcePath(getTestName() + ".dsl"));

			// put a dsl definition in text editor, save and close
			TextEditor editor = new TextEditor();
			editor.setText(getTemplateText("dslr/DslFile"));
			editor.save();
			editor.close();
		}
		{ // create *.dslr file
			NewRuleResourceWizard wizard = new NewRuleResourceWizard();
			wizard.open();
			wizard.getFirstPage().setParentFolder(getRulesLocation());
			wizard.getFirstPage().setRulePackageName("com.redhat");
			wizard.getFirstPage().setFileName(getTestName());
			wizard.getFirstPage().setUseDSL(true);
			wizard.finish();

			// put a default dslr definition in rule editor, save and close
			DslrEditor dslr = new DslrEditor();
			RuleEditor editor = dslr.showRuleEditor();
			editor.setText(String.format(getTemplateText("dslr/DslrTestFile"), getTestName()));
			editor.close(true);
		}
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testTranslation() {
		DslrEditor editor = openTestDslr();

		String translation = editor.showDrlViewer().getText();

		Assert.assertEquals("Wrong text in translation!", getTemplateText("dslr/DslrTestTransformation"), translation);
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
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

		Assert.assertEquals("Known issue: No DSL files were found/proposed", 1, dsls);
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testKeywordCompletion() {
		RuleEditor editor = openTestDslr().showRuleEditor();
		editor.setPosition(18, 0);

		ContentAssist assist = editor.createContentAssist();
		List<String> items = assist.getItems();

		Assert.assertTrue("Known issue: DSL for rule keyword was not proposed", items.contains("pravidlo"));

		assist.selectItem("pravidlo");
		Assert.assertEquals("Wrong text was inserted", "pravidlo \"new rule\"", editor.getTextOnCurrentLine());
		editor.close(true);

		RuleEditor drl = openTestDslr().showDrlViewer();
		drl.setPosition(16, 0);
		Assert.assertEquals("Wrong translation", "rule \"new rule\"", drl.getTextOnCurrentLine().trim());
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testConditionCompletion() {
		RuleEditor editor = openTestDslr().showRuleEditor();
		editor.setPosition(21, 8);

		ContentAssist assist = editor.createContentAssist();
		List<String> items = assist.getItems();

		Assert.assertEquals("Wrong number of items in the list", 1, items.size());
		assist.selectItem("There is an? {entity}");
		Assert.assertEquals("Wrong text inserted", "There is an? {entity}", editor.getTextOnCurrentLine().trim());

		editor.setPosition(21, 18);
		editor.replaceText(" Person", 11);

		Assert.assertEquals("Wrong text", "There is a Person", editor.getTextOnCurrentLine().trim());
		editor.close(true);

		RuleEditor drl = openTestDslr().showDrlViewer();
		drl.setPosition(19, 0);
		Assert.assertEquals("Wrong translation", "$person: Person()", drl.getTextOnCurrentLine().trim());
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testConstraintsCompletion() {
		RuleEditor editor = openTestDslr().showRuleEditor();
		editor.setPosition(21, 8);
		editor.writeText("There is a Person\n            ");

		ContentAssist assist = editor.createContentAssist();
		List<String> items = assist.getItems();

		Assert.assertEquals("Wrong number of items in the list", 3, items.size());
		assist.selectItem("- with an? {attr} greater than {amount}");
		Assert.assertEquals("Wrong text inserted", "- with an? {attr} greater than {amount}",
				editor.getTextOnCurrentLine().trim());

		editor.setPosition(22, 21);
		editor.replaceText(" age", 8);

		editor.setPosition(22, 39);
		editor.replaceText("18", 8);

		Assert.assertEquals("Wrong text", "- with an age greater than 18", editor.getTextOnCurrentLine().trim());
		editor.close(true);

		RuleEditor drl = openTestDslr().showDrlViewer();
		drl.setPosition(19, 0);
		Assert.assertEquals("Wrong translation", "$person: Person(age > 18)", drl.getTextOnCurrentLine().trim());
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
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
		editor.save();

		master.showRuleEditor();
		RuleEditor drl = master.showDrlViewer();
		drl.setPosition(21, 0);
		Assert.assertEquals("Wrong translation", "System.out.println(\"Hello world!\");",
				drl.getTextOnCurrentLine().trim());
	}

	private DslrEditor openTestDslr() {
		OpenUtility.openResource(DEFAULT_PROJECT_NAME, getResourcePath(getTestName() + ".dslr"));

		// this has to be done to allow DRL Viewer to work properly
		DslrEditor editor = new DslrEditor();
		editor.showRuleEditor();
		editor.showDrlViewer();
		editor.showRuleEditor();

		return editor;
	}
}
