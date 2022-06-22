package pl.project.wwsis.ecommerceshop.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.cglib.transform.AbstractTransformTask;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {
    private static final int MAXIMUM_NUMBER_OF_LOGIN_ATTEMPTS = 6;
    private static final int INCREMENT_NUMBER = 1;

    private LoadingCache<String, Integer> loginAttemptCache;

    public LoginAttemptService() {
        loginAttemptCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).maximumSize(100)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String s) throws Exception {
                        return 0;
                    }
                });
    }
    public void evictUserFromLoginAttemptCache(String username){
        loginAttemptCache.invalidate(username);
    }

    public void addUserToLoginAttemptCache(String username){
        int attempts = 0;
        try{
            attempts = INCREMENT_NUMBER + loginAttemptCache.get(username);
            loginAttemptCache.put(username, attempts);
        }
        catch (ExecutionException exception){
            exception.printStackTrace();
        }
    }

    public boolean ifHasExceededMaxAmountOfLoginAttemps(String username) {
        boolean attemptsBoolean = false;
        try {
            attemptsBoolean = loginAttemptCache.get(username) > MAXIMUM_NUMBER_OF_LOGIN_ATTEMPTS;
        } catch (ExecutionException exception) {
            exception.printStackTrace();
        }
        return attemptsBoolean;
    }
}
