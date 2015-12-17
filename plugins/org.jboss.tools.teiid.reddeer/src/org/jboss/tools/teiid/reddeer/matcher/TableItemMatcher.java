package org.jboss.tools.teiid.reddeer.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
import org.jboss.reddeer.swt.api.TableItem;

public class TableItemMatcher extends BaseMatcher<TableItem> {

	private int columnIndex;
	private Matcher<String> matcher;
	
	
	public TableItemMatcher(int columnIndex, String text) {
		this.columnIndex = columnIndex;
		matcher = new IsEqual<String>(text);
	}
	
	public TableItemMatcher(int columnIndex, Matcher<String> matcher) {
		this.columnIndex = columnIndex;
		this.matcher = matcher;
	}

	@Override
	public boolean matches(Object obj) {
		if(obj instanceof TableItem){
			TableItem item = ((TableItem)obj);
			return matcher.matches(item.getText(columnIndex));
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("column " + columnIndex + " of table item matches ");
		matcher.describeTo(description);
	}

}
