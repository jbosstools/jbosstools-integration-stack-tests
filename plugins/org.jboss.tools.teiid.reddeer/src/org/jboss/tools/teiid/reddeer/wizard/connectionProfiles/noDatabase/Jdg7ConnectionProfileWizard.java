package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.ConnectionProfileWizard;

public class Jdg7ConnectionProfileWizard extends ConnectionProfileWizard {

    private static final Logger log = Logger.getLogger(Jdg7ConnectionProfileWizard.class);
    public static final String DIALOG_TITLE = "New connection profile";

    private Jdg7ConnectionProfileWizard(String name) {
        super("JDG 7.x", name);
        log.info("JDG7 profile wizard is opened");
    }

    public static Jdg7ConnectionProfileWizard getInstance() {
        return new Jdg7ConnectionProfileWizard(null);
    }

    public static Jdg7ConnectionProfileWizard openWizard(String name) {
        Jdg7ConnectionProfileWizard wizard = new Jdg7ConnectionProfileWizard(name);
        wizard.open();
        return wizard;
    }

    public Jdg7ConnectionProfileWizard activateWizard() {
        new DefaultShell(DIALOG_TITLE);
        return this;
    }

    public Jdg7ConnectionProfileWizard setJndiName(String jndiName) {
        log.info("Set JNDI name to: '" + jndiName + "'");
        activateWizard();
        new DefaultText(0).setText(jndiName);
        return this;
    }

    public Jdg7ConnectionProfileWizard setRemoteServerList(String remoteServerList) {
        log.info("Set remote server list to: '" + remoteServerList + "'");
        activateWizard();
        new DefaultText(1).setText(remoteServerList);
        return this;
    }

    @Deprecated
    @Override
    public ConnectionProfileWizard testConnection() {
        // TODO This method is not supported in the JDG7
        return this;
    }

}
