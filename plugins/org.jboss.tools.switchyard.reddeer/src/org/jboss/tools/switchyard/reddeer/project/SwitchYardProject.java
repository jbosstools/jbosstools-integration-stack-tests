package org.jboss.tools.switchyard.reddeer.project;

import java.io.File;

import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.reddeer.common.exception.RedDeerException;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.direct.preferences.PreferencesUtil;
import org.eclipse.reddeer.eclipse.core.resources.MavenProject;
import org.eclipse.reddeer.eclipse.core.resources.ProjectItem;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.swt.api.MenuItem;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.editor.XPathEvaluator;
import org.jboss.tools.switchyard.reddeer.properties.ProjectProperties;
import org.jboss.tools.switchyard.reddeer.shell.ProjectCapabilitiesShell;
import org.w3c.dom.Node;

public class SwitchYardProject extends MavenProject {

	public static final String MAIN_JAVA = "src/main/java";
	public static final String MAIN_RESOURCE = "src/main/resources";
	public static final String TEST_JAVA = "src/test/java";
	public static final String TEST_RESOURCE = "src/test/resources";

	public SwitchYardProject(String projectName) {
		super(findTreeItem(projectName));
	}

	protected static TreeItem findTreeItem(String projectName) {
		new WorkbenchShell().setFocus();
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		return projectExplorer.getProject(projectName).getTreeItem();
	}

	public SwitchYardEditor openSwitchYardFile() {
		getProjectItem("SwitchYard").open();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitUntil(new ShellIsAvailable("Progress Information"), false);
		new WaitWhile(new ShellIsAvailable("Progress Information"), TimePeriod.LONG);
		return new SwitchYardEditor();
	}

	public TextEditor createJavaInterface(String pkg, String name) {
		getProjectItem(MAIN_JAVA).select();
		select();
		new ShellMenuItem(new WorkbenchShell(), "File", "New", "Other...").select();
		new DefaultShell("New");
		new DefaultTreeItem("Java", "Interface").select();
		new PushButton("Next >").click();
		new DefaultShell("New Java Interface");
		new LabeledText("Name:").setText(name);
		new LabeledText("Package:").setText(pkg);
		new PushButton("Finish").click();
		new WaitWhile(new ShellIsAvailable("New Java Interface"));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		getProjectItem(MAIN_JAVA, pkg, name + ".java").open();
		return new TextEditor(name + ".java");
	}

	public ProjectItemExt getClass(String... path) {
		return getProjectItemExt(MAIN_JAVA, path);
	}

	public ProjectItemExt getTestClass(String... path) {
		return getProjectItemExt(TEST_JAVA, path);
	}

	public ProjectItemExt getResourceExt(String... path) {
		return getProjectItemExt(MAIN_RESOURCE, path);
	}

	public ProjectItemExt getTestResource(String... path) {
		return getProjectItemExt(TEST_RESOURCE, path);
	}

	public ProjectItemExt getProjectItemExt(String category, String... path) {
		String[] newPath = new String[path.length + 1];
		newPath[0] = category;
		for (int i = 0; i < path.length; i++) {
			newPath[i + 1] = path[i];
		}
		ProjectItem projectItem = getProjectItem(newPath);
		return new ProjectItemExt(projectItem);
	}

	public void update() {
		try {
			File pomFile = new File(getFile(), "pom.xml");
			XPathEvaluator xpath = new XPathEvaluator(pomFile);

			// fix kie version if needed due to SWITCHYARD-2834
			Node node = xpath.evaluateNode(
					"/project/build/plugins/plugin/executions/execution/configuration/versions/switchyard.osgi.version");
			if (node != null) {
				node.setTextContent("${switchyard.version}");
			}

			// fix bpm and rules groupId
			String kieVersion = xpath.evaluateString("/project/properties/kie.version");
			if (kieVersion != null && kieVersion.startsWith("6.4.0")) {
				node = xpath.evaluateNode(
						"/project/dependencies/dependency[artifactId='switchyard-component-bpm']/groupId");
				if (node != null) {
					node.setTextContent("org.jboss.integration.fuse");
				}
				node = xpath.evaluateNode(
						"/project/dependencies/dependency[artifactId='switchyard-component-rules']/groupId");
				if (node != null) {
					node.setTextContent("org.jboss.integration.fuse");
				}
			}

			// fix order of boms
			node = xpath
					.evaluateNode("/project/dependencyManagement/dependencies/dependency[artifactId='switchyard-bom']");
			if (node != null) {
				Node parentNode = node.getParentNode(); 
				parentNode.removeChild(node);
				parentNode.appendChild(node);
			}

			// save the changes
			xpath.printDocument(new StreamResult(pomFile));
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}

		select();
		new ContextMenuItem("Maven", "Update Project...").select();
		new DefaultShell("Update Maven Project");
		new CheckBox("Force Update of Snapshots/Releases").toggle(true);
		new PushButton("OK").click();

		AbstractWait.sleep(TimePeriod.DEFAULT);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}

	public void enableFuseCamelNature() {
		select();
		new ContextMenuItem("Enable Fuse Camel Nature").select();

		AbstractWait.sleep(TimePeriod.DEFAULT);
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
		new ContextMenuItem("SwitchYard", "Configure Capabilities...").select();
		return new ProjectCapabilitiesShell("Properties for " + getName());
	}

	public ProjectProperties openProperties() {
		select();
		new ContextMenuItem("Properties").select();
		return new ProjectProperties(getText());
	}

	public void build() {
		if (PreferencesUtil.isAutoBuildingOn()) {
			throw new RedDeerException("Cannot builld a project if projects are built automatically");
		}
		select();
		MenuItem menu = new ShellMenuItem(new WorkbenchShell(), "Project", "Build Project");
		if (menu.isEnabled()) {
			menu.select();
		}
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
}
