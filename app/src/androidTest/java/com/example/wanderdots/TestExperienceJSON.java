package com.example.wanderdots;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import static org.junit.Assert.assertEquals ;
import static org.junit.Assert.assertTrue ;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.concurrent.CountDownLatch;
import wanderdots.Dot;
import wanderdots.Observer;

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
    //Note, this is testing experience logic so no need for adventure
    //testing.
    public void testDotJSON() {
        this.lock = new CountDownLatch(1) ;
        String pictureIds1 = "69696969696" ;
        String pictureIds2 = "420420420" ;

        String category1 = "Hiking" ;
        String category2 = "Stuff" ;

        Context appContext = InstrumentationRegistry.getTargetContext();
        MainActivity.setDefaultContext(appContext) ;
        Dot dot = new Dot() ;

        dot.addPictureId(pictureIds1) ;
        dot.addPictureId(pictureIds2) ;
        dot.addCategory(category1) ;
        dot.addCategory(category2) ;

        try {
            //PictureIds
            JSONObject dotJSON = dot.toJSON() ;
            JSONArray pictureIds = dotJSON.getJSONArray("pictureIds") ;

            assertEquals(pictureIds1, pictureIds.getString(0)) ;
            assertEquals(pictureIds2, pictureIds.getString(1)) ;

            //Categories
            JSONArray categories = dotJSON.getJSONArray("categories") ;

            assertEquals(category1, categories.getString(0)) ;
            assertEquals(category2, categories.getString(1)) ;
        }catch(JSONException e){
            e.printStackTrace();
            assertTrue(true == false) ;
        }
    }

    //Runs asynchronously with any change in object subscribed to
    public void subscriberHasChanged(String message){
       this.lock.countDown();
    }
}
