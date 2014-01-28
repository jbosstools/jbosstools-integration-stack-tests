package org.jboss.tools.drools.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.ui.bot.test.functional.DroolsRuntimeManagementTest;
import org.jboss.tools.drools.ui.bot.test.functional.DslEditorTest;
import org.jboss.tools.drools.ui.bot.test.functional.DslrEditorTest;
import org.jboss.tools.drools.ui.bot.test.functional.NewResourcesTest;
import org.jboss.tools.drools.ui.bot.test.functional.PerspectiveTest;
import org.jboss.tools.drools.ui.bot.test.functional.ProjectManagementTest;
import org.jboss.tools.drools.ui.bot.test.functional.RulesManagementTest;
import org.jboss.tools.drools.ui.bot.test.functional.drleditor.ConditionsCompletionTest;
import org.jboss.tools.drools.ui.bot.test.functional.drleditor.ConsequencesCompletionTest;
import org.jboss.tools.drools.ui.bot.test.functional.drleditor.DeclareCompletionTest;
import org.jboss.tools.drools.ui.bot.test.functional.drleditor.MetadataCompletionTest;
import org.jboss.tools.drools.ui.bot.test.functional.view.AgendaViewTest;
import org.jboss.tools.drools.ui.bot.test.functional.view.GlobalDataViewTest;
import org.jboss.tools.drools.ui.bot.test.functional.view.WorkingMemoryViewTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
    // basic tests
    PerspectiveTest.class,
    DroolsRuntimeManagementTest.class,
    ProjectManagementTest.class,
    NewResourcesTest.class,
    RulesManagementTest.class,
    // DRL editor code completion
    MetadataCompletionTest.class,
    DeclareCompletionTest.class,
    ConditionsCompletionTest.class,
    ConsequencesCompletionTest.class,
    // dsl editor
    DslEditorTest.class,
    // dslr editor
    DslrEditorTest.class,
    // TODO: debugging tests
    // views test
    AgendaViewTest.class,
    GlobalDataViewTest.class,
    WorkingMemoryViewTest.class
    // TODO: 5.x tests
})
public class BrmsTestSuite {

}
