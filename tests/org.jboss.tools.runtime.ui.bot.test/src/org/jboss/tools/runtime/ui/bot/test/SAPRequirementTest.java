package org.jboss.tools.runtime.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.runtime.reddeer.requirement.SAPRequirement;
import org.jboss.tools.runtime.reddeer.requirement.SAPRequirement.SAP;
import org.junit.Test;
import org.junit.runner.RunWith;

@SAP
@RunWith(RedDeerSuite.class)
public class SAPRequirementTest {

	@InjectRequirement
	protected SAPRequirement sapRequirement;

	@Test
	public void sapRequirementTest() {
		assertEquals("sapjco3-3.0.11.zip", sapRequirement.getConfig().getLib().getJco3());
		assertEquals("sapjidoc30P_10-10009485.zip", sapRequirement.getConfig().getLib().getJidoc());
		
		assertEquals("localhost", sapRequirement.getConfig().getDestination().getAshost());
		assertEquals("200", sapRequirement.getConfig().getDestination().getClient());
		assertEquals("00", sapRequirement.getConfig().getDestination().getSysnr());
		assertEquals("admin", sapRequirement.getConfig().getDestination().getUser());
		assertEquals("admin", sapRequirement.getConfig().getDestination().getUserName());
		assertEquals("admin123$", sapRequirement.getConfig().getDestination().getPasswd());
		assertEquals("admin123$", sapRequirement.getConfig().getDestination().getPassword());
		assertEquals("en", sapRequirement.getConfig().getDestination().getLang());
		
		assertEquals("localhost", sapRequirement.getConfig().getServer().getGwhost());
		assertEquals("QUICKSTART", sapRequirement.getConfig().getServer().getProgid());
		assertEquals("2", sapRequirement.getConfig().getServer().getConnectionCount());
	}

}
