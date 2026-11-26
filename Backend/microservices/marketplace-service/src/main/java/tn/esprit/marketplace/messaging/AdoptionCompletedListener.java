package tn.esprit.marketplace.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tn.esprit.marketplace.config.RabbitConfig;

@Component
public class AdoptionCompletedListener {
    private static final Logger log = LoggerFactory.getLogger(AdoptionCompletedListener.class);

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void onAdoptionCompleted(AdoptionCompletedEvent event) {
        log.info("AdoptionCompleted reçu : {}", event);
        // TODO : créer / proposer le kit de bienvenue pour event.idCandidat()
        // Idempotence (doc §5.5) : ignorer si candidatureId déjà traité
    }
}