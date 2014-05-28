package org.jboss.tools.jbpm.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.ui.perspectives.AbstractPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public class PerspectiveTest {

	@Test
	public void openPerspectiveTest() {
		JBPMPerspective perspective = new JBPMPerspective();
		perspective.open();
		assertTrue(perspective.isOpened());
	}

	public class JBPMPerspective extends AbstractPerspective {

		public JBPMPerspective() {
			super("jBPM jPDL 3");
		}

	}
}
