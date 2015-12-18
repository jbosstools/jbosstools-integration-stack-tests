package org.jboss.tools.esb.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.esb.reddeer.editor.ESBEditor;
import org.jboss.tools.esb.reddeer.wizard.ESBListenerWizard;
import org.jboss.tools.esb.reddeer.wizard.ESBProjectWizard;
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
public class ListenersTest {

	public static final String PROJECT_NAME = "esb-listeners";
	public static final String ESB_FILE = "jboss-esb.xml";
	public static final String NAME_SUFFIX = "-name";
	public static final String SERVICE = "aaa";

	public Logger log = Logger.getLogger(ListenersTest.class);

	@BeforeClass
	public static void createProject() {
		new WorkbenchShell().maximize();
		new ESBProjectWizard().openWizard().setName(PROJECT_NAME).finish();
		new ProjectExplorer().getProject(PROJECT_NAME).getProjectItem("esbcontent", "META-INF", ESB_FILE).open();
		new ESBEditor().addService(SERVICE, "bbb", "ccc");
		new ESBEditor().save();
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
	public void listenerTest() {
		String type = "Listener";
		String name = "listener";

		ESBListenerWizard wizard = new ESBEditor().addListenerToService(SERVICE, type);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.finish();

		check(name);
	}

	@Test
	public void camelGatewayTest() {
		String type = "Camel Gateway";
		String name = "camel-gateway";

		ESBListenerWizard wizard = new ESBEditor().addListenerToService(SERVICE, type);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.finish();

		check(name);
	}

	@Test
	public void fptListenerTest() {
		String type = "FTP Listener";
		String name = "ftp-listener";

		ESBListenerWizard wizard = new ESBEditor().addListenerToService(SERVICE, type);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.finish();

		check(name);
	}

	@Test
	public void fsListenerTest() {
		String type = "FS Listener";
		String name = "fs-listener";

		ESBListenerWizard wizard = new ESBEditor().addListenerToService(SERVICE, type);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.finish();

		check(name);
	}

	@Test
	public void groovyListenerTest() {
		String type = "Groovy Listener";
		String name = "groovy-listener";

		ESBListenerWizard wizard = new ESBEditor().addListenerToService(SERVICE, type);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setScript("test.groovy");
		wizard.finish();

		check(name);
	}

	@Test
	public void hibernateListenerTest() {
		String type = "Hibernate Listener";
		String name = "hibernate-listener";

		ESBListenerWizard wizard = new ESBEditor().addListenerToService(SERVICE, type);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.finish();

		check(name);
	}

	@Test
	public void httpGatewayTest() {
		String type = "HTTP Gateway";
		String name = "http-gateway";

		ESBListenerWizard wizard = new ESBEditor().addListenerToService(SERVICE, type);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.finish();

		check(name);
	}

	@Test
	public void jbrListenerTest() {
		String type = "JBR Listener";
		String name = "jbr-listener";

		ESBListenerWizard wizard = new ESBEditor().addListenerToService(SERVICE, type);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.finish();

		check(name);
	}

	@Test
	public void jmsListenerTest() {
		String type = "JMS Listener";
		String name = "jms-listener";

		ESBListenerWizard wizard = new ESBEditor().addListenerToService(SERVICE, type);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.finish();

		check(name);
	}

	@Test
	public void jcaGatewayTest() {
		String type = "JCA Gateway";
		String name = "jca-gateway";

		ESBListenerWizard wizard = new ESBEditor().addListenerToService(SERVICE, type);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setAdapter("test.adapter");
		wizard.setEndpointClass("java.lang.Object");
		wizard.finish();

		check(name);
	}

	@Test
	public void scheduledListenerTest() {
		String type = "Scheduled Listener";
		String name = "scheduled-listener";

		ESBListenerWizard wizard = new ESBEditor().addListenerToService(SERVICE, type);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.finish();

		check(name);
	}

	@Test
	public void sqlListenerTest() {
		String type = "SQL Listener";
		String name = "sql-listener";

		ESBListenerWizard wizard = new ESBEditor().addListenerToService(SERVICE, type);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.finish();

		check(name);
	}

	@Test
	public void udpListenerTest() {
		String type = "UDP Listener";
		String name = "udp-listener";

		ESBListenerWizard wizard = new ESBEditor().addListenerToService(SERVICE, type);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setHost("localhost");
		wizard.setPort("123");
		wizard.finish();

		check(name);
	}

	private void check(String name) {
		String source = new ESBEditor().getSource();
		String xpath = "count(//jbossesb/services/service[@name='" + SERVICE + "']/listeners/" + name + "[@name='"
				+ name + NAME_SUFFIX + "'])=1";
		Assertions.assertXmlContentBool(source, xpath);
		Assertions.assertEmptyProblemsView("after " + name + " was added");

		/*
		 * Added test for - https://issues.jboss.org/browse/JBQA-6527 - Add
		 * support for gateway messaging priority in the ESB editor
		 */
		if (name.contains("gateway")) {
			Combo combo = new ESBEditor().getMessageFlowPriorityCombo();
			assertTrue(combo.getSelectionIndex() == -1);
			List<String> theItems = combo.getItems();
			assertTrue(theItems.size() == 11);

			assertEquals("0", theItems.get(1));
			assertEquals("1", theItems.get(2));
			assertEquals("2", theItems.get(3));
			assertEquals("3", theItems.get(4));
			assertEquals("4", theItems.get(5));
			assertEquals("5", theItems.get(6));
			assertEquals("6", theItems.get(7));
			assertEquals("7", theItems.get(8));
			assertEquals("8", theItems.get(9));
			assertEquals("9", theItems.get(10));

			new ESBEditor().setMessageFlowPriority("7").save();
			assertEquals("7", new ESBEditor().getMessageFlowPriority());
		}
	}

}
