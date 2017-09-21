package org.jboss.tools.bpel.reddeer.activity;

import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author apodhrad
 * 
 */
public class Throw extends Activity {

	public Throw(String name) {
		super(name, THROW);
	}

	public void setUserFaultName(String name) {
		select();
		openProperties().selectDetails();
		new RadioButton("User-defined").click();
		new LabeledText("Fault Name:").setText(name);
		save();
	}
}
