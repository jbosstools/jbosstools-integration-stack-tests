package org.jboss.tools.switchyard.reddeer.condition;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.api.TableItem;
import org.hamcrest.Matcher;

public class TableHasRow extends AbstractWaitCondition {

	private Table table;
	private Matcher<String> matcher;
	private List<TableItem> items;
	private TableItem tableItem;

	public TableHasRow(Table table, Matcher<String> matcher) {
		this.table = table;
		this.matcher = matcher;
		this.items = new ArrayList<TableItem>();
	}

	@Override
	public boolean test() {
		items = table.getItems();
		for (TableItem item : items) {
			if (!item.getSWTWidget().isDisposed() && matcher.matches(item.getText())) {
				tableItem = item;
				item.select();
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TableItem getResult() {
		return tableItem;
	}

	@Override
	public String description() {
		StringBuffer message = new StringBuffer();
		message.append("an item matching ").append(matcher.toString()).append(" in\n");
		for (TableItem item : items) {
			String text = null;
			if (!item.getSWTWidget().isDisposed()) {
				text = item.getText();
			}
			message.append("\t").append(text).append("\n");
		}
		return message.toString();
	}

}
