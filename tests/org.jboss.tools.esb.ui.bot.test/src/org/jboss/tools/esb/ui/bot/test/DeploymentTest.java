package org.jboss.tools.esb.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesDialog;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.esb.reddeer.editor.ESBEditor;
import org.jboss.tools.esb.reddeer.widget.SelectableFormLabel;
import org.jboss.tools.esb.reddeer.wizard.ESBActionWizard;
import org.jboss.tools.esb.reddeer.wizard.ESBProjectWizard;
import org.jboss.tools.esb.ui.bot.test.requirement.ESBRequirement;
import org.jboss.tools.esb.ui.bot.test.requirement.ESBRequirement.ESB;
import org.jboss.tools.esb.ui.bot.test.requirement.ServerRequirement;
import org.jboss.tools.esb.ui.bot.test.requirement.ServerRequirement.Server;
import org.jboss.tools.esb.ui.bot.test.util.HttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author apodhrad
 * 
 */
@ESB
@Server
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class DeploymentTest {

	public static final String PROJECT = "esb-deployment";

	public Logger log = Logger.getLogger(DeploymentTest.class);

	@InjectRequirement
	private ESBRequirement esbRequirement;

	@InjectRequirement
	private ServerRequirement serverRequirement;

	@Before
	public void startServer() {
		getServer().start();
	}

	@After
	public void stopServer() {
		getServer().stop();
	}

	@After
	public void deleteProject() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		for (Project project : projectExplorer.getProjects()) {
			project.delete(true);
		}
	}

	@Test
	public void deploymentTest() {
		new WorkbenchShell().maximize();

		ESBProjectWizard projectWizard = new ESBProjectWizard();
		projectWizard.open();
		projectWizard.setName(PROJECT);
		projectWizard.setServer(serverRequirement.getName());
		projectWizard.setVersion(esbRequirement.getVersion());
		projectWizard.finish();

		ESBEditor editor = new ESBEditor();
		editor.save();
		editor.addHttpProvider("http-provider", "http-channel");
		editor.addService("hello", "example", "description");
		editor.getService("hello").setInvmScope("GLOBAL");
		editor.getService("hello").addHttpListener("http-provider", "http-channel");
		new SWTWorkbenchBot().textWithLabel("URL Pattern:").setText("hello");

		ESBActionWizard actionWizard = editor.addCustomActionToService("hello");
		actionWizard.setName("SayHello");
		new SelectableFormLabel("Class:*").select();
		new DefaultShell("New ESB Action");
		new LabeledText("Name:").setText("Hello");
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsAvailable("New ESB Action"));
		actionWizard.ok();

		TextEditor textEditor = new TextEditor("Hello.java");
		int lineIndex = 0;
		for (int i = 0; i < 20; i++) {
			String line = textEditor.getTextAtLine(i);
			if (line.contains("ADD CUSTOM ACTION")) {
				lineIndex = i;
				break;
			}
		}
		assertTrue("Cannot find text 'ADD CUSTOM ACTION'", lineIndex > 0);
		textEditor.insertLine(lineIndex, "message.getBody().add(\"Hello ESB\");");
		textEditor.save();

		new ESBEditor().save();

		ModifyModulesDialog addWizard = getServer().addAndRemoveModules();
		new DefaultShell("Add and Remove...");
		new DefaultTreeItem(PROJECT).select();
		new PushButton("Add >").click();
		AbstractWait.sleep(TimePeriod.SHORT);
		addWizard.finish();
		new WaitWhile(new ShellWithTextIsAvailable("Add and Remove..."));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitUntil(new ConsoleHasText("Starting ESB Deployment '" + PROJECT + ".esb'"), TimePeriod.LONG);

		try {
			assertEquals("Hello ESB", HttpClient.send("http://localhost:8080/" + PROJECT + "/http/hello"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private org.jboss.reddeer.eclipse.wst.server.ui.view.Server getServer() {
		ServersView serversView = new ServersView();
		serversView.open();
		return serversView.getServer(serverRequirement.getName());
	}
}
