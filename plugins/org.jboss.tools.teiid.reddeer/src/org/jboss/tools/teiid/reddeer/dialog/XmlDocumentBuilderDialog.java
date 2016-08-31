package org.jboss.tools.teiid.reddeer.dialog;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

public class XmlDocumentBuilderDialog extends AbstractDialog {
	private static final Logger log = Logger.getLogger(XmlDocumentBuilderDialog.class);
	
	public XmlDocumentBuilderDialog(){
		super("Build XML Documents From XML Schema");
	}
	
	public static XmlDocumentBuilderDialog getInstance(){
		return new XmlDocumentBuilderDialog();
	}
	
	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new FinishButton().click();
		new WaitWhile(new ShellWithTextIsActive(title), TimePeriod.NORMAL);
		AbstractWait.sleep(TimePeriod.SHORT);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
	}

	/**
	 * Selects specified schema from which document will be build.
	 * @param path - path to schema (<PROJECT>/.../<SCHEMA>)
	 */
	public XmlDocumentBuilderDialog setSchema(String... path){
		log.info("Setting schema to " + Arrays.toString(path));
		new PushButton("...").click();
		new DefaultShell("Select an XML Schema");
		new DefaultTreeItem(path).select();
		new PushButton("OK").click();
		return this;
	}
	
	/**
	 * Creates virtual document from element of schema
	 * i.e. pushes element from left list to right list.
	 */
	public XmlDocumentBuilderDialog addElement(String element){
		log.info("Adding element '" + element + "'");
		new DefaultTable().select(element);
		new PushButton(1).click();
		return this;
	}
}
