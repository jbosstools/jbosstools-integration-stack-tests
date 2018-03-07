package org.jboss.tools.teiid.ui.bot.test.suite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.GeometryTypeTest;
import org.jboss.tools.teiid.ui.bot.test.LdapImportTest;
import org.jboss.tools.teiid.ui.bot.test.SalesforceImportTest;
import org.jboss.tools.teiid.ui.bot.test.imports.Greenplum;
import org.jboss.tools.teiid.ui.bot.test.imports.H2;
import org.jboss.tools.teiid.ui.bot.test.imports.HSql;
import org.jboss.tools.teiid.ui.bot.test.imports.MariaDB;
import org.jboss.tools.teiid.ui.bot.test.imports.MongoDB;
import org.jboss.tools.teiid.ui.bot.test.imports.MySql;
import org.jboss.tools.teiid.ui.bot.test.imports.Odata;
import org.jboss.tools.teiid.ui.bot.test.imports.Oracle;
import org.jboss.tools.teiid.ui.bot.test.imports.PostgreSql;
import org.jboss.tools.teiid.ui.bot.test.imports.SqlServer;
import org.jboss.tools.teiid.ui.bot.test.imports.Vertica;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ 
    Odata.class,
    SalesforceImportTest.class,
    LdapImportTest.class,
    GeometryTypeTest.class,
    Greenplum.class,
    H2.class,
    HSql.class,
    MariaDB.class,
    MongoDB.class,
    Odata.class,
    MySql.class,
    Oracle.class,
    PostgreSql.class,
    SqlServer.class,
    Vertica.class
})
@RunWith(RedDeerSuite.class)
public class ImportTests {}
