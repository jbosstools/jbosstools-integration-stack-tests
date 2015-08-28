package org.jboss.tools.teiid.reddeer;

import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.core.lookup.WidgetLookup;
import org.jboss.reddeer.core.util.Display;

public abstract class ModelObject {

	protected static final Logger LOGGER = Logger.getLogger(ModelObject.class);
	
	public static final String PROPERTIES = "Properties";
	public static final String NATIVE_QUERY = "Native Query";
	public static final String TRANSFORMATION_SQL = "Transformation SQL";
	
	public void fillTransformationSQL(Properties props) {
		
		String prop = props.getProperty("template");
		new DefaultTabItem(TRANSFORMATION_SQL).activate();
		
		if (prop != null) {
			
			new PushButton("Select SQL Template").click();
			new RadioButton(prop).click();
			new PushButton("OK").click();
		}

		prop = props.getProperty("sql");
		if (prop != null) {
			
			new DefaultStyledText().setText(prop);
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
			new DefaultTable().getItem(cols.length - i).doubleClick();
			setTextToFocusedTextArea(col);
			i--;
		}
		return cols.length;
	}
	
	public void setupReturnParam(String param, int lines){
		new PushButton("Add").click();
		new DefaultTable().getItem(lines).doubleClick();
		setTextToFocusedTextArea(param);
		new SWTWorkbenchBot().table().click(lines,3);
		new SWTWorkbenchBot().ccomboBox().setSelection("RETURN");
	}
	
	private void setTextToFocusedTextArea(final String text){
		final Widget w = WidgetLookup.getInstance().getFocusControl();
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				LOGGER.debug("Trying to set text \"" + text + "\" to widget \"" + w + "\".");
				((Text) w).setText(text);
				KeyboardFactory.getKeyboard().type(SWT.CR);
			}
		});
	}
}
