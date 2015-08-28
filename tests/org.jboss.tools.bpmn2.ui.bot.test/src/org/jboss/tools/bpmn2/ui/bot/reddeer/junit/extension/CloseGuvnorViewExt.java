package org.jboss.tools.bpmn2.ui.bot.reddeer.junit.extension;

import org.apache.log4j.Logger;
import org.eclipse.ui.IViewPart;
import org.jboss.reddeer.junit.extensionpoint.IBeforeTest;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.core.handler.ViewHandler;
import org.jboss.reddeer.core.lookup.WorkbenchPartLookup;

public class CloseGuvnorViewExt implements IBeforeTest {
	
	private static final Logger log = Logger.getLogger(CloseGuvnorViewExt.class);

	private static boolean closedAlready;
	
	@Override
	public void runBeforeTest() {
		closeGuvnorViews();
		closedAlready = true;
	}
	
	private void closeGuvnorViews() {
		log.debug("Trying to close Guvnor views");
		IViewPart repositories = WorkbenchPartLookup.getInstance().getViewByTitle(new WithTextMatcher("Guvnor Repositories"));
		if(repositories != null) {
			ViewHandler.getInstance().close(repositories);
		}
		
		IViewPart history = WorkbenchPartLookup.getInstance().getViewByTitle(new WithTextMatcher("Guvnor Resource History"));
		if(history != null) {
			ViewHandler.getInstance().close(history);
		}
	}

	@Override
	public boolean hasToRun() {
		return !closedAlready;
	}

}
