/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sezar.encryption_algorithms;
import java.util.Random;

/**
 *
 * @author casper
 */
public class RandomStringGenerator {
    public  String randomStringGenerator(){
        int leftLimit = 65; // letter 'a' from ascii table
        int rightLimit = 90; // letter 'z' from ascii table
        int targetStringLength = 10;
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
