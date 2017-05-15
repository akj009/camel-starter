package com.emptyminds.demo;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.jndi.JndiContext;

import com.emptyminds.demo.beans.MyBean1;
import com.emptyminds.demo.beans.MyBean2;

public class CamelChangeRouteInOnlyExample {
	public static void main(String[] args) throws Exception {
		JndiContext jndiContext = new JndiContext();
		jndiContext.bind("myBean1", new MyBean1());
		jndiContext.bind("myBean2", new MyBean2());
		CamelContext camelContext = new DefaultCamelContext(jndiContext);
		try {
			camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					from("direct:start")
					.to("bean:myBean1")
					.inOnly("direct:oneWay")
					.to("bean:myBean1")
					.transform().constant("Done");;
					
					from("direct:oneWay")
					.to("bean:myBean2");
				}
			});
			camelContext.start();	
			ProducerTemplate template = camelContext.createProducerTemplate();
			Object response = template.sendBody("direct:oneWay", ExchangePattern.InOut, "X");
			System.out.println("Response: " + response);
		} finally {
			camelContext.stop();
		}
	}
}