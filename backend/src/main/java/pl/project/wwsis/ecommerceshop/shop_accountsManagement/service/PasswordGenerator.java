package pl.project.wwsis.ecommerceshop.shop_accountsManagement.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator {
    public String generatePassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }
}
