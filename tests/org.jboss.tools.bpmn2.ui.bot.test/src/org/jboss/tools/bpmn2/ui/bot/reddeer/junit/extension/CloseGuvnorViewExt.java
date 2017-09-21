package org.jboss.tools.bpmn2.ui.bot.reddeer.junit.extension;

import org.apache.log4j.Logger;
import org.eclipse.reddeer.junit.extensionpoint.IBeforeTest;
import org.eclipse.reddeer.workbench.impl.view.WorkbenchView;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

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
		try {
			new WorkbenchView("Guvnor Repositories").close();
		} catch (Exception e) {
			// the view is already closed
		}
		try {
			new WorkbenchView("Guvnor Repositories").close();
		} catch (Exception e) {
			// the view is already closed
		}
	}

	@Override
	public boolean hasToRun() {
		return !closedAlready;
	}

	@Override
	public long getPriority() {
		return 0;
	}

}
