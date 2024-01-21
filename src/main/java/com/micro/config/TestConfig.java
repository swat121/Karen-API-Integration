package com.micro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.net.URI;

@Profile("test")
@Configuration
public class TestConfig {

    @Value("${mock.host}")
    private String mockHost;

    @Value("${mock.port}")
    private String mockPort;

    @Bean
    public LoadBalancerClient loadBalancerClient() {
        return new MockLoadBalancerClient();
    }

    public class MockLoadBalancerClient implements LoadBalancerClient {
        @Override
        public ServiceInstance choose(String serviceId) {
            return new DefaultServiceInstance("mockInstanceId", serviceId, mockHost, Integer.parseInt(mockPort), false);
        }

        @Override
        public <T> ServiceInstance choose(String serviceId, Request<T> request) {
            return new DefaultServiceInstance("mockInstanceId", serviceId, mockHost, Integer.parseInt(mockPort), false);
        }

        @Override
        public <T> T execute(String serviceId, LoadBalancerRequest<T> request) {
            try {
                return request.apply(choose(serviceId));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public <T> T execute(String serviceId, ServiceInstance serviceInstance, LoadBalancerRequest<T> request) throws IOException {
            try {
                return request.apply(choose(serviceId));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public URI reconstructURI(ServiceInstance instance, URI original) {
            return original;
        }
    }
}
