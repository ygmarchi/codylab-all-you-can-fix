package it.intesys.shippingservice.api;

import io.swagger.v3.oas.annotations.Operation;
import it.intesys.shippingservice.dto.ShippingDTO;
import it.intesys.shippingservice.service.ShippingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ShippingController implements V1Api {

    private final ShippingService shippingService;

    @PostMapping("/api/shipping")
    @Operation(summary = "Executes a new shipping")
    public Long ship(@RequestBody @Valid ShippingDTO shippingDTO) {

        log.info("Shipping request received for order %s".formatted(shippingDTO.id()));
        return shippingService.save(shippingDTO);
    }

}
