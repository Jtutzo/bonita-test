package com.example.bonitatest;

import com.example.bonitatest.user.UserRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;
import java.util.function.Supplier;

@ApplicationPath("api")
public class RestApplication extends ResourceConfig {

    public RestApplication() {
        packages("com.example.bonitatest");
        register(JacksonFeature.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(EntityManagerFactoryProvider.class).to(EntityManagerFactory.class).in(Singleton.class);
                bindAsContract(UserRepository.class).in(Singleton.class);
            }
        });
        SLF4JBridgeHandler.install();
    }

    public static class EntityManagerFactoryProvider implements Supplier<EntityManagerFactory> {

        private static final EntityManagerFactory INSTANCE = Persistence.createEntityManagerFactory("default");

        @Override
        public EntityManagerFactory get() {
            return INSTANCE;
        }

    }

}