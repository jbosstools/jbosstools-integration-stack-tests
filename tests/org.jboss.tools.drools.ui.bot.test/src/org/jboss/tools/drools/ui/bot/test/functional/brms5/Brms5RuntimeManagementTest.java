package org.jboss.tools.drools.ui.bot.test.functional.brms5;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.reddeer.dialog.DroolsRuntimeDialog;
import org.jboss.tools.drools.reddeer.preference.DroolsRuntimesPreferencePage;
import org.jboss.tools.drools.ui.bot.test.annotation.Drools5Runtime;
import org.jboss.tools.drools.ui.bot.test.group.SmokeTest;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeReqType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@Runtime(type = RuntimeReqType.DROOLS)
@RunWith(RedDeerSuite.class)
public class Brms5RuntimeManagementTest extends TestParent {

	@InjectRequirement
	private RuntimeRequirement droolsRequirement;

	@Before
	public void clearRuntimes() {
		deleteAllRuntimes();
	}

	@Test
	@Category(SmokeTest.class)
	@Drools5Runtime
	public void testBrms5Runtime() {
		String runtimeHome = droolsRequirement.getConfig().getRuntimeFamily().getHome();
		Assume.assumeNotNull(runtimeHome);

		DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
		pref.open();
		DroolsRuntimeDialog wiz = pref.addDroolsRuntime();
		wiz.setName(getTestName());
		wiz.setLocation(runtimeHome);

		Assert.assertTrue("Impossible to use created runtime.", wiz.isValid());
		wiz.ok();
		Assert.assertEquals("Runtime was not created.", 1, pref.getDroolsRuntimes().size());
		pref.ok();
	}
}
