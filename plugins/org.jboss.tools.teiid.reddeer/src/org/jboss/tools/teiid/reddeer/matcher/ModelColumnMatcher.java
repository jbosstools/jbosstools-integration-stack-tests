/**
 * 
 */
package org.jboss.tools.teiid.reddeer.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.eclipse.reddeer.swt.api.TableItem;

/**
 * @author asmigala
 *
 */
public class ModelColumnMatcher extends BaseMatcher<TableItem> {

	private final String columnName;
	private final String tableName;

	public ModelColumnMatcher(String tableName, String columnName) {
		this.columnName = columnName;
		this.tableName = tableName;
	}

	@Override
	public boolean matches(Object arg0) {
		if (arg0 instanceof TableItem) {
			TableItem item = ((TableItem) arg0);
			return item.getText(0).equals(tableName) && item.getText(1).equals(columnName);
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("is a model column called " + columnName + " in table " + tableName);

	}

}
