package WanderDots;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

   public String toString(){

        try {
            JSONObject data = new JSONObject(super.toString()) ;
            data.put("adventures", (Object) adventureIds);
            return data.toString() ;
        }catch(JSONException e) {
            e.printStackTrace();
        }

        return "Dot Error: Could not convert to string" ;
   }
}
