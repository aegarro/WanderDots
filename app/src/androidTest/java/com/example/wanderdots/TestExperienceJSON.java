package com.example.wanderdots;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import wanderdots.Adventure;
import wanderdots.Dot;
import wanderdots.Observer;
import wanderdots.server.get.Get;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/*
 * Verifies that
 * 1. Dot class loads once an Observer has been added to it
 * 2. Adventure class loads once an Observer has been added to it
 * 3. Get class loads Dots
 * 4. Get class loads Adventures
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TestExperienceJSON implements Observer {

    private CountDownLatch lock ;

    @Test
    public void testDotJSON() {
        this.lock = new CountDownLatch(1) ;
        Context appContext = InstrumentationRegistry.getTargetContext();
        MainActivity.setDefaultContext(appContext) ;
        Dot dot = new Dot() ;
        dot.addPictureId("69696969696");
        dot.addPictureId("420420420") ;
        dot.addCategory("Hiking");
        dot.addCategory("Balls");
        Log.d("arodr", "Dot: " + dot.toJSON().toString()) ;
    }

    //Runs asynchronously with any change in object subscribed to
    public void subscriberHasChanged(String message){
       this.lock.countDown();
    }
}
