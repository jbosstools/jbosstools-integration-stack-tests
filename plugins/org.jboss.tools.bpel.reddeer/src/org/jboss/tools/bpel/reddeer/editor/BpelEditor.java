package org.jboss.tools.bpel.reddeer.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.gef.editor.GEFEditor;

/**
 * 
 * @author apodhrad
 * 
 */
public class BpelEditor extends GEFEditor {

	@SuppressWarnings("unused")
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

	public void removeVariable() {
		throw new UnsupportedOperationException();

	}
}
