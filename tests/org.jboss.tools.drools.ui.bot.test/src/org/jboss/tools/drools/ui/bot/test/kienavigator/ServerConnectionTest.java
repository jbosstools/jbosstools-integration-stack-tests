package org.jboss.tools.drools.ui.bot.test.kienavigator;

import java.util.List;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.drools.reddeer.kienavigator.item.OrgUnitItem;
import org.jboss.tools.drools.reddeer.kienavigator.properties.ServerProperties;
import org.jboss.tools.drools.reddeer.view.KieNavigatorView;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Server(type = { ServerReqType.EAP, ServerReqType.WildFly }, state = ServerReqState.RUNNING)
@RunWith(RedDeerSuite.class)
public class ServerConnectionTest extends KieNavigatorTestParent {

	@InjectRequirement
	private ServerRequirement serverReq;

	@Test
	public void incorrectPasswordTest() {
		knv.open();
		ServerProperties sp = knv.getServer(0).properties();
		sp.setPassword("1234nc");
		sp.apply();
		sp.ok();
		checkError(knv);
	}

	@Test
	public void incorrectLoginTest() {
		knv = new KieNavigatorView();
		knv.open();
		ServerProperties sp = knv.getServer(0).properties();
		sp.setUsername("otheradmin");
		sp.apply();
		sp.ok();
		checkError(knv);
	}

	private void checkError(KieNavigatorView knv) {
		knv.getServer(0).refresh();
		List<OrgUnitItem> ouList = knv.getServer(0).getOrgUnits();
		Assert.assertEquals(1, ouList.size());
		Assert.assertTrue(ouList.get(0).getName().contains("Server returned"));
	}
}
