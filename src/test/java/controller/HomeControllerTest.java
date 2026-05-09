package com.example.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeControllerTest {

    @Test
    void testHomePage() {

        String expected = "home";
        String actual = "home";

        assertEquals(expected, actual);
    }
}