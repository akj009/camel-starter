package com.emptyminds.demo;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RouteBuilderExperiment {
	public static void main(String[] args) throws Exception {
		Thread t1 = new Thread(new RouteRunner("send_sms"));
		Thread t2 = new Thread(new RouteRunner("renewal"));
		
		System.out.println("Starting t1");
		t1.start();
		System.out.println("Starting t2");
		t2.start();
	}
	
	static CamelContext ctx = new DefaultCamelContext();
	
	static {
		try {
			ctx.addRoutes(new RestRouteBuilder());
			ctx.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static synchronized void invokeRoute(String routeName) throws Exception {
		ctx.startRoute(routeName);
		Thread.sleep(1000);
		System.out.println("after sleep: " + routeName);
	}
}

class RestRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		System.out.println("Creating route definitions");
		for(int i=0; i<=1; i++) {
			if(i==0) {
				System.out.println("Creating first route");
				from("timer://simpletimer?repeatCount=1").autoStartup(false)
				.routeId("send_sms")
				.to("http://localhost:8089/mockcarrier/otvs_send_sms")
				.process(arg0 -> System.out.println("EXCHANGE: " + arg0.getIn().getBody(String.class)));
			}
			else {
				System.out.println("Creating second route");
				from("timer://simpletimer1?repeatCount=1").autoStartup(false)
				.routeId("renewal")
				.to("http://localhost:8089/mockcarrier/otvs_renewal_success")
				.process(arg0 -> System.out.println("EXCHANGE: " + arg0.getIn().getBody(String.class)));	
			}
		}		
	}
}

class RouteRunner implements Runnable {

	private String routeName;
	
	public RouteRunner(String routeName) {
		this.routeName = routeName;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Invoking route: " + routeName);
			RouteBuilderExperiment.invokeRoute(routeName);
		} catch (Exception e) {
			System.out.println("Exception while invoking route: " + e.getMessage());
			e.printStackTrace();
		}
	}
}