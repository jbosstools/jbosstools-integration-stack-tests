package org.jboss.tools.drools.ui.bot.test.kienavigator;

import java.io.IOException;
import java.util.List;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.drools.reddeer.kienavigator.item.OrgUnitItem;
import org.jboss.tools.drools.reddeer.kienavigator.item.RepositoryItem;
import org.jboss.tools.drools.reddeer.kienavigator.item.ServerItem;
import org.jboss.tools.drools.reddeer.kienavigator.properties.ServerProperties;
import org.jboss.tools.drools.reddeer.view.KieNavigatorView;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Server(type = {ServerReqType.EAP, ServerReqType.WildFly}, state = ServerReqState.RUNNING)
@RunWith(RedDeerSuite.class)
public class OpenKieNavigatorTest extends KieNavigatorTestParent {
	
	@InjectRequirement
	private ServerRequirement serverReq;
	
	@Test
	public void openKieNavigatorTest() throws IOException, InterruptedException {
		KieNavigatorView knv = new KieNavigatorView();
		knv.open();
		ServerItem si = knv.getServers().get(0);
		
		ServerProperties sp = si.properties();
		sp.setUsername(USERNAME);
		sp.setPassword(PASSWORD);
		sp.setApplicationName(WEB_APP_NAME);
		sp.setHttpPort(HTTP_PORT);
		sp.setGitPort(GIT_PORT);
		sp.setGitRepoPath(getGitDirectory());
		sp.apply();
		sp.ok();
		
		List<OrgUnitItem> orgUnits = si.getOrgUnits();
		
		Assert.assertEquals(1, orgUnits.size());
		Assert.assertEquals("example", orgUnits.get(0).getName());
		
		List<RepositoryItem> ril = orgUnits.get(0).getRepositories();
		
		Assert.assertEquals(1, ril.size());
		Assert.assertEquals("repository1", ril.get(0).getName());
	}
}
