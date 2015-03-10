package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.handler.ShellHandler;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.switchyard.reddeer.condition.ErrorsExist;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.wizard.ImportMavenWizard;
import org.junit.After;
import org.junit.runner.RunWith;

/**
 * Abstract test class for importing quickstarts
 * 
 * @author apodhrad
 * 
 */
@SwitchYard(server = @Server(type = ServerReqType.ANY, state = ServerReqState.PRESENT))
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public abstract class QuickstartsTest {

	protected String quickstartPath;

	@InjectRequirement
	private SwitchYardRequirement switchyardRequirement;
	
	public QuickstartsTest(String quickstartPath) {
		this.quickstartPath = quickstartPath;
	}

	protected void testQuickstart(String path) {
		String fullPath = switchyardRequirement.getConfig().getServerBase().getHome() + "/" + quickstartPath + "/" + path;
		assertTrue("Path '" + fullPath + "' doesn exist!", new File(fullPath).exists());

		new ImportMavenWizard().importProject(fullPath);

		checkErrors(path);
	}

	protected void checkErrors(String path) {
		AbstractWait.sleep(TimePeriod.NORMAL);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		ErrorsExist errorsExistCondition = new ErrorsExist();
		new WaitUntil(errorsExistCondition, TimePeriod.LONG, false);
		List<TreeItem> errors = errorsExistCondition.getAllErrors();
		assertTrue("After importing the quickstart '" + path
				+ "' there are the following errors:\n" + toString(errors), errors.isEmpty());
	}

	@After
	public void deleteAllProjects() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
		
		PackageExplorer packageExplorer = new PackageExplorer();
		packageExplorer.open();
		packageExplorer.deleteAllProjects(false);
	}

	protected String toString(List<TreeItem> items) {
		StringBuffer result = new StringBuffer();
		for (TreeItem item : items) {
			result.append(item.getText());
			result.append("\n");
		}
		return result.toString();
	}

}
