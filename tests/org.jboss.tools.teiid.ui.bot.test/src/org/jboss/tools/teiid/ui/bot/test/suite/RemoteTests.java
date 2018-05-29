package org.jboss.tools.teiid.ui.bot.test.suite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.jdg.Jdg6ImportTest;
import org.jboss.tools.teiid.ui.bot.test.jdg.Jdg7ImportTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ 
    Jdg6ImportTest.class,
    Jdg7ImportTest.class })
@RunWith(RedDeerSuite.class)
public class RemoteTests {}
