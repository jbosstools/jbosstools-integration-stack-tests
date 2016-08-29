package org.jboss.tools.teiid.reddeer;

import java.util.Properties;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

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
		if (prop != null) {
			new DefaultTabItem(NATIVE_QUERY).activate();
			new DefaultStyledText().setText(prop);
		}

	}

	public int setupTableProps(String prop) {
		String[] cols = { prop };
		if (prop.contains(",")) {
			cols = prop.split(",");
		}
		int i = cols.length;
		for (String col : cols) {
			new PushButton("Add").click();
			TableItem item = new DefaultTable().getItem(cols.length - i);
			item.doubleClick();
			KeyboardFactory.getKeyboard().type(col);
			i--;
		}
		return cols.length;
	}
}
