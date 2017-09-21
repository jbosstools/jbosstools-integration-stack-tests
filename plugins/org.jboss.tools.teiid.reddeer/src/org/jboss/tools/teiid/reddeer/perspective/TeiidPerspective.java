package org.jboss.tools.teiid.reddeer.perspective;

import org.eclipse.reddeer.eclipse.ui.perspectives.AbstractPerspective;

/**
 * Represents a Teiid Designer perspective.
 */
public class TeiidPerspective extends AbstractPerspective {
	private static final String NAME = "Teiid Designer";

	public TeiidPerspective() {
		super(NAME);
	}

	public static TeiidPerspective getInstance() {
		return new TeiidPerspective();
	}
	
	public static void activate(){
		new TeiidPerspective().open();
	}
}
