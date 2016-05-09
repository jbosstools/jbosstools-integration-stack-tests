package org.jboss.tools.drools.ui.bot.test.functional.drleditor;

import static org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType.ERROR;
import static org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType.WARNING;

import java.util.List;

import org.apache.log4j.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.drools.reddeer.editor.ContentAssist;
import org.jboss.tools.drools.reddeer.editor.DrlEditor;
import org.jboss.tools.drools.reddeer.editor.RuleEditor;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizard;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

public abstract class DrlCompletionParent extends TestParent {
	private static final Logger LOGGER = Logger.getLogger(DrlCompletionParent.class);
	public static final String MESSAGE_TEXT = getTemplateText("MyMessageClass");
	public static final String RULE_RESOURCE_TEXT = getTemplateText("DummyRuleFile");

	private int errors, warnings;
	protected DrlEditor master;

	@Before
	public void setUpDomainAndRule() {
		// create domain class
		NewJavaClassWizardDialog diag = new NewJavaClassWizardDialog();
		diag.open();
		new NewJavaClassWizardPage().setName("MyMessage");
		new NewJavaClassWizardPage().setPackage("com.sample.domain");
		diag.finish();

		TextEditor txtEditor = new TextEditor();
		txtEditor.setText(MESSAGE_TEXT);
		txtEditor.save();
		txtEditor.close();

		ProblemsView problems = new ProblemsView();
		problems.open();
		errors = problems.getProblems(ERROR).size();
		warnings = problems.getProblems(WARNING).size();

		// create RuleResource
		NewRuleResourceWizard wiz = new NewRuleResourceWizard();
		wiz.open();
		wiz.getFirstPage().setParentFolder(getRulesLocation());
		wiz.getFirstPage().setFileName(getTestName());
		wiz.getFirstPage().setRulePackageName("com.sample");
		wiz.finish();

		RuleEditor drlEditor = new DrlEditor().showRuleEditor();
		drlEditor.setText(RULE_RESOURCE_TEXT);
		drlEditor.save();
		drlEditor.close();

		master = openDrlEditor();
	}

	@After
	public void checkNoNewErrorsAndRete() {
		master.save();
		AbstractWait.sleep(TimePeriod.SHORT);

		assertNoNewProblems();
		assertWorkingReteTree();

		master.showRuleEditor();
	}

	protected void selectFromContentAssist(RuleEditor editor, String line) {
		ContentAssist assist = editor.createContentAssist();
		List<String> items = assist.getItems();

		Assert.assertTrue(String.format("Could not find '%s' in content assist (%s).", line, items),
				items.contains(line));
		assist.selectItem(line);
	}

	protected void assertCorrectText(RuleEditor editor, String text) {
		Assert.assertEquals("Wrong text inserted:", text, editor.getTextOnCurrentLine().trim());
	}

	private void assertNoNewProblems() {
		ProblemsView problems = new ProblemsView();
		problems.open();

		List<Problem> items = problems.getProblems(ERROR);
		for (Problem error : items) {
			LOGGER.debug(error.getDescription());
		}
		Assert.assertEquals("New errors occured!", errors, items.size());
		items = problems.getProblems(WARNING);
		for (Problem warning : items) {
			LOGGER.debug(warning.getDescription());
		}
		Assert.assertEquals("New warnings occured!", warnings, items.size());
	}

	private void assertWorkingReteTree() {
		DrlEditor editor = new DrlEditor();
		editor.save();

		editor.showReteTree();
		Assert.assertTrue("Rete Tree is not opened! Check log for errors.", editor.isReteTreeOpened());
	}

	private DrlEditor openDrlEditor() {
		PackageExplorer explorer = new PackageExplorer();
		explorer.open();
		explorer.getProject(DEFAULT_PROJECT_NAME).getProjectItem(getResourcePath(getTestName() + ".drl")).open();

		return new DrlEditor();
	}
}
