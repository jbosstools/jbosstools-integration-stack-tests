package org.jboss.tools.runtime.reddeer.condition;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.jobs.Job;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.logging.Logger;

/**
 * 
 * @author apodhrad
 *
 */
public class JobIsKilled extends AbstractWaitCondition {

	private static final Logger log = Logger.getLogger(JobIsKilled.class);

	private Job[] currentJobs;
	private Set<String> jobsToBeKilled;

	public JobIsKilled(String... jobsToBeKilled) {
		this.jobsToBeKilled = new HashSet<>();
		for (String jobToBeKilled : jobsToBeKilled) {
			this.jobsToBeKilled.add(jobToBeKilled);
		}
	}

	@Override
	public boolean test() {
		currentJobs = Job.getJobManager().find(null);
		for (Job job : currentJobs) {
			if (jobsToBeKilled.contains(job.getName())) {
				log.info("Job '" + job.getName() + "' will be killed");
				job.cancel();
				jobsToBeKilled.remove(job.getName());
			}
		}
		return jobsToBeKilled.isEmpty();
	}

	@Override
	public String description() {
		return "at least one job is waiting for killing";
	}

	@Override
	public String errorMessage() {
		return "The following jobs were not killed " + jobsToBeKilled;
	}

}
