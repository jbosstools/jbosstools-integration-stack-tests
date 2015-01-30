package org.jboss.tools.modeshape.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.jboss.reddeer.eclipse.jdt.ui.ide.NewJavaProjectWizardDialog;
import org.jboss.reddeer.eclipse.ui.ide.NewFileCreationWizardDialog;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.modeshape.reddeer.shell.PublishedLocations;
import org.jboss.tools.modeshape.reddeer.util.ModeshapeWebdav;
import org.jboss.tools.modeshape.reddeer.view.ModeshapeExplorer;
import org.jboss.tools.modeshape.reddeer.view.ModeshapeView;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Bot test for publishing/unpublishing files into/from ModeShape repository.
 * 
 * @author apodhrad, tsedmik
 */
@CleanWorkspace
@OpenPerspective(JavaPerspective.class)
@Server(type = ServerReqType.ANY, state = ServerReqState.RUNNING)
@RunWith(RedDeerSuite.class)
public class PublishingTest {

	@InjectRequirement
	private ServerRequirement server;

	public static final String PROJECT_NAME = "testproject";
	public static final String FILE_NAME = "testfile.txt";
	public static final String FILE_CONTENT = "testcontent";
	public static final String SERVER_URL = "http://localhost:8080/modeshape-rest";
	public static final String PUBLISH_AREA = "files";
	public static final String WORKSPACE = "default";

	@Test
	public void publishingTest() throws Exception {

		String repository = server.getConfig().getServerBase().getProperty("modeshape");
		String user =  server.getConfig().getServerBase().getProperty("modeshapeUser");
		String password =  server.getConfig().getServerBase().getProperty("modeshapePassword");
		
		/* Create Java Project */
		NewJavaProjectWizardDialog projectWizard = new NewJavaProjectWizardDialog();
		projectWizard.open();
		projectWizard.getFirstPage().setProjectName(PROJECT_NAME);
		projectWizard.finish();

		/* Create Text File */
		NewFileCreationWizardDialog fileWizard = new NewFileCreationWizardDialog();
		fileWizard.open();
		fileWizard.getFirstPage().setFolderPath(PROJECT_NAME);
		fileWizard.getFirstPage().setFileName(FILE_NAME);
		fileWizard.finish();

		/* Edit Text File */
		TextEditor editor = new TextEditor(FILE_NAME);
		editor.setText(FILE_CONTENT);
		editor.save();
		editor.close();

		/* Create ModeSHape Server */
		new ModeshapeView().addServer(SERVER_URL, user, password);
		
		/* Add publish area */
		new ModeshapeView().addPublishArea(SERVER_URL, repository, WORKSPACE, PUBLISH_AREA);
		System.out.println("DEBUG: publishing area " + PUBLISH_AREA + " added");

		/* Publish */
		new ModeshapeExplorer().publish(PROJECT_NAME).finish();
		assertTrue(new ModeshapeWebdav(SERVER_URL + "/v1", repository, WORKSPACE + "/items", PUBLISH_AREA).isFileAvailable(FILE_NAME, user, password));
		System.out.println("DEBUG: files published (webdav)");

		/* Published Locations */
		PublishedLocations locations = new ModeshapeExplorer().showPublishedLocations(PROJECT_NAME, FILE_NAME);
		String url = locations.getPublishedUrl();
		locations.ok();
		assertTrue(new ModeshapeWebdav(repository).isFileAvailable(new URL(url), user, password));
		System.out.println("DEBUG: files published (url)");

		/* Unpublish */
		new ModeshapeExplorer().unpublish(PROJECT_NAME).finish();
		assertFalse(new ModeshapeWebdav(SERVER_URL + "/v1", repository, WORKSPACE + "/items", PUBLISH_AREA).isFileAvailable(PROJECT_NAME + "/" + FILE_NAME, user, password));
		System.out.println("DEBUG: files unpublished");
	}
}

