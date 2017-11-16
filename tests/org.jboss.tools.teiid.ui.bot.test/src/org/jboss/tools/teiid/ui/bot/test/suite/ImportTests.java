package org.jboss.tools.teiid.ui.bot.test.suite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.FlatFileTest;
import org.jboss.tools.teiid.ui.bot.test.GeometryTypeTest;
import org.jboss.tools.teiid.ui.bot.test.LdapImportTest;
import org.jboss.tools.teiid.ui.bot.test.SalesforceImportTest;
import org.jboss.tools.teiid.ui.bot.test.imports.Apache;
import org.jboss.tools.teiid.ui.bot.test.imports.Db2;
import org.jboss.tools.teiid.ui.bot.test.imports.Dv6;
import org.jboss.tools.teiid.ui.bot.test.imports.Excel;
import org.jboss.tools.teiid.ui.bot.test.imports.File;
import org.jboss.tools.teiid.ui.bot.test.imports.GoogleSpreadSheet;
import org.jboss.tools.teiid.ui.bot.test.imports.Greenplum;
import org.jboss.tools.teiid.ui.bot.test.imports.H2;
import org.jboss.tools.teiid.ui.bot.test.imports.HSql;
import org.jboss.tools.teiid.ui.bot.test.imports.Informix;
import org.jboss.tools.teiid.ui.bot.test.imports.Ingres10;
import org.jboss.tools.teiid.ui.bot.test.imports.MariaDB;
import org.jboss.tools.teiid.ui.bot.test.imports.Modeshape;
import org.jboss.tools.teiid.ui.bot.test.imports.MongoDB;
import org.jboss.tools.teiid.ui.bot.test.imports.MySql;
import org.jboss.tools.teiid.ui.bot.test.imports.Odata;
import org.jboss.tools.teiid.ui.bot.test.imports.Oracle;
import org.jboss.tools.teiid.ui.bot.test.imports.PostgreSql;
import org.jboss.tools.teiid.ui.bot.test.imports.SalesForce;
import org.jboss.tools.teiid.ui.bot.test.imports.SapHana;
import org.jboss.tools.teiid.ui.bot.test.imports.SapIq;
import org.jboss.tools.teiid.ui.bot.test.imports.SqlServer;
import org.jboss.tools.teiid.ui.bot.test.imports.Sybase;
import org.jboss.tools.teiid.ui.bot.test.imports.Vertica;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ 
    SalesforceImportTest.class,
	LdapImportTest.class, 
	GeometryTypeTest.class, 
	FlatFileTest.class,
	Apache.class,
	Db2.class,
	Dv6.class,
	Excel.class,
	File.class,
	GoogleSpreadSheet.class,
	Greenplum.class,
	H2.class,
	HSql.class,
	Informix.class,
	Ingres10.class,
	MariaDB.class,
	Modeshape.class,
	MongoDB.class,
	MySql.class,
	Odata.class,
    Oracle.class,
	PostgreSql.class,
	SalesForce.class,
	SapHana.class,
	SapIq.class,
	SqlServer.class,
	Sybase.class,
	Vertica.class
})
@RunWith(RedDeerSuite.class)
public class ImportTests {}
