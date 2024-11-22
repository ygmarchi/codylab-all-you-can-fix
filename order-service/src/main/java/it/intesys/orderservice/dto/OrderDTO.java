package it.intesys.orderservice.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record OrderDTO(Long id, OffsetDateTime orderDate, List<String> items, String status, String name, String address){
}
