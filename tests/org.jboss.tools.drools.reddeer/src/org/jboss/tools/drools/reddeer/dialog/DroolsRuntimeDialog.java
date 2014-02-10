package org.jboss.tools.drools.reddeer.dialog;

import java.lang.reflect.Method;

import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.osgi.framework.wiring.BundleWiring;

public class DroolsRuntimeDialog {

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
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
        setLocation(location);
    }

    public void cancel() {
        new PushButton("Cancel").click();
    }

    public void ok() {
        new PushButton("OK").click();
    }

    public boolean isValid() {
        return new PushButton("OK").isEnabled();
    }
}
