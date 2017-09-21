package org.jboss.tools.teiid.reddeer.perspective;

import org.eclipse.reddeer.eclipse.ui.perspectives.AbstractPerspective;
import org.jboss.tools.teiid.reddeer.view.DataSourceExplorer;
import org.jboss.tools.teiid.reddeer.view.SQLResultView;

/**
 * Represents a Database Development perspective.
 */
public class DatabaseDevelopmentPerspective extends AbstractPerspective {
	private static final String NAME = "Database Development";

	private DatabaseDevelopmentPerspective() {
		super(NAME);
	}

	public static final DatabaseDevelopmentPerspective getInstance() {
		return new DatabaseDevelopmentPerspective();
	}
	
	public static void activate(){
		new DatabaseDevelopmentPerspective().open();
	}

	public DataSourceExplorer getExplorerView() {
		open();
		return new DataSourceExplorer();
	}

	public SQLResultView getSqlResultsView() {
		open();
		return new SQLResultView();
	}
}
