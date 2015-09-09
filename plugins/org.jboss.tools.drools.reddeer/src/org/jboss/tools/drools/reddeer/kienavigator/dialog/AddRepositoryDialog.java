package org.jboss.tools.drools.reddeer.kienavigator.dialog;

import org.jboss.reddeer.swt.impl.list.DefaultList;

public class AddRepositoryDialog extends Dialog {

	public void selectRepository(String repository) {
		new DefaultList(0).select(repository);
	}
}
