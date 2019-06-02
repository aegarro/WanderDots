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
public class TestImageUpload implements Observer {

    private CountDownLatch lock ;

    @Test
    public void testUploadRick() {
        int timeout = 2000 ; //in millis

        this.lock = new CountDownLatch(1) ;
        Context appContext = InstrumentationRegistry.getTargetContext();
        MainActivity.setDefaultContext(appContext) ;
        try {
            int width = 150, height = 150;
            Bitmap.Config config = Bitmap.Config.ALPHA_8 ;
            Picture emptyPicture = new Picture() ;
            Bitmap emptyImage = Bitmap.createBitmap(emptyPicture, width, height, config) ;
            ImagePoster fileUpload = new ImagePoster(this) ;
            fileUpload.postImage(emptyImage);
            lock.await(timeout, TimeUnit.MILLISECONDS);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    //Runs asynchronously with any change in object subscribed to
    public void subscriberHasChanged(String message){
       this.lock.countDown();
    }
}
