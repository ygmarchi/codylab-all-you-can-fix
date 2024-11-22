package it.intesys.shippingservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.intesys.shippingservice.client.api.DefaultApi;
import it.intesys.shippingservice.client.model.TrackingDTO;
import it.intesys.shippingservice.entity.Shipping;
import it.intesys.shippingservice.mapper.ShippingMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ShippingConsumer {

    private final ObjectMapper objectMapper;
    private final ShippingMapper shippingMapper;
    private final DefaultApi shippingProviderApi;
    private final ShippingService shippingService;

    @KafkaListener(topics = "order.shipping")
    public void consumeOrderShippingEvent(JsonNode jsonNode)
                    throws JsonProcessingException {

        Shipping shipping = objectMapper.treeToValue(jsonNode, Shipping.class);
        TrackingDTO trackingDTO = shippingProviderApi.delegateShipping(shippingMapper.toShippingProviderClientDTO(shipping));
        log.info("Shipping done for order {} tracking number is {}", jsonNode.get("orderId")
                        .asLong(), trackingDTO.getTrackingNumber());
        shippingService.updateTrackingInfo(shipping.getOrderId(), trackingDTO);
    }

}
