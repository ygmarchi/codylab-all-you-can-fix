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

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ShippingController implements V1Api {

    private final ShippingService shippingService;

    @Override @Operation(summary = "Executes a new shipping")
    public ResponseEntity<Long> v1ApiShippingPost(@Valid @RequestBody OrderDTO orderDTO) {
        Long shippingId = ship(orderDTO);
        return ResponseEntity.ok(shippingId);
    }

    private Long ship(OrderDTO orderDTO) {
        log.info("Shipping request received for order {}", orderDTO.getId());
        ShippingDTO shippingDTO = map(orderDTO);
        return shippingService.save(shippingDTO);
    }

    private static ShippingDTO map(OrderDTO orderDTO) {
        return new ShippingDTO(null, orderDTO.getName(), orderDTO.getAddress(), orderDTO.getId());
    }

    private List<Long> shipAll(List<OrderDTO> orderDTOs) {
        log.info("Shipping bulk request received of size {} ({})", orderDTOs.size(), orderDTOs.stream().map(OrderDTO::getId).toList());
        List<ShippingDTO> shippingDTOs = orderDTOs.stream().map(ShippingController::map).toList();
        return shippingService.saveAll(shippingDTOs);
    }

    @Override @Operation(summary = "Executes a new bulk shipping")
    public ResponseEntity<List<Long>> v1ApiShippingBulkPost(@Valid @RequestBody(required = false) List<@Valid OrderDTO> orderDTOs) {
        log.info("Shipping bulk request received with {} orders", orderDTOs.size());
        List<Long> shippingIds = orderDTOs.stream().map(this::ship).toList();
        return ResponseEntity.ok(shippingIds);
    }
}
