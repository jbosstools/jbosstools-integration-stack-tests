package org.jboss.tools.drools.reddeer.wizard;

import java.util.List;

import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;

public class NewDroolsOnlineExampleProjectWizardPage {

	private static final String COMBO_REPOSITORY_LABEL = "Select Online Example Repository:";

	public void selectRepository(Repository repository) {
		new LabeledCombo(COMBO_REPOSITORY_LABEL).setSelection(repository.getName());
	}

	public void selectAllProjects(Repository repository) {
		selectRepository(repository);

		Tree tree = new DefaultTree();
		List<TreeItem> treeItems = tree.getAllItems();
		tree.selectItems(treeItems.toArray(new TreeItem[treeItems.size()]));
	}

	public enum Repository {
		
		DROOLS_601("Drools Playground at github (drools-6.0.1)"),
		DROOLSJBPM_54("Drools Playground at github (droolsjbpm-5.4)"),
		DROOLSJBPM_63("Drools Playground at github (droolsjbpm-6.3)"),
		JBPM_62("Drools Playground at github (jbpm-6.2)");

		private String name;

		Repository(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
