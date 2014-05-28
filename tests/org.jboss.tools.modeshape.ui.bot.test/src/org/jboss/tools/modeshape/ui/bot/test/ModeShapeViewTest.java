package org.jboss.tools.modeshape.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.modeshape.reddeer.view.ModeshapeView;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public class ModeShapeViewTest {

	@Test
	public void openModeShapeViewTest() {
		ModeshapeView view = new ModeshapeView();
		view.open();
	}

}
