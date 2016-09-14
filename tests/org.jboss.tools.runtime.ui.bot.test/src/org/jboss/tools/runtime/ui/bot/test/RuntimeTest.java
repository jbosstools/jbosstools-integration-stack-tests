package org.jboss.tools.runtime.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeReqType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.jboss.tools.runtime.reddeer.wizard.DroolsRuntimePreferencePage;
import org.junit.Test;
import org.junit.runner.RunWith;

@Runtime(type = RuntimeReqType.ANY)
@RunWith(RedDeerSuite.class)
public class RuntimeTest {

	@InjectRequirement
	protected RuntimeRequirement requirement;

	@Test
	public void runtimePresentTest() {
		// Drools Runtimes
		DroolsRuntimePreferencePage droolsPage = new DroolsRuntimePreferencePage();
		droolsPage.open();
		List<String> droolsRuntimes = droolsPage.getDroolsRuntimes();
		droolsPage.ok();

		List<String> runtimes = new ArrayList<String>();
		runtimes.addAll(droolsRuntimes);
		assertTrue(runtimes.contains(requirement.getConfig().getName()));
	}

}
