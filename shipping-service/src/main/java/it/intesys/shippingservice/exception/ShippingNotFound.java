package it.intesys.shippingservice.exception;

public class ShippingNotFound extends ShippingServiceException {
    public ShippingNotFound(long id) {
        super (id);
    }
}
