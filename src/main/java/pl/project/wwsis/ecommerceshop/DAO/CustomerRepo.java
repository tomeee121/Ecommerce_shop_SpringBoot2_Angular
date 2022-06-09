package pl.project.wwsis.ecommerceshop.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import pl.project.wwsis.ecommerceshop.model.Customer;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {
    Optional<Customer> findCustomerByEmail(String email);
}
