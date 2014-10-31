package org.jboss.tools.fuse.ui.bot.test.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.matcher.WithTooltipTextMatcher;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.fuse.reddeer.utils.ResourceHelper;
import org.jboss.tools.fuse.ui.bot.test.Activator;

/**
 * Support static methods manipulates with Text Editor
 * 
 * @author tsedmik
 */
public class EditorManipulator {

	private static Logger log = Logger.getLogger(EditorManipulator.class);

	/**
	 * Replaces content of a file opened in active text editor with content of
	 * the file <i>source</i>
	 * 
	 * @param source
	 *            Path to the source file
	 */
	public static void copyFileContent(String source) {

		TextEditor editor = new TextEditor();
		editor.setText(getFileContent(source));
		editor.save();
	}

	/**
	 * Replaces content of a file opened in active XML editor of the CamelEditor
	 * with content of the file <i>source</i>
	 * 
	 * @param source
	 *            Path to the source file
	 */
	public static void copyFileContentToCamelXMLEditor(String source) {

		new DefaultStyledText().setText(EditorManipulator.getFileContent(source));
		new DefaultToolItem(new WorkbenchShell(), 0, new WithTooltipTextMatcher(new RegexMatcher("Save.*"))).click();
		
		// FIXME temporary added due to https://issues.jboss.org/browse/FUSETOOLS-1208
		try {
			log.debug("Check whether 'Could not parse your changes to the XML' dialog is appeared");
			new WaitUntil(new ShellWithTextIsAvailable("Could not parse your changes to the XML"), TimePeriod.SHORT);
			new DefaultShell("Could not parse your changes to the XML");
			new PushButton("OK").click();
		} catch (Exception e) {
			log.debug("Dialog 'Could not parse your changes to the XML' didn't appeared");
		}
	}

	/**
	 * Gets content of a given file
	 * 
	 * @param source
	 *            the source file
	 * @return content of the file, in case of some error - empty string
	 */
	public static String getFileContent(String source) {

		File testFile = new File(ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, source));
		String text = "";

		try {
			Scanner scanner = new Scanner(testFile);
			scanner.useDelimiter("\\Z");
			text = scanner.next();
			scanner.close();
		} catch (FileNotFoundException e) {
			log.error("Resource missing: can't find a failing test case to copy (" + source + ")!");
		}

		log.info("Text in active text editor was replaced with content of the file: " + source + ".");

		return text;
	}

	/**
	 * Compares content of the active XML editor of the Camel Editor with
	 * content of the given file
	 * 
	 * @param file
	 *            path to the file
	 * @return true - content of the file and the text editor is the same, false
	 *         - otherwise
	 */
	public static boolean isEditorContentEqualsFile(String file) {

		String editorText = new DefaultStyledText().getText();
		String fileText = getFileContent(file);

		return editorText.equals(fileText);
	}
}
