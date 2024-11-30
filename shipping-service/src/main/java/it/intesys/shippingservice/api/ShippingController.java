package it.intesys.shippingservice.api;

import io.swagger.v3.oas.annotations.Operation;
import it.intesys.shippingservice.api.model.OrderDTO;
import it.intesys.shippingservice.dto.ShippingDTO;
import it.intesys.shippingservice.service.ShippingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ShippingController implements V1Api {

    private final ShippingService shippingService;

    @Override @Operation(summary = "Executes a new shipping")
    public ResponseEntity<Long> v1ApiShippingPost(
            @Valid @RequestBody(required = false) OrderDTO orderDTO
    ) {

        log.info("Shipping request received for order %s".formatted(orderDTO.getId()));
        ShippingDTO shippingDTO = new ShippingDTO(null, orderDTO.getName(), orderDTO.getAddress(), orderDTO.getId());
        Long shippingId = shippingService.save(shippingDTO);
        return ResponseEntity.ok(shippingId);
    }

}
