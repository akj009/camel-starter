package com.emptyminds.demo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpClientConfigurer;
import org.apache.camel.component.http4.HttpComponent;
import org.apache.commons.httpclient.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;

public class DirectRestRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:otvs_send_sms")
			.to("http4://localhost:8089/mockcarrier/otvs_send_sms?httpClient.socketTimeout=1000");
		
		from("direct:otvs_renewal_success")
//			.process(this::handleTimeout)
			.to("http4://localhost:8089/mockcarrier/otvs_renewal_success?httpClient.socketTimeout=8000")
			;
		
		from("direct:digi_verify")
		.to("http4:localhost:8089/mockcarrier/digiext/verify");
		
		from("direct:send")
			.routeId("send_sms")
			//.to("http://localhost:8089/mockcarrier/otvs_send_sms")
			.to("direct:otvs_send_sms")
			//.process(arg0 -> System.out.println("EXCHANGE: " + arg0.getIn().getBody(String.class)))
			;
		
		from("direct:renewal")
			.routeId("renewal")
			//.to("http://localhost:8089/mockcarrier/otvs_renewal_success")
			.to("direct:otvs_renewal_success")
			//.process(arg0 -> System.out.println("EXCHANGE: " + arg0.getIn().getBody(String.class)))
			;
		
		from("direct:sendWithTimeout")
			.routeId("send_sms_timeout")
			.hystrix()
				.hystrixConfiguration()
					.executionIsolationStrategy("SEMAPHORE")
//					.executionIsolationSemaphoreMaxConcurrentRequests(2)
					.circuitBreakerRequestVolumeThreshold(1)
					.metricsRollingStatisticalWindowInMilliseconds(10000)
//					.executionTimeoutInMilliseconds(500)
//					.circuitBreakerErrorThresholdPercentage(100)
					.circuitBreakerSleepWindowInMilliseconds(1000)
					.executionTimeoutEnabled(false)
				.end()
				.toD("http4://localhost:8089/mockcarrier/send_sms/${headers.msisdn}?httpClient.socketTimeout=3000")
				.onFallback()
//				.log("Failed")
				.process(this::handleFallback)
				//.to("direct:otvs_renewal_success")
			.end()
//			.process(this::dummy)
			;
	}
	
	private void dummy(Exchange exchange) {
		System.out.println("DUMMY METHOD");
		System.out.println(exchange.getProperties());
	}
	
	private void handleFallback(Exchange ex) {
		System.out.println("Fallback");
	}
	
	/*private void handleTimeout(Exchange exchange) {
		HttpComponent httpComponent = exchange.getContext().getComponent("http4",HttpComponent.class);
		httpComponent.setHttpClientConfigurer(new SpecificHTTPClientConfigurer());
	}*/
}