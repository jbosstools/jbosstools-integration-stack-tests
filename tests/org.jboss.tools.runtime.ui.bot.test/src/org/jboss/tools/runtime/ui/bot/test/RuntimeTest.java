package org.jboss.tools.runtime.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeReqType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.jboss.tools.runtime.reddeer.wizard.ESBRuntimePreferencePage;
import org.jboss.tools.runtime.reddeer.wizard.JBPMRuntimePreferencePage;
import org.junit.Test;
import org.junit.runner.RunWith;

@Runtime(type = RuntimeReqType.ANY)
@RunWith(RedDeerSuite.class)
public class RuntimeTest {

	@InjectRequirement
	protected RuntimeRequirement requirement;

	@Test
	public void runtimePresentTest() {
		// ESB Runtimes
		ESBRuntimePreferencePage esbPage = new ESBRuntimePreferencePage();
		esbPage.open();
		List<String> esbRuntimes = esbPage.getESBRuntimes();
		esbPage.ok();
		
		// jBPM Runtimes
		JBPMRuntimePreferencePage jbpmPage = new JBPMRuntimePreferencePage();
		jbpmPage.open();
		List<String> jbpmRuntimes = jbpmPage.getJBPMRuntimes();
		jbpmPage.ok();
		
		List<String> runtimes = new ArrayList<String>();
		runtimes.addAll(esbRuntimes);
		runtimes.addAll(jbpmRuntimes);
		assertTrue(runtimes.contains(requirement.getConfig().getName()));
	}

}
