package com.example.sezar.encryption_algorithms;

import java.util.Random;

public class RandomStringGeneratorVerification {
    public  String randomStringGenerator(){
        int leftLimit = 65; // letter 'a' from ascii table
        int rightLimit = 90; // letter 'z' from ascii table
        int targetStringLength = 3;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }
}
