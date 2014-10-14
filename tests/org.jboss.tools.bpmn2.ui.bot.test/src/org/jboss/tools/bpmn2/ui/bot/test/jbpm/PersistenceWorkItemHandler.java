package org.jboss.tools.bpmn2.ui.bot.test.jbpm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

/**
 * Serves as persistence of occurred WorkItem-s  
 * @author jomarko
 *
 */
public class PersistenceWorkItemHandler implements WorkItemHandler {

	private List<WorkItem> workItems = new ArrayList<WorkItem>();
	private Map<String, Map<String, Object>> results;
	
	public PersistenceWorkItemHandler() {
		results = new HashMap<String, Map<String,Object>>();
	}
	
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
		Map<String, Object> result = null;
		
		
		for(String nodeName : results.keySet()) {
			if(((String) workItem.getParameter("NodeName")).compareTo(nodeName) == 0){
				result = results.get(nodeName);
				break;
			}
		}
		
		manager.completeWorkItem(workItem.getId(), result);
		workItems.remove(workItem);
	}
	
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		workItems.add(workItem);
	}
	
	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		workItems.remove(workItem);
		manager.abortWorkItem(workItem.getId());
	}
	
	public void setResult(String nodeName, Map<String, Object> result) {
		results.put(nodeName, result);
	}
}
