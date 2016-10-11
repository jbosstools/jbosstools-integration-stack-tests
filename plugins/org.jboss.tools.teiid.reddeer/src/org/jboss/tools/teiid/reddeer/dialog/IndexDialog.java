package org.jboss.tools.teiid.reddeer.dialog;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class IndexDialog extends AbstractDialog{
	private static final Logger log = Logger.getLogger(IndexDialog.class);

	public IndexDialog(boolean viewIndex) {
		super(viewIndex ? "Create Relational View Index" : "Create Relational Index");	
	}

	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsActive(title), TimePeriod.NORMAL);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	public IndexDialog setName(String name){
		log.info("Setting table name to '" + name + "'");
		new LabeledText("Name").setText(name);
		return this;
	}
	
	public IndexDialog setNameInSource(String name){
		log.info("Setting name is source to '" + name + "'");
		new LabeledText("Name In Source").setText(name);
		return this;
	}
	
	public IndexDialog addReferencedColumns(String tableName, String column,boolean viewModel){
		log.info("Setting referenced columns");
		new DefaultTabItem("Referenced Columns").activate();
		new PushButton("...").click();
		new DefaultShell("Table Selection");
		new DefaultTable(0).getItem(tableName).select();	
		new PushButton("OK").click();
		if(viewModel){
			new DefaultShell("Create Relational View Index");
		}else{
			new DefaultShell("Create Relational Index");
		}
		new DefaultTable(0).getItem(column).setChecked(true);
		return this;
	}
	
	public IndexDialog addProperties( boolean autoUpdate, boolean nullable, boolean unique){
		log.info("Setting properties");
		new DefaultTabItem("Properties").activate();
		if (autoUpdate){
			new CheckBox("Auto Update").click();
		}
		if (nullable){
			new CheckBox("Nullable").click();
		}
		if (unique){
			new CheckBox("Unique").click();
		}
		return this;
	}
	
	public IndexDialog addDescription(String text){
		log.info("Setting description");
		new DefaultTabItem("Description").activate();
		new DefaultStyledText().setText(text);
		return this;
	}
}