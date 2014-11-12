package org.jboss.tools.switchyard.reddeer.preference.binding;

import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.list.DefaultList;
import org.jboss.tools.switchyard.reddeer.binding.AtomBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.CXFBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.CamelBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.FTPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.FTPSBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.FileBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.HTTPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.JCABindingPage;
import org.jboss.tools.switchyard.reddeer.binding.JMSBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.JPABindingPage;
import org.jboss.tools.switchyard.reddeer.binding.MQTTBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.MailBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.NettyTCPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.NettyUDPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.RESTBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.RSSBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SAPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SCABindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SFTPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SOAPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SQLBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SchedulingBindingPage;

/**
 * Represents a properties page "Bindigs".
 * 
 * @author apodhrad
 */
public class BindingsPage extends PreferencePage {

	public BindingsPage remove() {
		new PushButton("Remove").click();
		return this;
	}

	public BindingsPage removeAll() {
		while (new DefaultList().getListItems().length > 0) {
			new DefaultList().select(0);
			remove();
		}
		return this;
	}

	public AtomBindingPage selectAtomBinding(String name) {
		new DefaultList().select("Atom (" + name + ")");
		return new AtomBindingPage();
	}

	public CXFBindingPage selectCXFBinding(String name) {
		new DefaultList().select("CXF (" + name + ")");
		return new CXFBindingPage();
	}

	public CamelBindingPage selectCamelBinding(String name) {
		new DefaultList().select("Camel URI (" + name + ")");
		return new CamelBindingPage();
	}

	public FileBindingPage selectFileBinding(String name) {
		new DefaultList().select("File (" + name + ")");
		return new FileBindingPage();
	}

	public FTPBindingPage selectFTPBinding(String name) {
		new DefaultList().select("FTP (" + name + ")");
		return new FTPBindingPage();
	}

	public FTPSBindingPage selectFTPSBinding(String name) {
		new DefaultList().select("FTPS (" + name + ")");
		return new FTPSBindingPage();
	}

	public SFTPBindingPage selectSFTPBinding(String name) {
		new DefaultList().select("SFTP (" + name + ")");
		return new SFTPBindingPage();
	}

	public HTTPBindingPage selectHTTPBinding(String name) {
		new DefaultList().select("HTTP (" + name + ")");
		return new HTTPBindingPage();
	}

	public JCABindingPage selectJCABinding(String name) {
		new DefaultList().select("JCA (" + name + ")");
		return new JCABindingPage();
	}

	public JMSBindingPage selectJMSBinding(String name) {
		new DefaultList().select("JMS (" + name + ")");
		return new JMSBindingPage();
	}

	public JPABindingPage selectJPABinding(String name) {
		new DefaultList().select("JPA (" + name + ")");
		return new JPABindingPage();
	}

	public MQTTBindingPage selectMQTTBinding(String name) {
		new DefaultList().select("MQTT (" + name + ")");
		return new MQTTBindingPage();
	}

	public MailBindingPage selectMailBinding(String name) {
		new DefaultList().select("Mail (" + name + ")");
		return new MailBindingPage();
	}

	public NettyTCPBindingPage selectNettyTCPBinding(String name) {
		new DefaultList().select("TCP (" + name + ")");
		return new NettyTCPBindingPage();
	}

	public NettyUDPBindingPage selectNettyUDPBinding(String name) {
		new DefaultList().select("UDP (" + name + ")");
		return new NettyUDPBindingPage();
	}

	public RESTBindingPage selectRESTBinding(String name) {
		new DefaultList().select("REST (" + name + ")");
		return new RESTBindingPage();
	}

	public RSSBindingPage selectRSSBinding(String name) {
		new DefaultList().select("RSS (" + name + ")");
		return new RSSBindingPage();
	}

	public SAPBindingPage selectSAPBinding(String name) {
		new DefaultList().select("SAP (" + name + ")");
		return new SAPBindingPage();
	}

	public SCABindingPage selectSCABinding(String name) {
		new DefaultList().select("SCA (" + name + ")");
		return new SCABindingPage();
	}

	public SchedulingBindingPage selectSchedulingBinding(String name) {
		new DefaultList().select("Scheduling (" + name + ")");
		return new SchedulingBindingPage();
	}

	public SOAPBindingPage selectSOAPBinding(String name) {
		new DefaultList().select("SOAP (" + name + ")");
		return new SOAPBindingPage();
	}

	public SQLBindingPage selectSQLBinding(String name) {
		new DefaultList().select("SQL (" + name + ")");
		return new SQLBindingPage();
	}

}