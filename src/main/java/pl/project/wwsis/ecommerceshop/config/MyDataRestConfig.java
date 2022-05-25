package pl.project.wwsis.ecommerceshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import pl.project.wwsis.ecommerceshop.model.Product;
import pl.project.wwsis.ecommerceshop.model.ProductCategory;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {
    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod[] theUnsupportedActions = {HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH};

        //disable HTTP methods that modify (POST, PUT, PATCH) for Product
        config.getExposureConfiguration().forDomainType(Product.class).withItemExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
        .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions));

        //disable HTTP methods that modify (POST, PUT, PATCH) for ProductCategory
        config.getExposureConfiguration().forDomainType(ProductCategory.class).withItemExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        //expose entities id
        //get entities
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        //create list of entity types
        List<Class> classesEntities = new ArrayList<>();
        for (EntityType<?> entity : entities) {
            classesEntities.add(entity.getJavaType());
        }
        //create array of entity ids
        Class[] classes = classesEntities.toArray(new Class[0]);
        config.exposeIdsFor(classes);
    }
}
