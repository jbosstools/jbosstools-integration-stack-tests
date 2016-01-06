package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.teiid.reddeer.condition.StyledTextHasText;

public class GenerateDynamicVdbWizard extends WizardDialog {

	private static final String DIALOG_TITLE = "Generate Dynamic VDB";

	public GenerateDynamicVdbWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}

	public GenerateDynamicVdbWizard setName(String name) {
		new LabeledText("Dynamic VDB Name").setText(name);
		return this;
	}

	public GenerateDynamicVdbWizard setVersion(String version) {
		new LabeledText("Version").setText(version);
		return this;
	}

	public GenerateDynamicVdbWizard setLocation(String... location) {
		new PushButton("Change...").click();
		new SelectTargetFolder().select(location);
		return this;
	}

	public GenerateDynamicVdbWizard setExcludeSourceMetadata(boolean checked) {
		new CheckBox("Exclude source DDL metadata").toggle(checked);
		return this;
	}

	public GenerateDynamicVdbWizard setSuppressAttributesWithDefaultValues(boolean checked) {
		new CheckBox("Suppress attributes with default values").toggle(checked);
		return this;
	}

	public GenerateDynamicVdbWizard setOvewriteExisting(boolean checked) {
		new CheckBox("Overwrite existing files").toggle(checked);
		return this;
	}

	public GenerateDynamicVdbWizard generate() {
		new PushButton("Generate").click();
		return this;
	}

	public String getContents() {
		DefaultStyledText contentsText = new DefaultStyledText(new DefaultGroup("Dynamic VDB XML Contents"));
		new WaitUntil(new StyledTextHasText(contentsText), TimePeriod.NORMAL, false);
		return contentsText.getText();
	}
}
