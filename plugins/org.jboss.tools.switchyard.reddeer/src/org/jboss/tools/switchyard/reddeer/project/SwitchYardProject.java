package org.jboss.tools.switchyard.reddeer.project;

import java.io.File;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.shell.ProjectCapabilitiesShell;

public class SwitchYardProject extends Project {

	public static final String TEST_SOURCE = "src/test/java";

	public SwitchYardProject(String projectName) {
		super(findTreeItem(projectName));
	}

	protected static TreeItem findTreeItem(String projectName) {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		return projectExplorer.getProject(projectName).getTreeItem();
	}

	public SwitchYardEditor openSwitchYardFile() {
		getProjectItem("SwitchYard").open();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		return new SwitchYardEditor();
	}

	public ProjectItemExt getTestClass(String... path) {
		String[] newPath = new String[path.length + 1];
		newPath[0] = TEST_SOURCE;
		for (int i = 0; i < path.length; i++) {
			newPath[i + 1] = path[i];
		}
		ProjectItem projectItem = getProjectItem(newPath);
		return new ProjectItemExt(projectItem);
	}

	public void update() {
		select();
		new ContextMenu("Maven", "Update Project...").select();
		new DefaultShell("Update Maven Project");
		new CheckBox("Force Update of Snapshots/Releases").toggle(true);
		new PushButton("OK").click();

		AbstractWait.sleep(TimePeriod.NORMAL);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}

	public File getFile() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		

		return new File(new File(root.getLocationURI().getPath()), getName());
	}

	public ProjectCapabilitiesShell configureCapabilities() {
		AbstractWait.sleep(TimePeriod.SHORT);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		select();
		new ContextMenu("SwitchYard", "Configure Capabilities...").select();
		return new ProjectCapabilitiesShell("Properties for " + getName());
	}

}
