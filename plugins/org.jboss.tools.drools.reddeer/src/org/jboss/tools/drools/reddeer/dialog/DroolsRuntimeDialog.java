package org.jboss.tools.drools.reddeer.dialog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.osgi.framework.wiring.BundleWiring;

public class DroolsRuntimeDialog {
    private static final Logger LOGGER = Logger.getLogger(DroolsRuntimeDialog.class);

    public void setName(String name) {
        new LabeledText("Name:").setText(name);
    }

    public void setLocation(String location) {
        new LabeledText("Path:").setText(location);
    }

    public void createNewRuntime(String location) {
        // FIXME find a way to do this using SWTBot/RedDeer
        ClassLoader pluginCl = Platform.getBundle("org.drools.eclipse").adapt(BundleWiring.class).getClassLoader();
        try {
            Class<?> clazz = pluginCl.loadClass("org.drools.eclipse.util.DroolsRuntimeManager");
            Method method = clazz.getMethod("createDefaultRuntime", String.class);
            method.invoke(null, location);
        } catch (ClassNotFoundException ex) {
            LOGGER.error(ex);
        } catch (NoSuchMethodException ex) {
            LOGGER.error(ex);
        } catch (InvocationTargetException ex) {
            LOGGER.error(ex);
        } catch (IllegalAccessException ex) {
            LOGGER.error(ex);
        }
        setLocation(location);
    }

    public void cancel() {
        new PushButton("Cancel").click();
    }

    public void ok() {
    	new LabeledText("Name:").setFocus();
    	AbstractWait.sleep(TimePeriod.SHORT);
    	new LabeledText("Path:").setFocus();
    	AbstractWait.sleep(TimePeriod.SHORT);
        new PushButton("OK").click();
    }

    public boolean isValid() {
        return new PushButton("OK").isEnabled();
    }
}
