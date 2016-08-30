package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.ConnectionProfileWizard;

public class XmlRemoteConnectionProfileWizard extends ConnectionProfileWizard {

		private XmlRemoteConnectionProfileWizard(String name) {
			super("XML File URL Source",name);
			log.info("Remote XML profile wizard is opened");
		}
		
		public static XmlRemoteConnectionProfileWizard getInstance(){
			return new XmlRemoteConnectionProfileWizard(null);
		}
		
		public static XmlRemoteConnectionProfileWizard openWizard(String name){
			XmlRemoteConnectionProfileWizard wizard = new XmlRemoteConnectionProfileWizard(name);
			wizard.open();
			return wizard;
		}
		
		public XmlRemoteConnectionProfileWizard activate() {
			new DefaultShell(DIALOG_TITLE);
			return this;
		}
		
		public XmlRemoteConnectionProfileWizard setUrl(String url) {
			log.info("Set url to : '" + url + "'");
			activate();
			new LabeledText("Connection URL").setText(url);
			activate();
			return this;
		}
		
		@Override
		public XmlRemoteConnectionProfileWizard testConnection(){
			new PushButton("Test Connection").click();
			new DefaultShell("Success");
			new PushButton("OK").click();
			activate();
			return this;
		}
}
