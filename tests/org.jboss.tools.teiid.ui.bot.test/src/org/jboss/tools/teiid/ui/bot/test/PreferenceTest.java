package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.*;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.preference.TeiidDesignerPreferencePage;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public class PreferenceTest {

	@Test
	public void preferenceTest() {
		testPref();
	}

	private void testPref() {
		try {

			new TeiidDesignerPreferencePage().setTeiidConnectionImporterTimeout(240);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
