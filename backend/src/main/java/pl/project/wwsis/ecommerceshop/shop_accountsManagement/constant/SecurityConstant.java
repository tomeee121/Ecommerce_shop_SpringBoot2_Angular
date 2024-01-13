package pl.project.wwsis.ecommerceshop.shop_accountsManagement.constant;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 432_000_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String AUTHORITIES = "Authorities";
    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_COULD_NOT_BE_VERIFIED = "Token could not be verified";
    public static final String TOKEN_PROVIDER = "Token provided by BTC-shop-app company";
    public static final String ADMINISTRATION = "Administration of BTC-shop-app";
    public static final String FORBIDDEN_MESSAGE = "Log in before you could access this page!";
    public static final String ACCESS_DENIED = "You do not have enough permission to access the page!";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = { "/console/**","/customer/login", "/customer/register", "/customer/resetPassword/**", "/api/**", "/checkout/**"};
//public static final String[] PUBLIC_URLS = {"**"};




}
