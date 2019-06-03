package com.example.Tests;

import android.util.Log;

import org.junit.Test;

import wanderdots.Dot; ;
/**
 * 1. Tests if Dot class loads Dots in server and notifies observers
 */
public class TestDotsLoading {

    @Test
    public void test1(){

        Dot dot = new Dot();
        dot.addPictureId("5454545454544545");
        Log.d("arodr", "dot: " + dot.toJSON().toString()) ;
    }
}