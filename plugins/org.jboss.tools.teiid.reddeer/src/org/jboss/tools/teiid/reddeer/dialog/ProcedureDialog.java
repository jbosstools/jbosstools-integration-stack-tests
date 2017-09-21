package org.jboss.tools.teiid.reddeer.dialog;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.exception.SWTLayerException;
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

public class ProcedureDialog extends AbstractDialog {
	private static final Logger log = Logger.getLogger(ProcedureDialog.class);

	public static class RestMethod{
		public static final String GET = "GET";
		public static final String DELETE = "DELETE";
		public static final String POST = "POST";
		public static final String PUT = "PUT";
	}
	
	public ProcedureDialog(boolean viewProcedure) {		
		super(viewProcedure ? "Create Relational View Procedure" : "Create Relational Procedure");
	}

	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new PushButton("OK").click();
		new WaitWhile(new ShellIsActive(title), TimePeriod.DEFAULT);
	}
	
	public ProcedureDialog setName(String name){
		log.info("Setting procedure's name to " + name);
		new LabeledText("Name").setText(name);
		return this;
	}
	
	public ProcedureDialog setNameInSource(String name){
		log.info("Setting procedure's name in source to " + name);
		new LabeledText("Name In Source").setText(name);
		return this;
	}
	
	public ProcedureDialog toggleRest(boolean flag){
		log.info((flag) ? "Setting REST to enabled" : "Setting REST to disabled");
		new DefaultTabItem("Properties").activate();
		new CheckBox("Enable REST for this procedure").toggle(flag);
		return this;
	}
	
	/**
	 * Sets REST method of this procedure.
	 * @param method ViewProcedureDialog.RestMethod.*;
	 */
	public ProcedureDialog setRestMethod(String method){
		log.info("Setting REST method to " + method);
		new DefaultTabItem("Properties").activate();
		new LabeledCombo("REST Method").setSelection(method);
		return this;
	}
	
	/**
	 * Sets REST URI of this procedure.
	 */
	public ProcedureDialog setRestUri(String uri){
		log.info("Setting REST URI to " + uri);
		new DefaultTabItem("Properties").activate();
		new LabeledText("URI").setText(uri);
		return this;
	}
	
	public ProcedureDialog addParameter(String name, String dataType, String length, String direction){
		log.info("Adding parameter " + name + " " + dataType + " " + length + " " + direction);
		new DefaultTabItem("Parameters").activate();
		new PushButton("Add").click();
		DefaultTable table = new DefaultTable();
		table.getItem(table.rowCount() - 1).select();
		new PushButton("Edit...").click();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultShell("Edit Parameter");
		new LabeledText("Name").setText(name);
		new LabeledCombo("Data Type").setSelection(dataType);
		new LabeledText("Length").setText(length);
		new LabeledCombo("Direction").setSelection(direction);
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
		this.activate();
		return this;
	}
	
	public ProcedureDialog deleteParameter(String name){
		new DefaultTabItem("Parameters").activate();
		new DefaultTable(0).getItem(name).select();
		new PushButton("Delete").click();
		return this;
	}
	
	public ProcedureDialog setTransformationSql(String sql){
		log.info("Setting transformation SQL to " + sql);
		new DefaultTabItem("Transformation SQL").activate();
		new DefaultStyledText(new DefaultGroup("SQL Definition")).setText(sql);
		return this;
	}
	
	public ProcedureDialog toggleResultSet(boolean flag){
		log.info((flag) ? "Setting Result Set as included" : "Setting Result Set as excluded");
		new DefaultTabItem("Result Set").activate();
		new CheckBox("Include").toggle(flag);
		return this;
	}
	
	public ProcedureDialog setResultSetName(String resultSetName){
		log.info("Setting Result Set name to " + resultSetName);
		new DefaultTabItem("Result Set").activate();
		new DefaultText(3).setText(resultSetName);
		return this;
	}
	
	public ProcedureDialog addResultSetColumn(String name, String dataType, String length){
		log.info("Adding column " + name + " " + dataType + " " + length + " to Result Set");
		new DefaultTabItem("Result Set").activate();
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
	
	public ProcedureDialog setDescription(String text){
		log.info("Setting description ");
		new DefaultTabItem("Description").activate();
		new DefaultStyledText().setText(text);
		return this;
	}
	
	public ProcedureDialog setPropertyTab(String updateCount, boolean nonPrepared){
		log.info("Setting property tab ");
		new DefaultTabItem("Properties").activate();
		new LabeledCombo("Update Count").setSelection(updateCount);
		if(nonPrepared){
			new CheckBox("Non-Prepared").click();
		}
		return this;
	}
	
	public ProcedureDialog setSqlTemplate(String templateName, String insertOption){
		log.info("Inserting SQL tamplate " + templateName + " with option " + insertOption);
		new DefaultTabItem("Transformation SQL").activate();
		new PushButton("Select SQL Template").click();
		new DefaultShell("Choose a SQL Template");
		new RadioButton(new DefaultGroup("Template Options"), templateName).click();
		new RadioButton(new DefaultGroup("Insert Text Options"), insertOption).click();
		new OkButton().click();
		try {
			new DefaultShell("Confirm Replace SQL Text");
			new OkButton().click();
			new WaitWhile(new ShellIsActive("Confirm Replace SQL Text"), TimePeriod.DEFAULT);
		} catch (SWTLayerException e) {	
			// shell not opened -> continue
		}
		this.activate();
		return this;
	}	
	
	public String getTransformationSql(){
		log.info("Returning transformation SQL");
		new DefaultTabItem("Transformation SQL").activate();
		return new DefaultStyledText(new DefaultGroup("SQL Definition")).getText();
	}
}
