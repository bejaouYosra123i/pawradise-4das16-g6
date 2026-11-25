package tn.esprit.adoption.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import tn.esprit.adoption.config.RabbitMQConfig;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdoptionEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishAdoptionCompleted(AdoptionCompletedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_ADOPTION_COMPLETED,
                event
        );
        log.info("Événement AdoptionCompleted publié : candidature {} / animal {}",
                event.candidatureId(), event.idAnimal());
    }
}