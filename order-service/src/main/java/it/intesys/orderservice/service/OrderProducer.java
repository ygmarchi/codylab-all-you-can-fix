package it.intesys.orderservice.service;

import it.intesys.orderservice.client.api.DefaultApi;
import it.intesys.orderservice.dto.OrderDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OrderProducer {

    private final KafkaTemplate<String, OrderDTO> kafkaTemplate;

    public void publishOrderCreatedEvent(OrderDTO orderDTO) {

        kafkaTemplate.send("order.created", orderDTO);
        log.info("Order created event published for order {}", orderDTO.id());
    }
}
