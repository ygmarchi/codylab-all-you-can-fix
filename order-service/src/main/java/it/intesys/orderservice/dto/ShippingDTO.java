package it.intesys.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ShippingDTO(@NotNull Long id, @NotBlank String name, @NotBlank String address) {

}
