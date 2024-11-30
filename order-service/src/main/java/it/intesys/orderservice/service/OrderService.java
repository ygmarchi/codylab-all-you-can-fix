package it.intesys.orderservice.service;

import it.intesys.orderservice.mapper.OrderMapper;
import it.intesys.orderservice.repository.OrderRepository;
import it.intesys.orderservice.entity.Order;
import it.intesys.orderservice.dto.OrderDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;

    public void updateStatus(long id, String status) {

        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        order.setStatus(status);
        orderRepository.save(order);
    }

    public Long save(OrderDTO orderDTO) {

        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        log.info("Order {} placed", order.getId());
        orderProducer.publishOrderCreatedEvent(orderMapper.toDTO(order));
        return order.getId();
    }
}
