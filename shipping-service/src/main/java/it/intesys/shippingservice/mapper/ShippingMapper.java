package it.intesys.shippingservice.mapper;


import com.fasterxml.jackson.databind.JsonNode;
import it.intesys.shippingservice.dto.ShippingDTO;
import it.intesys.shippingservice.entity.Shipping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShippingMapper {

    Shipping toEntity(ShippingDTO shippingDTO);

    it.intesys.shippingservice.client.model.ShippingDTO toShippingProviderClientDTO(Shipping shipping);
}
