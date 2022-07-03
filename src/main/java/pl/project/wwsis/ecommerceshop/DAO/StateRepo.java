package pl.project.wwsis.ecommerceshop.DAO;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import pl.project.wwsis.ecommerceshop.model.Order;
import pl.project.wwsis.ecommerceshop.model.State;

import java.util.List;

@Repository
@CrossOrigin("http://localhost:4200")
@RepositoryRestResource
public interface StateRepo extends JpaRepository<State, Integer> {
    List<State> findStatesByCountryCode(@RequestParam(name = "code") String code);
    List<State> findAll();

}
