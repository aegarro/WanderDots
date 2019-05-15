package WanderDots;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Dot extends Experience {

    private ArrayList<String> adventureIds;
    private String[] requiredFields = {"adventures"} ;

    public Dot(){
        super() ;
        this.adventureIds = new ArrayList<String>() ;
    }

    public Dot(JSONObject dot) throws org.json.JSONException{
        super(dot) ;

        if(!containsRequiredFields(dot, requiredFields))
            throw new RuntimeException("Dot Validation Error: Given dot missing " + getMissingField(dot, requiredFields)) ;
        instantiateFromJSON(dot) ;
    }

    @Override
    public void instantiateFromJSON(JSONObject dot) throws org.json.JSONException{
        super.instantiateFromJSON(dot) ;
        createStringList(dot.getJSONArray("adventures")) ;
    }

    public void addAdventureId(String adventureID){
        this.adventureIds.add(adventureID) ;
    }

    public ArrayList<String> getAdventureIds(){
        return new ArrayList<String>(this.adventureIds) ;
    }

    public JSONObject toJSON(){
        try {
            JSONObject data = super.toJSON() ;
            data.put("adventures", (Object) adventureIds);
            return data ;
        }catch(JSONException e) {
            Log.d("arodr: toJSON:", e.toString()) ;
            return null ;
        }
    }

   public String toString(){
        return this.toJSON().toString() ;
   }

   @Override
   public HashMap<String, String> getHashMap(){
        HashMap<String, String> dot = super.getHashMap() ;
        dot.put("adventures", jsonifyArray(this.adventureIds)) ;
        return dot ;
   }
}
