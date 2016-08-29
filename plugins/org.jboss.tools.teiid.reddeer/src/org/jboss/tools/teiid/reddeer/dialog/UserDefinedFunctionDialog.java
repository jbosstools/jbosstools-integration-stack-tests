package org.jboss.tools.teiid.reddeer.dialog;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class UserDefinedFunctionDialog extends AbstractDialog {
	private static final Logger log = Logger.getLogger(UserDefinedFunctionDialog.class);
	
	public UserDefinedFunctionDialog() {
		super("Create User Defined Function");
	}

	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(title), TimePeriod.NORMAL);
	}

	public UserDefinedFunctionDialog setName(String name){
		log.info("Setting udf's name to " + name);
		new LabeledText("Name").setText(name);
		return this;
	}
	
	public UserDefinedFunctionDialog addParameter(String name, String dataType, String length, String direction){
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
	
	public UserDefinedFunctionDialog setFunctionCategory(String category){
		log.info("Setting function category to " + category);
		new DefaultTabItem("Properties").activate();
		new LabeledText("Function Category").setText(category);
		return this;
	}
	
	public UserDefinedFunctionDialog setJavaClass(String clazz){
		log.info("Setting java class to " + clazz);
		new DefaultTabItem("Properties").activate();
		new LabeledText("Java Class").setText(clazz);
		return this;
	}

	public UserDefinedFunctionDialog setJavaMethod(String method){
		log.info("Setting java method to " + method);
		new DefaultTabItem("Properties").activate();
		new LabeledText("Java Method").setText(method);
		return this;
	}

	public UserDefinedFunctionDialog setUdfJarPath(String path){
		log.info("Setting jar path to " + path);
		new DefaultTabItem("Properties").activate();
		new LabeledText("UDF Jar Path").setText(path);
		return this;
	}	
}
