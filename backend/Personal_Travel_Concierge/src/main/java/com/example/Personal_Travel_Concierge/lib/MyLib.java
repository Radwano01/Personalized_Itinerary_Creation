package com.example.Personal_Travel_Concierge.lib;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.concurrent.*;


@Component
public class MyLib {

    public static boolean checkIfSentEmptyData(Object dto) {
        try {
            for (Field field : dto.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(dto);
                if (value != null) {
                    return true;
                }
            }
            return false;
        }catch (IllegalAccessException e){
            return false;
        }
    }

    //check if the password is strong using multi-threading.
    public static boolean isStrongPassword(String password){
        if (password == null || password.length() < 8) {
            return true;
        }

        int numThreads = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        Callable<Boolean> isUppercase = () -> password.chars().anyMatch(Character::isUpperCase);
        Callable<Boolean> isLowercase = () -> password.chars().anyMatch(Character::isLowerCase);
        Callable<Boolean> isDigit = () -> password.chars().anyMatch(Character::isDigit);
        Callable<Boolean> isSpecial = () ->password.chars().anyMatch(c -> !Character.isLetterOrDigit(c));

        try{
            Future<Boolean> futureUppercase = executorService.submit(isUppercase);
            Future<Boolean> futureLowercase = executorService.submit(isLowercase);
            Future<Boolean> futureDigit = executorService.submit(isDigit);
            Future<Boolean> futureSpecial = executorService.submit(isSpecial);

            executorService.shutdown();

            return !futureUppercase.get() || !futureLowercase.get() || !futureDigit.get() || !futureSpecial.get();
        }catch (InterruptedException | ExecutionException e){
            executorService.shutdown();
            return true;
        }
    }
}