package WanderDots;

import android.util.Log;

import org.json.JSONObject;

public class Adventure extends Experience {

    private String[] dotsVisited ;
    private String[] requiredFields = {"dotsVisited"} ;

    public Adventure(String name, double latitude, double longitude){
        super(name, latitude, longitude) ;
    }

    public Adventure(JSONObject adventure){
        super(adventure) ;

        if(!containsRequiredFields(adventure, requiredFields))
            throw new RuntimeException("Adventure Validation Error: Missing Field: " + getMissingField(adventure, requiredFields)) ;

        try{
            this.dotsVisited = initializeStringArray(adventure.getJSONArray("dotsVisited")) ;
        } catch(org.json.JSONException e){
            Log.e("Dot Validation Error", "Dot: " + e.toString());
        }
    }

    public String[] getDotsVisited(){
        return this.dotsVisited ;
    }
}
