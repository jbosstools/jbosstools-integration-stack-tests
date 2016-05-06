package org.jboss.tools.drools.ui.bot.test.smoke;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.reddeer.view.KieNavigatorView;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public class KieNavigatorTest extends TestParent {
	
	@Test
	public void openKieNavigatorTest() {
		KieNavigatorView kieNavigatorView = new KieNavigatorView();
		kieNavigatorView.open();
		Assert.assertEquals("Kie Navigator is not opened.", true, kieNavigatorView.isOpened());
		Assert.assertEquals("There is no link to the Servers View.", true, kieNavigatorView.isLinkToServersViewExists());
	}
}
