package org.jboss.tools.teiid.reddeer.dialog;

import static org.junit.Assert.assertEquals;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.impl.tab.DefaultTabItem;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

public class TableDialog extends AbstractDialog{
	private static final Logger log = Logger.getLogger(TableDialog.class);
	
	public TableDialog(boolean viewTable) {
		super(viewTable ? "Create Relational View Table" : "Create Relational Table");
	}

	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new OkButton().click();
		new WaitWhile(new ShellIsActive(title), TimePeriod.DEFAULT);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	public TableDialog setName(String name){
		log.info("Setting table name to '" + name + "'");
		new LabeledText("Name").setText(name);
		return this;
	}
	
	public TableDialog setNameInSource(String name){
		log.info("Setting name is source to '" + name + "'");
		new LabeledText("Name In Source").setText(name);
		return this;
	}
	
	public TableDialog setTransformationSql(String sql){
		log.info("Setting transformation SQL to " + sql);
		new DefaultTabItem("Transformation SQL").activate();
		new DefaultStyledText(new DefaultGroup("SQL Definition")).setText(sql);
		return this;
	}
	
	public String getTransformationSql(){
		log.info("Returning transformation SQL");
		new DefaultTabItem("Transformation SQL").activate();
		return new DefaultStyledText(new DefaultGroup("SQL Definition")).getText();
	}
	
	public TableDialog addColumn(String name, String dataType, String length){
		log.info("Adding column " + name + " " + dataType + " " + length);
		new DefaultTabItem("Columns").activate();
		new PushButton("Add").click();
		DefaultTable table = new DefaultTable();
		table.getItem(table.rowCount() - 1).select();
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
	
	public TableDialog setCardinality(String value){
		log.info("Setting cardinality to '" + value + "'");
		new DefaultTabItem("Properties").activate();
		new LabeledText("Cardinality").setText(value);
		return this;
	}
	
	public TableDialog chceckSupportUpdateIsSystemTable(boolean supportTable, boolean isSystemTable){
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
	
	public TableDialog setDescription(String value){
		log.info("Setting description to '" + value + "'");
		new DefaultTabItem("Properties").activate();
		new DefaultStyledText().setText(value);
		return this;
	}
	
	public TableDialog deleteColumn(String value){
		log.info("Deleting column '" + value + "'");
		new DefaultTabItem("Columns").activate();
		int count = new DefaultTable(0).rowCount();	
		new DefaultTable(0).getItem(value).select();
		new PushButton("Delete").click();
		assertEquals(count-1,new DefaultTable(0).rowCount());	
		return this;
	}
	
	public TableDialog setPrimaryKey(String name, String nameInSource, String column){
		log.info("Setting primary key");
		new DefaultTabItem("PK").activate();
		new CheckBox("Include").click();	
		new DefaultText(3).setText(name);
		new DefaultText(4).setText(nameInSource);
		
		new PushButton("Change...").click();
		new DefaultShell("Select Columns");	
		new DefaultTable(0).getItem(column).setChecked(true);
		new PushButton("OK").click();
		AbstractWait.sleep(TimePeriod.SHORT);
		return this;
	}
	
	public TableDialog addUniqueConstraint(String name,String nameInSource, String column){
		log.info("Adding unique constraint to '" + name + "'");
		new DefaultTabItem("UC").activate();
		new PushButton("Add").click();
		new DefaultShell("Create Unique Constraint");
		new LabeledText("Name").setText(name);
		new LabeledText("Name In Source").setText(nameInSource);
		new DefaultTable(0).getItem(column).setChecked(true);
		new PushButton("OK").click();
		AbstractWait.sleep(TimePeriod.SHORT);
		return this;
	}
	
	public TableDialog editUniqueConstraint(String oldName,String newName,String nameInSource, String column){
		log.info("Editing unique constraint to '" + newName + "'");
		new DefaultTabItem("UC").activate();
		new DefaultTable(0).getItem(oldName + " : " + column).select();
		new PushButton("Edit...").click();
		new DefaultShell("Edit Unique Constraint");
		new LabeledText("Name").setText(newName);
		new LabeledText("Name In Source").setText(nameInSource);
		new DefaultTable(0).getItem(column).setChecked(true);
		new PushButton("OK").click();			
		return this;
	}
	
	public TableDialog deleteUniqueConstraint(String name, String column){
		log.info("Deleting unique constraint '" + name + "'");
		new DefaultTabItem("UC").activate();
		new DefaultTable(0).getItem(name+ " : " + column).select();
		new PushButton("Delete").click();		
		return this;
	}
	
	public TableDialog addForeignKey(String name,String nameInSource, String foreignColumn, String column){
		log.info("Adding foreign key to '" + name + "'");
		new DefaultTabItem("FKs").activate();
		new PushButton("Add").click();
		new DefaultShell("Create Foreign Key");
		new LabeledText("Name").setText(name);	
		new LabeledText("Name In Source").setText(nameInSource);
		new DefaultTable(0).getItem(foreignColumn).setChecked(true);
		new DefaultTable(1).getItem(column).setChecked(true);
		new PushButton("OK").click();	
		AbstractWait.sleep(TimePeriod.SHORT);
		return this;
	}
	
	public TableDialog editForeignKey(String oldName,String newName, String nameInSource, String foreignColumn, String column){
		log.info("Editing foreign key to '" + newName + "'");
		new DefaultTabItem("FKs").activate();
		new DefaultTable(0).getItem(oldName).select();
		new PushButton("Edit...").click();
		new DefaultShell("Edit Foreign Key");
		new LabeledText("Name").setText(newName);	
		new LabeledText("Name In Source").setText(nameInSource);
		new DefaultTable(0).getItem(foreignColumn).setChecked(true);
		new DefaultTable(1).getItem(column).setChecked(true);
		new PushButton("OK").click();	
		return this;
	}
	
	public TableDialog deleteForeignKey(String name){
		log.info("Deleting foreign key '" + name + "'");
		new DefaultTabItem("FKs").activate();
		new DefaultTable(0).getItem(name).select();
		new PushButton("Delete").click();		
		return this;
	}
	
	public TableDialog addIndex(String name, String column, boolean autoUpdate, boolean nullable, boolean unique){
		log.info("Adding index '" + name + "'");
		new DefaultTabItem("Indexes").activate();
		new PushButton("Add").click();
		new DefaultShell("Create Index");
		new LabeledText("Name").setText(name);	
		new DefaultTable(0).getItem(column).setChecked(true);
		if (autoUpdate){
			new CheckBox("Auto Update").click();
		}
		if (nullable){
			new CheckBox("Nullable").click();
		}
		if (unique){
			new CheckBox("Unique").click();
		}
		new PushButton("OK").click();
		//new DefaultShell("Create Relational Table");	
		return this;
	}
	
	public TableDialog editIndex(String oldName,String newName, String column, boolean autoUpdate, boolean nullable, boolean unique){
		log.info("Editing index '" + oldName + "'");
		new DefaultTabItem("Indexes").activate();
		new DefaultTable(0).getItem(oldName + " : " + column).select();
		new PushButton("Edit...").click();		
		new DefaultShell("Create Index");
		new LabeledText("Name").setText(newName);	
		new DefaultTable(0).getItem(column).setChecked(true);
		
		if(!new CheckBox("Auto Update").isChecked() && autoUpdate){
			new CheckBox("Auto Update").click();
		}
		if(!new CheckBox("Nullable").isChecked() && nullable){
			new CheckBox("Nullable").click();
		}
		if(!new CheckBox("Unique").isChecked() && unique){
			new CheckBox("Unique").click();
		}
		new PushButton("OK").click();
		return this;
	}
	
	public TableDialog deleteIndex(String name, String column){
		log.info("Deleting index '" + name + "'");
		new DefaultTabItem("Indexes").activate();
		new DefaultTable(0).getItem(name+ " : " + column).select();
		new PushButton("Delete").click();		
		return this;
	}	

	public TableDialog setSqlTemplate(String templateName, String insertOption){
		log.info("Inserting SQL tamplate " + templateName + " with option " + insertOption);
		new DefaultTabItem("Transformation SQL").activate();
		new PushButton("Select SQL Template").click();
		new DefaultShell("Choose a SQL Template");
		new RadioButton(new DefaultGroup("Template Options"), templateName).click();
		new RadioButton(new DefaultGroup("Insert Text Options"), insertOption).click();
        new OkButton().click();
        if (new ShellIsActive("Confirm Replace SQL Text").test()) {
            new DefaultShell("Confirm Replace SQL Text");
            new OkButton().click();
            new WaitWhile(new ShellIsActive("Confirm Replace SQL Text"), TimePeriod.DEFAULT);
        }
		this.activate();
		return this;
	}
}
