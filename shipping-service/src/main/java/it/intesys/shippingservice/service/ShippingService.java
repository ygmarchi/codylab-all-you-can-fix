package it.intesys.shippingservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.intesys.shippingservice.client.model.TrackingDTO;
import it.intesys.shippingservice.dto.ShippingDTO;
import it.intesys.shippingservice.entity.Shipping;
import it.intesys.shippingservice.exception.ShippingNotFound;
import it.intesys.shippingservice.mapper.ShippingMapper;
import it.intesys.shippingservice.repository.ShippingRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/*
 * Faccio l'aggiornamento bulk del db in un'unica transazione per performance.
 * Quindi gli eventi devono essere mandati a transazione conclusa per evitare rientri anticipati.
 */
@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ShippingService {

    private final ObjectMapper objectMapper;
    private final ShippingMapper shippingMapper;
    private final KafkaTemplate kafkaTemplate;
    private final ShippingRepository shippingRepository;
    private final ApplicationContext applicationContext;

    public Long save(ShippingDTO shippingDTO) {
        log.info("Shipping request received for order with id {}", shippingDTO.orderId());
        Shipping shipping1 = shippingMapper.toEntity(shippingDTO);
        shipping1 = shippingRepository.save(shipping1);
        log.info("Shipping {} saved", shipping1.getId());
        Shipping shipping = shipping1;
        kafkaTemplate.send("order.shipping", objectMapper.valueToTree(shipping));
        return shipping.getId();
    }

    public List<Long> saveAll(List<ShippingDTO> shippingDTOs) {
        return shippingDTOs.stream()
            .map(this::save)
            .toList();
    }

    public void updateTrackingInfo(Long id, TrackingDTO trackingDTO) {
        log.info("Request to update tracking info for shipping with id {}", id);
        Shipping shipping1 = shippingRepository.findById(id)
                        .orElseThrow(() -> new ShippingNotFound(id));
        shipping1.setTrackingNumber(trackingDTO.getTrackingNumber());
        shippingRepository.save(shipping1);
        log.info("Shipping {} updated tracking info", shipping1.getId());
        Shipping shipping = shipping1;
        kafkaTemplate.send("order.shipped", shipping.getOrderId());
    }

    public void updateTrackingInfos(Map<Long, TrackingDTO> trackingDTOs) {
        trackingDTOs.forEach(this::updateTrackingInfo);
    }


}
