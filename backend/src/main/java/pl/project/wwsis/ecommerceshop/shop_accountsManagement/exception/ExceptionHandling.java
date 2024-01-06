package pl.project.wwsis.ecommerceshop.shop_accountsManagement.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.DTO.HttpResponse;

import javax.persistence.NoResultException;

@RestControllerAdvice
public class ExceptionHandling {
    Logger logger = LoggerFactory.getLogger(getClass());
    private static final String ACCOUNT_LOCKED = "Your account has been locked, contact the administration";
    private static final String METHOD_NOT_ALLOWED = "The request is not allowed, please send a '%s' request";
    private static final String INTERNAL_SERVER_ERROR_MSG = "An error occured while processing the request";
    private static final String INCORRECT_CREDENTIALS = "Username / password incorrect. Please try again";
    private static final String ACCOUNT_DISABLED = "Your account has been disabled";
    private static final String ERROR_PROCESSING_FILE = "There was an error while processing the file";
    private static final String NOT_ENOUGH_PERMISSION = "You do not have enough permission";

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisabledException(){
        ResponseEntity<HttpResponse> httpResponse = createHttpResponse(HttpStatus.BAD_REQUEST, ACCOUNT_DISABLED.toUpperCase());
        return httpResponse;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException(BadCredentialsException exception){
        ResponseEntity<HttpResponse> httpResponse = createHttpResponse(HttpStatus.UNAUTHORIZED, exception.getMessage().toUpperCase());
        return httpResponse;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException(AccessDeniedException exception){
        ResponseEntity<HttpResponse> httpResponse = createHttpResponse(HttpStatus.UNAUTHORIZED, exception.getMessage().toUpperCase());
        return httpResponse;
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> lockedException(LockedException exception){
        ResponseEntity<HttpResponse> httpResponse = createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage().toUpperCase());
        return httpResponse;
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException exception){
        ResponseEntity<HttpResponse> httpResponse = createHttpResponse(HttpStatus.UNAUTHORIZED, exception.getMessage().toUpperCase());
        return httpResponse;
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<HttpResponse> emailExistException(EmailExistException exception){
        ResponseEntity<HttpResponse> httpResponse = createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage().toUpperCase());
        return httpResponse;
    }

    @ExceptionHandler(UsernameExistException.class)
    public ResponseEntity<HttpResponse> usernameExistException(UsernameExistException exception){
        ResponseEntity<HttpResponse> httpResponse = createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage().toUpperCase());
        return httpResponse;
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException exception){
        ResponseEntity<HttpResponse> httpResponse = createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage().toUpperCase());
        return httpResponse;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException exception){
        ResponseEntity<HttpResponse> httpResponse = createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage().toUpperCase());
        return httpResponse;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException(NoHandlerFoundException exception){
//        HttpMethod httpMethod = Objects.requireNonNull(exception.getSupportedHttpMethods().iterator().next());
//        ResponseEntity<HttpResponse> httpResponse = createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, String.format(METHOD_NOT_ALLOWED, httpMethod).toUpperCase());
        ResponseEntity<HttpResponse> httpResponse = createHttpResponse(HttpStatus.NOT_FOUND, "The page was not found");
        return httpResponse;
    }
    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse> notFoundException(NoResultException exception){
        ResponseEntity<HttpResponse> httpResponse = createHttpResponse(HttpStatus.NOT_FOUND, exception.getMessage().toUpperCase());
        return httpResponse;
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception){
//        ResponseEntity<HttpResponse> httpResponse = createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG.toUpperCase());
//        return httpResponse;
//    }
    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message){
        HttpResponse httpResponse = new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message.toUpperCase());
        return new ResponseEntity<>(httpResponse, httpStatus);
    }
}
