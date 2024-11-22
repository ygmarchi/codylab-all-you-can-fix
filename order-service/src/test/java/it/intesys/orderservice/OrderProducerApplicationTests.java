package it.intesys.orderservice;

import it.intesys.orderservice.dto.OrderDTO;
import it.intesys.orderservice.dto.OrderDTOFactory;
import it.intesys.orderservice.entity.Order;
import it.intesys.orderservice.entity.OrderStatus;
import it.intesys.orderservice.mapper.OrderMapper;
import it.intesys.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@Transactional
//@Rollback
class OrderProducerApplicationTests {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;


//    @Disabled
    @Test
    public void testOrderStatusCreated() {
        // Create a new order
        OrderDTO orderDTO = OrderDTOFactory.createOrderDTO();
        Order order = orderMapper.toEntity(orderDTO);
        orderRepository.save(order);

        // Retrieve the order from the database
        Order retrievedOrder = orderRepository.findById(order.getId()).orElse(null);

        // Verify the status is CREATED
        assertThat(retrievedOrder).isNotNull();
        assertThat(retrievedOrder.getStatus()).isEqualTo(OrderStatus.CREATED.getValue());
        orderRepository.delete(order);
    }

//    @Disabled
    @Test
    public void testOrderStatusShipping() {
        // Create a new order
        OrderDTO orderDTO = OrderDTOFactory.createOrderDTO();
        Order order = orderMapper.toEntity(orderDTO);
        order.setStatus(OrderStatus.SHIPPING.getValue());
        orderRepository.save(order);

        // Retrieve the order from the database
        Order retrievedOrder = orderRepository.findById(order.getId()).orElse(null);

        // Verify the status is CREATED
        assertThat(retrievedOrder).isNotNull();
        assertThat(retrievedOrder.getStatus()).isEqualTo(OrderStatus.SHIPPING.name());
        orderRepository.delete(order);
    }

//    @Disabled
    @Test
    public void testOrderStatusShipped() {
        // Create a new order
        OrderDTO orderDTO = OrderDTOFactory.createOrderDTO();
        Order order = orderMapper.toEntity(orderDTO);
        order.setStatus(OrderStatus.SHIPPED.getValue());
        orderRepository.save(order);

        // Retrieve the order from the database
        Order retrievedOrder = orderRepository.findById(order.getId()).orElse(null);

        // Verify the status is CREATED
        assertThat(retrievedOrder).isNotNull();
        assertThat(retrievedOrder.getStatus()).isEqualTo(OrderStatus.SHIPPED.name());
        orderRepository.delete(order);
    }
}
