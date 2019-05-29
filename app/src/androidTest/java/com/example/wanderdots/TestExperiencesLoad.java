package com.example.wanderdots;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import wanderDots.Server.Get.get;
import wanderDots.adventure;
import wanderDots.dot;
import wanderDots.observer;

import static org.junit.Assert.*;

/*
 * Verifies that
 * 1. dot class loads once an observer has been added to it
 * 2. adventure class loads once an observer has been added to it
 * 3. get class loads Dots
 * 4. get class loads Adventures
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TestExperiencesLoad implements observer {

    private CountDownLatch lock ;

    @Test
    public void testDotsLoad() {
        int timeout = 2000 ; //in millis

        this.lock = new CountDownLatch(1) ;
        Context appContext = InstrumentationRegistry.getTargetContext();
        MainActivity.setDefaultContext(appContext) ;
        try {
            dot.addObserver(this);
            lock.await(timeout, TimeUnit.MILLISECONDS);
            assertFalse("error occurred during loadData", dot.hasError()) ;
            assertTrue("Number of Dots", dot.getData().size() > 0) ;
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testAdventuresLoad() {
        int timeout = 2000 ; //in millis

        this.lock = new CountDownLatch(1) ;
        Context appContext = InstrumentationRegistry.getTargetContext();
        MainActivity.setDefaultContext(appContext) ;
        try {
            adventure.addObserver(this);
            this.lock.await(timeout, TimeUnit.MILLISECONDS);
            assertFalse("error occurred on load", adventure.hasError()) ;
            assertTrue("Number of Adventures", adventure.getData().size() > 0);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLoader_Dot() {
        int timeout = 2000 ; //in millis

        this.lock = new CountDownLatch(1) ;
        Context appContext = InstrumentationRegistry.getTargetContext();
        MainActivity.setDefaultContext(appContext) ;
        try {
            get getLoader = new get(this, true) ;
            getLoader.loadData();
            this.lock.await(timeout, TimeUnit.MILLISECONDS);

            //Verify No Error while loading data
            assertFalse("Has Error", getLoader.hasError()) ;

            //Verify Response contains Array of Dots
            JSONObject response = getLoader.getResponse() ;
            assertTrue("Response contains Dots", response.has("dots"));

            //Verify Data contains at least 1 dot
            JSONArray dots = response.getJSONArray("dots");
            assertTrue("Contains nonzero dots", dots.length() > 0) ;
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(JSONException e){
            assertNull(e);
        }
    }

    @Test
    public void testGetLoader_Adventure() {
        int timeout = 2000 ; //in millis

        this.lock = new CountDownLatch(1) ;
        Context appContext = InstrumentationRegistry.getTargetContext();
        MainActivity.setDefaultContext(appContext) ;
        try {
            get getLoader = new get( this, false) ;
            getLoader.loadData();
            this.lock.await(timeout, TimeUnit.MILLISECONDS);

            //Verify no error while loading data
            assertFalse("Has Error", getLoader.hasError()) ;

            //Verify response contains array of adventures
            JSONObject response = getLoader.getResponse() ;
            assertTrue("Response contains ads:", response.has("adventures"));

            //Verify data contains at least one adventure
            JSONArray adventures = response.getJSONArray("adventures");
            assertTrue("Contains nonzero adventures", adventures.length() > 0) ;
        }catch(InterruptedException e){
            e.printStackTrace();
        } catch(JSONException e){
            assertNull(e);
        }
    }

    //Runs asynchronously with any change in object subscribed to
    public void subscriberHasChanged(String message){
       this.lock.countDown();
    }
}
