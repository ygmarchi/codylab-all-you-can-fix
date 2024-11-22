package it.intesys.shippingservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.intesys.shippingservice.client.model.TrackingDTO;
import it.intesys.shippingservice.dto.ShippingDTO;
import it.intesys.shippingservice.entity.Shipping;
import it.intesys.shippingservice.mapper.ShippingMapper;
import it.intesys.shippingservice.repository.ShippingRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ShippingService {

    private final ObjectMapper objectMapper;
    private final ShippingMapper shippingMapper;
    private final KafkaTemplate kafkaTemplate;
    private final ShippingRepository shippingRepository;

    public Long save(ShippingDTO shippingDTO) {

        log.info("Shipping request received for order with id {}", shippingDTO.orderId());
        Shipping shipping = shippingMapper.toEntity(shippingDTO);
        shipping = shippingRepository.save(shipping);
        log.info("Shipping {} saved", shipping.getId());
        kafkaTemplate.send("order.shipping", objectMapper.valueToTree(shipping));
        return shipping.getId();
    }

    public void updateTrackingInfo(long id, TrackingDTO trackingDTO) {

        log.debug("Request to update tracking info for shipping with id {}", id);
        Shipping shipping = shippingRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Shipping not found with id: " + id));
        shipping.setTrackingNumber(trackingDTO.getTrackingNumber());
        shippingRepository.save(shipping);
        kafkaTemplate.send("order.shipped", shipping.getOrderId());
    }

}
