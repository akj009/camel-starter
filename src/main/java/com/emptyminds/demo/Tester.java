package com.emptyminds.demo;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class Tester {
	
	static void invokeTimerDemo() throws Exception {
		CamelContext camelContext = new DefaultCamelContext();
		try {
			camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					from("timer://foo?repeatCount=1")
							.setBody(simple("Hello from timer at ${header.firedTime}"))
							.to("stream:out");
				}
			});
			camelContext.start();
			Thread.sleep(3000);
		} finally {
			camelContext.stop();
		}
	}
	
	static void invokeFileRoute() {
		CamelContext ctx = new DefaultCamelContext();
		MyRouteBuilder routebuilder = new MyRouteBuilder();
		System.out.println("invokeFileRoute() started.");
		try {
			ctx.addRoutes(routebuilder);
			ctx.start();
			Thread.sleep(1000);
			ctx.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("invokeFileRoute() done");
	}


	private static void invokeRestToFileRoute() {
		CamelContext ctx = new DefaultCamelContext();
		RouteBuilder routebuilder = new RestToFileRoute();
		
		
		System.out.println("invokeRestToFileRoute() started.");
		try {
			ctx.addRoutes(routebuilder);
			
			System.out.println(routebuilder.getRouteCollection().toString());
			ctx.start();
			Thread.sleep(10000);
			ctx.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		System.out.println("invokeRestToFileRoute() done");
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Main started.");
		
		//invokeTimerDemo();
		//invokeFileRoute();
		invokeRestToFileRoute();
		
		System.out.println("Done with main.");
		
	}
}
