package org.jboss.tools.teiid.reddeer.dialog;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.condition.StyledTextHasText;

public class GenerateDynamicVdbDialog extends AbstractDialog {
	private static final Logger log = Logger.getLogger(ProcedureViewDialog.class);

	public GenerateDynamicVdbDialog() {
		super("Generate Dynamic VDB");
		log.info("Generate dynamic vdb dialog is opened");

	}
	
	public GenerateDynamicVdbDialog setName(String name) {
		log.info("Set name to: '" + name + "'");
		activate();	
		new LabeledText("Dynamic VDB Name").setText(name);
		return this;
	}

	public GenerateDynamicVdbDialog setVersion(String version) {
		log.info("Set version to: '" + version + "'");
		activate();	
		new LabeledText("Version").setText(version);
		return this;
	}

	public GenerateDynamicVdbDialog setLocation(String... location) {
		log.info("Set location to: '" + location + "'");
		activate();	
		new PushButton("Change...").click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(location).select();
		new PushButton("OK").click();
		activate();
		return this;
	}

	public GenerateDynamicVdbDialog setExcludeSourceMetadata(boolean checked) {
		log.info("Set Exclude Source Metadata is : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Exclude source DDL metadata");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}

	public GenerateDynamicVdbDialog setSuppressAttributesWithDefaultValues(boolean checked) {
		log.info("Set Suppress Attributes With Default Values is : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Suppress attributes with default values");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}

	public GenerateDynamicVdbDialog setOvewriteExisting(boolean checked) {
		log.info("Set Ovewrite Existing is : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Overwrite existing files");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}

	public GenerateDynamicVdbDialog generate() {
		log.info("Generate dynamic VDB");
		activate();
		new PushButton("Generate").click();
		if (new ShellWithTextIsAvailable("Generate Dynamic VDB Status\", ").test()){
			new PushButton("OK").click();
		}
		return this;
	}

	public String getContents() {
		activate();
		DefaultStyledText contentsText = new DefaultStyledText(new DefaultGroup("Dynamic VDB XML Contents"));
		new WaitUntil(new StyledTextHasText(contentsText), TimePeriod.NORMAL, false);
		return contentsText.getText();
	}
	
	public GenerateDynamicVdbDialog next(){
		log.info("Go to next dialog page");
		new PushButton("Next >").click();
		return this;
	}

	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive(title), TimePeriod.NORMAL);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
}
