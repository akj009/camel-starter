package com.emptyminds.demo.tester;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.http4.HttpClientConfigurer;
import org.apache.camel.component.http4.HttpComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.httpclient.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;

import com.emptyminds.demo.models.MyHttpClientConfigurer;
import com.emptyminds.demo.routes.DirectRestRouteBuilder;

public class DirectRestRouteTester {
	
	static CamelContext ctx = new DefaultCamelContext(); 

	static {
		try {
			HttpComponent httpComponent = ctx.getComponent("http4", HttpComponent.class);
//			httpComponent.setConnectionTimeToLive(10);
			httpComponent.setHttpClientConfigurer(new MyHttpClientConfigurer());
			ctx.addRoutes(new DirectRestRouteBuilder());
			ctx.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		ExecutorService ex = Executors.newFixedThreadPool(4);
		for(int i=0; i<100; i++) {
//			Thread t1 = new Thread(new InvokeRouteThread());
			ex.execute(new InvokeRouteThread());
			Thread.sleep(100);
		}
		ctx.stop();
//		ProducerTemplate pt = ctx.createProducerTemplate();
//		Exchange ex = pt.send("direct:otvs_send_sms", exOb -> {});
//		System.out.println(ex.getOut().getBody(String.class));
	}
	
	static class InvokeRouteThread implements Runnable {

		@Override
		public void run() {
			ProducerTemplate pt = ctx.createProducerTemplate();
//			String registrationRequestBody = "{\"service_id\":\"DG_VIU_VOD\",\"msisdn\":\"1373700001\",\"ref_id\":\"192716648244132\"}";
			Exchange ex = pt.request("direct:sendWithTimeout", exOb -> {
//			Exchange ex = pt.request("direct:digi_verify", exOb -> {
				Message inMessage = exOb.getIn();
				inMessage.setHeader("msisdn", "1234567");
//				inMessage.setHeader(Exchange.HTTP_METHOD, "POST");
//				inMessage.setBody(registrationRequestBody);
			});
			System.out.println("\n-----------------------------------");
			System.out.println(ex.getOut().getBody(String.class));
//			System.out.println(ex.getProperty(Exchange.EXCEPTION_CAUGHT));
//			System.out.println(ex.getProperty(Exchange.TO_ENDPOINT));
//			System.out.println(ex.getProperties());
			System.out.println("------------------------------------");
			
		}
	}
}
/*
class MyHTTPClientConfigurer implements HttpClientConfigurer {

	@Override
	public void configureHttpClient(HttpClientBuilder clientBuilder) {
		clientBuilder.setDefaultRequestConfig(
				RequestConfig.custom()
//					.setConnectionRequestTimeout(1000)
					.setConnectTimeout(1000)
					.setSocketTimeout(1000).build());
	}
	
}*/