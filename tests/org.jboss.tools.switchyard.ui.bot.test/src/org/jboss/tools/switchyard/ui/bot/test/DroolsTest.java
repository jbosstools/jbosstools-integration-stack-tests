package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.editor.SimpleTextEditor;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.project.ProjectItemExt;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.view.JUnitView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Create simple Drools application, run JUnit test
 * 
 * @author lfabriko
 *
 */
@SwitchYard
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class DroolsTest {

	private static final String PROJECT = "switchyard-drools-interview";
	private static final String PACKAGE = "org.switchyard.quickstarts.drools.service";
	private static final String GROUP_ID = "org.switchyard.quickstarts.drools.service";
	private static final String PACKAGE_MAIN_JAVA = "src/main/java";
	private static final String PACKAGE_MAIN_RESOURCES = "src/main/resources";
	private static final String INTERVIEW = "Interview";
	private static final String APPLICANT = "Applicant";
	private static final String TEST = INTERVIEW + "ServiceTest.java";

	@InjectRequirement
	private SwitchYardRequirement switchyardRequirement;

	@Before
	@After
	public void closeSwitchyardFile() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}

	@Test
	public void droolsCreationTest() {
		// new project, support rules
		switchyardRequirement.project(PROJECT).impl("Rules (Drools)").groupId(GROUP_ID).packageName(PACKAGE).create();
		// open sy.xml, add rules, service, promote
		new SwitchYardEditor().addDroolsImplementation().setFileName(INTERVIEW + ".drl")
				.createNewInterface(INTERVIEW + "Service").finish();
		new Service(INTERVIEW + "Service").promoteService().setServiceName(INTERVIEW + "ServiceMain").finish();

		// insert rules
		openFile(PACKAGE_MAIN_RESOURCES, INTERVIEW + ".drl");
		new SimpleTextEditor(INTERVIEW + ".drl").deleteLineWith("Interview").deleteLineWith("System.out.println")
				.deleteLineWith("global").type("global java.lang.String userName").newLine()
				.type("rule \"Is of valid age\"").typeAfter("when", "$a : Applicant( age > 17 )")
				.typeAfter("then", "$a.setValid( true );").newLine()
				.type("System.out.println(\"********** \" + $a.getName() + \" is a valid applicant **********\");")
				.typeAfter("end", "rule \"Is not of valid age\"").newLine().type("when").newLine()
				.type("$a : Applicant( age < 18 )").newLine().type("then").newLine().type("$a.setValid( false );")
				.newLine().type("System.out.println(\"***\" + $a.getName() + \" is not a valid applicant ***\");")
				.newLine().type("end");

		// insert code
		openFile(PACKAGE_MAIN_JAVA, PACKAGE, INTERVIEW + "Service.java");
		new SimpleTextEditor(INTERVIEW + "Service.java").typeAfter("interface", "public void verify(Applicant applicant);");

		createPojo(APPLICANT);
		new SimpleTextEditor(APPLICANT + ".java")
				.typeAfter("class", "private String name; private int age; private boolean valid;").newLine()
				.type("public Applicant(String name, int age){this.name=name;this.age=age;}")
				.generateGettersSetters("name");

		// test
		new SwitchYardEditor();
		AbstractWait.sleep(TimePeriod.SHORT);
		new Service(INTERVIEW + "Service").createNewServiceTestClass();
		new SimpleTextEditor(TEST).deleteLineWith("null").type("Applicant message=new Applicant(\"Twenty\", 20);")
				.deleteLineWith("Implement").type("message=new Applicant(\"Ten\", 10);")
				.type("service.operation(\"verify\").sendInOnly(message);");

		new ShellMenu("File", "Save All").select();

		ProjectItem item = new SwitchYardProject(PROJECT).getProjectItem("src/test/java", PACKAGE, TEST);
		new ProjectItemExt(item).runAsJUnitTest();
		AbstractWait.sleep(TimePeriod.LONG);
		assertEquals("1/1", new JUnitView().getRunStatus());
		assertEquals(0, new JUnitView().getNumberOfErrors());
		assertEquals(0, new JUnitView().getNumberOfFailures());
	}

	private void openFile(String... file) {
		new SwitchYardProject(PROJECT).getProjectItem(file).open();
	}

	private void createPojo(String name) {
		NewJavaClassWizardDialog wizard = new NewJavaClassWizardDialog();
		wizard.open();
		NewJavaClassWizardPage page = new NewJavaClassWizardPage();
		page.setName(name);
		new LabeledText("Source folder:").setText(PROJECT + "/" + PACKAGE_MAIN_JAVA);
		page.setPackage(PACKAGE);
		wizard.finish();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
}
