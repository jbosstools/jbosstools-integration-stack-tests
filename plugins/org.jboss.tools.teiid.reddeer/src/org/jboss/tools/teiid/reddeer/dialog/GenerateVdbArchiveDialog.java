package org.jboss.tools.teiid.reddeer.dialog;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.condition.LabelWithTextIsAvailable;

public class GenerateVdbArchiveDialog extends AbstractDialog {

	private static final Logger log = Logger.getLogger(GenerateVdbArchiveDialog.class);

	public GenerateVdbArchiveDialog() {
		super("Generate VDB Archive");
		log.info("Generate vdb archive dialog is opened");
	}
	
	public GenerateVdbArchiveDialog setVersion(String version) {
		log.info("Set version to: '" + version + "'");
		activate();
		new LabeledText("Version").setText(version);
		return this;
	}

	public GenerateVdbArchiveDialog setLocation(String... location) {
		log.info("Set location to: '" + location + "'");
		activate();
		new PushButton("Change...").click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(location).select();
		new PushButton("OK").click();
		activate();
		return this;
	}

	public GenerateVdbArchiveDialog setArchiveName(String name) {
		log.info("Set archive vdb name to: '" + name + "'");
		activate();
		new LabeledText("Archive VDB Name").setText(name);
		return this;
	}

	public GenerateVdbArchiveDialog setFileName(String name) {
		log.info("Set file name to: '" + name + "'");
		activate();
		new LabeledText("VDB Archive File Name").setText(name);
		return this;
	}

	public GenerateVdbArchiveDialog setDdlAsDescription(boolean checked) {
		log.info("Set parsed DDL as the generated tables' descriptions is : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Set parsed DDL as the generated tables' descriptions");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}

	public GenerateVdbArchiveDialog setOvewriteExisting(boolean checked) {
		log.info("Overwrite existing files is : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Overwrite existing files");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}

	public GenerateVdbArchiveDialog generate() {
		log.info("Generate archive");
		activate();
		new PushButton("Generate").click();
		new WaitWhile(new LabelWithTextIsAvailable("Converting Dynamic Vdb to Xmi Vdb"));
		return this;
	}

	public List<String> getSourceModels() {
		return getModels("Source Models");
	}

	public List<String> getViewModels() {
		return getModels("View Models");
	}

	private List<String> getModels(String group) {
		List<String> result = new ArrayList<String>();
		for (TableItem it : new DefaultTable(new DefaultGroup(group)).getItems()) {
			result.add(it.getText());
		}
		return result;
	}
	
	public GenerateVdbArchiveDialog next(){
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
