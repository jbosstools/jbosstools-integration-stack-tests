package org.jboss.tools.teiid.reddeer;

import java.util.Properties;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;

public abstract class ModelObject {

	public static final String PROPERTIES = "Properties";
	public static final String NATIVE_QUERY = "Native Query";
	public static final String TRANSFORMATION_SQL = "Transformation SQL";
	
	public void fillTransformationSQL(Properties props) {
		String prop = props.getProperty("template");
		if (prop != null){
			new SWTWorkbenchBot().tabItem(TRANSFORMATION_SQL).activate();
			//select template
			new PushButton("Select SQL Template").click();
			new RadioButton(prop).click();
			new PushButton("OK").click();
		}	
		prop = props.getProperty("sql");
		if (prop != null){
			new SWTWorkbenchBot().tabItem(TRANSFORMATION_SQL).activate();
			new SWTWorkbenchBot().styledText().setText(prop);
		}
	}
	
	public void fillNativeQuery(Properties props) {
			String prop = props.getProperty("nativeQuery");
			if (prop != null){
				new SWTWorkbenchBot().tabItem(NATIVE_QUERY).activate();
				new SWTWorkbenchBot().styledText().setText(prop);
			}
		
	}
	
	public int setupTableProps(String prop){
		String[] cols = {prop};
		if (prop.contains(",")){
			cols = prop.split(",");
		} 	
		int i = cols.length;
		for (String col : cols){
			new PushButton("Add").click();
			String defaultName = new DefaultTable().getItem(cols.length - i).getText(0);
			new DefaultTable().getItem(cols.length - i).doubleClick();
			new DefaultText(defaultName).setText(col);
			i--;
		}
		return cols.length;
	}
	
	public void setupReturnParam(String param, int lines){
		new PushButton("Add").click();
		String defaultName = new DefaultTable().getItem(lines).getText(0);
		new DefaultTable().getItem(lines).doubleClick();
		new DefaultText(defaultName).setText(param);
		new SWTWorkbenchBot().table().click(lines,3);
		new SWTWorkbenchBot().ccomboBox().setSelection("RETURN");
	}
}
