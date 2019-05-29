package com.example.wanderdots;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.wanderdots.FindExperiencesActivity.State.AdventureState;
import com.example.wanderdots.FindExperiencesActivity.State.DotState;
import com.google.android.gms.maps.model.Marker;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import wanderDots.observer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class TestFindExperienceState implements observer {

    private CountDownLatch lock ;

    @Test
    public void testMarkersDisableOnExit(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        MainActivity.setDefaultContext(appContext) ;

        DotState dotState = new DotState(appContext) ;
        AdventureState adventureState = new AdventureState(appContext) ;

        for(Marker marker : dotState.getMarkers())
            assertFalse(marker.isVisible());
        for(Marker marker : adventureState.getMarkers())
            assertFalse(marker.isVisible());

        dotState.enter() ;
        adventureState.enter() ;

        for(Marker marker : dotState.getMarkers())
            assertTrue(marker.isVisible());
        for(Marker marker : adventureState.getMarkers())
            assertTrue(marker.isVisible());

        dotState.exit() ;

        for(Marker marker : dotState.getMarkers())
            assertFalse(marker.isVisible());
        for(Marker marker : adventureState.getMarkers())
            assertFalse(marker.isVisible());
    }

    @Test
    public void testDotStateInitialization() {
        int timeout = 4000 ; //in millis
        this.lock = new CountDownLatch(1) ;
        Context appContext = InstrumentationRegistry.getTargetContext();
        MainActivity.setDefaultContext(appContext) ;

        //Initialize States
        try {
            DotState dotState = new DotState(appContext) ;
            dotState.addObserver(this);

            lock.await(timeout, TimeUnit.MILLISECONDS);

            assertTrue("state loaded dots", dotState.getAdapter().getItemCount() > 0) ;
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testAdventureStateInitialization() {
        int timeout = 4000 ; //in millis
        this.lock = new CountDownLatch(1) ;
        Context appContext = InstrumentationRegistry.getTargetContext();
        MainActivity.setDefaultContext(appContext) ;

        //Initialize States
        try {
            AdventureState adventureState = new AdventureState(appContext) ;
            adventureState.addObserver(this);

            lock.await(timeout, TimeUnit.MILLISECONDS);

            assertTrue("state loaded adventures", adventureState.getAdapter().getItemCount() > 0) ;
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    //Runs asynchronously with any change in object subscribed to
    public void subscriberHasChanged(String message){
       this.lock.countDown();
    }
}
