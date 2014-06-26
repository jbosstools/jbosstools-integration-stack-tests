package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

/**
 * SQL binding page
 * 
 * @author apodhrad
 * 
 */
public class SQLBindingPage extends OperationOptionsPage<SQLBindingPage> {

	public static final String QUERY = "Query*";
	public static final String DATA_SOURCE = "Data Source*";

	public SQLBindingPage setQuery(String query) {
		new LabeledText(QUERY).setFocus();
		new LabeledText(QUERY).setText(query);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getQuery() {
		return new LabeledText(QUERY).getText();
	}

	public SQLBindingPage setDataSource(String dataSource) {
		new LabeledText(DATA_SOURCE).setFocus();
		new LabeledText(DATA_SOURCE).setText(dataSource);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getDataSource() {
		return new LabeledText(DATA_SOURCE).getText();
	}
}
