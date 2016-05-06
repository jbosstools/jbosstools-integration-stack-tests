package org.jboss.tools.drools.ui.bot.test.smoke;

import org.jboss.reddeer.eclipse.ui.perspectives.AbstractPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.reddeer.perspective.JbpmPerspective;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public class PerspectiveTest extends TestParent {

	@Test
	public void openDroolsPerspectiveTest() {
		final String droolsPerspectiveName = "Drools";
		openAndCheckPerspective(new DroolsPerspective(), droolsPerspectiveName);
	}

	@Test
	public void openJbpmPerspectiveTest() {
		final String jbpmPerspectiveName = "jBPM";
		openAndCheckPerspective(new JbpmPerspective(), jbpmPerspectiveName);
	}
	
	private void openAndCheckPerspective(AbstractPerspective perspective, String perspectiveName) {
		perspective.open();
		String mainWindowTitle = new DefaultShell().getText();
		Assert.assertTrue(perspectiveName + " perspective is not opened.", mainWindowTitle.startsWith(perspectiveName));
	}
}
