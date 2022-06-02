package pl.project.wwsis.ecommerceshop.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.project.wwsis.ecommerceshop.model.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {
}
