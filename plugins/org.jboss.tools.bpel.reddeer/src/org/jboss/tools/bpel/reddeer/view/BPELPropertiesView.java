package org.jboss.tools.bpel.reddeer.view;

import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.clabel.DefaultCLabel;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.bpel.reddeer.widget.QuickPickTree;

/**
 * 
 * @author apodhrad
 *
 */
public class BPELPropertiesView extends PropertiesView {

	public static final String TAB_DESCRIPTION = "Description";
	public static final String TAB_DETAILS = "Details";

	public static final String CONDITION_DATE = "Date";
	public static final String CONDITION_DURATION = "Duration";

	public static final String CHECKBOX_CREATE = "Create a new Process instance if one does not already exist";

	public BPELPropertiesView() {
		super();
		open();
	}

	public void setName(String name) {
		selectDescription();
		if (name != null) {
			new DefaultText(0).setText(name);
		}
	}

	public void setCondition(String condition) {
		setCondition(condition, null);
	}

	public void setCondition(String condition, String conditionType) {
		selectDetails();
		if (condition != null) {
			new DefaultStyledText().setText(condition);
		}
		if (conditionType != null) {
			new RadioButton(conditionType).click();
		}
	}

	public void pickOperation(String operation) {
		selectDetails();
		if (operation != null) {
			new QuickPickTree().pick(operation);
		}
	}

	public void toggleCreateInstance(boolean createInstance) {
		selectDetails();
		new CheckBox(CHECKBOX_CREATE).toggle(createInstance);
	}

	public BPELPropertiesView selectDescription() {
		selectTab(TAB_DESCRIPTION);
		return this;
	}

	public BPELPropertiesView selectDetails() {
		selectTab(TAB_DETAILS);
		return this;
	}

	public String getType() {
		return new DefaultCLabel().getText();
	}
}
