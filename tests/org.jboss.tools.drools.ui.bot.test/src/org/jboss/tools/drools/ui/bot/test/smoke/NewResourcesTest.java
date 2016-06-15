package org.jboss.tools.drools.ui.bot.test.smoke;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.reddeer.editor.DrlEditor;
import org.jboss.tools.drools.reddeer.wizard.NewDslSamplesWizardPage;
import org.jboss.tools.drools.reddeer.wizard.NewDslWizard;
import org.jboss.tools.drools.reddeer.wizard.NewDslWizardPage;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizard;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizardPage;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizardPage.RuleResourceType;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
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
public class NewResourcesTest extends TestParent {

	private static final Pattern RULE_PATTERN = Pattern.compile("(?s)rule.*?when.*?then.*?end");

	@InjectRequirement
	private RuntimeRequirement droolsRequirement;

	@Test
	@UsePerspective(JavaPerspective.class)
	@UseDefaultProject
	public void testNewDRL() {
		final String drlName = getMethodName();

		createRuleResource(drlName, RuleResourceType.rulePackage, false);

		Project project = openDefaultProject();

		Assert.assertTrue("Rule resource was not created", project.containsItem(getResourcePath(drlName + ".drl")));

		String drlText = new DrlEditor().showRuleEditor().getText();

		Assert.assertTrue("Wrong package declaration.", drlText.contains("package " + DEFAULT_PACKAGE_NAME));

		Matcher m = RULE_PATTERN.matcher(drlText);

		Assert.assertTrue("No rule present in file", m.find());
		Assert.assertTrue("Only one rule present in file", m.find());
		Assert.assertFalse("More than two rules present in file", m.find());
	}

	@Test
	@UsePerspective(JavaPerspective.class)
	@UseDefaultProject
	public void testNewIndividualRule() {
		final String drlName = getMethodName();

		createRuleResource(drlName, RuleResourceType.individualRule, false);

		Project project = openDefaultProject();

		Assert.assertTrue("Rule resource was not created", project.containsItem(getResourcePath(drlName + ".drl")));

		String drlText = new DrlEditor().showRuleEditor().getText();

		Assert.assertTrue("Wrong package declaration.", drlText.contains("package " + DEFAULT_PACKAGE_NAME));

		Matcher m = RULE_PATTERN.matcher(drlText);

		Assert.assertTrue("No rule present in file", m.find());
		Assert.assertFalse("More than one rules present in file", m.find());
	}

	@Test
	@UsePerspective(JavaPerspective.class)
	@UseDefaultProject
	public void testNewDsl() {
		final String dslName = getMethodName();

		createDslResource(dslName);

		PackageExplorer packageExplorer = new PackageExplorer();
		Assert.assertTrue(packageExplorer.getProject(DEFAULT_PROJECT_NAME).containsItem(getResourcePath(dslName + ".dsl")));
	}

	@Test
	@UsePerspective(JavaPerspective.class)
	@UseDefaultProject
	public void testNewDslr() {
		final String dslrName = getMethodName();

		createRuleResource(dslrName, RuleResourceType.rulePackage, true);

		Project project = openDefaultProject();
		
		Assert.assertTrue("Rule resource was not created", project.containsItem(getResourcePath(dslrName + ".dslr")));

		String text = new DrlEditor().showRuleEditor().getText();

		Assert.assertTrue("Wrong package declaration.", text.contains("package " + DEFAULT_PACKAGE_NAME));

		Matcher m = RULE_PATTERN.matcher(text);

		Assert.assertTrue("No expander definition found!", Pattern.compile("expander .*\\.dsl").matcher(text).find());
		Assert.assertTrue("No rule present in file", m.find());
		Assert.assertTrue("Only one rule present in file", m.find());
		Assert.assertFalse("More than two rules present in file", m.find());
	}

	private void createRuleResource(String ruleFileName, RuleResourceType type, boolean useDsl) {
		NewRuleResourceWizard wizard = new NewRuleResourceWizard();
		wizard.open();
		NewRuleResourceWizardPage page = wizard.getFirstPage();
		page.setParentFolder(getRulesLocation());
		page.setFileName(ruleFileName);
		page.setRulePackageName(DEFAULT_PACKAGE_NAME);
		page.setTypeOfRuleResource(type);
		page.setUseDSL(useDsl);
		wizard.finish();
	}

	private void createDslResource(String dslName) {
		NewDslWizard wizard = new NewDslWizard();
		wizard.open();
		NewDslWizardPage page = new NewDslWizardPage();
		page.setParentFolder(getRulesLocation());
		page.setFileName(dslName);
		wizard.next();
		NewDslSamplesWizardPage samplePage = new NewDslSamplesWizardPage();
		samplePage.setAddSampleDsl(true);
		wizard.finish();
	}

	private Project openDefaultProject() {
		PackageExplorer packageExplorer = new PackageExplorer();
		packageExplorer.open();
		return packageExplorer.getProject(DEFAULT_PROJECT_NAME);
	}
}
