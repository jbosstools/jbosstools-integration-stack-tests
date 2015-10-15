package org.jboss.tools.drools.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.ui.bot.test.functional.DefaultRuntimeTest;
import org.jboss.tools.drools.ui.bot.test.functional.DroolsRuntimeManagementTest;
import org.jboss.tools.drools.ui.bot.test.functional.DslEditorTest;
import org.jboss.tools.drools.ui.bot.test.functional.DslrEditorTest;
import org.jboss.tools.drools.ui.bot.test.functional.NewResourcesTest;
import org.jboss.tools.drools.ui.bot.test.functional.PerspectiveTest;
import org.jboss.tools.drools.ui.bot.test.functional.RulesManagementTest;
import org.jboss.tools.drools.ui.bot.test.functional.brms6.Brms6ProjectTest;
import org.jboss.tools.drools.ui.bot.test.functional.drleditor.ConditionsCompletionTest;
import org.jboss.tools.drools.ui.bot.test.functional.drleditor.ConsequencesCompletionTest;
import org.jboss.tools.drools.ui.bot.test.functional.drleditor.DeclareCompletionTest;
import org.jboss.tools.drools.ui.bot.test.functional.drleditor.MetadataCompletionTest;
import org.jboss.tools.drools.ui.bot.test.functional.view.AgendaViewTest;
import org.jboss.tools.drools.ui.bot.test.functional.view.AuditLogTest;
import org.jboss.tools.drools.ui.bot.test.functional.view.GlobalDataViewTest;
import org.jboss.tools.drools.ui.bot.test.functional.view.WorkingMemoryViewTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This is temporary class. It will be removed after Kie Navigator Tests are stabilized.
 */
@RunWith(RedDeerSuite.class)
@SuiteClasses({
    // basic tests
    PerspectiveTest.class,
    DefaultRuntimeTest.class,
    DroolsRuntimeManagementTest.class,
    Brms6ProjectTest.class,
    NewResourcesTest.class,
    RulesManagementTest.class,
    // DRL editor code completion
    MetadataCompletionTest.class,
    DeclareCompletionTest.class,
    ConditionsCompletionTest.class,
    ConsequencesCompletionTest.class,
    // DSL editor
    DslEditorTest.class,
    // DSLR editor
    DslrEditorTest.class,
    // TODO debugging tests
    // views test
    AgendaViewTest.class,
    GlobalDataViewTest.class,
    WorkingMemoryViewTest.class,
    AuditLogTest.class
})
public class Brms6WithoutKieNavTestSuite {

}