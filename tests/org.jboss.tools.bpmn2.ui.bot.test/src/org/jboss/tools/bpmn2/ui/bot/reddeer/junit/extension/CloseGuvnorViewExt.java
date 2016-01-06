package org.jboss.tools.bpmn2.ui.bot.reddeer.junit.extension;

import org.apache.log4j.Logger;
import org.eclipse.ui.IViewPart;
import org.jboss.reddeer.junit.extensionpoint.IBeforeTest;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.core.handler.ViewHandler;
import org.jboss.reddeer.core.lookup.WorkbenchPartLookup;

public class CloseGuvnorViewExt implements IBeforeTest {

	private static final Logger log = Logger.getLogger(CloseGuvnorViewExt.class);

	private static boolean closedAlready;

	@Override
	public void runBeforeTestClass(String config, TestClass testClass) {
		// TODO Do we really need to close the view here?
	}

	@Override
	public void runBeforeTest(String config, Object target, FrameworkMethod method) {
		closeGuvnorViews();
		closedAlready = true;
	}

	private void closeGuvnorViews() {
		log.debug("Trying to close Guvnor views");
		IViewPart repositories = WorkbenchPartLookup.getInstance()
				.getViewByTitle(new WithTextMatcher("Guvnor Repositories"));
		if (repositories != null) {
			ViewHandler.getInstance().close(repositories);
		}

		IViewPart history = WorkbenchPartLookup.getInstance()
				.getViewByTitle(new WithTextMatcher("Guvnor Resource History"));
		if (history != null) {
			ViewHandler.getInstance().close(history);
		}
	}

	@Override
	public boolean hasToRun() {
		return !closedAlready;
	}

}
