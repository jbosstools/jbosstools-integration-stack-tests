package org.jboss.tools.teiid.ui.bot.test.suite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.ddl.SourceAccessPattern;
import org.jboss.tools.teiid.ui.bot.test.ddl.SourceColumns;
import org.jboss.tools.teiid.ui.bot.test.ddl.SourceForeignKey;
import org.jboss.tools.teiid.ui.bot.test.ddl.SourceIndexes;
import org.jboss.tools.teiid.ui.bot.test.ddl.SourcePrimaryKey;
import org.jboss.tools.teiid.ui.bot.test.ddl.SourceProcedureSettings;
import org.jboss.tools.teiid.ui.bot.test.ddl.SourceTableSettings;
import org.jboss.tools.teiid.ui.bot.test.ddl.SourceUniqueConstraint;
import org.jboss.tools.teiid.ui.bot.test.ddl.StaticVDBdataRoles;
import org.jboss.tools.teiid.ui.bot.test.ddl.StaticVDBsettings;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	SourceAccessPattern.class,
	SourceColumns.class,
	SourceForeignKey.class,
	SourceIndexes.class,
	SourcePrimaryKey.class,
	SourceProcedureSettings.class,
	SourceTableSettings.class,
	SourceUniqueConstraint.class,
	StaticVDBsettings.class,
	StaticVDBdataRoles.class,
})
@RunWith(RedDeerSuite.class)
public class DDLTestsSource {}
