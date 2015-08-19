package org.jboss.tools.switchyard.reddeer.condition;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.condition.WaitCondition;

public class TableHasRow implements WaitCondition {

	private Table table;
	private Matcher<String> matcher;
	private List<TableItem> items;

	public TableHasRow(Table table, Matcher<String> matcher) {
		this.table = table;
		this.matcher = matcher;
		this.items = new ArrayList<TableItem>();
	}

	@Override
	public boolean test() {
		items = table.getItems();
		for (TableItem item : items) {
			if (matcher.matches(item.getText())) {
				item.select();
				return true;
			}
		}
		return false;
	}

	@Override
	public String description() {
		StringBuffer message = new StringBuffer();
		message.append("an item matching ").append(matcher.toString()).append(" in\n");
		for (TableItem item : items) {
			message.append("\t").append(item.getText()).append("\n");
		}
		return message.toString();
	}

}
