package com.fiestacabin.dropwizard.guice;

import com.codahale.dropwizard.jersey.setup.JerseyEnvironment;
import com.codahale.dropwizard.servlets.tasks.Task;
import com.codahale.dropwizard.setup.AdminEnvironment;
import com.codahale.dropwizard.setup.Environment;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.fiestacabin.dropwizard.common.resources.CommonResource;
import com.fiestacabin.dropwizard.guice.test.MultiPackageService;
import com.fiestacabin.dropwizard.guice.test.SampleService;
import com.fiestacabin.dropwizard.guice.test.SampleServiceConfiguration;
import com.fiestacabin.dropwizard.guice.test.resources.MyResource;
import com.fiestacabin.dropwizard.guice.test.tasks.MyTask;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class AutoConfigServiceTest {
    @Mock
    private SampleServiceConfiguration configuration;
    @Mock
    private Environment environment;
    @Mock
    private HealthCheckRegistry hcRegistry;
    @Mock
    private JerseyEnvironment jersey;
    @Mock
    private AdminEnvironment admin;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(environment.healthChecks()).thenReturn(hcRegistry);
        when(environment.jersey()).thenReturn(jersey);
        when(environment.admin()).thenReturn(admin);
    }

    @Test
    public void itInstallsResources() throws Exception {
        SampleService s = new SampleService();
        s.run(configuration, environment);

        ArgumentCaptor<MyResource> resource = ArgumentCaptor.forClass(MyResource.class);
        verify(environment.jersey()).register(resource.capture());
        assertThat(resource.getValue(), is(MyResource.class));
    }

    @Test
    public void itInstallsMultiPackageResources() throws Exception {
        MultiPackageService s = new MultiPackageService();
        s.run(configuration, environment);

        ArgumentCaptor<?> captor = ArgumentCaptor.forClass(Object.class);
        verify(environment.jersey(), times(2)).register(captor.capture());

        List<?> values = captor.getAllValues();
        assertEquals(2, values.size());

        Set<Class<?>> expectedResults = new HashSet<Class<?>>();
        expectedResults.add(MyResource.class);
        expectedResults.add(CommonResource.class);
        for (Object obj : values) {
            Class<?> cls = obj.getClass();
            expectedResults.remove(cls);
        }

        assertTrue(expectedResults.isEmpty());
    }

    @Test
    public void itWiresUpDependencies() throws Exception {
        SampleService s = new SampleService();
        s.run(configuration, environment);

        ArgumentCaptor<MyResource> resource = ArgumentCaptor.forClass(MyResource.class);
        verify(environment.jersey()).register(resource.capture());

        MyResource r = resource.getValue();
        assertThat(r.getMyService(), not(nullValue()));
        assertThat(r.getMyService().getMyOtherService(), not(nullValue()));
    }

    @Test
    public void itInstallsHealthChecks() throws Exception {
        SampleService s = new SampleService();
        s.run(configuration, environment);

        ArgumentCaptor<? extends HealthCheck> healthCheck = ArgumentCaptor.forClass(HealthCheck.class);
        ArgumentCaptor<String> healthCheckName = ArgumentCaptor.forClass(String.class);
        verify(environment.healthChecks()).register(healthCheckName.capture(), healthCheck.capture());
        assertThat(healthCheck.getValue(), instanceOf(HealthCheck.class));
    }

    @Test
    public void itInstallsTasks() throws Exception {
        SampleService s = new SampleService();
        s.run(configuration, environment);

        ArgumentCaptor<? extends Task> task = ArgumentCaptor.forClass(Task.class);
        verify(environment.admin()).addTask(task.capture());
        assertThat(task.getValue(), instanceOf(MyTask.class));
    }
}
