package it.intesys.orderservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import it.intesys.orderservice.client.api.DefaultApi;
import it.intesys.orderservice.dto.OrderDTO;
import it.intesys.orderservice.mapper.OrderMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OrderConsumer {

    private final OrderProducer orderProducer;
    private final OrderService orderService;
    private final DefaultApi shippingApi;
    private final OrderMapper orderMapper;

    @KafkaListener(topics = "order.created")
    public void consumeOrderCreatedEvent(OrderDTO orderDTO) {

        log.info("Order created event received for order {}", orderDTO.id());
        shippingApi.v1ApiShippingPost(orderMapper.toShippingClientDTO(orderDTO));
    }

    @KafkaListener(topics = "order.shipping")
    public void consumeOrderShippingEvent(JsonNode jsonNode) {

        long orderId = jsonNode.get("orderId")
                        .asLong();
        log.info("Shipping request received for order {}", orderId);
        orderService.updateStatus(orderId, "SHIPPING");
    }

    @KafkaListener(topics = "order.shipped")
    public void consumeOrderShippedEvent(Long orderId) {

        orderService.updateStatus(orderId, "SHIPPING");
    }
}
