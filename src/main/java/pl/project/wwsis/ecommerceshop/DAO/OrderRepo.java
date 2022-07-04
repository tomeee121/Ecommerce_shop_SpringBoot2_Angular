package pl.project.wwsis.ecommerceshop.DAO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.project.wwsis.ecommerceshop.model.Order;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    @Query(value = "SELECT o from Order o inner join Customer c on o.customer.id = c.id " +
            "inner join Address a on o.shippingAddress.id = a.id where c.email = :email order by o.dateCreated DESC")
    List<Order> findOrdersByEmail(@Param("email") String email, Pageable pageable);


    @Query(value = "SELECT o from Order o inner join Customer c on o.customer.id = c.id " +
            "inner join Address a on o.shippingAddress.id = a.id order by o.dateCreated DESC")
    List<Order> findAllOrders();
}
