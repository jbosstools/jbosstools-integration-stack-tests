package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.ui.perspectives.AbstractPerspective;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.tools.teiid.ui.bot.test.suite.TeiidSuite;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(TeiidSuite.class)
public class PerspectiveTest extends RedDeerTest {

	@Test
	public void openPerspectiveTest() {

		TeiidPerspective perspective = new TeiidPerspective();
		perspective.open();
		assertTrue(perspective.isOpened());
	}

	public class TeiidPerspective extends AbstractPerspective {

		public TeiidPerspective() {

			super("Teiid Designer");
		}
	}
}
