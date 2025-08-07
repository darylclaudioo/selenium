package com.daryl.testng.definitions;

import com.daryl.testng.utils.HelperClass;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

    @Before
    public static void setUp() {

        HelperClass.setUpDriver();
    }

    @After
    public static void tearDown() {

        HelperClass.tearDown();
    }
}
