package com.emptyminds.demo.models;

import java.util.concurrent.TimeUnit;

import org.apache.camel.component.http4.HttpClientConfigurer;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;

public class MyHttpClientConfigurer implements HttpClientConfigurer{
	
	@Override
	public void configureHttpClient(HttpClientBuilder clientBuilder) {
		clientBuilder
			.setDefaultRequestConfig(
				RequestConfig.custom()
					.setConnectionRequestTimeout(100)
					.setConnectTimeout(100)
					.setSocketTimeout(100).build())
			.setMaxConnTotal(1)
			.setConnectionTimeToLive(10, TimeUnit.MINUTES)
//			.setMaxConnPerRoute(1)
			;
	}
}
