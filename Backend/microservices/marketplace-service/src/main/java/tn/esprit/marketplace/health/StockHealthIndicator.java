package tn.esprit.marketplace.health;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.health.contributor.AbstractHealthIndicator;
import org.springframework.boot.health.contributor.Health;
import org.springframework.stereotype.Component;
import tn.esprit.marketplace.repository.ProductRepository;

@Component
@RequiredArgsConstructor
public class StockHealthIndicator extends AbstractHealthIndicator {

    private final ProductRepository productRepository;

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        long total = productRepository.count();
        long lowStock = productRepository.countLowStock();
        builder.up()
                .withDetail("totalProducts", total)
                .withDetail("lowStockProducts", lowStock);
    }
}