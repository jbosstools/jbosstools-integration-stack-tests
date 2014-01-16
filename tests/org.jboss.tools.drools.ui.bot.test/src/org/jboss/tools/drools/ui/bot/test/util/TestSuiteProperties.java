package org.jboss.tools.drools.ui.bot.test.util;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.drools.ui.bot.test.Activator;
import org.osgi.framework.Bundle;

public class TestSuiteProperties {
    private static final Logger LOGGER = Logger.getLogger(TestSuiteProperties.class);
    private static final Properties PROPERTIES = new Properties(System.getProperties());

    public static final String DROOLS5_RUNTIME = "location.drools5";
    public static final String DROOLS6_RUNTIME = "location.drools6";

    public TestSuiteProperties() {
        reloadProperties();
    }

    public String getDrools5RuntimeLocation() {
        return getProperty(DROOLS5_RUNTIME);
    }

    public String getDrools6RuntimeLocation() {
        return getProperty(DROOLS6_RUNTIME);
    }

    public String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }

    public boolean reloadProperties() {
        try {
            LOGGER.info("Loading properties for Drools tests");
            // Read project properties
            Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
            InputStream is = bundle.getResource("project.properties").openStream();
            PROPERTIES.load(is);
            LOGGER.info("Properties for Drools test loaded");
            return true;
        } catch (Exception ex) {
            LOGGER.warn("External properties were not loaded.");
            return false;
        }
    }
}
