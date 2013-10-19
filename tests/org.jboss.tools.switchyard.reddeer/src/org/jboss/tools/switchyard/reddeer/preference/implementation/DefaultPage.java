package org.jboss.tools.switchyard.reddeer.preference.implementation;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.tools.switchyard.reddeer.widget.RadioButton;

/**
 * Manipulates with a common properties page's components.
 * 
 * @author tsedmik
 */
public class DefaultPage {

	public List<String> getAllComboValues(int index) {

		SWTBotShell shell = new SWTWorkbenchBot().activeShell();
		SWTBotCombo comboSWT = shell.bot().comboBox(index);
		return Arrays.asList(comboSWT.items());
	}
	
	public boolean isComboEnabled(String name) {
		
		return new DefaultCombo(name).isEnabled();
	}
	
	public boolean isComboEnabled(int index) {
		
		return new DefaultCombo(index).isEnabled();
	}
	
	public int getComboSelectionIndex(String name) {
		
		return new DefaultCombo(name).getSelectionIndex();
	}
	
	public int getComboSelectionIndex(int index) {
		
		return new DefaultCombo(index).getSelectionIndex();
	}
	
	public String getComboSelection(String name) {
		
		return new DefaultCombo(name).getSelection();
	}
	
	public String getComboSelection(int index) {
		
		return new DefaultCombo(index).getSelection();
	}

	public void setCheckBox(String name, boolean value) {

		CheckBox chkbox = new CheckBox(name);

		if (value && !chkbox.isChecked()) {
			chkbox.click();
		} else if (!value && chkbox.isChecked()) {
			chkbox.click();
		}
	}
	
	public boolean isCheckBoxChecked(String name) {
		
		return new CheckBox(name).isChecked();
	}
	
	public boolean isCheckBoxEnabled(String name) {
		
		return new CheckBox(name).isEnabled();
	}
	
	public boolean isRadioButtonSelected(String name) {
		
		return new RadioButton(name).isSelected();
	}
	
	public boolean isRadioButtonEnabled(String name) {
		
		return new RadioButton(name).isEnabled();
	}
}
