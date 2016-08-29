package org.jboss.tools.teiid.reddeer.dialog;

import org.jboss.reddeer.common.logging.Logger;

public class TableDialog extends AbstractDialog{
	private static final Logger log = Logger.getLogger(TableDialog.class);
	
	public TableDialog(boolean viewTable) {
		super(viewTable ? "Create Relational View Table" : "Create Relational Table");
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

}
