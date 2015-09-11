package org.jboss.tools.drools.reddeer.kienavigator.dialog;

import org.jboss.reddeer.swt.impl.table.DefaultTable;

public class AddRepositoryDialog extends Dialog {

	public void selectRepository(String repository) {
		new DefaultTable().select(repository);
	}
}
