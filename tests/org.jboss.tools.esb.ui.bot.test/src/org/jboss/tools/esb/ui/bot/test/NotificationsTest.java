package org.jboss.tools.esb.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.esb.reddeer.editor.ESBEditor;
import org.jboss.tools.esb.reddeer.wizard.ESBProjectWizard;
import org.jboss.tools.esb.reddeer.wizard.ESBWizard;
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
public class NotificationsTest {

	public static final String PROJECT_NAME = "esb-notifications";
	public static final String SERVICE = "aaa";
	public static final String CATEGORY = "bbb";
	public static final String DESCRIPTION = "ccc";

	public static final String NOTIFIER = "notifier";
	public static final String NOTIFICATION_LIST = "list";

	public Logger log = Logger.getLogger(NotificationsTest.class);

	@BeforeClass
	public static void createProject() {
		new WorkbenchShell().maximize();
		new ESBProjectWizard().openWizard().setName(PROJECT_NAME).finish();
		new ProjectExplorer().getProject(PROJECT_NAME).getProjectItem("esbcontent", "META-INF", "jboss-esb.xml").open();
		new ESBEditor().addService(SERVICE, CATEGORY, DESCRIPTION).save();
		new ESBEditor().getService(SERVICE).addNotifier(NOTIFIER).select();

		new ContextMenu("Add Notification List...").select();
		ESBWizard wizard = new ESBWizard("Add Notification List...");
		wizard.setText("Type:*", "list");
		wizard.finish();

		new ESBEditor().save();
	}

	@AfterClass
	public static void deleteProject() {
		new ProjectExplorer().getProject(PROJECT_NAME).delete(true);
	}

	@Before
	public void openFile() {
		new ProjectExplorer().getProject(PROJECT_NAME).getProjectItem("esbcontent", "META-INF", "jboss-esb.xml").open();
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
	public void addTargetTest() {
		String target = "java.lang.Object";

		selectNotoficationList();
		new ContextMenu("New", "Target...").select();
		ESBWizard wizard = new ESBWizard("Add Target");
		wizard.setText("Class:*", target);
		wizard.finish();

		check(target);
	}

	@Test
	public void notifyConsoleTest() {
		String target = "NotifyConsole";

		selectNotoficationList();
		new ContextMenu("New", "Notify Console...").select();
		new WaitWhile(new JobIsRunning());

		check(target);
	}

	@Test
	public void notifyEmailTest() {
		String target = "NotifyEmail";

		selectNotoficationList();
		new ContextMenu("New", "Notify Email...").select();
		ESBWizard wizard = new ESBWizard("Notify Email...");
		wizard.setText("From:*", "from");
		wizard.setText("Send to:*", "to");
		wizard.setText("Subject:*", "subject");
		wizard.finish();

		check(target);

		ESBEditor editor = new ESBEditor();
		editor.selectTree();
		assertEquals(target, editor.getText("Class:"));
		assertEquals("from", editor.getText("From:"));
		assertEquals("to", editor.getText("Send to:"));
		assertEquals("subject", editor.getText("Subject:"));
		editor.setText("Host:", "afrodita");
		editor.setText("Port:", "4321");
		editor.setText("Username:", "user");
		editor.setText("Password:", "user123$");
		editor.setText("Auth:", "LDAP");
		editor.setText("Copy to:", "cc");
		editor.setText("Message:", "Hello");
		editor.setText("Message Attachment Name:", "Hello2");

		Map<String, String> map = new HashMap<String, String>();
		map.put("class", target);
		map.put("from", "from");
		map.put("sendTo", "to");
		map.put("subject", "subject");
		map.put("host", "afrodita");
		map.put("port", "4321");
		map.put("username", "user");
		map.put("password", "user123$");
		map.put("auth", "LDAP");
		map.put("ccTo", "cc");
		map.put("message", "Hello");
		map.put("msgAttachmentName", "Hello2");
		check(map);

		new ESBEditor().selectTree();
		new PushButton("Add...").click();
		new DefaultShell("Add Attachment");
		new DefaultTreeItem(PROJECT_NAME, "esbcontent", "META-INF", "jboss-esb.xml").select();
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsAvailable("Add Attachment"));
	}

	@Test
	public void notifyFilesTest() {
		String target = "NotifyFiles";

		selectNotoficationList();
		new ContextMenu("New", "Notify Files...").select();
		new WaitWhile(new JobIsRunning());

		check(target);
	}

	@Test
	public void notifyFTPTest() {
		String target = "NotifyFTP";

		selectNotoficationList();
		new ContextMenu("New", "Notify FTP...").select();
		new WaitWhile(new JobIsRunning());

		check(target);
	}

	@Test
	public void notifyFTPListTest() {
		String target = "NotifyFTPList";

		selectNotoficationList();
		new ContextMenu("New", "Notify FTP List...").select();
		new WaitWhile(new JobIsRunning());

		check(target);
	}

	@Test
	public void notifyQueuesTest() {
		String target = "NotifyQueues";

		selectNotoficationList();
		new ContextMenu("New", "Notify Queues...").select();
		new WaitWhile(new JobIsRunning());

		check(target);
	}

	@Test
	public void notifySQLTableTest() {
		String target = "NotifySQLTable";

		selectNotoficationList();
		new ContextMenu("New", "Notify SQL Table...").select();
		ESBWizard wizard = new ESBWizard("Notify SQL Table...");
		wizard.setText("Class:*", "SQLClass");
		wizard.finish();

		check(target);

		ESBEditor editor = new ESBEditor();
		editor.selectTree();
		assertEquals(target, editor.getText("Class:"));
		editor.setText("Driver Class:", "SuperDriver");
		editor.setText("Connection URL:", "db.com");
		editor.setText("User Name:", "db");
		editor.setText("Password:", "db123$");
		editor.setText("Table:", "Person");
		editor.setText("Data Column:", "name");

		Map<String, String> map = new HashMap<String, String>();
		map.put("class", target);
		map.put("driver-class", "SuperDriver");
		map.put("connection-url", "db.com");
		map.put("table", "Person");
		map.put("dataColumn", "name");
		map.put("user-name", "db");
		map.put("password", "db123$");
		check(map);
	}

	@Test
	public void notifyTCPTest() {
		String target = "NotifyTCP";

		selectNotoficationList();
		new ContextMenu("New", "Notify TCP...").select();
		new WaitWhile(new JobIsRunning());

		check(target);
	}

	@Test
	public void notifyTopicsTest() {
		String target = "NotifyTopics";

		selectNotoficationList();
		new ContextMenu("New", "Notify Topics...").select();
		new WaitWhile(new JobIsRunning());

		check(target);
	}

	private void selectNotoficationList() {
		selectNotoficationList(NOTIFICATION_LIST);
	}

	private void selectNotoficationList(String list) {
		new ESBEditor().getService(SERVICE).getItem("Actions").getItem(NOTIFIER).getItem(list).select();
	}

	private void check(String target) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("class", target);
		check(map);
	}

	private void check(Map<String, String> map) {
		String source = new ESBEditor().getSource();
		String xpath = "count(//jbossesb/services/service[@name='"
				+ SERVICE
				+ "']/actions/action[@name='"
				+ NOTIFIER
				+ "' and @class='org.jboss.soa.esb.actions.Notifier']/property[@name='destinations']/NotificationList[@type='"
				+ NOTIFICATION_LIST + "']/target[" + mapToXPath(map) + "])=1";
		Assertions.assertXmlContentBool(source, xpath);
		Assertions.assertEmptyProblemsView("after " + map.get("class") + " was added");
	}

	private String mapToXPath(Map<String, String> map) {
		StringBuffer xpath = new StringBuffer();

		boolean isFirst = true;
		for (String key : map.keySet()) {
			String value = map.get(key);
			if (isFirst) {
				isFirst = false;
			} else {
				xpath.append(" and ");
			}
			xpath.append("@" + key + "='" + value + "'");
		}
		return xpath.toString();
	}

}
