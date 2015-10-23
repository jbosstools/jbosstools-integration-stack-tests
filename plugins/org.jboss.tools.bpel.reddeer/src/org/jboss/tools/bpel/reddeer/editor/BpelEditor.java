package org.jboss.tools.bpel.reddeer.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.ui.views.contentoutline.OutlineView;
import org.jboss.reddeer.gef.editor.GEFEditor;
import org.jboss.reddeer.swt.api.TreeItem;

/**
 * 
 * @author apodhrad
 * 
 */
public class BpelEditor extends GEFEditor {

	private Logger log = Logger.getLogger(BpelEditor.class);
	
	protected File sourceFile;

	public BpelEditor() {
		super();
	}

	public BpelEditor(String title) {
		super(title);
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

	public void delete() {
		// we assume that an activity was selected
		// clickContextMenu("Delete");
		// TODO JBTISTEST-168
		throw new UnsupportedOperationException();
	}

	public void saveAndClose() {

	}

	public void addPartnerLink() {
		throw new UnsupportedOperationException();
	}

	public void removePartnerLink() {
		throw new UnsupportedOperationException();

	}

	public void addVariable() {
		throw new UnsupportedOperationException();
	}
	
	public Variable getVariable(String variableName) {
		OutlineView outlineView = new OutlineView();
		outlineView.open();
		for (TreeItem treeItem :outlineView.outlineElements()) {
			if ("Variables".equals(treeItem.getText())) {
				for (TreeItem variableItem : treeItem.getItems()) {
					if (variableItem.getText().equals(variableName)) {
						return new Variable(variableItem);
					}
				}
			}
		}
		throw new RedDeerException("Cannot find a variable with name '" + variableName + "'");
	}

	public void removeVariable() {
		throw new UnsupportedOperationException();

	}
	
	public void debug() {
		try {
			getVariable("sayHelloRequest").setType("Messages", "sayHello");
			getVariable("sayHelloResponse").setType("Messages", "sayHelloResponse");
			System.out.println("done");
		} catch (Throwable t) {
			t.printStackTrace();
			System.out.println();
		}
	}
}
