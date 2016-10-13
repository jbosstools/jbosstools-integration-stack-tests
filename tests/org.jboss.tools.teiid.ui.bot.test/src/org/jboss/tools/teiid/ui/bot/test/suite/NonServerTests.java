package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.BasicTest;
import org.jboss.tools.teiid.ui.bot.test.CloneAndCopyTest;
import org.jboss.tools.teiid.ui.bot.test.CreateModelTest;
import org.jboss.tools.teiid.ui.bot.test.ImportModelTest;
import org.jboss.tools.teiid.ui.bot.test.ModelEditorActionsTest;
import org.jboss.tools.teiid.ui.bot.test.ModelRefactoringTest;
import org.jboss.tools.teiid.ui.bot.test.TransformationToolsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	BasicTest.class,
	ImportModelTest.class,
	CreateModelTest.class,
	ModelRefactoringTest.class,
	CloneAndCopyTest.class,
	TransformationToolsTest.class,
	ModelEditorActionsTest.class})
@RunWith(RedDeerSuite.class)
public class NonServerTests {}
