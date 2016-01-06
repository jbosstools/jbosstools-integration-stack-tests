package org.jboss.tools.fuse.reddeer.wizard;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;

public class ImportMavenWizard extends ImportWizardDialog {

	public ImportMavenWizard() {
		super("Maven", "Existing Maven Projects");
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
		new WaitWhile(new ShellWithTextIsActive("Import Maven Projects"), TimePeriod.NORMAL, false);
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
		} catch (SWTLayerException | CoreLayerException e) {
			// ok, it means that the warning wasn't displayed
		}
		new WaitWhile(new ShellWithTextIsActive("Import Maven Projects"), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(20 * 60 * 1000));

		AbstractWait.sleep(TimePeriod.NORMAL);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		AbstractWait.sleep(TimePeriod.NORMAL);

		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		projectExplorer.getProjects().get(0).select();
		new ContextMenu("Maven", "Update Project...").select();
		new DefaultShell("Update Maven Project");
		new CheckBox("Force Update of Snapshots/Releases").toggle(true);
		new PushButton("OK").click();

		AbstractWait.sleep(TimePeriod.NORMAL);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
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
