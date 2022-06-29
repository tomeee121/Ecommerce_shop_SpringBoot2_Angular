package pl.project.wwsis.ecommerceshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.project.wwsis.ecommerceshop.constant.FileConstant;

import java.io.File;

import static pl.project.wwsis.ecommerceshop.constant.FileConstant.*;

@SpringBootApplication
public class EcommerceShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceShopApplication.class, args);
        new File(USER_FOLDER).mkdirs();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
