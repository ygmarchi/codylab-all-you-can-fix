package it.intesys.orderservice.exception;

public class DuplicateOrderId extends OrderServiceException {
    public DuplicateOrderId(Long id) {
        super (id);
    }
}
