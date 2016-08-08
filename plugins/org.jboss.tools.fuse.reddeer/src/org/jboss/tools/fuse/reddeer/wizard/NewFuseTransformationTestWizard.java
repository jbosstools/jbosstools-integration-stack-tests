package org.jboss.tools.fuse.reddeer.wizard;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Represents 'New Fuse Transformation Test Wizard'
 * 
 * @author tsedmik
 */
public class NewFuseTransformationTestWizard extends NewWizardDialog {

	private Logger log = Logger.getLogger(NewFuseTransformationTestWizard.class);

	public NewFuseTransformationTestWizard() {
		super("JBoss Fuse", "Fuse Transformation Test");
	}

	public void selectTransformationID(String name) {
		log.debug("Set 'Transformation ID' to '" + name + "'");
		new DefaultCombo().setSelection(name);
	}

	public void setPackage(String name) {
		log.debug("Set 'Package' to '" + name + "'");
		new LabeledText("Package:").setText(name);
	}
}
