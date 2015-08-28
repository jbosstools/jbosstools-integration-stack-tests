package org.jboss.tools.switchyard.reddeer.binding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.list.DefaultList;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.widget.LabeledTextExt;
import org.jboss.tools.switchyard.reddeer.widget.WithLabelMatcherExt;

/**
 * REST binding page
 * 
 * @author apodhrad
 * 
 */
public class RESTBindingPage extends OperationOptionsPage<RESTBindingPage> {

	public static final String CONTEXT_PATH = "Context path:";
	public static final String Address = "Address";

	public LabeledText getContextPath() {
		return new LabeledText("Context path:");
	}

	public RESTBindingPage setAddress(String address) {
		new LabeledTextExt("Address").setFocus();
		new LabeledTextExt("Address").setText(address);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public RESTBindingPage addInterface(String javaInterface) {
		new PushButton("Add").click();
		new DefaultShell("Select entries");
		new DefaultText().setText(javaInterface);
		new WaitUntil(new TableHasRows(new DefaultTable()), TimePeriod.LONG);
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Select entries"));
		return this;
	}

	public List<String> getInterfaces() {
		String[] items = new DefaultList(null, 0, new WithLabelMatcherExt("RESTful Interfaces*")).getListItems();
		return new ArrayList<String>(Arrays.asList(items));
	}

	public Text getAddress() {
		return new LabeledText("Address");
	}
}
