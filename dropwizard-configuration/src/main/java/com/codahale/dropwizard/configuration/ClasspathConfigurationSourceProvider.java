package com.codahale.dropwizard.configuration;

import java.io.*;

/**
 * An implementation of {@link ConfigurationSourceProvider} that reads the configuration from the
 * local file system. In case that configuration file was not found tries to read configuration from classpath.
 *
 * @author Vaso Putica  2013.08.07
 */
public class ClasspathConfigurationSourceProvider implements ConfigurationSourceProvider {
    @Override
    public InputStream open(String path) throws IOException {
        final File file = new File(path);
        if (file.exists()) {
            return new FileInputStream(file);
        }

        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(path);
        if (stream == null) {
            throw new FileNotFoundException("File " + file + " not found");
        }
        return stream;
    }
}
