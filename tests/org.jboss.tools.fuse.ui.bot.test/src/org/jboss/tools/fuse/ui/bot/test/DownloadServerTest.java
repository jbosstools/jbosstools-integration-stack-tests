package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.fuse.reddeer.preference.ServerRuntimePreferencePage;
import org.jboss.tools.fuse.reddeer.server.ServerManipulator;
import org.jboss.tools.fuse.reddeer.utils.ResourceHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests a download server runtime feature
 * 
 * @author tsedmik
 */
@RunWith(RedDeerSuite.class)
public class DownloadServerTest extends DefaultTest {

	private static final String ADD_BUTTON = "Add...";
	private static final String NEXT_BUTTON = "Next >";
	private static final String BACK_BUTTON = "< Back";
	private static final String CANCEL_BUTTON = "Cancel";
	private static final String FINISH_BUTTON = "Finish";
	private static final String NEW_WINDOW = "New Server Runtime Environment";
	private static final String SERVER_SECTION = "JBoss Fuse";
	private static final String SERVER_TYPE = "Apache Karaf 3.0";
	private static final String DOWNLOAD_LINK = "Download and install runtime...";
	private static final String DOWNLOAD_FOLDER = "Download folder:";
	private static final String DOWNLOAD_TITLE = "Download Runtimes";
	private static final String INSTALL_FOLDER = "Install folder:";

	@Test
	public void downloadServerRuntimeTest() {

		new ServerRuntimePreferencePage().open();
		new PushButton(ADD_BUTTON).click();
		new DefaultShell(NEW_WINDOW).setFocus();
		new DefaultTreeItem(SERVER_SECTION, SERVER_TYPE).select();
		new PushButton(NEXT_BUTTON).click();

		DefaultLink downloadLink = new DefaultLink(DOWNLOAD_LINK);
		for (int i = 0; i < 10; i++) {
			if (downloadLink.isEnabled()) break;
			AbstractWait.sleep(TimePeriod.SHORT);
		}
		new DefaultLink(DOWNLOAD_LINK).click();

		new WaitUntil(new ShellWithTextIsAvailable(DOWNLOAD_TITLE));
		new DefaultShell(DOWNLOAD_TITLE);

		PushButton buttonNext = new PushButton(NEXT_BUTTON);
		PushButton buttonBack = new PushButton(BACK_BUTTON);
		PushButton buttonCancel = new PushButton(CANCEL_BUTTON);
		PushButton buttonFinish = new PushButton(FINISH_BUTTON);

		assertTrue(buttonNext.isEnabled());
		assertFalse(buttonBack.isEnabled());
		assertFalse(buttonFinish.isEnabled());
		assertTrue(buttonCancel.isEnabled());

		buttonNext.click();

		assertFalse(buttonNext.isEnabled());
		assertTrue(buttonBack.isEnabled());
		assertFalse(buttonFinish.isEnabled());
		assertTrue(buttonCancel.isEnabled());

		new RadioButton(0).toggle(true);

		assertTrue(buttonNext.isEnabled());
		assertTrue(buttonBack.isEnabled());
		assertFalse(buttonFinish.isEnabled());
		assertTrue(buttonCancel.isEnabled());

		buttonNext.click();
		new LabeledText(DOWNLOAD_FOLDER).setText(ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, "target"));
		new LabeledText(INSTALL_FOLDER).setText(ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, "target"));
		buttonFinish.click();

		AbstractWait.sleep(TimePeriod.getCustom(2));
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(3600));
 		AbstractWait.sleep(TimePeriod.getCustom(2));
		new PushButton(FINISH_BUTTON).click();
		assertEquals(1, ServerManipulator.getServerRuntimes().size());	
	}
}
