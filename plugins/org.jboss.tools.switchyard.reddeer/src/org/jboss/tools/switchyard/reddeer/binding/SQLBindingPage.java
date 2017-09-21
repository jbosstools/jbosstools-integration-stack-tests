package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * SQL binding page
 * 
 * @author apodhrad
 * 
 */
public class SQLBindingPage extends OperationOptionsPage<SQLBindingPage> {

	public LabeledText getInitialDelayMS() {
		return new LabeledText("Initial Delay (MS)");
	}

	public LabeledText getPeriod() {
		return new LabeledText("Period*");
	}

	public LabeledText getPlaceholder() {
		return new LabeledText("Placeholder:");
	}

	public LabeledText getDataSource() {
		return new LabeledText("Data Source*");
	}

	public LabeledText getQuery() {
		return new LabeledText("Query*");
	}

}
