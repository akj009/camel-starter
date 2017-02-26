package com.emptyminds.demo;

import org.apache.camel.builder.RouteBuilder;

public class RestToFileRoute extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		System.out.println("RestToFileRoute {}");
		from("timer://simpleTimer?period=1000")
			.hystrix()
				.to("http://localhost:8089/mockcarrier/otvs_send_sms")
			.onFallback()
				.to("http://localhost:8089/mockcarrier/otvs_renewal_success")
			.end()
			.process(exchange -> {
				String msg = exchange.getIn().getBody(String.class);
				System.out.println("Message: " + msg);
				exchange.getOut().setBody(msg);})
			.to("file://D:/tmp/?fileName=restoutput&fileExist=Append");
	}

}
