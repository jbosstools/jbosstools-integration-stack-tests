package org.jboss.tools.switchyard.reddeer.preference;

import java.util.List;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

/**
 * Represents a properties page "Resource".
 * 
 * @author tsedmik, apodhrad
 */
public class ResourcePage {

	private static final String ATTR_DERIVED = "Derived";
	// private static final String ENCODING_DEFAULT = "Default (inherited from container: UTF-8)";
	private static final String ENCODING_OTHER = "Other:";

	public ResourcePage setAttributeDerived(boolean set) {
		new CheckBox(ATTR_DERIVED).toggle(set);
		return this;
	}

	public boolean getAttributeDerived() {
		return new CheckBox(ATTR_DERIVED).isChecked();
	}

	public boolean isAttrDerivedEnabled() {
		return new CheckBox(ATTR_DERIVED).isEnabled();
	}

	public ResourcePage setEncoding(String encoding) {
		RadioButton defaultEncoding = new RadioButton(0);
		RadioButton other = new RadioButton(ENCODING_OTHER);
		DefaultCombo combo = new DefaultCombo(0);

		if (encoding.equals("default")) {
			defaultEncoding.click();
		} else {
			other.click();
			combo.setText(encoding);
		}

		return this;
	}

	public String getEncoding() {
		RadioButton defaultEncoding = new RadioButton(0);
		DefaultCombo combo = new DefaultCombo(0);

		if (defaultEncoding.isSelected()) {
			return "default";
		} else {
			return combo.getText();
		}
	}

	public List<String> getAllEncodings() {
		return new DefaultCombo(0).getItems();
	}

	public boolean isDefaultEncodingEnabled() {
		return new RadioButton(0).isEnabled();
	}

	public boolean isOtherEncodingEnabled() {
		return new RadioButton(ENCODING_OTHER).isEnabled();
	}

	public boolean isOtherEncodingComboEnabled() {
		return new DefaultCombo(0).isEnabled();
	}

}
