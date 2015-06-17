package org.jboss.tools.fuse.reddeer.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.gef.GEFLayerException;
import org.jboss.reddeer.gef.editor.GEFEditor;
import org.jboss.reddeer.gef.impl.editpart.LabeledEditPart;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.tools.fuse.reddeer.component.CamelComponent;
import org.jboss.tools.fuse.reddeer.utils.MouseAWTManager;
import org.jboss.tools.fuse.reddeer.utils.XPathEvaluator;

/**
 * Manipulates with Camel Editor
 * 
 * @author tsedmik
 */
public class CamelEditor extends GEFEditor {

	private static Logger log = Logger.getLogger(CamelEditor.class);

	private File sourceFile;
	
	public CamelEditor(String title) {

		super(title);
	}

	/**
	 * Adds a component into the Camel Editor
	 * 
	 * @param component
	 *            component to add
	 */
	public void addCamelComponent(CamelComponent component) {

		log.debug("Adding '" + component.getLabel() + "' component into the Camel Editor");
		addToolFromPalette(component.getPaletteEntry(), 0, 0);
		AbstractWait.sleep(TimePeriod.SHORT);
	}

	/**
	 * Deletes the given component from the Camel Editor
	 * 
	 * @param component
	 *            component to delete
	 */
	public void deleteCamelComponent(CamelComponent component) {

		log.debug("Removing '" + component.getLabel() + "' component from the Camel Editor");
		new LabeledEditPart(component.getLabel()).select();
		new ContextMenu("Remove").select();
	}

	/**
	 * Sets a breakpoint on given component in the Camel Editor
	 * 
	 * @param component
	 *            instance of component in Camel Editor
	 */
	public void setBreakpoint(String label) {

		log.debug("Setting a new breakpoint on component: " + label);
		doOperation(label, "Set Breakpoint");
		if (new ShellWithTextIsAvailable("Please confirm...").test()) {
			new DefaultShell("Please confirm...");
			new PushButton("OK").click();
		}
	}

	/**
	 * Sets a conditional breakpoint on the given component in the Camel Editor
	 * 
	 * @param label
	 *            label of instance of component in Camel Editor
	 * @param language
	 *            language of the condition
	 * @param condition
	 *            condition's expression
	 */
	public void setConditionalBreakpoint(String label, String language, String condition) {

		log.debug("Setting a new conditional breakpoint on component: " + label);
		doOperation(label, "Set Conditional Breakpoint");
		new DefaultShell();
		new DefaultCombo().setSelection(language);
		new DefaultStyledText().setText(condition);
		new PushButton("OK").click();
		if (new ShellWithTextIsAvailable("Please confirm...").test()) {
			new DefaultShell("Please confirm...");
			new PushButton("OK").click();
		}
	}

	/**
	 * Disables a breakpoint on the component
	 * 
	 * @param label
	 *            label of instance of component in Camel Editor
	 */
	public void disableBreakpoint(String label) {

		log.debug("Disabling breakpoint on component: " + label);
		doOperation(label, "Disable Breakpoint");
	}

	/**
	 * Enables a breakpoint on the component
	 * 
	 * @param label
	 *            label of instance of component in Camel Editor
	 */
	public void enableBreakpoint(String label) {

		log.debug("Enabling breakpoint on component: " + label);
		doOperation(label, "Enable Breakpoint");
	}

	/**
	 * Deletes a breakpoint on the component
	 * 
	 * @param label
	 *            label of instance of component in Camel Editor
	 */
	public void deleteBreakpoint(String label) {

		log.debug("Deleting breakpoint on component: " + label);
		doOperation(label, "Delete Breakpoint");
	}

	/**
	 * Sets new parameters of a conditional breakpoint on the component
	 * 
	 * @param label
	 *            label of instance of component in Camel Editor
	 * @param language
	 *            condition's language
	 * @param condition
	 *            condition's expression
	 */
	public void editConditionalBreakpoint(String label, String language, String condition) {

		log.debug("Editing conditional breakpoint on component: " + label);
		doOperation(label, "Edit Conditonal Breakpoint");
		new DefaultShell();
		new DefaultCombo().setSelection(language);
		new DefaultText().setText(condition);
		new PushButton("OK").click();
	}

	/**
	 * Checks whether is a breakpoint set on the component
	 * 
	 * @param label
	 *            label of instance of component in Camel Editor
	 * @return true - breakpoint is set, false - otherwise
	 */
	public boolean isBreakpointSet(String label) {

		new GEFEditor().click(5, 5);
		new LabeledEditPart(label).select();
		try {
			new ContextMenu("Set Breakpoint");
		} catch (SWTLayerException ex) {
			return true;
		}

		return false;
	}

	/**
	 * Checks whether is breakpoint enabled on the component
	 * 
	 * @param label
	 *            label of instance of component in Camel Editor
	 * @return true - breakpoint is enabled, false - otherwise
	 */
	public boolean isBreakpointEnabled(String label) {

		if (!isBreakpointSet(label))
			return false;

		new GEFEditor().click(5, 5);
		new LabeledEditPart(label).select();
		try {
			new ContextMenu("Enable Breakpoint");
		} catch (SWTLayerException ex) {
			return true;
		}

		return false;
	}

	/**
	 * Performs the given operation on a component described with the given
	 * label
	 * 
	 * @param label
	 *            label of instance of component in Camel Editor
	 * @param operation
	 *            name of operation in the context menu
	 */
	public void doOperation(String label, String... operation) {

		log.debug("Executing operation '" + operation + "' on the component: " + label);
		new GEFEditor().click(5, 5);
		new LabeledEditPart(label).select();
		try {
			new ContextMenu(operation).select();
		} catch (SWTLayerException ex) {
			log.error("Given operation is not present in the context menu of the component: " + label);
		}
	}

	/**
	 * Sets an id to a component described with the given label
	 * 
	 * @param label
	 *            label of instance of component in Camel Editor
	 * @param id
	 *            component's id
	 */
	public void setId(String label, String id) {

		log.debug("Setting id '" + id + "' to the component: " + label);
		PropertiesView properties = new PropertiesView();
		properties.open();
		selectEditPart(label);
		properties.activate();
		properties.selectTab("Generic");
		AbstractWait.sleep(TimePeriod.SHORT);
		new LabeledText("Id").setText(id);
		activate();
		AbstractWait.sleep(TimePeriod.SHORT);
	}

	/**
	 * Switchs between 'Design' and 'Source' tab of the Camel Editor
	 * 
	 * @param name
	 *            'Design' or 'Source'
	 */
	public static void switchTab(String name) {

		log.debug("Switching on the tab '" + name + "'");
		new DefaultCTabItem(name).activate();
	}

	/**
	 * Checks whether a component with given name is available in the Camel
	 * Editor
	 * 
	 * @param name
	 *            name of the component
	 * @return true - component is available, false - otherwise
	 */
	public boolean isComponentAvailable(String name) {

		log.debug("Looking for '" + name + "' component in the Camel Editor");
		new GEFEditor().click(5, 5);
		try {
			new LabeledEditPart(name).select();
		} catch (GEFLayerException ex) {
			return false;
		}
		return true;
	}

	/**
	 * Sets a property to desired value. It presumes that some component in the Camel Editor is selected.
	 * 
	 * @param component
	 * 			  component in the Camel editor
	 * @param name
	 *            name of the property
	 * @param value
	 *            value of the property
	 */
	public void setProperty(String name, String value) {

		log.debug("Setting '" + value + "' as the property '" + name + "' of selelected component in the Camel Editor");
		new PropertiesView().open();
		new PropertiesView().selectTab("Generic");
		new LabeledText(name).setText(value);
		activate();
		AbstractWait.sleep(TimePeriod.SHORT);
	}

	/**
	 * Sets a property to desired value.
	 * 
	 * @param component
	 * 			  component in the Camel editor
	 * @param name
	 *            name of the property
	 * @param value
	 *            value of the property
	 */
	public void setProperty(String component, String name, String value) {

		log.debug("Setting '" + value + "' as the property '" + name + "' of selelected component in the Camel Editor");
		PropertiesView properties = new PropertiesView();
		properties.open();
		selectEditPart(component);
		properties.activate();
		properties.selectTab("Generic");
		new LabeledText(name).setText(value);
		activate();
		AbstractWait.sleep(TimePeriod.SHORT);
	}

	/**
	 * Sets a property to desired value.
	 * 
	 * @param component
	 * 			  component in the Camel editor
	 * @param name
	 *            name of the property
	 * @param value
	 *            value of the property
	 */
	public void setComboProperty(String component, int position, String value) {

		log.debug("Setting '" + value + "' as the property number '" + position
				+ "' of selelected component in the Camel Editor");
		PropertiesView properties = new PropertiesView();
		properties.open();
		selectEditPart(component);
		properties.activate();
		properties.selectTab("Generic");
		new DefaultCombo(position).setText(value);
		activate();
		AbstractWait.sleep(TimePeriod.SHORT);
	}

	/**
	 * Tries to add connection between 'source' component and 'target' component
	 * 
	 * @param source
	 *            name of the source component
	 * @param target
	 *            name of the target component
	 */
	public void addConnection(String source, String target) {

		activate();
		final Point fromCoords = getCoords(source);
		final Point toCoords = getCoords(target);
		MouseAWTManager.AWTMouseMove(fromCoords.x, fromCoords.y);
		new LabeledEditPart(source).click();
		AbstractWait.sleep(TimePeriod.SHORT);
		MouseAWTManager.AWTMouseMoveFromTo(new Point(fromCoords.x + 70, fromCoords.y + 20), new Point(fromCoords.x + 140, fromCoords.y + 20));
		MouseAWTManager.AWTMousePress();
		MouseAWTManager.AWTMouseMoveFromTo(new Point(fromCoords.x + 140, fromCoords.y + 20), new Point(toCoords.x + 10, toCoords.y + 20));
		MouseAWTManager.AWTMouseRelease();
		activate();
		AbstractWait.sleep(TimePeriod.SHORT);
	}

	/**
	 * Selects an edit part with a given name
	 * 
	 * @param name name of edit part
	 */
	public void selectEditPart(String name) {
		
		activate();
		new LabeledEditPart(name).select();
	}

	/**
	 * Retrieves coordinates of given component
	 * 
	 * @param item
	 *            an item.
	 * @return absolute coordinates of given element
	 */
	private Point getCoords(String name) {

		activate();
		final CamelComponentEditPart component = new CamelComponentEditPart(name);
		final int x = component.getBounds().x;
		final int y = component.getBounds().y;
		return Display.syncExec(new ResultRunnable<Point>() {

			@Override
			public Point run() {
				
				Composite parent = component.getControl().getParent();
				int tempX = x + parent.toDisplay(1, 1).x;
				int tempY = y + parent.toDisplay(1, 1).y;
				return new Point(tempX, tempY);
			}
		});
	}
	
	public String xpath(String expr) throws FileNotFoundException {
		XPathEvaluator xpath = new XPathEvaluator(new FileReader(getSourceFile()));
		String result = xpath.evaluateString(expr);
		return result;
	}

	public File getSourceFile() {
		if (sourceFile == null) {
			IEditorInput editorInput = editorPart.getEditorInput();
			if (editorInput instanceof FileEditorInput) {
				FileEditorInput fileEditorInput = (FileEditorInput) editorInput;
				sourceFile = fileEditorInput.getPath().toFile();
			}
		}
		return sourceFile;
	}

	public String getSource() throws IOException {
		StringBuffer source = new StringBuffer();
		BufferedReader in = new BufferedReader(new FileReader(getSourceFile()));
		String line = null;
		while ((line = in.readLine()) != null) {
			source.append(line).append("\n");
		}
		in.close();
		return source.toString();
	}
}
