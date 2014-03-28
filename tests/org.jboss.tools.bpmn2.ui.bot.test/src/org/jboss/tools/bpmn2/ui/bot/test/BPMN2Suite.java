package org.jboss.tools.bpmn2.ui.bot.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.ScreenshotCaptureListener;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class BPMN2Suite extends RedDeerSuite {

	public BPMN2Suite(Class<?> clazz, RunnerBuilder builder) throws InitializationError {
		super(clazz, foo(builder));
	}

	private static RunnerBuilder foo(RunnerBuilder builder) {
		// readConfigurationProperties();
		closeWelcomeView();
		closeGuvnorView();
		return builder;
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

	@SuppressWarnings("unused")
	private static void readConfigurationProperties() {
		Properties props = null;
		try {
			String propsFilePath = System.getProperty("swtbot.test.properties");

			props = System.getProperties();
			props.load(new FileInputStream(new File(propsFilePath)));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void closeWelcomeView() {
		try {
			new SWTWorkbenchBot().viewByTitle("Welcome").close();
		} catch (Exception ex) {
			// Ignore
		}
	}
	
	private static void closeGuvnorView() {
		try {
			new SWTWorkbenchBot().viewByTitle("Guvnor Repositories").close();
			new SWTWorkbenchBot().viewByTitle("Guvnor Resource History").close();
		} catch (Exception ex) {
			// Ignore
		}
	}

}