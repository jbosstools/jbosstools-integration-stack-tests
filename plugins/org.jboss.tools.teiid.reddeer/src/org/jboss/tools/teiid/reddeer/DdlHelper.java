package org.jboss.tools.teiid.reddeer;

import static org.hamcrest.core.Is.is;

import java.io.StringReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.dialog.GenerateDynamicVdbDialog;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.exports.DDLTeiidExportWizard;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.xml.sax.InputSource;

public class DdlHelper {
	
	@Rule
	private ErrorCollector collector;
	
	public DdlHelper(ErrorCollector collector){
		this.collector = collector;
	}
	
	public String createDynamicVdb(String sourceProject, String staticVdbName, String dynamicVdbName) {
		GenerateDynamicVdbDialog wizard = new ModelExplorer().generateDynamicVDB(sourceProject, staticVdbName);
		wizard.setName(dynamicVdbName)
				.setFileName(dynamicVdbName)
				.setLocation(sourceProject)
				.next()
				.generate();
		String contents = wizard.getContents();
		wizard.finish();
		return contents;
	}
	
	public String exportDDL(String sourceProject, String model, String targetProject) {
		new ModelExplorer().selectItem(sourceProject,model + ".xmi");
	
		DDLTeiidExportWizard.openWizard()
				.setLocation(sourceProject,model + ".xmi")
				.setNameInSource(true)
				.setNativeType(true)
				.nextPage()
				.exportToWorkspace(model, targetProject)
				.finish();
		new DefaultTreeItem(targetProject,model).doubleClick();
		
		return new DefaultStyledText().getText();
	}
	
	public String getXPath(String xml, String path) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			return xpath.evaluate(path, new InputSource(new StringReader(xml)));
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("error evaluating xpath");
		}
		return null;
	}
	
	public String deploy(String project, String dynamicVDB,TeiidServerRequirement teiidServer) {
		ServersViewExt serversView = new ServersViewExt();
		if(serversView.isVDBDeployed(teiidServer.getName(), ServersViewExt.ServerType.DV6, dynamicVDB)){
			serversView.undeployVdb(teiidServer.getName(), dynamicVDB);
		}
		new ModelExplorer().deployVdb(project, dynamicVDB);
		serversView.refreshServer(teiidServer.getName());
		return serversView.getVdbStatus(teiidServer.getName(), dynamicVDB);
		
	}
	
	public void checkXpathPermission(String xml, String role, String resource, String permission, String expected) {
		try {
			String path = String.format("/vdb/data-role[@name='%s']/permission[resource-name='%s']/%s", role, resource,
					permission);
			String perm = getXPath(xml, path);
			collector.checkThat("wrong value for " + path, perm, is(expected));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
}
