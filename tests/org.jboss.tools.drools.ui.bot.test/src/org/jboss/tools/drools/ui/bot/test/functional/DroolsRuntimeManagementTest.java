package org.jboss.tools.drools.ui.bot.test.functional;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed.Jira;
import org.jboss.tools.drools.reddeer.dialog.DroolsRuntimeDialog;
import org.jboss.tools.drools.reddeer.preference.DroolsRuntimesPreferencePage;
import org.jboss.tools.drools.reddeer.preference.DroolsRuntimesPreferencePage.DroolsRuntime;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeReqType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@Runtime(type = RuntimeReqType.DROOLS)
@RunWith(RedDeerSuite.class)
public class DroolsRuntimeManagementTest extends TestParent {
	private static final Logger LOGGER = Logger.getLogger(DroolsRuntimeManagementTest.class);

	@InjectRequirement
	private RuntimeRequirement droolsRequirement;

	@Before
	public void clearRuntimes() {
		deleteAllRuntimes();
	}

	@Test
	public void testAddAndRemoveRuntime() {
		final String name = "testRuntimeCreation";

		// add new runtime
		DroolsRuntimeManagementTest.addRuntime(name, droolsRequirement.getConfig().getRuntimeFamily().getHome(), false);

		// check that runtime was added
		DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
		pref.open();
		DroolsRuntime runtime = null;
		for (DroolsRuntime r : pref.getDroolsRuntimes()) {
			if (r.getName().equals(name)) {
				runtime = r;
				break;
			}
		}
		Assert.assertNotNull("Runtime was not created.", runtime);

		// remove runtime
		pref.removeDroolsRuntime(name);

		// check that runtime was deleted
		for (DroolsRuntime r : pref.getDroolsRuntimes()) {
			Assert.assertNotSame("Runtime was not deleted.", name, r.getName());
		}
	}

	@Test
	public void testWrongLocation() {
		final String name = "testWrongLocation";
		final String location = "/path/to/runtime";

		DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
		pref.open();
		DroolsRuntimeDialog wiz = pref.addDroolsRuntime();
		wiz.setName(name);
		wiz.setLocation(location);

		Assert.assertFalse("It is possible to save invalid path to runtime.", wiz.isValid());
		wiz.cancel();
		pref.cancel();
	}

	@Test
	@Jira("DROOLS-1160")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void testDuplicateName() {
		final String name = "testDuplicateName";
		final String runtimeHome = droolsRequirement.getConfig().getRuntimeFamily().getHome();
		addRuntime(name, runtimeHome, false);

		DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
		pref.open();

		DroolsRuntimeDialog wiz = pref.addDroolsRuntime();
		wiz.setName(name);
		Assert.assertEquals("The Runtime \"" + name + "\" is already registered", wiz.getWarningText());
		
		wiz.setLocation(runtimeHome);
		Assert.assertFalse("It is possible to save runtime with duplicate name.", wiz.isValid());
		wiz.cancel();
		pref.cancel();
	}

	@Test
	@Jira("DROOLS-1162")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void testSetDefaultRuntime() {
		final String name1 = "testSetDefaultRuntime1";
		final String name2 = "testSetDefaultRuntime2";
		final String runtimeHome = droolsRequirement.getConfig().getRuntimeFamily().getHome();

		DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
		pref.open();
		addRuntime(name1, runtimeHome, false, pref);
		addRuntime(name2, runtimeHome, false, pref);
		boolean warning = pref.okCloseWarning();
		Assert.assertFalse("Warning was shown although the default runtime was not set.", warning);

		pref.open();
		pref.setDroolsRuntimeAsDefault(name1);
		warning = pref.okCloseWarning();
		Assert.assertFalse("Warning was not shown although the default runtime has changed.", warning);

		pref.open();
		Table table = new DefaultTable();
		for (int row = 0; row < table.rowCount(); row++) {
			if (table.getItem(row).getText(0).equals(name1)) {
				Assert.assertTrue("Default runtime is not checked", table.getItem(row).isChecked());
			} else {
				Assert.assertFalse("Other than default runtime is checked", table.getItem(row).isChecked());
			}
		}

		pref.setDroolsRuntimeAsDefault(name2);
		warning = pref.okCloseWarning();
		Assert.assertTrue("Warning was  not shown although the default runtime has changed.", warning);

		pref.open();
		table = new DefaultTable();
		for (int row = 0; row < table.rowCount(); row++) {
			if (table.getItem(row).getText(0).equals(name2)) {
				Assert.assertTrue("Default runtime is not checked", table.getItem(row).isChecked());
			} else {
				Assert.assertFalse("Other than default runtime is checked", table.getItem(row).isChecked());
			}
		}
	}

	@Test
	@Jira("DROOLS-1162")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void testDeleteDefaultRuntime() {
		final String name1 = "testDeleteDefaultRuntime1";
		final String name2 = "testDeleteDefaultRuntime2";
		final String name3 = "testDeleteDefaultRuntime3";
		final String runtimeHome = droolsRequirement.getConfig().getRuntimeFamily().getHome();

		DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
		pref.open();
		addRuntime(name1, runtimeHome, true, pref);
		addRuntime(name2, runtimeHome, false, pref);
		addRuntime(name3, runtimeHome, false, pref);
		pref.okCloseWarning();

		pref.open();
		Table table = new DefaultTable();
		for (int row = 0; row < table.rowCount(); row++) {
			if (table.getItem(row).getText(0).equals(name1)) {
				Assert.assertTrue("Default runtime is not checked", table.getItem(row).isChecked());
			} else {
				Assert.assertFalse("Other than default runtime is checked", table.getItem(row).isChecked());
			}
		}
		pref.removeDroolsRuntime(name1);

		Assert.assertEquals("Default runtime was not deleted.", 2, pref.getDroolsRuntimes().size());
		table = new DefaultTable();
		for (int row = 0; row < table.rowCount(); row++) {
			Assert.assertFalse("Default runtime is set although it was deleted earlier",
					table.getItem(row).isChecked());
		}

		pref.setDroolsRuntimeAsDefault(name2);
		pref.removeDroolsRuntime(name2);

		Assert.assertEquals("Default runtime was not deleted.", 1, pref.getDroolsRuntimes().size());
		table = new DefaultTable();
		Assert.assertEquals("Wrong runtimes deleted.", name3, table.getItem(0).getText(0));
	}

	@Test
	@Jira("DROOLS-1161")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void testApply() {
		DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
		pref.open();
		DroolsRuntimeDialog wiz = pref.addDroolsRuntime();
		wiz.setName(getMethodName());
		wiz.setLocation(droolsRequirement.getConfig().getRuntimeFamily().getHome());
		Assert.assertTrue("Impossible to use created runtime.", wiz.isValid());
		wiz.ok();

		Assert.assertEquals("The runtime was not created!", 1, pref.getDroolsRuntimes().size());
		pref.setDroolsRuntimeAsDefault(getMethodName());
		Assert.assertNotNull("The default runtime was not set!", pref.getDefaultDroolsRuntime());
		pref.apply();
		try {
			new DefaultShell("Warning");
			new PushButton("OK").click();
		} catch (Exception ex) {
			LOGGER.info("'Default runtime changed' warning was not shown.");
		}

		Assert.assertNotNull("The default runtime was reset!", pref.getDefaultDroolsRuntime());
		pref.cancel();
	}

	private static void addRuntime(String name, String location, boolean setAsDefault) {
		DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
		pref.open();
		addRuntime(name, location, setAsDefault, pref);
		pref.okCloseWarning();
	}

	private static void addRuntime(String name, String location, boolean setAsDefault,
			DroolsRuntimesPreferencePage pref) {
		DroolsRuntimeDialog wiz = pref.addDroolsRuntime();
		wiz.setName(name);
		wiz.setLocation(location);
		wiz.ok();

		if (setAsDefault) {
			pref.setDroolsRuntimeAsDefault(name);
		}

		Collection<DroolsRuntime> runtimes = pref.getDroolsRuntimes();
		Assert.assertNotSame("No runtimes are present.", 0, runtimes.size());

		DroolsRuntime runtime = getRuntime(name, pref);
		Assert.assertNotNull("Requested runtime was not created.", runtime);
	}

	private static DroolsRuntime getRuntime(String name, DroolsRuntimesPreferencePage pref) {
		for (DroolsRuntime runtime : pref.getDroolsRuntimes()) {
			if (runtime.getName().equals(name)) {
				return runtime;
			}
		}
		return null;
	}
}
