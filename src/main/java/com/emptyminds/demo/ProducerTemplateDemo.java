package com.emptyminds.demo;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ProducerTemplateDemo {

	private static final DefaultCamelContext defaultCamelContext = new DefaultCamelContext(); 
	public static void main(String[] args) throws Exception {
//		ProducerTemplate template = defaultCamelContext.createProducerTemplate();
//		Exchange exchange = template.request("http://localhost:8089/mockcarrier/otvs_send_sms", exchangeObjcet -> {	});
//		System.out.println("Response: " + exchange.getOut().getBody(String.class));
		
//		func1();
		func2();
		
//		func3();
	}
	
	public static void func1() throws Exception {
		
		defaultCamelContext.addRoutes(new ProducerTemplateDemo().new MyTempRoutes());
		defaultCamelContext.start();
		Thread.sleep(1000);
		defaultCamelContext.stop();
		
	}
	
	public static void func2() {
		RouteBuilder rb = new ProducerTemplateDemo().new MyTempRoutes();
		try {
			defaultCamelContext.addRoutes(rb);
			
			System.out.println(defaultCamelContext.getEndpointMap());
			System.out.println(defaultCamelContext.getComponentNames());
			System.out.println(defaultCamelContext.getRoutes());
			System.out.println(defaultCamelContext.getRouteDefinitions());
//		ProducerTemplate pt = defaultCamelContext.createProducerTemplate();
			System.out.println("Starting...");
			defaultCamelContext.startRoute(defaultCamelContext.getRouteDefinition("rest-route-1"));
			defaultCamelContext.startRoute("rest-route-2");
			defaultCamelContext.start();
			Thread.sleep(1000);
			defaultCamelContext.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		Exchange ex1 = pt.send("direct:rest1", exchangeObject ->{});
//		Exchange ex2 = pt.send("direct:rest2", exchangeObject ->{});
		
//		System.out.println("Response[rest1]: " + ex1.getOut().getBody(String.class));
//		System.out.println("Response[rest2]: " + ex2.getOut().getBody(String.class));
	}
	
	public static void func3() throws Exception{
		defaultCamelContext.addRoutes(new ProducerTemplateDemo().new MyConsumerRoute());
		ConsumerTemplate ct = defaultCamelContext.createConsumerTemplate();
		defaultCamelContext.start();
		Exchange ex = ct.receive("direct:end");
		defaultCamelContext.stop();
		System.out.println(ex.getOut().getBody());
	}
	
	class MyTempRoutes extends RouteBuilder{

		@Override
		public void configure() throws Exception {
			from("timer://simpleTimer?repeatCount=1").routeId("rest-route-1")
				.to("http://localhost:8089/mockcarrier/otvs_renewal_success")
				.process(exchange -> {
					String msg = exchange.getIn().getBody(String.class);
					System.out.println("Message: " + msg);
					exchange.getOut().setBody(msg);})
				.to("file://D:/tmp/?fileName=restoutput2&autoCreate=true&fileExist=Append");
			
			
			from("timer://anothersimpleTimer?repeatCount=1").routeId("rest-route-2")
				.to("http://localhost:8089/mockcarrier/otvs_send_sms")
				.process(exchange -> {
					String msg = exchange.getIn().getBody(String.class);
					System.out.println("Message: " + msg);
					exchange.getOut().setBody(msg);})
				.to("file://D:/tmp/?fileName=restoutput1&autoCreate=true&fileExist=Append");
			
		}
		
	}
	
	class MyConsumerRoute extends RouteBuilder {

		@Override
		public void configure() throws Exception {
			from("restlet:http://localhost:8089/mockcarrier/otvs_renewal_success")
				.routeId("rest-route-3")
				.to("direct:end");
			
		}
		
	}

}
