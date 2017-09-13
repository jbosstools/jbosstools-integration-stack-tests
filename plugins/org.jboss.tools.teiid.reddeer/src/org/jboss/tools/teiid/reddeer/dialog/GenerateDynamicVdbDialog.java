package org.jboss.tools.teiid.reddeer.dialog;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.impl.tab.DefaultTabItem;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.condition.StyledTextHasText;

public class GenerateDynamicVdbDialog extends AbstractDialog {
	private static final Logger log = Logger.getLogger(GenerateDynamicVdbDialog.class);

	public GenerateDynamicVdbDialog() {
		super("Save as VDB XML file");
		log.info("Generate dynamic vdb dialog is opened");

	}
	
	@Override
	public void activate() {
	    super.activate();
	    new DefaultTabItem("Dynamic VDB Definition").activate();
	}
	
	public GenerateDynamicVdbDialog setName(String name) {
		log.info("Set name to: '" + name + "'");
		activate();	
		new LabeledText("Name").setText(name);
		return this;
	}
	
	public GenerateDynamicVdbDialog setFileName(String name) {
		log.info("Set file name to: '" + name + "'");
		activate();	
		new LabeledText("File Name").setText(name);
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
		new PushButton("Browse...").click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(location).select();
		new PushButton("OK").click();
		activate();
		return this;
	}

	public GenerateDynamicVdbDialog setExcludeSourceMetadata(boolean checked) {
		log.info("Set Exclude Source Metadata is : '" + checked + "'");
		activate();
	    new DefaultTabItem("Options").activate();
		CheckBox checkBox = new CheckBox("Exclude source DDL metadata");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}

	public GenerateDynamicVdbDialog setSuppressAttributesWithDefaultValues(boolean checked) {
		log.info("Set Suppress Attributes With Default Values is : '" + checked + "'");
		activate();
		new DefaultTabItem("Options").activate();
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
		AbstractWait.sleep(TimePeriod.SHORT);
		if (new ShellIsAvailable("Generate Dynamic VDB Status ").test()){
			new PushButton("OK").click();
		}
		return this;
	}

	public String getContents() {
		activate();
		DefaultStyledText contentsText = new DefaultStyledText(new DefaultGroup("Dynamic VDB XML Contents"));
		new WaitUntil(new StyledTextHasText(contentsText), TimePeriod.DEFAULT, false);
		return contentsText.getText();
	}

	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
        new OkButton().click();
		new WaitWhile(new ShellIsActive(title), TimePeriod.DEFAULT);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
}
