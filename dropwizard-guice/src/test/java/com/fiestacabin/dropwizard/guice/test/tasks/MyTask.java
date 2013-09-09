package com.fiestacabin.dropwizard.guice.test.tasks;

import java.io.PrintWriter;

import com.codahale.dropwizard.servlets.tasks.Task;
import com.google.common.collect.ImmutableMultimap;

public class MyTask extends Task {

	public MyTask() {
		super("my-task");
	}
	
	@Override
	public void execute(ImmutableMultimap<String, String> parameters,
			PrintWriter output) throws Exception {

		output.println("my task complete.");
	}

}
