package org.jboss.tools.teiid.ui.bot.test.suite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.ddl.SourceAccessPattern;
import org.jboss.tools.teiid.ui.bot.test.ddl.SourceColumns;
import org.jboss.tools.teiid.ui.bot.test.ddl.SourcePrimaryKey;
import org.jboss.tools.teiid.ui.bot.test.ddl.SourceTableSettings;
import org.jboss.tools.teiid.ui.bot.test.ddl.SourceUniqueConstraint;
import org.jboss.tools.teiid.ui.bot.test.ddl.StaticVDBdataRoles;
import org.jboss.tools.teiid.ui.bot.test.ddl.StaticVDBsettings;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewAccessPattern;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewColumns;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewGlobalTable;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewPrimaryKey;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewProcedureSettings;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewRestProcedure;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewTableSettings;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewUdf;
import org.jboss.tools.teiid.ui.bot.test.ddl.ViewUniqueConstraint;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	SourceAccessPattern.class,
	SourceColumns.class,
	SourcePrimaryKey.class,
	SourceTableSettings.class,
	SourceUniqueConstraint.class,
	StaticVDBsettings.class,
	StaticVDBdataRoles.class,
	ViewAccessPattern.class,
	ViewColumns.class,
	ViewGlobalTable.class,
	ViewPrimaryKey.class,
	ViewProcedureSettings.class,
	ViewRestProcedure.class,
	ViewUdf.class,
	ViewTableSettings.class,
	ViewUniqueConstraint.class
})
@RunWith(RedDeerSuite.class)
public class DDLTests {}
