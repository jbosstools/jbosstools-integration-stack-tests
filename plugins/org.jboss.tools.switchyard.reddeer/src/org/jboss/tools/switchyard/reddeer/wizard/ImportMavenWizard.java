package org.jboss.tools.switchyard.reddeer.wizard;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.selectionwizard.ImportMenuWizard;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.exception.SWTLayerException;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;

public class ImportMavenWizard extends ImportMenuWizard {

	public ImportMavenWizard() {
		super("Import Maven Projects", "Maven", "Existing Maven Projects");
	}

	public void importProject(String path) {
		open();

		new DefaultShell("Import Maven Projects");
		new LabeledCombo("Root Directory:").setText(path);
		new PushButton("Refresh").click();
		new WaitUntil(new ProjectsIsLoaded(), TimePeriod.VERY_LONG);
		if (new PushButton("Next >").isEnabled()) {
			new PushButton("Next >").click();
		}
		new PushButton("Finish").click();
		new WaitWhile(new ShellIsAvailable("Import Maven Projects"), TimePeriod.DEFAULT, false);
		try {
			new DefaultShell("Import Maven Projects");
			new PushButton("Resolve All Later").click();
			new PushButton("Finish").click();
		} catch (Exception e) {
			// ok, it means that the warning wasn't displayed
		}
		try {
			new DefaultShell("Incomplete Maven Goal Execution");
			new PushButton("OK").click();
		} catch (SWTLayerException e) {
			// ok, it means that the warning wasn't displayed
		}
		new WaitWhile(new ShellIsAvailable("Import Maven Projects"), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(20 * 60 * 1000));

		AbstractWait.sleep(TimePeriod.DEFAULT);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		projectExplorer.getProjects().get(0).select();
		new ContextMenuItem("Maven", "Update Project...").select();
		new DefaultShell("Update Maven Project");
		new CheckBox("Force Update of Snapshots/Releases").toggle(true);
		new PushButton("OK").click();

		AbstractWait.sleep(TimePeriod.DEFAULT);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	protected class ProjectsIsLoaded extends AbstractWaitCondition {

		@Override
		public boolean test() {
			return new PushButton("Finish").isEnabled() || new PushButton("Next >").isEnabled();
		}

		@Override
		public String description() {
			return "The project is still not loaded.";
		}

	}
}
