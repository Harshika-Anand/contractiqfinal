package utils;

import java.util.Random;
import java.util.UUID;

/**
 * TestDataGenerator - Generates random test data
 * Used for creating unique users, emails etc. during testing
 */
public class TestDataGenerator {
    
    private static Random random = new Random();
    
    /**
     * Generate random username
     */
    public static String generateUsername() {
        return "testuser_" + System.currentTimeMillis();
    }
    
    /**
     * Generate random email
     */
    public static String generateEmail() {
        return "testuser_" + System.currentTimeMillis() + "@test.com";
    }
    
    /**
     * Generate valid password that meets requirements:
     * - At least 8 characters
     * - One uppercase letter
     * - One lowercase letter
     * - One number
     * - One special character
     */
    public static String generateValidPassword() {
        return "Test@" + random.nextInt(9000) + 1000 + "Pass";
    }
    
    /**
     * Generate a weak/invalid password
     */
    public static String generateInvalidPassword() {
        return "weak";
    }
    
    /**
     * Generate random string of specified length
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
    
    /**
     * Generate unique ID
     */
    public static String generateUniqueId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * Generate random phone number
     */
    public static String generatePhoneNumber() {
        StringBuilder phone = new StringBuilder("9");
        for (int i = 0; i < 9; i++) {
            phone.append(random.nextInt(10));
        }
        return phone.toString();
    }
    
    /**
     * Get a specific valid test password
     */
    public static String getValidTestPassword() {
        return "Test@1234";
    }
    
    /**
     * Sample contract text for testing text extraction
     */
    public static String getSampleContractText() {
        return "This Agreement is entered into as of the Effective Date between the parties. " +
               "TERMINATION: Either party may terminate this agreement with 30 days written notice. " +
               "PAYMENT TERMS: Client agrees to pay all invoices within 30 days of receipt. " +
               "CONFIDENTIALITY: Both parties agree to keep all proprietary information confidential. " +
               "LIABILITY: Neither party shall be liable for indirect or consequential damages. " +
               "INTELLECTUAL PROPERTY: All work product shall remain the property of the creating party.";
    }
}
