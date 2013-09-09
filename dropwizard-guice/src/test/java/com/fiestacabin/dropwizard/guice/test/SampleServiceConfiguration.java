package com.fiestacabin.dropwizard.guice.test;

import com.codahale.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SampleServiceConfiguration extends Configuration {

	@JsonProperty
	private String foo;
	
	public String getFoo() {
		return foo;
	}
	
}
