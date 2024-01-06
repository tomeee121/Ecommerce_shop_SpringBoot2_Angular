package pl.project.wwsis.ecommerceshop.shop_accountsManagement.enums;

import static pl.project.wwsis.ecommerceshop.shop_accountsManagement.constant.Authority.*;

public enum Role {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_HR(HR_AUTHORITIES),
    ROLE_MANAGER(MANAGER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES),
    ROLE_SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES);

    Role(String userAuthorities) {
        this.authorities = userAuthorities;
    }
    private String authorities;

    public String getAuthorities() {
        return authorities;
    }
}
