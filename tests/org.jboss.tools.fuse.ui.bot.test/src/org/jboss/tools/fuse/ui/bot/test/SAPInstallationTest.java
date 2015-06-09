package org.jboss.tools.fuse.ui.bot.test;

import org.jboss.reddeer.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.NoButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.runtime.reddeer.requirement.SAPRequirement;
import org.jboss.tools.runtime.reddeer.requirement.SAPRequirement.SAP;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author apodhrad
 *
 */
@SAP
@CleanWorkspace
@RunWith(RedDeerSuite.class)
public class SAPInstallationTest {

	@InjectRequirement
	private SAPRequirement sapRequirement;

	@Test
	public void sapInstalltionTest() {
		InstallationWizard wizard = new InstallationWizard();
		wizard.open();
		wizard.activate();
		wizard.next();
		wizard.getJCo3ArchiveFile().setText(sapRequirement.getConfig().getLib().getJco3());
		wizard.getIDoc3ArchiveFile().setText(sapRequirement.getConfig().getLib().getJidoc());
		wizard.finish(TimePeriod.VERY_LONG);

		try {
			new WaitUntil(new ShellWithTextIsAvailable("Security Warning"), TimePeriod.LONG);
			new DefaultShell("Security Warning");
			new OkButton().click();
		} catch (Exception e) {
			// sometimes the window doesn't pop up
		}

		new WaitUntil(new ShellWithTextIsAvailable("Software Updates"), TimePeriod.VERY_LONG);
		new DefaultShell("Software Updates");
		new NoButton().click();
	}

	public class InstallationWizard extends ImportWizardDialog {

		public static final String JCO3_SECTION = "Select JCo3 Archive File";
		public static final String IDOC3_SECTION = "Select IDoc3 Archive File";

		public InstallationWizard() {
			super("JBoss Fuse", "Install JBoss Fuse SAP Tool Suite");
		}

		public InstallationWizard activate() {
			new DefaultShell("Install JBoss Fuse SAP Tool Suite ");
			return this;
		}

		public Text getIDoc3ArchiveVersion() {
			return new LabeledText("Archive Version:");
		}

		public Text getIDoc3ArchiveFile() {
			return new LabeledText("IDoc3 Archive File:");
		}

		public Text getJCo3ArchiveOSPlatform() {
			return new LabeledText("Archive OS Platform:");
		}

		public Text getJCo3ArchiveVersion() {
			return new LabeledText("Archive Version:");
		}

		public Text getJCo3ArchiveFile() {
			return new LabeledText("JCo3 Archive File: ");
		}

	}
}
