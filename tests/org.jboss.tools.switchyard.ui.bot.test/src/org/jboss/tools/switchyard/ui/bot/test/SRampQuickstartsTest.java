package org.jboss.tools.switchyard.ui.bot.test;

import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test importing s-ramp quickstarts
 * 
 * @author apodhrad
 * 
 */
@SwitchYard(server = @Server(type = ServerReqType.ANY, state = ServerReqState.PRESENT))
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class SRampQuickstartsTest extends QuickstartsTest {

	public SRampQuickstartsTest() {
		super("quickstarts/overlord/sramp");
	}

	@Test
	public void sRampDemosArchivePackageTest() {
		testQuickstart("s-ramp-demos-archive-package");
	}

	@Test
	public void sRampDemosClassificationsTest() {
		testQuickstart("s-ramp-demos-classifications");
	}

	@Test
	public void sRampDemosCustomDeriverTest() {
		testQuickstart("s-ramp-demos-custom-deriver");
	}

	@Test
	public void sRampDemosDerivedArtifactsTest() {
		testQuickstart("s-ramp-demos-derived-artifacts");
	}
	
	@Test
	public void sRampDemosMvnIntegrationTest() {
		testQuickstart("s-ramp-demos-mvn-integration");
	}

	@Test
	public void sRampDemosOntologiesTest() {
		testQuickstart("s-ramp-demos-ontologies");
	}

	@Test
	public void sRampDemosProperiesTest() {
		testQuickstart("s-ramp-demos-properties");
	}

	@Test
	public void sRampDemosQueryTest() {
		testQuickstart("s-ramp-demos-query");
	}

	@Test
	public void sRampDemosRelationshipsTest() {
		testQuickstart("s-ramp-demos-relationships");
	}

	@Test
	public void sRampDemosShellCommandTest() {
		testQuickstart("s-ramp-demos-shell-command");
	}

	@Test
	public void sRampDemosSimpleClientTest() {
		testQuickstart("s-ramp-demos-simple-client");
	}

	@Test
	public void sRampDemosSwitchYardTest() {
		testQuickstart("s-ramp-demos-switchyard");
	}

	@Test
	public void sRampDemosSwitchYardMultiAppTest() {
		testQuickstart("s-ramp-demos-switchyard-multiapp");
	}

}
