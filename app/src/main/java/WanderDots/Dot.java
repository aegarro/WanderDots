package WanderDots;

import android.util.Log;

import org.json.JSONObject;

public class Dot extends Experience {

    private String[] adventureIds;
    private String[] requiredFields = {"adventures"} ;

    public Dot(String name, double latitude, double longitude) {
        super(name, latitude, longitude);
    }

    public Dot(JSONObject dot){
        super(dot) ;

        if(!containsRequiredFields(dot, requiredFields))
            throw new RuntimeException("Dot Validation Error: Given dot missing " + getMissingField(dot, requiredFields)) ;

        try {
            initializeStringArray(dot.getJSONArray("adventures")) ;
        } catch(org.json.JSONException e){
            Log.e("Dot Validation Error", "Dot: " + e.toString());
        }
    }

   public String[] getAdventureIds(){
        return this.adventureIds;
   }
}
