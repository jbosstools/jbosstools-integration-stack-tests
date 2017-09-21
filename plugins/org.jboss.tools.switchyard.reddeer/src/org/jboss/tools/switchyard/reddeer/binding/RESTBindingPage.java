package org.jboss.tools.switchyard.reddeer.binding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.matcher.WithLabelMatcher;
import org.eclipse.reddeer.swt.api.Text;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.condition.TableHasRows;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.list.DefaultList;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.keyboard.KeyboardFactory;
import org.eclipse.swt.SWT;

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
		new LabeledText("Address").setFocus();
		new LabeledText("Address").setText(address);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public RESTBindingPage addInterface(String javaInterface) {
		new PushButton("Add").click();
		new DefaultShell("Select entries");
		new DefaultText().setText(javaInterface);
		new WaitUntil(new TableHasRows(new DefaultTable()), TimePeriod.LONG);
		new PushButton("OK").click();
		new WaitWhile(new ShellIsAvailable("Select entries"));
		return this;
	}

	public List<String> getInterfaces() {
		String[] items = new DefaultList(null, 0, new WithLabelMatcher("RESTful Interfaces*")).getListItems();
		return new ArrayList<String>(Arrays.asList(items));
	}

	public Text getAddress() {
		return new LabeledText("Address");
	}

}
