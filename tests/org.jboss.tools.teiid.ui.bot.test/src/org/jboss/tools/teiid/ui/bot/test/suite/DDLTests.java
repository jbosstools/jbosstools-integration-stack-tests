package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.DynamicVdbTest;
import org.jboss.tools.teiid.ui.bot.test.ImportDDLtest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	ImportDDLtest.class,
	DynamicVdbTest.class})
@RunWith(RedDeerSuite.class)
public class DDLTests {}
