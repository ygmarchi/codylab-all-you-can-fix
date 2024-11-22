package it.intesys.shippingservice.repository;

import it.intesys.shippingservice.entity.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {
}
