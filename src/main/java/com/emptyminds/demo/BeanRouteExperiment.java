package com.emptyminds.demo;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.PropertyPlaceholderDelegateRegistry;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.spi.Registry;

import com.emptyminds.demo.beans.SendSMSBean;

public class BeanRouteExperiment {

	static CamelContext ctx = new DefaultCamelContext(new SimpleRegistry());
	
	static {
		try {
			registerBeans();
			ctx.addRoutes(new RestBeanRouteBuilder());
			ctx.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
	}

	private static void registerBeans() {
		Registry registry = ctx.getRegistry();
		if(registry instanceof PropertyPlaceholderDelegateRegistry) {
			registry = ((PropertyPlaceholderDelegateRegistry)registry).getRegistry();
		}
		
		((SimpleRegistry)registry).put("send-sms-bean", new SendSMSBean());
		((SimpleRegistry)registry).put("renewal-bean", new SendSMSBean());
	}
}

class RestBeanRouteBuilder extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
		
		System.out.println("Creating route definitions");
		for(int i=0; i<=1; i++) {
			if(i==0) {
				System.out.println("Creating first route");
				from("timer://simpletimer?repeatCount=1").autoStartup(false)
				.routeId("send_sms")
				.to("http://localhost:8089/mockcarrier/otvs_send_sms")
				.process(arg0 -> System.out.println("EXCHANGE: " + arg0.getIn().getBody(String.class)))
				;
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
