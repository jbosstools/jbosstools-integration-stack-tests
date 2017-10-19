package org.jboss.tools.bpel.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.bpel.reddeer.perspective.BPELPerspective;
import org.jboss.tools.bpel.reddeer.wizard.NewProjectWizard;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@OpenPerspective(BPELPerspective.class)
@RunWith(RedDeerSuite.class)
@Server(state = ServerRequirementState.PRESENT)
public class AssociateRuntimeTest {

	@InjectRequirement
	private ServerRequirement serverRequirement;

	@Test
	public void testModeling() throws Exception {
		new NewProjectWizard("runtimeTest").execute();

		new ProjectExplorer().getProject("runtimeTest").select();

		new ContextMenuItem("Properties").select();

		new DefaultTreeItem("Targeted Runtimes").select();

		String runtimeName = serverRequirement.getConfig().getServer().getName() + " Runtime";
		assertTrue(containsItem(new DefaultTable(), runtimeName));

		new PushButton("Apply and Close").click();

		new WaitUntil(new ShellIsAvailable("Progress Information"), false);
		new WaitWhile(new ShellIsAvailable("Progress Information"), TimePeriod.LONG);
	}

	private static boolean containsItem(Table table, String item) {
		int count = table.rowCount();
		for (int i = 0; i < count; i++) {
			if (table.getItem(i).getText(0).equals(item)) {
				return true;
			}
		}
		return false;
	}

}
