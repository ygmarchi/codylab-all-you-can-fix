// OrderDTOFactory.java
package it.intesys.orderservice.dto;

import com.github.javafaker.Faker;
import it.intesys.orderservice.entity.OrderStatus;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class OrderDTOFactory {

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    public static OrderDTO createOrderDTO() {
        return new OrderDTO(
                null,
                OffsetDateTime.now(),
                List.of(faker.commerce().productName(), faker.commerce().productName()),
                OrderStatus.CREATED.name(),
                faker.name().fullName(),
                faker.address().fullAddress()
        );
    }

    private static OrderDTO createOrderDTO(Long id) {
        return new OrderDTO(
                id,
                OffsetDateTime.now(),
                List.of(faker.commerce().productName(), faker.commerce().productName()),
                OrderStatus.CREATED.name(),
                faker.name().fullName(),
                faker.address().fullAddress()
        );
    }

    public static List<OrderDTO> createOrderDTOList() {
        Long duplicatedId = 1L;
        int numberOfDuplicates = random.nextInt(100) + 1; // Random number between 1 and 100
        List<OrderDTO> orderDTOList = new ArrayList<>();

        IntStream.range(0, 1000).forEach(i -> {
            if (i < numberOfDuplicates) {
                orderDTOList.add(createOrderDTO(duplicatedId));
            } else {
            orderDTOList.add(createOrderDTO((long) i));
            }
        });

        return orderDTOList;
    }
}