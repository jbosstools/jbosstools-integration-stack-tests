package org.jboss.tools.teiid.reddeer.dialog;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class EditColumnDialog extends AbstractDialog {
	
	private static final Logger log = Logger.getLogger(CreateDataSourceDialog.class);
	
	public EditColumnDialog() {
		super("Edit Column");
		log.info("Generate edit column dialog is opened");
	}

	public EditColumnDialog setName(String name){
		log.info("Set column to: '"+ name +"'");
		activate();
		new LabeledText("Name").setText(name);
		return this;
	}
	
	public EditColumnDialog setType(String type){
		log.info("Set column type to: '"+ type +"'");
		activate();
		new DefaultCombo(0).setSelection(type);
		return this;
	}
	
	public EditColumnDialog setWidth(String width){
		log.info("Set column width to: '"+ width +"'");
		activate();
		new LabeledText("width").setText(width);
		return this;
	}
	
	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new PushButton("OK").click();
	}


	
}