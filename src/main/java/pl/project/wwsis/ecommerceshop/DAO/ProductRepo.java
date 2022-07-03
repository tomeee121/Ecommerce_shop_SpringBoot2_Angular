package pl.project.wwsis.ecommerceshop.DAO;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import pl.project.wwsis.ecommerceshop.model.Order;
import pl.project.wwsis.ecommerceshop.model.Product;

import java.util.List;

@CrossOrigin("http://localhost:4200")
public interface ProductRepo extends JpaRepository<Product, Long> {

    List<Product> findProductsByName(String productName);

    Page<Product> findByProductCategoryId(@RequestParam("id") Long id, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(@RequestParam("name") String name, Pageable pageable);
}
