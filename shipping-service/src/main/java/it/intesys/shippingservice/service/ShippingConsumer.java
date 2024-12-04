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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class ShippingConsumer {

    private final ObjectMapper objectMapper;
    private final ShippingMapper shippingMapper;
    private final DefaultApi shippingProviderApi;
    private final ShippingService shippingService;

    @KafkaListener(topics = "order.shipping", batch = "true")
    public void consumeOrderShippingEvent(List<JsonNode> jsonNodes)
                    throws JsonProcessingException {
        log.info("Received {} shipping events", jsonNodes.size());
        List<Shipping> shippings = jsonNodes.stream().map(this::map).filter(Objects::nonNull).toList();
        log.info("Mapped to {} shippings ({})", shippings.size(), shippings.stream().map(Shipping::getId).toList());
        Map<Long, TrackingDTO> trackingInfos = shippings.stream()
                .parallel()
                .map(this::ship)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (t1, t2) -> t2));
        shippingService.updateTrackingInfos(trackingInfos);
    }


    private Map.Entry<Long, TrackingDTO> ship(Shipping shipping) {
        TrackingDTO trackingDTO = shippingProviderApi.delegateShipping(shippingMapper.toShippingProviderClientDTO(shipping));
        log.info("Shipping done for order {} tracking number is {}", shipping.getId(), trackingDTO.getTrackingNumber());
        return Map.entry(shipping.getId(), trackingDTO);
    }

    private Shipping map(JsonNode jsonNode){
        try {
            return objectMapper.treeToValue(jsonNode, Shipping.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
