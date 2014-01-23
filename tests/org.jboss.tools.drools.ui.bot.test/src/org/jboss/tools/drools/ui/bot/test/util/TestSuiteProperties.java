package org.jboss.tools.drools.ui.bot.test.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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
        BufferedReader br = null;
        try {
            LOGGER.info("Loading properties for Drools tests");
            // Read project properties
            Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
            URL resource = bundle.getResource("project.properties");
            br = new BufferedReader(new InputStreamReader(resource.openStream()));
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split("=");
                if (split.length == 2) {
                    PROPERTIES.setProperty(split[0].trim(), split[1].trim());
                }
            }
            LOGGER.info("Properties for Drools test loaded");
            return true;
        } catch (Exception ex) {
            LOGGER.warn("External properties were not loaded.", ex);
            return false;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    // Can't do much else than logging exception
                    LOGGER.error("Unable to close properties reader", ex);
                }
            }
        }
    }
}
