package org.jboss.tools.fuse.reddeer.component;

import java.util.ArrayList;
import java.util.List;

public class CamelComponents {

	public static List<CamelComponent> getAll() {
		List<CamelComponent> list = new ArrayList<CamelComponent>();
		list.addAll(getEndpoints());
		list.addAll(getRouting());
		list.addAll(getControlFlow());
		list.addAll(getTransformation());
		list.addAll(getMiscellaneous());
		return list;
	}

	public static List<CamelComponent> getEndpoints() {
		List<CamelComponent> list = new ArrayList<CamelComponent>();
		list.add(new Generic());
		list.add(new Bean());
		list.add(new Log());
		list.add(new Process());
		return list;
	}

	public static List<CamelComponent> getRouting() {
		List<CamelComponent> list = new ArrayList<CamelComponent>();
		list.add(new Aggregate());
		list.add(new Choice());
		list.add(new DynamicRouter());
		list.add(new Filter());
		list.add(new IdempotentConsumer());
		list.add(new LoadBalance());
		list.add(new Multicast());
		list.add(new Otherwise());
		list.add(new Pipeline());
		list.add(new RecipientList());
		list.add(new Resequence());
		list.add(new RoutingSlip());
		list.add(new Sort());
		list.add(new Split());
		list.add(new When());
		list.add(new WireTap());
		return list;
	}

	public static List<CamelComponent> getControlFlow() {
		List<CamelComponent> list = new ArrayList<CamelComponent>();
		list.add(new Catch());
		list.add(new Delay());
		list.add(new Finally());
		list.add(new Intercept());
		list.add(new InterceptFrom());
		list.add(new InterceptSendToEndpoint());
		list.add(new Loop());
		list.add(new OnCompletion());
		list.add(new OnException());
		list.add(new Rollback());
		list.add(new Throttle());
		list.add(new ThrowException());
		list.add(new Transacted());
		list.add(new Try());
		return list;
	}

	public static List<CamelComponent> getTransformation() {
		List<CamelComponent> list = new ArrayList<CamelComponent>();
		list.add(new ConvertBody());
		list.add(new Enrich());
		list.add(new InOnly());
		list.add(new InOut());
		list.add(new Marshal());
		list.add(new PollEnrich());
		list.add(new RemoveHeader());
		list.add(new RemoveHeaders());
		list.add(new RemoveProperty());
		list.add(new SetBody());
		list.add(new SetExchangePattern());
		list.add(new SetFaultBody());
		list.add(new SetHeader());
		list.add(new SetOutHeader());
		list.add(new SetProperty());
		list.add(new Transform());
		list.add(new Unmarshal());
		return list;
	}

	public static List<CamelComponent> getMiscellaneous() {
		List<CamelComponent> list = new ArrayList<CamelComponent>();
		list.add(new AOP());
		list.add(new Policy());
		list.add(new Sampling());
		list.add(new Stop());
		list.add(new Threads());
		list.add(new Validate());
		return list;
	}

	public static List<CamelComponent> getSAP() {
		List<CamelComponent> list = new ArrayList<CamelComponent>();
		list.add(new SAPIDocListServer());
		list.add(new SAPSRFCServer());
		list.add(new SAPTRFCServer());
		list.add(new SAPIDocDestination());
		list.add(new SAPIDocListDestination());
		list.add(new SAPQIDocDestination());
		list.add(new SAPQIDocListDestination());
		list.add(new SAPQRFCDestination());
		list.add(new SAPSRFCDestination());
		list.add(new SAPTRFCDestination());
		return list;
	}
}
