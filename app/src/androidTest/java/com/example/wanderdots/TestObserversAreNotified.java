package com.example.wanderdots;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.concurrent.CountDownLatch;
import wanderdots.Dot;
import wanderdots.Observer;

import static org.junit.Assert.assertTrue ;
import static org.junit.Assert.assertEquals ;
/* Does loop testing on the observers of a class
 *
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class TestObserversAreNotified implements Observer {

    private CountDownLatch lock ;

    @Test
    public void loopTestOneObserver() {
        this.lock = new CountDownLatch(1) ;
        Context appContext = InstrumentationRegistry.getTargetContext();
        MainActivity.setDefaultContext(appContext) ;
        Dot.addObserver(this);

        try {
            this.lock.await();
            assertEquals("Notified by Dot", this.lock.getCount(), 0) ;
        }catch(InterruptedException e){
            e.printStackTrace();
            assertTrue(true == false) ;
        }
    }

    @Test
    public void loopTestTwoObserver() {
        this.lock = new CountDownLatch(2) ;
        Context appContext = InstrumentationRegistry.getTargetContext();
        MainActivity.setDefaultContext(appContext) ;
        Dot.addObserver(this);
        Dot.addObserver(this);
        try {
            this.lock.await();
            assertEquals("Notified by Dot", this.lock.getCount(), 0) ;
        }catch(InterruptedException e){
            e.printStackTrace();
            assertEquals(true, false) ;
        }
    }

    //Runs asynchronously with any change in object subscribed to
    public void subscriberHasChanged(String message){
       this.lock.countDown();
    }
}
