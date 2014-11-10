package org.jboss.tools.switchyard.reddeer.binding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * REST binding page
 * 
 * @author apodhrad
 * 
 */
public class RESTBindingPage extends OperationOptionsPage<RESTBindingPage> {

	public static final String CONTEXT_PATH = "Context path:";
	public static final String Address = "Address";

	private SWTWorkbenchBot bot = new SWTWorkbenchBot();

	public LabeledText getContextPath() {
		return new LabeledText("Context path:");
	}

	public RESTBindingPage setAddress(String address) {
		bot.textWithLabel("Address").setFocus();
		bot.textWithLabel("Address").setText(address);
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
		String[] items = bot.listWithLabel("RESTful Interfaces*").getItems();
		return new ArrayList<String>(Arrays.asList(items));
	}

}
