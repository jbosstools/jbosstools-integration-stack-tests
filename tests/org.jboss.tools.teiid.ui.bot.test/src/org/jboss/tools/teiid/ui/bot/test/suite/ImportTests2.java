package org.jboss.tools.teiid.ui.bot.test.suite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.imports.Apache;
import org.jboss.tools.teiid.ui.bot.test.imports.Db2;
import org.jboss.tools.teiid.ui.bot.test.imports.Dv6;
import org.jboss.tools.teiid.ui.bot.test.imports.Excel;
import org.jboss.tools.teiid.ui.bot.test.imports.File;
import org.jboss.tools.teiid.ui.bot.test.imports.GoogleSpreadSheet;
import org.jboss.tools.teiid.ui.bot.test.imports.Informix;
import org.jboss.tools.teiid.ui.bot.test.imports.Ingres10;
import org.jboss.tools.teiid.ui.bot.test.imports.Modeshape;
import org.jboss.tools.teiid.ui.bot.test.imports.SalesForce;
import org.jboss.tools.teiid.ui.bot.test.imports.SapHana;
import org.jboss.tools.teiid.ui.bot.test.imports.SapIq;
import org.jboss.tools.teiid.ui.bot.test.imports.Sybase;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ 
    Apache.class,
    Db2.class,
    Dv6.class,
    Excel.class,
    File.class,
    GoogleSpreadSheet.class,
    Informix.class,
    Ingres10.class,    
    Modeshape.class,
    SalesForce.class,
    SapHana.class,
    SapIq.class,
    Sybase.class
})
@RunWith(RedDeerSuite.class)
public class ImportTests2 {}
