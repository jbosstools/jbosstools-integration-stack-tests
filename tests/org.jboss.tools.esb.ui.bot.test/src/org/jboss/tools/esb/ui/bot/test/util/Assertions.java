package org.jboss.tools.esb.ui.bot.test.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.Button;

public class Assertions {
//	public static void assertTreeContent(SWTBotEditor editor, String... items) {
//		assertTrue("Editor tree does not contain expected path of nodes : "
//				+ Arrays.toString(items),
//				SWTEclipseExt.containstInTree(editor.bot().tree(), items));
//	}
	public static void assertButtonEnabled(Button button, boolean enabled) {
		if (enabled) {
			assertTrue(button.getText()+ " button is not enabled when all required fields are filled",button.isEnabled());
		}
		else {
			assertFalse(button.getText()+" button is not enabled when all required fields are filled",button.isEnabled());
		}
			
	}
	public static void assertXmlContentBool(String xml, String xpath) {
		XmlFileValidator validator = null;		
		try {
			validator = new XmlFileValidator(xml);
		} catch (Exception e) {
			fail("Unable to parse ESB editor source content to XML");
		}
		assertTrue("XML content was not expected by xpath '"+xpath+"'\n instead content was\n"+xml,validator.executeBoolean(xpath));
		
	}
	public static void assertXmlContentString(String xml, String xpath,
			String expected) {
		XmlFileValidator validator = null;
		try {
			validator = new XmlFileValidator(xml);
		} catch (Exception e) {
			fail("Unable to parse ESB editor source content to XML");
		}
		String selected = validator.executeString(xpath);
		assertTrue("Expected content was '" + expected + "', selected was '"
				+ selected + "' - xpath '" + xpath
				+ "'\n XML instead content was\n" + xml,
				expected.equals(selected));

	}
	/**
	 * asserts given xpath against given xml, given xpath is covered by xpath count expression
	 * @param xml
	 * @param xpath
	 */
	public static void assertXmlContentExists(String xml, String xpath) {
		 assertXmlContentBool(xml, "count("+xpath+")=1");
	}
	
	public static void assertEmptyProblemsView(String msg) {
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		assertTrue("ESB Editor has errors in 'Problems View' - " + msg, problemsView.getAllErrors().isEmpty());
	}
}
