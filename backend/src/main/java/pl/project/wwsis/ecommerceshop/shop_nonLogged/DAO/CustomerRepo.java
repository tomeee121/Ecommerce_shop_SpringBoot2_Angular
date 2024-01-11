package pl.project.wwsis.ecommerceshop.shop_nonLogged.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {
    Optional<Customer> findCustomerByEmail(String email);
    Optional<Customer> findCustomerByUsername(String username);
    @Query(value = "SELECT * from customer c ORDER BY c.id", nativeQuery = true)
    List<Customer> getCustomersCustomized();

    @Query(value = "SELECT c from Customer c inner join Order o on c.id = o.customer.id where o.orderTrackingNumber = :order_tracking_number")
    Customer findCustomerByOrderTrackingNumber(@Param("order_tracking_number") String order_tracking_number);

    @Modifying
    @Query(value = "delete from Order o where o.orderTrackingNumber=:order_tracking_number")
    void deleteOrdersCascade(@Param("order_tracking_number") String order_tracking_number);

    @Modifying
    @Query(value = "update Customer set imageUrl=:image_url")
    void updateProfileImageKey(@Param("image_url") String image_url);

    @Query(value = "SELECT c.imageUrl from Customer c where c.username=:username")
    String getProfileImageKeyByUsername(@Param("username") String username);
}
