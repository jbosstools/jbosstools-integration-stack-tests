package org.jboss.tools.teiid.reddeer.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
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
		if(new ShellIsActive("Table 'Supports Update' Property Changed").test()){
			new PushButton("OK").click();
		}
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
		activate();
		new PushButton("Finish").click();
		new WaitWhile(new ShellIsActive(title), TimePeriod.DEFAULT);
		AbstractWait.sleep(TimePeriod.SHORT);
	}

}
