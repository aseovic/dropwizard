package com.fiestacabin.dropwizard.guice.test.health;

import com.codahale.metrics.health.HealthCheck;

public class MyHealthCheck extends HealthCheck {

	public MyHealthCheck() {
        super();
	}
	
	@Override
	protected Result check() throws Exception {
		return Result.healthy();
	}

}
