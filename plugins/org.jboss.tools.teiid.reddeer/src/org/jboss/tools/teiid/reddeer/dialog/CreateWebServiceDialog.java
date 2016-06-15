package org.jboss.tools.teiid.reddeer.dialog;

import java.util.Arrays;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

public class CreateWebServiceDialog extends AbstractDialog{
	private static final Logger log = Logger.getLogger(CreateWebServiceDialog.class);
	
	private boolean fromXml;
	
	/** 
	 * @param xmlDialog - used to set dialog's title, because it has different titles 
	 * 		when creating WS from XmlModel and RelationalModel
	 */
	public CreateWebServiceDialog(boolean fromXml) {
		super((fromXml) ? "Web Service Generation Wizard" : "Create Web Service");
		this.fromXml = fromXml;
	}

	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new FinishButton().click();
		new WaitWhile(new ShellWithTextIsActive(title), TimePeriod.NORMAL);
		AbstractWait.sleep(TimePeriod.getCustom(3));
		new DefaultShell(fromXml ? "Generation Completed" : "Generation completed successfully.");
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Sets location of new model.
	 */
	public CreateWebServiceDialog setLocation(String... path) {
		log.info("Setting location to " + Arrays.toString(path));
		new PushButton("Browse...").click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(path).select();
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
		new WaitWhile(new ShellWithTextIsActive("Select a Folder"), TimePeriod.NORMAL);
		activate();
		return this;
	}
	
	/**
	 * Sets name of new model.
	 */
	public CreateWebServiceDialog setModelName(String modelName) {
		log.info("Setting model name to '" + modelName + "'");
		new LabeledText(fromXml ? "WebService Model:" : "Web Service").setText(modelName);
		activate();
		return this;
	}	
	
	/**
	 * Sets name of interface.
	 */
	public CreateWebServiceDialog setInterfaceName(String interfaceName) {
		if (!fromXml){
			throw new UnsupportedOperationException("Method used only in creation from XML model");
		}
		log.info("Setting interface name to '" + interfaceName + "'");
		new LabeledText("Interface Name:").setText(interfaceName);
		activate();
		return this;
	}	
	
	/**
	 * Sets name of operation.
	 */
	public CreateWebServiceDialog setOperationName(String operationName) {
		if (!fromXml){
			throw new UnsupportedOperationException("Method used only in creation from XML model");
		}
		log.info("Setting operation name to '" + operationName + "'");
		new LabeledText("Operation Name:").setText(operationName);
		activate();
		return this;
	}	
	
	/**
	 * Sets element of input message.
	 * @param pathToElement (<PROJECT>, ..., <SCHEMA>, ..., <ELEMENT>)
	 */
	public CreateWebServiceDialog setInputMsgElement(String... pathToElement) {
		if (!fromXml){
			throw new UnsupportedOperationException("Method used only in creation from XML model");
		}
		log.info("Setting input message element to '" + pathToElement[pathToElement.length - 1] + "'");
		new PushButton(new DefaultGroup("Operation Definition"), "...").click();
		new DefaultShell("Select a Schema Element");
		new DefaultTreeItem(pathToElement).select();
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
		new WaitWhile(new ShellWithTextIsActive("Select a Schema Element"), TimePeriod.NORMAL);
		activate();
		return this;
	}	
	
	/**
	 * Sets name of input message.
	 */
	public CreateWebServiceDialog setInputMsgName(String inputMsg) {
		if (!fromXml){
			throw new UnsupportedOperationException("Method used only in creation from XML model");
		}
		log.info("Setting input message name to '" + inputMsg + "'");
		new LabeledText("Input Message Name:").setText(inputMsg);
		activate();
		return this;
	}	
	
	/**
	 * Sets name of output message.
	 */
	public CreateWebServiceDialog setOutputMsgName(String outputMsg) {
		if (!fromXml){
			throw new UnsupportedOperationException("Method used only in creation from XML model");
		}
		log.info("Setting output message name to '" + outputMsg + "'");
		new LabeledText("Output Message Name:").setText(outputMsg);
		activate();
		return this;
	}
	
	/**
	 * Sets name of input schema.
	 */
	public CreateWebServiceDialog setInputSchemaName(String schemaName){
		if (fromXml){
			throw new UnsupportedOperationException("Method used only in creation from relational model");
		}
		log.info("Setting input schema name to '" + schemaName + "'");
		new LabeledText("Input Schema").setText(schemaName);
		activate();
		return this;
	}
	
	/**
	 * Sets name of output message.
	 */
	public CreateWebServiceDialog setOutputSchemaName(String schemaName){
		if (fromXml){
			throw new UnsupportedOperationException("Method used only in creation from relational model");
		}
		log.info("Setting output schema name to '" + schemaName + "'");
		new LabeledText("Output Schema").setText(schemaName);
		activate();
		return this;
	}

}
