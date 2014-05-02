package org.jboss.tools.esb.ui.bot.test.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardDialog;
import org.jboss.reddeer.junit.requirement.PropertyConfiguration;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.esb.ui.bot.test.requirement.ServerRequirement.Server;

/**
 * 
 * @author apodhrad
 * 
 */
public class ServerRequirement implements Requirement<Server>, PropertyConfiguration {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Server {
		String type() default "ANY";
	}

	private Server server;

	private String type;
	private String version;
	private String path;

	@Override
	public boolean canFulfill() {
		if (server.type().equals("ANY")) {
			return true;
		}
		return server.type().equals(type);
	}

	@Override
	public void fulfill() {
		System.out.println("Fulfilling Server requirement with\nType: " + type + "\nPath: " + path);
		ServersView serversView = new ServersView();
		serversView.open();

		// check if server already exists
		for (org.jboss.reddeer.eclipse.wst.server.ui.view.Server server : serversView.getServers()) {
			if (server.getLabel().getName().equals(getName())) {
				return;
			}
		}

		// create new server
		NewServerWizardDialog wizard = serversView.newServer();
		new DefaultTreeItem("JBoss Enterprise Middleware", "JBoss Enterprise Application Platform 5.x").select();
		new LabeledText("Server name:").setText(getName());
		wizard.next();
		new LabeledText("Name").setText(getName());
		new LabeledText("Home Directory").setText(getPath());
		wizard.finish();
	}

	@Override
	public void setDeclaration(Server server) {
		this.server = server;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return type + "-" + version;
	}

}
