package com.example.wanderdots;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import wanderdots.Observer;
import wanderdots.server.post.ImagePoster;
import wanderdots.server.get.ImageGetter ;

/*
 * Verifies that
 * 1. Dot class loads once an Observer has been added to it
 * 2. Adventure class loads once an Observer has been added to it
 * 3. Get class loads Dots
 * 4. Get class loads Adventures
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TestImageDownload implements Observer {

    private CountDownLatch lock ;

    @Test
    public void testDownloadImage() {
        int timeout = 2000 ; //in millis
        String imageID = "5cf43f58b046e62dc7d7d407" ;

        this.lock = new CountDownLatch(1) ;
        Context appContext = InstrumentationRegistry.getTargetContext();
        MainActivity.setDefaultContext(appContext) ;
        try {
            ImageGetter imageGetter = new ImageGetter(this);
            imageGetter.loadImage(imageID) ;
            lock.await(timeout, TimeUnit.MILLISECONDS);
            assert(imageGetter.getImage() != null) ;
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    //Runs asynchronously with any change in object subscribed to
    public void subscriberHasChanged(String message){
       this.lock.countDown();
    }
}