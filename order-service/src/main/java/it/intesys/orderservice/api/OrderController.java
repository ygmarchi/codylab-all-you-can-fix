package it.intesys.orderservice.api;

import io.swagger.v3.oas.annotations.Operation;
import it.intesys.orderservice.dto.OrderDTO;
import it.intesys.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

import static it.intesys.orderservice.dto.OrderDTOFactory.*;

/**
 * REST controller for handling order-related HTTP requests.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /**
     * Places a new order.
     *
     * @return the ID of the saved order
     */
    @PostMapping("/api/orders")
    @Operation(summary = "Places a new order")
    public Long save() {

        return orderService.save(createOrderDTO());
    }

    /**
     * Places a thousand new orders.
     * This is a bulk operation that can be used to test the performance of the system.
     */
    @GetMapping("/api/orders/bulk")
    @Operation(summary = "Places a thousand new orders")
    public void bulkSave() {
        createOrderDTOList().stream().parallel().forEach(orderService::save);
    }
}
