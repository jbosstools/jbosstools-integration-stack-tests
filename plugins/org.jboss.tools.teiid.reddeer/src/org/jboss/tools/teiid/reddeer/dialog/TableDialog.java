package org.jboss.tools.teiid.reddeer.dialog;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class TableDialog extends AbstractDialog{
	private static final Logger log = Logger.getLogger(TableDialog.class);
	
	public TableDialog(boolean viewTable) {
		super(viewTable ? "Create Relational View Table" : "Create Relational Table");
	}

	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsActive(title), TimePeriod.NORMAL);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	public TableDialog setName(String name){
		log.info("Setting table name to '" + name + "'");
		new LabeledText("Name").setText(name);
		return this;
	}
	
	public TableDialog setTransformationSql(String sql){
		log.info("Setting transformation SQL to " + sql);
		new DefaultTabItem("Transformation SQL").activate();
		new DefaultStyledText(new DefaultGroup("SQL Definition")).setText(sql);
		return this;
	}
	
	public TableDialog addColumn(String name, String dataType, String length){
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
}
