package org.jboss.tools.esb.ui.bot.test;

import static org.junit.Assert.assertFalse;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.esb.reddeer.editor.ESBEditor;
import org.jboss.tools.esb.reddeer.wizard.ESBProjectWizard;
import org.jboss.tools.esb.reddeer.wizard.ESBProviderWizard;
import org.jboss.tools.esb.ui.bot.test.util.Assertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author apodhrad
 *
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class ProvidersTest {

	public static final String PROJECT_NAME = "esb-providers";
	public static final String ESB_FILE = "jboss-esb.xml";
	public static final String NAME_SUFFIX = "-name";

	public Logger log = Logger.getLogger(ProvidersTest.class);

	@BeforeClass
	public static void createProject() {
		new WorkbenchShell().maximize();
		new ESBProjectWizard().openWizard().setName(PROJECT_NAME).finish();
	}

	@AfterClass
	public static void deleteProject() {
		new ProjectExplorer().getProject(PROJECT_NAME).delete(true);
	}

	@Before
	public void openFile() {
		new ProjectExplorer().getProject(PROJECT_NAME).getProjectItem("esbcontent", "META-INF", ESB_FILE).open();
	}

	@After
	public void saveAndCloseEditor() {
		try {
			new ESBEditor().save();
			new ESBEditor().close();
		} catch (Exception e) {

		}
	}

	@Test
	public void busProviderTest() {
		String type = "Bus Provider";
		String name = "bus-provider";

		ESBProviderWizard wizard = new ESBEditor().addProvider(type);
		assertFalse(new PushButton("Next >").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.next();
		wizard.setChannel(name + "-channel");
		wizard.finish();

		check(name);
	}

	@Test
	public void camelProviderTest() {
		String type = "Camel Provider";
		String name = "camel-provider";

		ESBProviderWizard wizard = new ESBEditor().addProvider(type);
		assertFalse(new PushButton("Next >").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.next();
		wizard.setChannel(name + "-channel");
		wizard.finish();

		check(name);
	}

	@Test
	public void sqlProviderTest() {
		String type = "SQL Provider";
		String name = "sql-provider";

		ESBProviderWizard wizard = new ESBEditor().addProvider(type);
		assertFalse(new PushButton("Next >").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.next();
		wizard.setChannel(name + "-channel");
		wizard.finish();

		check(name);
	}

	@Test
	public void fsProviderTest() {
		String type = "FS Provider";
		String name = "fs-provider";

		ESBProviderWizard wizard = new ESBEditor().addProvider(type);
		assertFalse(new PushButton("Next >").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.next();
		wizard.setChannel(name + "-channel");
		wizard.finish();

		check(name);
	}

	@Test
	public void httpProviderTest() {
		String type = "HTTP Provider";
		String name = "http-provider";

		ESBProviderWizard wizard = new ESBEditor().addProvider(type);
		assertFalse(new PushButton("Next >").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.next();
		wizard.setChannel(name + "-channel");
		wizard.finish();

		check(name);
	}

	@Test
	public void scheduleProviderTest() {
		String type = "Schedule Provider";
		String name = "schedule-provider";

		ESBProviderWizard wizard = new ESBEditor().addProvider(type);
		assertFalse(new PushButton("Next >").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.next();
		wizard.setSchedule("Simple", "simple");
		wizard.finish();

		check(name);
	}

	@Test
	public void ftpProviderTest() {
		String type = "FTP Provider";
		String name = "ftp-provider";

		ESBProviderWizard wizard = new ESBEditor().addProvider(type);
		assertFalse(new PushButton("Next >").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setHostname("localhost");
		wizard.next();
		wizard.setChannel(name + "-channel");
		wizard.finish();

		check(name);
	}

	@Test
	public void hibernateProviderTest() {
		String type = "Hibernate Provider";
		String name = "hibernate-provider";

		ESBProviderWizard wizard = new ESBEditor().addProvider(type);
		assertFalse(new PushButton("Next >").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setCfgFile("hibernate.cfg.xml");
		wizard.next();
		wizard.setChannel(name + "-channel");
		wizard.next();
		wizard.setClassName("java.lang.Object");
		wizard.setEvent("funnyEvent");
		wizard.finish();

		check(name);
	}

	@Test
	public void jbrProviderTest() {
		String type = "JBR Provider";
		String name = "jbr-provider";

		ESBProviderWizard wizard = new ESBEditor().addProvider(type);
		assertFalse(new PushButton("Next >").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.next();
		wizard.setChannel(name + "-channel");
		wizard.setPort("8888");
		wizard.finish();

		check(name);
	}

	@Test
	public void jcaProviderTest() {
		String type = "JCA Provider";
		String name = "jms-jca-provider";

		ESBProviderWizard wizard = new ESBEditor().addProvider(type);
		assertFalse(new PushButton("Next >").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setConnectionFactory("ConnectionFactory");
		wizard.next();
		wizard.setChannel(name + "-channel");
		wizard.finish();

		check(name);
	}

	@Test
	public void jmsProviderTest() {
		String type = "JMS Provider";
		String name = "jms-provider";

		ESBProviderWizard wizard = new ESBEditor().addProvider(type);
		assertFalse(new PushButton("Next >").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setConnectionFactory("ConnectionFactory");
		wizard.next();
		wizard.setChannel(name + "-channel");
		wizard.finish();

		check(name);
	}

	private void check(String name) {
		String source = new ESBEditor().getSource();
		String xpath = "count(//jbossesb/providers/" + name + "[@name='" + name + NAME_SUFFIX + "'])=1";
		Assertions.assertXmlContentBool(source, xpath);
		Assertions.assertEmptyProblemsView("after " + name + " was added");
	}

}
