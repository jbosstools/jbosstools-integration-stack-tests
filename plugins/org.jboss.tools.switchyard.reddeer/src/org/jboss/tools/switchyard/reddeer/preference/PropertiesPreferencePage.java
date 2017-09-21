package org.jboss.tools.switchyard.reddeer.preference;

import org.eclipse.reddeer.swt.impl.list.DefaultList;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.switchyard.reddeer.binding.CamelBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.FTPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.FTPSBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.FileBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.HTTPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.JCABindingPage;
import org.jboss.tools.switchyard.reddeer.binding.JMSBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.JPABindingPage;
import org.jboss.tools.switchyard.reddeer.binding.MailBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.NettyTCPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.NettyUDPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.RESTBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SCABindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SFTPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SOAPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SQLBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SchedulingBindingPage;

/**
 * Properties Preference Page
 * 
 * @author apodhrad
 * 
 */
public class PropertiesPreferencePage {

	protected String title;

	public PropertiesPreferencePage(String title) {
		this.title = title;
	}

	public PropertiesPreferencePage activate() {
		new DefaultShell("Properties for " + title);
		return this;
	}

	public void selectTab(String... tab) {
		new DefaultTreeItem(tab).select();
	}

	public void selectBindingsTab() {
		selectTab("Bindings");
	}

	public void selectBinding(String binding) {
		selectBindingsTab();
		new DefaultList().select(binding);
	}

	public CamelBindingPage getCamelBindingPage() {
		return new CamelBindingPage();
	}

	public FTPBindingPage getFTPBindingPage() {
		return new FTPBindingPage();
	}

	public FTPSBindingPage getFTPSBindingPage() {
		return new FTPSBindingPage();
	}

	public FileBindingPage getFileBindingPage() {
		return new FileBindingPage();
	}

	public JCABindingPage getJCABindingPage() {
		return new JCABindingPage();
	}

	public JMSBindingPage getJMSBindingPage() {
		return new JMSBindingPage();
	}

	public JPABindingPage getJPABindingPage() {
		return new JPABindingPage();
	}

	public MailBindingPage getMailBindingPage() {
		return new MailBindingPage();
	}

	public NettyTCPBindingPage getNettyTCPBindingPage() {
		return new NettyTCPBindingPage();
	}

	public NettyUDPBindingPage getNettyUDPBindingPage() {
		return new NettyUDPBindingPage();
	}

	public HTTPBindingPage getHTTPBindingPage() {
		return new HTTPBindingPage();
	}

	public RESTBindingPage getRESTBindingPage() {
		return new RESTBindingPage();
	}

	public SOAPBindingPage getSOAPBindingPage() {
		return new SOAPBindingPage();
	}

	public SCABindingPage getSCABindingPage() {
		return new SCABindingPage();
	}

	public SFTPBindingPage getSFTPBindingPage() {
		return new SFTPBindingPage();
	}

	public SQLBindingPage getSQLBindingPage() {
		return new SQLBindingPage();
	}

	public SchedulingBindingPage getSchedulingBindingPage() {
		return new SchedulingBindingPage();
	}
}
