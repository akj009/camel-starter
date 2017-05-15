package com.emptyminds.demo.beans;

import org.apache.camel.Exchange;

public class MyBean2 {
	public void doSomething(Exchange exchange) {
		System.out.println("Bean Received Exchange: " + exchange.getIn().getBody(String.class) + ", MIP: " + exchange.getPattern());
	}
}
