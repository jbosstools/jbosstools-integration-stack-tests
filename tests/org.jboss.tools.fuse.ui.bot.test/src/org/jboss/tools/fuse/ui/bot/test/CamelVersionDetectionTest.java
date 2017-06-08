package org.jboss.tools.fuse.ui.bot.test;

import static org.jboss.reddeer.requirements.server.ServerReqState.PRESENT;
import static org.jboss.tools.runtime.reddeer.requirement.ServerReqType.Fuse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.common.reddeer.view.ErrorLogView;
import org.jboss.tools.fuse.reddeer.wizard.NewFuseIntegrationProjectWizard;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.impl.ServerFuse;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests wizard for creating a new Fuse Integration Project
 * 
 * @author djelinek
 */
@RunWith(RedDeerSuite.class)
@Server(type = Fuse, state = PRESENT)
public class CamelVersionDetectionTest {
	
	@InjectRequirement
	private ServerRequirement serverRequirement;

	/**
	 * Prepare/Clean test environment
	 */
	@Before
	@After
	public void setupDeleteProjects() {
		ProjectFactory.deleteAllProjects();
		new ErrorLogView().deleteLog();
		ShellHandler.getInstance().closeAllNonWorbenchShells();
	}
	
	/**
	 * <p>
	 * Tests 'Camel Version detection'
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>Invoke <i>File --> New --> Fuse Integration Project</i> wizard</li>
	 * <li>Set project name</li>
	 * <li>Hit 'Next'</li>
	 * <li>Select installed 'Target Runtime'</li>
	 * <li>Check whether the detected version of Camel is same as expected runtime version of Camel</li>
	 * </ol>
	 */
	@Test
	public void testCamelVersionDetection() {
		NewFuseIntegrationProjectWizard projectWizard = new NewFuseIntegrationProjectWizard();
		projectWizard.open();
		projectWizard.setProjectName("camel-version");
		projectWizard.next();
		projectWizard.selectTargetRuntime(projectWizard.getTargetRuntimes().get(1));
		String detected = projectWizard.getCamelVersion();
		String expected = ((ServerFuse) serverRequirement.getConfig().getServerBase()).getCamelVersion();
		assertTrue("Camel detection failed -> Detected: '" + detected + "', Expected: '" + expected + "'",
				detected.equals(expected));
		projectWizard.cancel();
	}

}
