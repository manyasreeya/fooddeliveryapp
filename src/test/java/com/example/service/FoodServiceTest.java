package com.example.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FoodServiceTest {

    @Test
    void testPriceCalculation() {

        int expected = 100;
        int actual = 50 + 50;

        assertEquals(expected, actual);
    }

    @Test
    void testOrderCount() {

        int expected = 5;
        int actual = 2 + 3;

        assertEquals(expected, actual);
    }
}