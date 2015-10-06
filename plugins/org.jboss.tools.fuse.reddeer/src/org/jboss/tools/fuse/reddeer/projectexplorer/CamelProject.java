package org.jboss.tools.fuse.reddeer.projectexplorer;

import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.fuse.reddeer.wizard.CamelXmlFileWizard;

/**
 * Manipulates with Camel projects
 * 
 * @author apodhrad, tsedmik
 */
public class CamelProject {

	private Project project;

	public CamelProject(String name) {

		project = new ProjectExplorer().getProject(name);
	}

	public void selectProjectItem(String... path) {
		project.getProjectItem(path).select();
	}

	public void openCamelContext(String name) {

		ProjectItem item = project.getProjectItem("src/main/resources", "META-INF", "spring");
		item.getChild(name).open();
	}

	public void deleteCamelContext(String name) {

		project.getProjectItem("src/main/resources", "META-INF", "spring", name).delete();
	}

	public void createCamelContext(String name) {

		project.getProjectItem("src/main/resources", "META-INF", "spring").select();
		new CamelXmlFileWizard().openWizard().setName(name).finish();
	}

	public void selectCamelContext(String name) {

		project.getProjectItem("src/main/resources", "META-INF", "spring", name).select();
	}

	public void runCamelContext() {

		project.getProjectItem("Camel Contexts").getChildren().get(0).select();
		try {
			new ContextMenu("Run As", "2 Local Camel Context").select();
		} catch (SWTLayerException|CoreLayerException ex) {
			new ContextMenu("Run As", "1 Local Camel Context").select();
		}
		new WaitUntil(new ConsoleHasText("(CamelContext: camel-1) started"), TimePeriod.getCustom(30));
	}

	public void runCamelContext(String name) {

		project.getProjectItem("src/main/resources", "META-INF", "spring", name).select();
		try {
			new ContextMenu("Run As", "2 Local Camel Context").select();
		} catch (SWTLayerException|CoreLayerException ex) {
			new ContextMenu("Run As", "1 Local Camel Context").select();
		}
		new WaitUntil(new ConsoleHasText("Total 1 routes, of which 1 is started."), TimePeriod.getCustom(300));
	}

	public void runCamelContextWithoutTests(String name) {

		project.getProjectItem("src/main/resources", "META-INF", "spring", name).select();
		new ContextMenu("Run As", "3 Local Camel Context (without tests)").select();
		new WaitUntil(new ConsoleHasText("Total 1 routes, of which 1 is started."), TimePeriod.getCustom(300));
	}

	public void runApplicationContextWithoutTests(String name) {

		project.getProjectItem("src", "main", "webapp", "WEB-INF", name).select();
		new ContextMenu("Run As", "3 Local Camel Context (without tests)").select();
	}

	public void debugCamelContext(String name) {

		project.getProjectItem("src/main/resources", "META-INF", "spring", name).select();
		new ContextMenu("Debug As", "2 Local Camel Context").select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		closePerspectiveSwitchWindow();
	}

	public void debugCamelContextWithoutTests(String name) {

		new ProjectExplorer().open();
		project.getProjectItem("src/main/resources", "META-INF", "spring", name).select();
		new ContextMenu("Debug As", "3 Local Camel Context (without tests)").select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		closePerspectiveSwitchWindow();
	}

	public CamelProject(Project project) {

		this.project = project;
	}

	public Project getProject() {

		return project;
	}

	public void deleteProject() {

		new ProjectExplorer().getProject(project.getName()).delete(true);
	}

	/**
	 * Tries to close 'Confirm Perspective Switch' window. This window is
	 * appeared after debugging is started.
	 */
	private void closePerspectiveSwitchWindow() {

		for (int i = 0; i < 5; i++) {
			if (new ShellWithTextIsAvailable("Confirm Perspective Switch").test()) {
				new DefaultShell("Confirm Perspective Switch");
				new CheckBox("Remember my decision").toggle(true);
				new PushButton("No").click();
			}
			AbstractWait.sleep(TimePeriod.SHORT);
		}
	}
}
