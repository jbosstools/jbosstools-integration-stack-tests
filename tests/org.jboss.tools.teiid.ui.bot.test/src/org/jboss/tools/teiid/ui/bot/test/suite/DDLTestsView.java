package org.jboss.tools.teiid.ui.bot.test.suite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewAccessPattern;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewColumns;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewForeignKey;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewGlobalTable;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewIndexes;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewPrimaryKey;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewProcedureSettings;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewRestProcedure;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewTableSettings;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewUdf;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewUniqueConstraint;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
    ViewAccessPattern.class,
    ViewColumns.class,
    ViewForeignKey.class,
    ViewGlobalTable.class,
    ViewIndexes.class,
    ViewPrimaryKey.class,
    ViewProcedureSettings.class,
    ViewRestProcedure.class,
    ViewUdf.class,
    ViewTableSettings.class,
    ViewUniqueConstraint.class })
@RunWith(RedDeerSuite.class)
public class DDLTestsView {
}