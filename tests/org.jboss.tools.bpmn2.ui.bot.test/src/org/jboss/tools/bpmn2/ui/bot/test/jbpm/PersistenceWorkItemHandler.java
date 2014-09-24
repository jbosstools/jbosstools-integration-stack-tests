package org.jboss.tools.bpmn2.ui.bot.test.jbpm;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

/**
 * Serves as persistence of occurred WorkItem-s  
 * @author jomarko
 *
 */
public class PersistenceWorkItemHandler implements WorkItemHandler {

	private Logger log = Logger.getLogger(getClass());  

	private List<WorkItem> workItems = new ArrayList<WorkItem>();
	
	public WorkItem getWorkItem(String nodeName) {
		for(WorkItem item : workItems) {
			if(((String) item.getParameter("NodeName")).compareTo(nodeName) == 0) {
				return item;
			}
		}
		
		return null;
	}
	
	public List<WorkItem> getWorkItems() {
		return workItems;
	}
	
	public void completeWorkItem(WorkItem workItem, WorkItemManager manager) {
		workItems.remove(workItem);
		manager.completeWorkItem(workItem.getId(), null);
	}
	
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		
		workItems.add(workItem);
	}
	
	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		log.debug("Aborting work item: " + workItem.getName());
	}
}
