package pl.project.wwsis.ecommerceshop.shop_accountsManagement.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pl.project.wwsis.ecommerceshop.shop_accountsManagement.constant.SecurityConstant;
import pl.project.wwsis.ecommerceshop.shop_accountsManagement.model.CustomerPrincipal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@PropertySource(value = {"classpath:application.properties"})
public class JWTTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    public String generateJwtToken(CustomerPrincipal customerPrincipal){
        String[] claims = getClaimsFromCustomer(customerPrincipal);
        String token = JWT.create()
                .withIssuer(SecurityConstant.ADMINISTRATION).withIssuedAt(new Date())
                .withSubject(customerPrincipal.getUsername())
                .withArrayClaim(SecurityConstant.AUTHORITIES, claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));
        return token;
    }

    public List<GrantedAuthority> getAuthorities(String token){
        String[] claims = getClaimsFromToken(token);
        return Arrays.stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities){
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
        return authentication;
    }

    public String getSubject(String token){
        JWTVerifier jwtVerifier = getJwtVerifier();
        return jwtVerifier.verify(token).getSubject();
    }

    public boolean isTokenValid(String token){
        JWTVerifier jwtVerifier = getJwtVerifier();
        return StringUtils.isNotEmpty(token) && isNotExpired(token);

    }

    private boolean isNotExpired(String token) {
        JWTVerifier jwtVerifier = getJwtVerifier();
        Date expiresAt = jwtVerifier.verify(token).getExpiresAt();
        return new Date().before(expiresAt);
    }

    private String[] getClaimsFromToken(String token) {
        JWTVerifier jwtVerifier = getJwtVerifier();
        String[] claimsFromToken;
        try{
            claimsFromToken = jwtVerifier.verify(token).getClaim(SecurityConstant.AUTHORITIES).asArray(String.class);}
            catch(JWTVerificationException jwtVerificationException){
            throw new JWTVerificationException(SecurityConstant.TOKEN_COULD_NOT_BE_VERIFIED);
        }
        return claimsFromToken;
    }

    private JWTVerifier getJwtVerifier(){
        JWTVerifier jwtVerifier;
        try{
            jwtVerifier = JWT.require(Algorithm.HMAC512(secret)).withIssuer(SecurityConstant.ADMINISTRATION).build();}
        catch(JWTVerificationException jwtVerificationException){
            throw new JWTVerificationException(SecurityConstant.TOKEN_COULD_NOT_BE_VERIFIED);
        }
        return jwtVerifier;
    }

    private String[] getClaimsFromCustomer(CustomerPrincipal customerPrincipal) {
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority authority : customerPrincipal.getAuthorities()) {
            authorities.add(authority.toString());
        }
        return authorities.toArray(new String[0]);
    }


}
