package com.emptyminds.demo.routes;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class Http4Route {

	public static void main(String[] args) throws Exception {
		CamelContext ctx = new DefaultCamelContext();
		ctx.addRoutes(new HTTP4RouteBuilder());
		ctx.start();
		// setting httpclientconfigurer
//		HttpComponent http4Component = ctx.getComponent("http4", HttpComponent.class);
//		http4Component.setHttpClientConfigurer(new MyHttpClientConfigurer());
//		
//		http4Component.setConnectionsPerRoute(1);
//		http4Component.setMaxTotalConnections(2);
//		
		
		
		ProducerTemplate pt = ctx.createProducerTemplate();
		ExecutorService exs = Executors.newFixedThreadPool(10);
		for(int i=0; i<10; i++) {
			exs.execute(() -> {
					Exchange ex = pt.send("direct:http4",exOb -> {});
					System.out.println(ex.getOut().getBody(String.class));
				});
			Thread.sleep(100);
		}
		
		ctx.stop();
	}
}

class HTTP4RouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:http4")
			.to("http4://127.0.0.1:8089/mockcarrier/digiext/verify"
					+ "?httpClient.maxConnTotal=5&authenticationPreemptive=true&authUsername=qwe&authPassword=1234"
					);		
	}
	
}
