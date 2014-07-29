package org.jboss.tools.runtime.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeReqType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.jboss.tools.runtime.reddeer.wizard.ESBRuntimePreferencePage;
import org.junit.Test;
import org.junit.runner.RunWith;

@Runtime(type = RuntimeReqType.ESB)
@RunWith(RedDeerSuite.class)
public class ESBRuntimeTest {

	@InjectRequirement
	protected RuntimeRequirement requirement;

	@Test
	public void runtimePresentTest() {
		ESBRuntimePreferencePage page = new ESBRuntimePreferencePage();
		page.open();
		List<String> esbRuntimes = page.getESBRuntimes();
		assertTrue(esbRuntimes.contains(requirement.getConfig().getName()));
		page.ok();
	}

}
