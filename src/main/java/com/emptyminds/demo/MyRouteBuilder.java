package com.emptyminds.demo;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("file://D:/tmp/?noop=true")
			.process(exchange -> {System.out.println("processing input from file >"+exchange.getIn().getBody(String.class));})
			.to("file://D:/tmp/?fileName=output");	
	}

}
