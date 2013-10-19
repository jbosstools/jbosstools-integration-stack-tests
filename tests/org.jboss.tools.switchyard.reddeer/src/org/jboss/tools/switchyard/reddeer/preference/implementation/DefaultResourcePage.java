package org.jboss.tools.switchyard.reddeer.preference.implementation;

import java.util.List;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

/**
 * Represents a properties page "Resource".
 * 
 * @author tsedmik
 */
public class DefaultResourcePage extends DefaultPage {
	
	private static final String ATTR_DERIVED = "Derived";
	// private static final String ENCODING_DEFAULT = "Default (inherited from container: UTF-8)";
	private static final String ENCODING_OTHER = "Other:";
	private static final String BUTTON_RESTORE_DEFAULTS = "Restore Defaults";
	private static final String BUTTON_APPLY = "Apply";
	
	public DefaultResourcePage setAttributeDerived(boolean set) {
		
		setCheckBox(ATTR_DERIVED, set);
		return this;
	}
	
	public boolean getAttributeDerived() {
		
		return isCheckBoxChecked(ATTR_DERIVED);
	}
	
	public boolean isAttrDerivedEnabled() {
		
		return isCheckBoxEnabled(ATTR_DERIVED);
	}

	public DefaultResourcePage setEncoding(String encoding) {
		
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
		
		return getAllComboValues(0);
	}
	
	public boolean isDefaultEncodingEnabled() {
		
		return new RadioButton(0).isEnabled();
	}
	
	public boolean isOtherEncodingEnabled() {
		
		return isRadioButtonEnabled(ENCODING_OTHER);
	}
	
	public boolean isOtherEncodingComboEnabled() {
		
		DefaultCombo combo = new DefaultCombo(0);
		
		return combo.isEnabled();
	}
	
	public DefaultResourcePage restoreDefaults() {
		
		new PushButton(BUTTON_RESTORE_DEFAULTS).click();
		
		return this;
	}
	
	public DefaultResourcePage apply() {
		
		new PushButton(BUTTON_APPLY).click();
		
		return this;
	}
}
