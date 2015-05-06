package org.jboss.tools.switchyard.reddeer.condition;

import java.util.List;

import org.hamcrest.Matcher;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.condition.WaitCondition;

public class TableHasRow implements WaitCondition {

	private Table table;
	private Matcher<String> matcher;

	public TableHasRow(Table table, Matcher<String> matcher) {
		this.table = table;
		this.matcher = matcher;
	}
	
	@Override
	public boolean test() {
		List<TableItem> items = table.getItems();
		for (TableItem item: items) {
			if (matcher.matches(item.getText())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String description() {
		return null;
	}

}
