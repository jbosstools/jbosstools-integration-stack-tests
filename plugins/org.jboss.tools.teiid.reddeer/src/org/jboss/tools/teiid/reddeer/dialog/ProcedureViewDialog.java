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
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class ProcedureViewDialog extends AbstractDialog {
	private static final Logger log = Logger.getLogger(ProcedureViewDialog.class);

	public static class RestMethod{
		public static final String GET = "GET";
		public static final String DELETE = "DELETE";
		public static final String POST = "POST";
		public static final String PUT = "PUT";
	}
	
	public ProcedureViewDialog() {
		super("Create Relational View Procedure");
	}

	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(title), TimePeriod.NORMAL);
	}
	
	public ProcedureViewDialog setName(String name){
		log.info("Setting procedure's name to " + name);
		new LabeledText("Name").setText(name);
		return this;
	}
	
	public ProcedureViewDialog setNameInSource(String name){
		log.info("Setting procedure's name in source to " + name);
		new LabeledText("Name In Source").setText(name);
		return this;
	}
	
	public ProcedureViewDialog toggleRest(boolean flag){
		log.info((flag) ? "Setting REST to enabled" : "Setting REST to disabled");
		new DefaultTabItem("Properties").activate();
		new CheckBox("Enable REST for this procedure").toggle(flag);
		return this;
	}
	
	/**
	 * Sets REST method of this procedure.
	 * @param method ViewProcedureDialog.RestMethod.*;
	 */
	public ProcedureViewDialog setRestMethod(String method){
		log.info("Setting REST method to " + method);
		new DefaultTabItem("Properties").activate();
		new LabeledCombo("REST Method").setSelection(method);
		return this;
	}
	
	/**
	 * Sets REST URI of this procedure.
	 */
	public ProcedureViewDialog setRestUri(String uri){
		log.info("Setting REST URI to " + uri);
		new DefaultTabItem("Properties").activate();
		new LabeledText("URI").setText(uri);
		return this;
	}
	
	public ProcedureViewDialog addParameter(String name, String dataType, String length, String direction){
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
	
	public ProcedureViewDialog setTransformationSql(String sql){
		log.info("Setting transformation SQL to " + sql);
		new DefaultTabItem("Transformation SQL").activate();
		try {
			new DefaultStyledText(new DefaultGroup("SQL Definition")).setText(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
}
