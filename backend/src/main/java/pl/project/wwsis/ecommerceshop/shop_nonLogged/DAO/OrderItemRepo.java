package pl.project.wwsis.ecommerceshop.shop_nonLogged.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.OrderItem;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
}
