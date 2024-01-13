package pl.project.wwsis.ecommerceshop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.project.wwsis.ecommerceshop.shop_accountsManagement.config.S3Buckets;
import pl.project.wwsis.ecommerceshop.shop_accountsManagement.service.S3Service;

import java.io.File;

import static pl.project.wwsis.ecommerceshop.shop_accountsManagement.constant.FileConstant.*;

@SpringBootApplication
public class EcommerceShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceShopApplication.class, args);
        new File(USER_FOLDER).mkdirs();
    }

    @Bean
    CommandLineRunner runner(S3Service service, S3Buckets s3Buckets) {
        return args -> {
            service.putObject(s3Buckets.getEcommerce(), "test3", "hello world 3".getBytes());
            byte[] object = service.getObject(s3Buckets.getEcommerce(), "test3");
            System.out.println("Test upload' result: " + new String(object));
        };
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //pushed with the command: mvn clean compile jib:build
}
