package com.emptyminds.demo.beans;

import org.apache.camel.Exchange;

public class MyBean1 {
	public String doSomething(Exchange exchange) {
		System.out.println("Bean1 Received Exchange: " + exchange.getIn().getBody(String.class) + ", MIP: " + exchange.getPattern());
		return exchange.getIn().getBody(String.class);
	}
}
