package org.jboss.tools.switchyard.reddeer.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Check if a PushButton is enabled. Depends on switching some input text fields
 * (LabeledText).
 * 
 * @author tsedmik
 */
public class IsButtonEnabled implements WaitCondition {

	private String button;
	private List<String> texts;

	public IsButtonEnabled(String button, String... args) {

		this.button = button;
		texts = new ArrayList<String>(Arrays.asList(args));
	}

	@Override
	public boolean test() {

		for (String text : texts) {
			new LabeledText(text).setFocus();
		}

		return new PushButton(button).isEnabled();
	}

	@Override
	public String description() {

		return "Button '" + button + "' was not enabled!";
	}
}
