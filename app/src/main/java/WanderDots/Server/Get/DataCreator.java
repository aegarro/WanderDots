package WanderDots.Server.Get;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import WanderDots.Dot ;
import WanderDots.Adventure;
import WanderDots.Experience;

public class DataCreator<T extends Experience> {

    private String collectionName ;
    private Class<T> handlerClass ;
    private boolean isDot ;

    public DataCreator(String type, final Class<T> handlerClass){
        this.isDot = type.contains("dot")  ;
        if(this.isDot){
            this.collectionName = "dots" ;
        }else {
           this.collectionName = "adventures" ;
        }
        this.handlerClass = handlerClass ;
    }

    public ArrayList<T> createMany(String json){
        try{
            JSONArray dataPoints = new JSONObject(json).getJSONArray(this.collectionName) ;
            ArrayList<T> dots = new ArrayList<T>() ;
            for(int i=0; i<dataPoints.length(); i++){
                JSONObject dataPoint = dataPoints.getJSONObject(i) ;
                T data = this.handlerClass.newInstance() ;
                data.instantiateFromJSON(dataPoint);
                dots.add(data)  ;
            }
            return dots ;
        } catch(Exception e){
            Log.d("arodr", "JSON Parsing Error:" + e.getMessage()) ;
        }
        return null ;
    }
}
