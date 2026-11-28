package tn.esprit.gateway.apigateway.config;

import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

public class WeightedLoadBalancerConfig {

    @Bean
    public ServiceInstanceListSupplier weightedSupplier(ConfigurableApplicationContext context) {
        return ServiceInstanceListSupplier.builder()
                .withDiscoveryClient()   // instances fournies par Eureka
                .withWeighted()          // pondérées selon la métadonnée "weight"
                .build(context);
    }
}