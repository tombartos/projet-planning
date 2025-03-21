package fr.univtln.m1im.png;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for simple App.
 */
class AppTest {
    /**
     * Rigorous Test.
     */
    @Test
    void testLogin() {
        // Simulate a login method
        String validUsername = "user";
        String validPassword = "password";

        assertTrue(login(validUsername, validPassword), "Login should succeed with valid credentials");
        assertFalse(login("invalidUser", "invalidPass"), "Login should fail with invalid credentials");
    }

    // Simulated login method for testing purposes
    private boolean login(String username, String password) {
        return "user".equals(username) && "password".equals(password);
    }
}
