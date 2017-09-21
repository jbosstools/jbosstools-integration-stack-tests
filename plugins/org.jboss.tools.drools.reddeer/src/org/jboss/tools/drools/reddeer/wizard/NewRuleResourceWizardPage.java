package org.jboss.tools.drools.reddeer.wizard;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

public class NewRuleResourceWizardPage extends NewResourceWizardPage {

	public NewRuleResourceWizardPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}

	public enum RuleResourceType {
		rulePackage("New DRL (rule package)"), individualRule("New Rule (individual rule)");

		private final String label;

		private RuleResourceType(String label) {
			this.label = label;
		}

		public String toString() {
			return label;
		}
	}

	public void setTypeOfRuleResource(RuleResourceType type) {
		new LabeledCombo("Type of rule resource:").setSelection(type.toString());
	}

	public void setUseDSL(boolean value) {
		new CheckBox(0).toggle(value);
	}

	public void setUseFunctions(boolean value) {
		new CheckBox(1).toggle(value);
	}

	public void setRulePackageName(String pkgName) {
		new LabeledText("Rule package name:").setText(pkgName);
	}

}
