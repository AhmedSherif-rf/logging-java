package com.webbats.payment.broker.utility;

import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.random.RandomGenerator;

@Service
public class PasswordGeneratorService {
    // Define the character sets to use in the password
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/~`";
    private static final String ALL_CHARS = LOWERCASE + UPPERCASE + DIGITS + SPECIAL_CHARS;

    // Define the password length
    private static final int PASSWORD_LENGTH = 12; // Minimum 8, so here taken 12 for better security
    
    private final RandomGenerator RANDOM;

    public PasswordGeneratorService() {
        RandomGenerator randomGenerator;
        try {
            randomGenerator = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            // Fallback to default SecureRandom if strong instance is not available
            randomGenerator = new SecureRandom();
        }
        this.RANDOM = randomGenerator;
    }

    public String generatePassword() {
        List<Character> passwordChars = new ArrayList<>(PASSWORD_LENGTH);
        
        // Ensure at least one character from each set is included
        passwordChars.add(getRandomChar(LOWERCASE));
        passwordChars.add(getRandomChar(UPPERCASE));
        passwordChars.add(getRandomChar(DIGITS));
        passwordChars.add(getRandomChar(SPECIAL_CHARS));
        
        // Fill the rest of the password length with random characters
        for (int i = 4; i < PASSWORD_LENGTH; i++) {
            passwordChars.add(getRandomChar(ALL_CHARS));
        }
        
        // Shuffle the password to avoid predictable patterns
        Collections.shuffle(passwordChars, RANDOM);
        
        // Convert list to string
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        passwordChars.forEach(password::append);
        
        return password.toString();
    }
    
    private char getRandomChar(String charSet) {
        return charSet.charAt(RANDOM.nextInt(charSet.length()));
    }
}
