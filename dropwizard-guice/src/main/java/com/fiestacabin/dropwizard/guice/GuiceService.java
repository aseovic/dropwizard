package com.fiestacabin.dropwizard.guice;

import com.codahale.dropwizard.Application;
import com.codahale.dropwizard.Configuration;
import com.codahale.dropwizard.setup.Bootstrap;
import com.codahale.dropwizard.setup.Environment;
import com.google.inject.Guice;
import com.google.inject.Injector;

public abstract class GuiceService<T extends Configuration> extends Application<T> {

	protected GuiceService() {}

	@Override
	public void initialize(Bootstrap<T> bootstrap) {
	}
	
	@Override
	public void run(T configuration, Environment environment) throws Exception {
		Injector injector = createInjector(configuration);
		injector.injectMembers(this);
		runWithInjector(configuration, environment, injector);
	}

	protected Injector createInjector(T configuration) {
		return Guice.createInjector();
	}
	
	protected abstract void runWithInjector(T configuration,
			Environment environment, Injector injector) throws Exception;
	
}
