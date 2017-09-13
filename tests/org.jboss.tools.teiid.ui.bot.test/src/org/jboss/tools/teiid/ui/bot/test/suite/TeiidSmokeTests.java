package org.jboss.tools.teiid.ui.bot.test.suite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.BasicTest;
import org.jboss.tools.teiid.ui.bot.test.CreateModelTest;
import org.jboss.tools.teiid.ui.bot.test.ModelRefactoringTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	BasicTest.class,
	CreateModelTest.class,
    ModelRefactoringTest.class/*
                               * , FlatFileTest.class
                               */ })
@RunWith(RedDeerSuite.class)
public class TeiidSmokeTests {}
