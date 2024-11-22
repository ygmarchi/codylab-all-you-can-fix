package it.intesys.orderservice.mapper;

import it.intesys.orderservice.entity.Order;
import it.intesys.orderservice.dto.OrderDTO;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toEntity(OrderDTO orderDTO);

    OrderDTO toDTO(Order order);
    it.intesys.orderservice.client.model.OrderDTO toShippingClientDTO(OrderDTO order);
}
