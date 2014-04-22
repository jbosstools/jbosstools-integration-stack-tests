package org.jboss.tools.bpmn2.ui.bot.test;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.ScreenshotCaptureListener;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * 
 */
public class BPMN2Suite extends RedDeerSuite {

	protected static Logger log = Logger.getLogger(BPMN2Suite.class);

	/**
	 * 
	 * @param clazz
	 * @param builder
	 * @throws InitializationError
	 */
	public BPMN2Suite(Class<?> clazz, RunnerBuilder builder) throws InitializationError {
		super(clazz, foo(builder));
	}

	/**
	 * 
	 * @param builder
	 * @return
	 */
	private static RunnerBuilder foo(RunnerBuilder builder) {
		closeWelcomeView();
		closeGuvnorView();
		return builder;
	}
	
	/**
	 * 
	 */
	private static void closeWelcomeView() {
		try {
			new SWTWorkbenchBot().viewByTitle("Welcome").close();
		} catch (Exception ex) {
			log.debug("An attempt to close the 'Welcome view' failed with message '" + ex.getMessage() + "'");
		}
	}
	
	/**
	 * 
	 */
	private static void closeGuvnorView() {
		try {
			new SWTWorkbenchBot().viewByTitle("Guvnor Repositories").close();
			new SWTWorkbenchBot().viewByTitle("Guvnor Resource History").close();
		} catch (Exception ex) {
			log.debug("An attempt to close the 'Guvnor views' failed with message '" + ex.getMessage() + "'");
		}
	}

	@Override
	public void run(RunNotifier notifier) {
		RunListener failureSpy = new ScreenshotCaptureListener();
		notifier.removeListener(failureSpy);
		notifier.addListener(failureSpy);
		try {
			super.run(notifier);
		} finally {
			notifier.removeListener(failureSpy);
		}
	}

}