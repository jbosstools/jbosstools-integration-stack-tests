package org.jboss.tools.fuse.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.swt.api.Text;

/**
 * 
 * @author apodhrad
 *
 */
public class ContainsText extends AbstractWaitCondition {

	private Text text;
	private String actualText;
	private String expectedText;

	public ContainsText(Text text, String expectedText) {
		this.text = text;
		this.expectedText = expectedText;
	}

	@Override
	public boolean test() {
		actualText = text.getText();
		return actualText.contains(expectedText);
	}

	@Override
	public String description() {
		return "Expected '" + expectedText + "' but was '" + actualText + "'";
	}

}