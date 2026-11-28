package tn.esprit.gateway.apigateway.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@LoadBalancerClients({
        @LoadBalancerClient(name = "lost-and-found-service", configuration = RandomLoadBalancerConfig.class),
        @LoadBalancerClient(name = "animal-rescue-service",  configuration = WeightedLoadBalancerConfig.class)
})
public class LoadBalancerConfig {
}