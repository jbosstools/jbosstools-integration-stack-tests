package org.jboss.tools.teiid.reddeer.dialog;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class ViewDialog extends AbstractDialog{
	private static final Logger log = Logger.getLogger(ViewDialog.class);
	
	public ViewDialog() {
		super("Create Relational View");
	}

	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsActive(title), TimePeriod.NORMAL);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	public ViewDialog setName(String name){
		log.info("Setting table name to '" + name + "'");
		new LabeledText("Name").setText(name);
		return this;
	}
	
	public ViewDialog setNameInSource(String name){
		log.info("Setting name is source to '" + name + "'");
		new LabeledText("Name In Source").setText(name);
		return this;
	}
	
	public ViewDialog setCardinality(String value){
		log.info("Setting cardinality to '" + value + "'");
		new DefaultTabItem("Properties").activate();
		new LabeledText("Cardinality").setText(value);
		return this;
	}
	
	public ViewDialog chceckSupportUpdateIsSystemTable(boolean supportTable, boolean isSystemTable){
		log.info("Setting checkboxes to ");
		new DefaultTabItem("Properties").activate();
		if (!new CheckBox("Supports Update").isChecked() && supportTable){
			new CheckBox("Supports Update").click();			
		}
		if (!new CheckBox("Is System Table").isChecked() && isSystemTable){			
			new CheckBox("Is System Table").click();
		}
		return this;
	}
	
	public ViewDialog setDescription(String value){
		log.info("Setting description to '" + value + "'");
		new DefaultTabItem("Properties").activate();
		new DefaultStyledText().setText(value);
		return this;
	}
	
	public ViewDialog addColumn(String name, String dataType, String length){
		log.info("Adding column " + name + " " + dataType + " " + length);
		new DefaultTabItem("Columns").activate();
		new PushButton("Add").click();
		DefaultTable table = new DefaultTable();
		table.getItem(table.rowCount() - 1).select();
		// TODO TEIIDDES-2903
		new PushButton("Edit...").click();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultShell("Edit Column");
		new LabeledText("Name").setText(name);
		new LabeledCombo("Data Type").setSelection(dataType);
		new LabeledText("Length").setText(length);
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
		this.activate();
		return this;
	}
	
	public ViewDialog setMaterializedTable(String table){
		new DefaultTabItem("Properties").activate();
		new CheckBox("Materialized").click();
		new PushButton("...").click();
		new DefaultShell("Table Selection");
		new DefaultTable(0).getItem(table).select();	
		new PushButton("OK").click();
		return this;
	}
}
