package it.intesys.orderservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import it.intesys.orderservice.client.api.DefaultApi;
import it.intesys.orderservice.dto.OrderDTO;
import it.intesys.orderservice.mapper.OrderMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class OrderConsumer {

    private final OrderProducer orderProducer;
    private final OrderService orderService;
    private final DefaultApi shippingApi;
    private final OrderMapper orderMapper;

    @KafkaListener(topics = "order.created", batch = "true")
    public void consumeOrderCreatedEvent(List<OrderDTO> orderDTOs) {
        log.info("Bulk order created event received of size {} ({})", orderDTOs.size(), orderDTOs.stream().map(OrderDTO::id).toList());
        shippingApi.v1ApiShippingBulkPost(orderDTOs.stream().map(orderMapper::toShippingClientDTO).toList());
    }

    @KafkaListener(topics = "order.shipping", batch = "true")
    public void consumeOrderShippingEvent(List<JsonNode> jsonNodes) {
        for (JsonNode jsonNode: jsonNodes) {
            long orderId = jsonNode.get("orderId")
                            .asLong();
            log.info("Shipping status update received for order {}", orderId);
            orderService.updateStatus(orderId, "SHIPPING");
        }

    }

    @KafkaListener(topics = "order.shipped", batch = "true")
    public void consumeOrderShippedEvent(List<Long> orderIds) {
        for (Long orderId: orderIds) {
            log.info("Shipped status update received for order {}", orderId);
            orderService.updateStatus(orderId, "SHIPPED");
        }
    }
}
